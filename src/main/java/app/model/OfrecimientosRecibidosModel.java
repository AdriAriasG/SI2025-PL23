package app.model;

import java.util.List;
import app.dto.OfrecimientoDTO;
import app.util.Database;

public class OfrecimientosRecibidosModel {
	
	private Database db = new Database();

	public List<OfrecimientoDTO> getOfrecimientos(){
		String sql = "SELECT o.id, e.nombre, e.fecha " +
                "FROM Ofrecimiento o " +
                "JOIN Evento e ON o.id_evento = e.id " +
                "WHERE o.estado = 'PENDIENTE'";
		
		return db.executeQueryPojo(OfrecimientoDTO.class, sql);
		
	}
	
}
