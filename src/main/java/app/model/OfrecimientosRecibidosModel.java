package app.model;

import java.util.List;
import app.dto.OfrecimientoDTO;
import app.util.Database;

public class OfrecimientosRecibidosModel {
	
	private Database db = new Database();
	

	public List<OfrecimientoDTO> getOfrecimientos(int idEmpresa){
		String sql = "SELECT o.id, e.nombre, e.fecha, o.estado " +
                "FROM Ofrecimiento o " +
                "JOIN Evento e ON o.id_evento = e.id " +
                "WHERE o.id_empresa = ? " +
                "ORDER BY e.fecha DESC";
		
		return db.executeQueryPojo(OfrecimientoDTO.class, sql, idEmpresa);
		
	}
	
	public void actualizarEstadoOfrecimiento(int id, String estado, boolean acceso) {
		
		int accesoValor = acceso? 1:0;
		String sql = "UPDATE Ofrecimiento SET estado = ?, acceso_concedido=? WHERE id =?";
		db.executeUpdate(sql, estado, accesoValor, id);
	}
	
}
