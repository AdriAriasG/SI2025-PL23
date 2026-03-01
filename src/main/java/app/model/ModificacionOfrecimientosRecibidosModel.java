package app.model;

import java.util.List;
import app.dto.OfrecimientoDTO;
import app.util.Database;



public class ModificacionOfrecimientosRecibidosModel {
	
	private Database db = new Database();
	
	public List<OfrecimientoDTO> getOfrecimientosFiltrados(int idEmpresa, boolean yaDecididos) {
	    String condicion = yaDecididos ? "WHERE o.id_empresa = ? AND o.estado != 'PENDIENTE'" 
	                                   : "WHERE o.id_empresa = ? AND o.estado = 'PENDIENTE'";
	    String sql = "SELECT o.id, e.nombre, e.fecha, o.estado, o.acceso_concedido " +
	                 "FROM Ofrecimiento o JOIN Evento e ON o.id_evento = e.id " +
	                 condicion + " ORDER BY e.fecha DESC";
	    return db.executeQueryPojo(OfrecimientoDTO.class, sql, idEmpresa);
	}
	
	public void actualizarEstadoOfrecimiento(int id, String estado, boolean acceso) {
	    // Convertimos el boolean de Java al 1 o 0 que entiende la base de datos
	    int accesoValor = acceso ? 1 : 0;
	    
	    String sql = "UPDATE Ofrecimiento SET estado = ?, acceso_concedido = ? WHERE id = ?";
	    
	    // Ejecutamos la actualización pasándole los 3 parámetros
	    db.executeUpdate(sql, estado, accesoValor, id);
	}

}
