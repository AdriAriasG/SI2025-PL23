package app.model;

import java.time.LocalDateTime;
import java.util.List;

import app.dto.EventoDTO;
import app.util.ApplicationException;
import app.util.Database;

public class ReportajeModel {

	private Database db = new Database();

	/*
	 * Devuelve los eventos asignados al reportero que no tiene reportaje.
	 */
	public List<EventoDTO> getEventosPendientes(int idReportero){

		String sql = """
				SELECT e.id, e.nombre, e.fecha
				FROM Evento e
				JOIN Asignacion a ON e.id = a.id_evento
				LEFT JOIN Reportaje r ON e.id = r.id_evento
				WHERE a.id_reportero = ?
				  AND r.id IS NULL
				ORDER BY e.fecha
				""";

		return db.executeQueryPojo(EventoDTO.class, sql, idReportero);
	}

	/*
	 * Entrega un reportaje
	 */
	public void entregarReportaje(int idEvento, int idReportero, String titulo, String subtitulo, String cuerpo) {
		validarTituloUnico(titulo);
		validarEventoAsignado(idEvento, idReportero);
		validarEventoSinReportaje(idEvento);

		String insertReportaje = "INSERT INTO Reportaje (id_evento, id_reportero_autor, titulo)"
				+ "VALUES (?, ?, ?)";

		db.executeUpdate(insertReportaje, idEvento, idReportero, titulo);

		// Obtener id generado
		Integer idReportaje = db.executeQueryScalar(
				Integer.class,
				"SELECT last_insert_rowid()"
				);

		String insertVersion = "INSERT INTO VersionReportaje (id_reportaje, subtitulo, cuerpo, fecha_hora, cambios_realizados)"
				+ "VALUES (?, ?, ?, ?, ?)";

		db.executeUpdate(insertVersion, idReportaje, subtitulo, cuerpo, LocalDateTime.now().toString(), "Versión inicial");

	}

	private void validarTituloUnico(String titulo) {
		String sql = "SELECT COUNT(*) FROM Reportaje"
				+ "WHERE titulo = ?";
		long count = db.executeQueryScalar(Long.class, sql, titulo);

		if (count > 0) {
			throw new ApplicationException("El título ya existe en el sistema");
		}
	}

	private void validarEventoAsignado(int idEvento, int idReportero) {
		String sql = "SELECT COUNT(*) FROM Asignacion"
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









}
