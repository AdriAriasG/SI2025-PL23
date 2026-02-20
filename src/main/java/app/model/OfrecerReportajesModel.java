package app.model;

import app.dto.EmpresaComunicacionDTO;
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
	
	public List<EmpresaComunicacionDTO> getEmpresasDisponibles(int idEvento) {
	    String sql =
	        "SELECT ec.id, ec.nombre " +
	        "FROM EmpresaComunicacion ec " +
	        "WHERE ec.id NOT IN (" +
	        "   SELECT o.id_empresa FROM Ofrecimiento o WHERE o.id_evento = ?" +
	        ") " +
	        "ORDER BY ec.nombre";
	    return db.executeQueryPojo(EmpresaComunicacionDTO.class, sql, idEvento);
	}
}
