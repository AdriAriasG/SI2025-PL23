package app.model;

import java.util.List;
import app.dto.ReportajeDTO;
import app.util.Database;

public class AccesoReportajeModel {
	
	private Database db = new Database();
	
	public List<ReportajeDTO> getReportajesConAcceso(int idEmpresa){
		String sql = "SELECT e.nombre AS nombreEvento, e.fecha, r.titulo, v.subtitulo, v.cuerpo, v.id AS version " +
                "FROM Reportaje r " +
                "JOIN VersionReportaje v ON v.id_reportaje = r.id " +
                "JOIN Evento e ON r.id_evento = e.id " +
                "JOIN Ofrecimiento o ON o.id_evento = e.id " +
                "WHERE o.id_empresa = ? " +
                "AND o.acceso_concedido = 1 " +
                "AND v.id = (SELECT MAX(id) FROM VersionReportaje WHERE id_reportaje = r.id) " +
                "ORDER BY v.fecha_hora DESC";
		
		return db.executeQueryPojo(ReportajeDTO.class, sql, idEmpresa);
	}
	

}
