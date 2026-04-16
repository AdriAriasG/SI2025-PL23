package app.model;

import java.util.ArrayList;
import java.util.List;
import app.dto.ReportajeDTO;
import app.util.Database;

public class AccesoReportajeModel {
    
    private Database db = new Database();
    
    /**
     * Obtiene los reportajes a los que una empresa tiene acceso concedido,
     * incluyendo la información de embargos y accesos especiales.
     */
    public List<ReportajeDTO> getReportajesConAcceso(int idEmpresa) {
        // SQL CORREGIDO: 
        // 1. Usamos alias (AS) para que r.fecha_fin_embargo se guarde en fechaEmbargo del DTO.
        // 2. Usamos alias (AS) para que o.acceso_especial se guarde en accesoEspecial del DTO.
        String sql = "SELECT r.id AS version, " +
                     "r.id_evento AS idEvento, " +
                     "e.nombre AS nombreEvento, " +
                     "e.fecha, " +
                     "r.titulo, " +
                     "v.subtitulo, " +
                     "v.cuerpo, " +
                     "r.fecha_fin_embargo AS fechaEmbargo, " + // Crucial: coincide con DTO
                     "o.acceso_especial AS accesoEspecial " +  // Crucial: coincide con DTO
                     "FROM Reportaje r " +
                     "JOIN VersionReportaje v ON v.id_reportaje = r.id " +
                     "JOIN Evento e ON r.id_evento = e.id " +
                     "JOIN Ofrecimiento o ON o.id_evento = e.id " +
                     "WHERE o.id_empresa = ? " +
                     "AND o.acceso_concedido = 1 " +
                     "AND v.id = (SELECT MAX(id) FROM VersionReportaje WHERE id_reportaje = r.id) " +
                     "ORDER BY v.fecha_hora DESC";

        // Ejecutamos la consulta mapeando al DTO
        List<ReportajeDTO> reportajes = db.executeQueryPojo(ReportajeDTO.class, sql, idEmpresa);

        // Carga de Multimedia para cada reportaje
        for (ReportajeDTO r : reportajes) {
            String sqlMedia = "SELECT ruta || ' (' || tipo || ')' AS nombreArchivo " +
                              "FROM Multimedia " +
                              "WHERE id_reportaje = ? AND estado = 'DEFINITIVO'";

            // Usamos el ID del reportaje (que guardamos en r.version) para buscar su multimedia
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

    /**
     * Actualiza el estado del ofrecimiento para marcar que el reportaje 
     * ha sido descargado por la empresa.
     */
    public void marcarComoDescargado(int idEvento, int idEmpresa) {
        // En SQLite 'descargado' se guarda como 1 (true)
        String sql = "UPDATE Ofrecimiento SET descargado = 1 " +
                     "WHERE id_evento = ? AND id_empresa = ?";
        
        db.executeUpdate(sql, idEvento, idEmpresa);
    }

    /**
     * Clase auxiliar interna para el mapeo de la tabla Multimedia.
     */
    public static class AuxMultimedia {
        private String nombreArchivo;
        public String getNombreArchivo() { return nombreArchivo; }
        public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }
    }
}