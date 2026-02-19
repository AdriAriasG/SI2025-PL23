package app.model;

import java.util.List;

import app.dto.EventoDTO;
import app.util.Database;
public class OfrecerReportajesModel {

	private Database db = new Database();

	public List<EventoDTO> getEventosConAsignados(int idAgencia) {
		 String sql =
		            "SELECT DISTINCT e.id, e.nombre, e.fecha, e.id_agencia " +
		            "FROM Evento e " +
		            "JOIN Asignacion a ON e.id = a.id_evento " +
		            "WHERE e.id_agencia = ? " +
		            "ORDER BY e.fecha, e.nombre";
		        return db.executeQueryPojo(EventoDTO.class, sql, idAgencia);
    }
}
