package app.model;

import java.util.List;
import app.dto.InformeReportajeEmpresaDTO;
import app.util.Database;

public class InformeReportajeEmpresaModel {
    private Database db = new Database();

    public List<InformeReportajeEmpresaDTO> getReportajesAccesibles(int idEmpresa, String fechaInicio, String fechaFin) {
        String sql = "SELECT r.titulo as tituloReportaje, e.nombre as nombreEvento, e.fecha as fechaEvento, o.precio as precio " +
                     "FROM Reportaje r " +
                     "JOIN Evento e ON r.id_evento = e.id " +
                     "JOIN Ofrecimiento o ON e.id = o.id_evento " +
                     "WHERE o.id_empresa = ? " +
                     "  AND o.acceso_concedido = 1 " +
                     "  AND e.fecha >= ? " +
                     "  AND e.fecha <= ? " +
                     "ORDER BY e.fecha DESC, r.titulo ASC";
        
        return db.executeQueryPojo(InformeReportajeEmpresaDTO.class, sql, idEmpresa, fechaInicio, fechaFin);
    }
}
