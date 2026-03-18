package app.model;

import java.util.List;
import app.dto.ReportajeDTO;
import app.dto.MultimediaDTO;
import app.util.Database;

public class AccesoReportajeModel {
	
	private Database db = new Database();
	
	public List<ReportajeDTO> getReportajesConAcceso(int idEmpresa) {
	    // 1. Consulta de reportajes (Se mantiene igual)
	    String sql = "SELECT r.id AS version, e.nombre AS nombreEvento, e.fecha, r.titulo, v.subtitulo, v.cuerpo " +
	                 "FROM Reportaje r " +
	                 "JOIN VersionReportaje v ON v.id_reportaje = r.id " +
	                 "JOIN Evento e ON r.id_evento = e.id " +
	                 "JOIN Ofrecimiento o ON o.id_evento = e.id " +
	                 "WHERE o.id_empresa = ? " +
	                 "AND o.acceso_concedido = 1 " +
	                 "AND v.id = (SELECT MAX(id) FROM VersionReportaje WHERE id_reportaje = r.id) " +
	                 "ORDER BY v.fecha_hora DESC";

	    List<ReportajeDTO> reportajes = db.executeQueryPojo(ReportajeDTO.class, sql, idEmpresa);

	    // 2. Consulta de Multimedia: SOLO DEFINITIVOS, etiqueta (IMAGEN) o (VIDEO) al FINAL
	    for (ReportajeDTO r : reportajes) {
	        // Filtramos por estado = 'DEFINITIVO'
	        // Concatenamos la ruta y el tipo al final
	        String sqlMedia = "SELECT ruta || ' (' || tipo || ')' AS nombreArchivo " +
	                          "FROM Multimedia " +
	                          "WHERE id_reportaje = ? AND estado = 'DEFINITIVO'";

	        List<AuxMultimedia> listaMedia = db.executeQueryPojo(AuxMultimedia.class, sqlMedia, r.getVersion());
	        
	        java.util.List<String> rutasFormateadas = new java.util.ArrayList<>();
	        
	        if (listaMedia != null) {
	            for (AuxMultimedia m : listaMedia) {
	                // El texto ya sale del SQL como "ruta (IMAGEN)" o "ruta (VIDEO)"
	                rutasFormateadas.add(m.getNombreArchivo());
	            }
	        }
	        r.setArchivosMultimedia(rutasFormateadas);
	    }

	    return reportajes;
	}

	
	/**
	 * Clase auxiliar (Asegúrate de tenerla al final del archivo)
	 */
	public static class AuxMultimedia {
	    private String nombreArchivo;
	    public String getNombreArchivo() { return nombreArchivo; }
	    public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }
	}


	
}
