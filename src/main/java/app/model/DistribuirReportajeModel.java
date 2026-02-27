package app.model;

import java.util.List;

import app.dto.EmpresaComunicacionDTO;
import app.dto.EventoDTO;
import app.util.Database;

public class DistribuirReportajeModel {
	
	private Database db = new Database();

	public List<EventoDTO> getEventosConReportaje(int idAgencia) {
	    String sql =
	        "SELECT e.id, e.nombre, e.fecha, e.id_agencia " +
	        "FROM Evento e " +
	        "WHERE e.id_agencia = ? " +
	        "AND EXISTS (SELECT 1 FROM Reportaje r WHERE r.id_evento = e.id) " +
	        "ORDER BY e.fecha, e.nombre";

	    return db.executeQueryPojo(EventoDTO.class, sql, idAgencia);
	}
	
	public List<EmpresaComunicacionDTO> getEmpresasAceptadasSinAcceso(int idEvento) {
	    String sql =
	        "SELECT ec.id, ec.nombre " +
	        "FROM EmpresaComunicacion ec " +
	        "JOIN Ofrecimiento o ON ec.id = o.id_empresa " +
	        "WHERE o.id_evento = ? " +
	        "AND o.estado = 'ACEPTADO' " +
	        "AND o.acceso_concedido = FALSE " +
	        "ORDER BY ec.nombre";

	    return db.executeQueryPojo(EmpresaComunicacionDTO.class, sql, idEvento);
	}
	
	public void concederAcceso(int idEvento, int idEmpresa) {
	    String sql =
	        "UPDATE Ofrecimiento " +
	        "SET acceso_concedido = TRUE " +
	        "WHERE id_evento = ? AND id_empresa = ?";

	    db.executeUpdate(sql, idEvento, idEmpresa);
	}
	
	
}
