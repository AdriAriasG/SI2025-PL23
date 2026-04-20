package app.model;

import java.util.*;
import app.dto.InformeBeneficiosDTO;
import app.dto.TematicaDTO;
import app.util.Database;

public class InformeBeneficiosModel {
    private Database db = new Database();

    public List<InformeBeneficiosDTO> getEventosPorAgenciaYTematica(int idAgencia, String tematica) {
        String sql = "SELECT DISTINCT e.id AS idEvento, e.nombre AS nombreEvento, e.fecha " +
                     "FROM Evento e " +
                     "JOIN EventoTematica et ON e.id = et.id_evento " +
                     "JOIN Tematica t ON et.id_tematica = t.id " +
                     "WHERE e.id_agencia = ? AND t.nombre = ? " +
                     "ORDER BY e.fecha DESC";
        return db.executeQueryPojo(InformeBeneficiosDTO.class, sql, idAgencia, tematica);
    }

    public List<InformeBeneficiosDTO> getDesgloseEmpresas(int idEvento) {
        String sql = "SELECT ec.nombre AS nombreEmpresa, o.precio AS importe, ec.tiene_tarifa_plana AS tieneTarifaPlana " +
                     "FROM Ofrecimiento o " +
                     "JOIN EmpresaComunicacion ec ON o.id_empresa = ec.id " +
                     "WHERE o.id_evento = ? AND o.estado = 'ACEPTADO'";
        return db.executeQueryPojo(InformeBeneficiosDTO.class, sql, idEvento);
    }

    public List<TematicaDTO> getTodasTematicas() {
        return db.executeQueryPojo(TematicaDTO.class, "SELECT id, nombre FROM Tematica ORDER BY nombre ASC");
    }
}