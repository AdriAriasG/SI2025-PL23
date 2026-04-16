package app.model;

import java.util.*;
import app.dto.OfrecimientoDTO;
import app.dto.TematicaDTO;
import app.util.Database;

public class ModificacionOfrecimientosRecibidosModel {
	private Database db = new Database();

	public List<OfrecimientoDTO> getOfrecimientosFiltrados(int idEmpresa, Boolean yaDecididos, 
			List<String> tematicasFiltro, Double pMin, Double pMax, boolean soloEmbargados) {
		
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();

		// NUEVO: Añadimos acceso_especial y la fecha de fin de embargo a la consulta
		// Usamos LEFT JOIN con Reportaje por si el evento aún no tiene uno
		sql.append("SELECT DISTINCT o.id, e.nombre, e.fecha, o.estado, o.acceso_concedido AS accesoConcedido, ");
		sql.append("o.precio, o.acceso_especial AS accesoEspecial, r.fecha_fin_embargo AS fechaFinEmbargo ");
		sql.append("FROM Ofrecimiento o ");
		sql.append("INNER JOIN Evento e ON o.id_evento = e.id ");
		sql.append("INNER JOIN EventoTematica etm ON e.id = etm.id_evento ");
		sql.append("INNER JOIN Tematica t ON etm.id_tematica = t.id ");
		sql.append("LEFT JOIN Reportaje r ON e.id = r.id_evento "); // NUEVO
		sql.append("WHERE o.id_empresa = ? ");
		params.add(idEmpresa);

		if (yaDecididos != null) {
			if (yaDecididos) sql.append(" AND o.estado != 'PENDIENTE' ");
			else sql.append(" AND o.estado = 'PENDIENTE' ");
		}

		if (pMin != null) { sql.append(" AND o.precio >= ? "); params.add(pMin); }
		if (pMax != null) { sql.append(" AND o.precio <= ? "); params.add(pMax); }

		if (tematicasFiltro != null && !tematicasFiltro.isEmpty()) {
			String placeholders = String.join(",", Collections.nCopies(tematicasFiltro.size(), "?"));
			sql.append(" AND t.nombre IN (").append(placeholders).append(") ");
			params.addAll(tematicasFiltro);
		}
		
		// NUEVO: Filtro de embargo activo (Fecha actual < Fecha fin embargo)
		if (soloEmbargados) {
			sql.append(" AND r.fecha_fin_embargo > date('now') ");
		}

		sql.append(" ORDER BY e.fecha DESC");
		
		return db.executeQueryPojo(OfrecimientoDTO.class, sql.toString(), params.toArray());
	}

	// NUEVO: Ahora actualizamos estado y acceso_concedido según la lógica del Controller
	public void actualizarEstadoDecision(int id, String nuevoEstado, boolean accesoConcedido) {
		db.executeUpdate("UPDATE Ofrecimiento SET estado = ? WHERE id = ?", 
				nuevoEstado, id);
	}

	public List<TematicaDTO> getTodasTematicas() {
		return db.executeQueryPojo(TematicaDTO.class, "SELECT id, nombre FROM Tematica ORDER BY nombre ASC");
	}

	public List<TematicaDTO> getTematicasEmpresa(int idEmpresa) {
		String sql = "SELECT t.id, t.nombre FROM Tematica t JOIN EmpresaTematica et ON t.id = et.id_tematica WHERE et.id_empresa = ? ORDER BY t.nombre ASC";
		return db.executeQueryPojo(TematicaDTO.class, sql, idEmpresa);
	}
}