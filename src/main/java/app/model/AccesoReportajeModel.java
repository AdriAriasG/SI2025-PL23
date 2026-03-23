package app.model;

import java.util.ArrayList;
import java.util.List;
import app.dto.ReportajeDTO;
import app.util.Database;

public class AccesoReportajeModel {
    
    private Database db = new Database();
    
    public List<ReportajeDTO> getReportajesConAcceso(int idEmpresa) {
        // Traemos r.id_evento AS idEvento para identificar el ofrecimiento después
        String sql = "SELECT r.id AS version, r.id_evento AS idEvento, e.nombre AS nombreEvento, e.fecha, " +
                     "r.titulo, v.subtitulo, v.cuerpo " +
                     "FROM Reportaje r " +
                     "JOIN VersionReportaje v ON v.id_reportaje = r.id " +
                     "JOIN Evento e ON r.id_evento = e.id " +
                     "JOIN Ofrecimiento o ON o.id_evento = e.id " +
                     "WHERE o.id_empresa = ? " +
                     "AND o.acceso_concedido = 1 " +
                     "AND v.id = (SELECT MAX(id) FROM VersionReportaje WHERE id_reportaje = r.id) " +
                     "ORDER BY v.fecha_hora DESC";

        List<ReportajeDTO> reportajes = db.executeQueryPojo(ReportajeDTO.class, sql, idEmpresa);

        // Carga de Multimedia
        for (ReportajeDTO r : reportajes) {
            String sqlMedia = "SELECT ruta || ' (' || tipo || ')' AS nombreArchivo " +
                              "FROM Multimedia " +
                              "WHERE id_reportaje = ? AND estado = 'DEFINITIVO'";

            List<AuxMultimedia> listaMedia = db.executeQueryPojo(AuxMultimedia.class, sqlMedia, r.getVersion());
            List<String> rutasFormateadas = new ArrayList<>();
            
            if (listaMedia != null) {
                for (AuxMultimedia m : listaMedia) {
                    rutasFormateadas.add(m.getNombreArchivo());
                }
            }
            r.setArchivosMultimedia(rutasFormateadas);
        }
        return reportajes;
    }

    // Método para actualizar el campo 'descargado' en la tabla Ofrecimiento
    public void marcarComoDescargado(int idEvento, int idEmpresa) {
        String sql = "UPDATE Ofrecimiento SET descargado = 1 WHERE id_evento = ? AND id_empresa = ?";
        db.executeUpdate(sql, idEvento, idEmpresa);
    }

    public static class AuxMultimedia {
        private String nombreArchivo;
        public String getNombreArchivo() { return nombreArchivo; }
        public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }
    }
}