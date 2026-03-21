package app.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import app.dto.OfrecimientoDTO;
import app.dto.TematicaDTO;
import app.util.Database;

public class ModificacionOfrecimientosRecibidosModel {
    private Database db = new Database();

    public List<OfrecimientoDTO> getOfrecimientosFiltrados(int idEmpresa, Boolean yaDecididos, List<String> tematicasFiltro) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT o.id, e.nombre, e.fecha, o.estado, o.acceso_concedido ");
        sql.append("FROM Ofrecimiento o JOIN Evento e ON o.id_evento = e.id ");
        sql.append("JOIN EventoTematica etm ON e.id = etm.id_evento JOIN Tematica t ON etm.id_tematica = t.id ");
        sql.append("WHERE o.id_empresa = ? ");

        if (yaDecididos != null) {
            if (yaDecididos) sql.append(" AND o.estado != 'PENDIENTE' ");
            else sql.append(" AND o.estado = 'PENDIENTE' ");
        }

        if (tematicasFiltro != null && !tematicasFiltro.isEmpty()) {
            String placeholders = String.join(",", Collections.nCopies(tematicasFiltro.size(), "?"));
            sql.append(" AND t.nombre IN (").append(placeholders).append(") ");
            Object[] params = new Object[1 + tematicasFiltro.size()];
            params[0] = idEmpresa;
            for (int i = 0; i < tematicasFiltro.size(); i++) params[i+1] = tematicasFiltro.get(i);
            return db.executeQueryPojo(OfrecimientoDTO.class, sql.toString(), params);
        }
        return db.executeQueryPojo(OfrecimientoDTO.class, sql.toString(), idEmpresa);
    }

    public List<TematicaDTO> getTodasTematicas() {
        return db.executeQueryPojo(TematicaDTO.class, "SELECT id, nombre FROM Tematica ORDER BY nombre ASC");
    }

    public List<TematicaDTO> getTematicasEmpresa(int idEmpresa) {
        String sql = "SELECT t.id, t.nombre FROM Tematica t JOIN EmpresaTematica et ON t.id = et.id_tematica WHERE et.id_empresa = ? ORDER BY t.nombre ASC";
        return db.executeQueryPojo(TematicaDTO.class, sql, idEmpresa);
    }

    public void actualizarEstadoOfrecimiento(int id, String estado, boolean acceso) {
        db.executeUpdate("UPDATE Ofrecimiento SET estado = ?, acceso_concedido = ? WHERE id = ?", estado, acceso ? 1 : 0, id);
    }
}