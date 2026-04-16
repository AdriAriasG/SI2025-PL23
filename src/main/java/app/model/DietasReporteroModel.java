package app.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import app.dto.DietaDTO;
import app.dto.EventoDTO;
import app.util.Database;

public class DietasReporteroModel {

	private Database db = new Database();

	// ==========================================
	// Obtener eventos asignados
	// ==========================================
	public List<EventoDTO> getEventosAsignados(int idReportero) {

		String sql = """
				    SELECT 
				        e.id,
				        e.nombre,
				        e.fecha,
				        e.fecha_inicio,
				        e.fecha_fin,
				        e.id_agencia,
				        e.asignacion_finalizada,
				        p.id,
				        p.nombre,
				        pa.nombre
				    FROM Evento e
				    JOIN Asignacion a ON e.id = a.id_evento
				    JOIN Provincia p ON e.id_provincia = p.id
				    JOIN Pais pa ON p.id_pais = pa.id
				    WHERE a.id_reportero = ?
				""";

		List<Object[]> rows = db.executeQueryArray(sql, idReportero);
		List<EventoDTO> lista = new ArrayList<>();

		for (Object[] row : rows) {

			boolean asignacionFinalizada = ((Number) row[6]).intValue() == 1;

			lista.add(new EventoDTO(
					(int) row[0],
					(String) row[1],
					row[2] != null ? row[2].toString() : null,
							row[3] != null ? row[3].toString() : null,
									row[4] != null ? row[4].toString() : null,
											(int) row[5],
											asignacionFinalizada,
											(int) row[7],
											(String) row[8],
											(String) row[9]
					));
		}

		return lista;
	}

	// ==========================================
	// Calcular dietas
	// ==========================================
	public DietaDTO calcularDietas(int idEvento, int idReportero) {

		// Obtener datos del evento + precios
		String sqlEvento = """
				    SELECT 
				        e.fecha_inicio,
				        e.fecha_fin,
				        p.id,
				        p.precio_alojamiento,
				        pa.precio_manutencion
				    FROM Evento e
				    JOIN Provincia p ON e.id_provincia = p.id
				    JOIN Pais pa ON p.id_pais = pa.id
				    WHERE e.id = ?
				""";

		Object[] evento = db.executeQueryArray(sqlEvento, idEvento).get(0);

		String fechaInicio = evento[0].toString();
		String fechaFin = evento[1].toString();

		int idProvinciaEvento = ((Number) evento[2]).intValue();
		double alojamiento = ((Number) evento[3]).doubleValue();
		double manutencion = ((Number) evento[4]).doubleValue();

		// Obtener provincia del reportero
		String sqlRep = """
				    SELECT id_provincia
				    FROM Reportero
				    WHERE id = ?
				""";

		Object[] rep = db.executeQueryArray(sqlRep, idReportero).get(0);
		int idProvinciaReportero = ((Number) rep[0]).intValue();
		
		// Calcular días
		LocalDate inicio = LocalDate.parse(fechaInicio);
		LocalDate fin = LocalDate.parse(fechaFin);

		int dias = (int) ChronoUnit.DAYS.between(inicio, fin) + 1;

		// Si reside en la misma provincia → no cobra alojamiento
		if (idProvinciaEvento == idProvinciaReportero) {
			alojamiento = 0.0;
		}

		// Total
		double total = (alojamiento + manutencion) * dias;

		return new DietaDTO(alojamiento, manutencion, dias, total);
	}
}