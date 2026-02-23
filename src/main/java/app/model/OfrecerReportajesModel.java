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
	        "WHERE ec.id NOT IN (SELECT o.id_empresa FROM Ofrecimiento o) " +
	        "ORDER BY ec.nombre";

	    return db.executeQueryPojo(EmpresaComunicacionDTO.class, sql);
	}
	
	public Integer getIdEmpresaPorNombre(String nombre) {
	    String sql = "SELECT id FROM EmpresaComunicacion WHERE nombre = ?";
	    List<Object[]> res = db.executeQueryArray(sql, nombre);
	    if (res.isEmpty() || res.get(0)[0] == null) return null;
	    return ((Number) res.get(0)[0]).intValue();
	}

	public boolean existeOfrecimiento(int idEvento, int idEmpresa) {
	    String sql = "SELECT COUNT(*) FROM Ofrecimiento WHERE id_evento = ? AND id_empresa = ?";
	    List<Object[]> res = db.executeQueryArray(sql, idEvento, idEmpresa);
	    if (res.isEmpty() || res.get(0)[0] == null) return false;
	    return ((Number) res.get(0)[0]).intValue() > 0;
	}

	public int getNextIdOfrecimiento() {
	    String sql = "SELECT COALESCE(MAX(id), 0) + 1 FROM Ofrecimiento";
	    List<Object[]> res = db.executeQueryArray(sql);
	    return ((Number) res.get(0)[0]).intValue();
	}

	public void crearOfrecimientoPendiente(int idEvento, int idEmpresa) {
	    if (existeOfrecimiento(idEvento, idEmpresa)) return;

	    int nextId = getNextIdOfrecimiento();

	    String sql = "INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) " +
	                 "VALUES (?, ?, ?, 'PENDIENTE', FALSE)";

	    db.executeUpdate(sql, nextId, idEvento, idEmpresa);
	}
}
