package app.model;

import java.time.LocalDateTime;
import java.util.List;

import app.dto.EventoDTO;
import app.util.ApplicationException;
import app.util.Database;


import app.dto.VersionDTO;


public class ReportajeModel {

	private Database db = new Database();

	/*
	 * Devuelve los eventos asignados al reportero que no tiene reportaje.
	 */
	public List<EventoDTO> getEventosAsignados(int idReportero, boolean soloEntregados){

		String condicion = soloEntregados
				? "r.id IS NOT NULL"
						: "r.id IS NULL";

		String sql =
				"SELECT e.id, e.nombre, e.fecha " +
						"FROM Evento e " +
						"JOIN Asignacion a ON e.id = a.id_evento " +
						"LEFT JOIN Reportaje r ON e.id = r.id_evento " +
						"WHERE a.id_reportero = ? " +
						"AND " + condicion + " " +
						"ORDER BY e.fecha";

		return db.executeQueryPojo(EventoDTO.class, sql, idReportero);
	}

	public List<VersionDTO> getVersiones(int idEvento) {
		String sql = """
				SELECT v.id,
				v.subtitulo,
				v.cuerpo,
				v.fecha_hora,
				v.cambios_realizados,
				v.id_reportero_modificador
				FROM VersionReportaje v
				JOIN Reportaje r ON v.id_reportaje = r.id
				WHERE r.id_evento = ?
				ORDER BY v.id DESC
				""";

		return db.executeQueryPojo(VersionDTO.class, sql, idEvento);
	}

	public VersionDTO getVersionActual(int idEvento) {

		String sql = """
				SELECT v.subtitulo, v.cuerpo
				FROM VersionReportaje v
				JOIN Reportaje r ON v.id_reportaje = r.id
				WHERE r.id_evento = ?
				ORDER BY v.id DESC
				LIMIT 1
				""";

		return db.executeQueryPojo(VersionDTO.class, sql, idEvento)
				.stream()
				.findFirst()
				.orElse(null);


	}

	/*
	 * Entrega un reportaje
	 */
	public void entregarReportaje(int idEvento,
			int idReportero,
			String titulo,
			String subtitulo,
			String cuerpo) {

		validarTituloUnico(titulo);
		validarEventoAsignado(idEvento, idReportero);
		validarEventoSinReportaje(idEvento);

		String insertReportaje = """
				INSERT INTO Reportaje (id_evento, id_reportero_autor, titulo)
				VALUES (?, ?, ?)
				RETURNING id
				""";

		Integer idReportaje = db.executeQueryScalar(
				Integer.class,
				insertReportaje,
				idEvento,
				idReportero,
				titulo
				);

		String insertVersion = """
				INSERT INTO VersionReportaje
				(id_reportaje, subtitulo, cuerpo, fecha_hora, cambios_realizados, id_reportero_modificador)
				VALUES (?, ?, ?, ?, ?, ?)
				""";

		db.executeUpdate(insertVersion,
				idReportaje,
				subtitulo,
				cuerpo,
				LocalDateTime.now().toString(),
				"Versión inicial",
				idReportero
				);
	}

	public void modificarReportaje(int idEvento,
			int idReportero,
			String nuevoSubtitulo,
			String nuevoCuerpo) {
		// Validamos si evento tiene reportaje
		Integer idReportaje = db.executeQueryScalar(Integer.class, "SELECT id FROM Reportaje WHERE id_evento = ?", idEvento);

		if (idReportaje == null) 
			throw new ApplicationException("El evento no tiene reportaje entregado");

		// Validar que el usuario es el autor
		Integer autor = db.executeQueryScalar(Integer.class, "SELECT id_reportero_autor FROM REPORTAJE WHERE id_evento = ?", idEvento);

		if (autor == null)
			throw new ApplicationException("Solo el autor puede modificar el reportaje");

		// Obtener version actual
		VersionDTO versionActual = db.executeQueryPojo(
				VersionDTO.class,
				"""
				SELECT subtitulo, cuerpo
				FROM VersionReportaje
				WHERE id_reportaje = ?
				ORDER BY id DESC
				LIMIT 1
				""",
				idReportaje
				).stream().findFirst().orElse(null);

		if (versionActual == null) 
			throw new ApplicationException("No existe ninguna versión previa del reportaje");

		// Detectar cambios
		String subtituloActual = versionActual.getSubtitulo() == null ? "" :
			versionActual.getSubtitulo().trim();

		String cuerpoActual = versionActual.getCuerpo() == null ? "" :
			versionActual.getCuerpo().trim();

		String nuevoSubtituloNormalizado =
				nuevoSubtitulo == null ? "" : nuevoSubtitulo.trim();

		String nuevoCuerpoNormalizado =
				nuevoCuerpo == null ? "" : nuevoCuerpo.trim();

		boolean cambioSubtitulo =
				!subtituloActual.equals(nuevoSubtituloNormalizado);

		boolean cambioCuerpo =
				!cuerpoActual.equals(nuevoCuerpoNormalizado);

		if(!cambioSubtitulo && !cambioCuerpo) 
			throw new ApplicationException("No se han realizado cambios");

		String cambios;

		if (cambioSubtitulo && cambioCuerpo) 
			cambios = "Actualización de subtítulo y cuerpo";
		else if (cambioSubtitulo)
			cambios = "Actualización de subtítulo";
		else 
			cambios = "Actualización de cuerpo";

		// Insertar nueva version
		String insertVersion = """
				INSERT INTO VersionReportaje (id_Reportaje, subtitulo, cuerpo, fecha_hora, cambios_realizados, id_reportero_modificador)
				VALUES (?, ?, ?, ?, ?, ?)
				""";

		db.executeUpdate(insertVersion, idReportaje, 
				nuevoSubtituloNormalizado, nuevoCuerpoNormalizado, LocalDateTime.now().toString(), cambios, idReportero);
	}

	private void validarTituloUnico(String titulo) {
		String sql = "SELECT COUNT(*) FROM Reportaje "
				+ "WHERE titulo = ?";
		long count = db.executeQueryScalar(Long.class, sql, titulo);

		if (count > 0) {
			throw new ApplicationException("El título ya existe en el sistema");
		}
	}

	private void validarEventoAsignado(int idEvento, int idReportero) {
		String sql = "SELECT COUNT(*) FROM Asignacion "
				+ "WHERE id_evento = ? AND id_reportero = ?";

		long count = db.executeQueryScalar(Long.class, sql, idEvento, idReportero);

		if (count == 0) {
			throw new ApplicationException("El evento ya tiene reportaje entregado");
		}
	}

	private void validarEventoSinReportaje(int idEvento) {
		String sql = "SELECT COUNT(*) FROM Reportaje WHERE id_evento = ?";
		long count = db.executeQueryScalar(Long.class, sql, idEvento);

		if (count > 0) {
			throw new ApplicationException("El evento ya tiene reportaje entregado");
		}

	}

	public void restaurarVersion(int idEvento,
			int idReportero,
			int idVersionSeleccionada,
			boolean restaurarSubtitulo,
			boolean restaurarCuerpo) {

		// Obtener reportaje
		Integer idReportaje = db.executeQueryScalar(
				Integer.class,
				"SELECT id FROM Reportaje WHERE id_evento = ?",
				idEvento
				);

		// Validar autor
		Integer autor = db.executeQueryScalar(
				Integer.class,
				"SELECT id_reportero_autor FROM Reportaje WHERE id_evento = ?",
				idEvento
				);

		if (autor == null || !autor.equals(idReportero))
			throw new ApplicationException("Solo el autor puede restaurar versiones");

		VersionDTO actual = getVersionActual(idEvento);

		VersionDTO seleccionada = db.executeQueryPojo(
				VersionDTO.class,
				"SELECT subtitulo, cuerpo FROM VersionReportaje WHERE id = ?",
				idVersionSeleccionada
				).stream().findFirst().orElse(null);

		if (seleccionada == null)
			throw new ApplicationException("Versión no encontrada");

		// Determinar contenido final
		String nuevoSubtitulo = restaurarSubtitulo ?
				seleccionada.getSubtitulo() :
					actual.getSubtitulo();

		String nuevoCuerpo = restaurarCuerpo ?
				seleccionada.getCuerpo() :
					actual.getCuerpo();

		if (nuevoSubtitulo.equals(actual.getSubtitulo()) &&
				nuevoCuerpo.equals(actual.getCuerpo())) {
			throw new ApplicationException("No hay cambios para restaurar");
		}

		String cambios;

		if (restaurarSubtitulo && restaurarCuerpo)
			cambios = "Restauración de subtítulo y cuerpo";
		else if (restaurarSubtitulo)
			cambios = "Restauración de subtítulo";
		else
			cambios = "Restauración de cuerpo";

		String insert = """
				INSERT INTO VersionReportaje
				(id_reportaje, subtitulo, cuerpo, fecha_hora,
				cambios_realizados, id_reportero_modificador)
				VALUES (?, ?, ?, ?, ?, ?)
				""";

		db.executeUpdate(insert,
				idReportaje,
				nuevoSubtitulo,
				nuevoCuerpo,
				LocalDateTime.now().toString(),
				cambios,
				idReportero);

	}









}
