package app.model;

import java.util.List;
import app.dto.ReportajeDTO;
import app.util.Database;

public class AccesoReportajeModel {
	
	private Database db = new Database();
	
	public List<ReportajeDTO> getReportajesConAcceso(){
		String sql = "SELECT e.nombre AS nombreEvento, r.titulo, r.subtitulo, r.cuerpo, r.version" +
					 "FROM reportahe r" +
					 "JOIN Evento e ON r.id_evento = e.id" +
					 "JOIN Ofrecimiento o ON o.id_evento = e.id_evento" +
					 "WHERE o.acceso_concedido = 1" +
					 "AND r.version = (SELECT MAX(version) FROM Reportaje WHERE id_evento = r.id_evento)" +
					 "ORDER BY e.fecha DESC";
		
		return db.executeQueryPojo(ReportajeDTO.class, sql);
	}
	

}
