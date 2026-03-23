package app.model;

import java.util.List;

import app.util.Database;

public class FreelanceDecisionModel {

	private Database db = new Database();

	public List<Object[]> getEventosDisponibles(int idReportero) {
		String sql =
			"SELECT DISTINCT e.id, e.nombre, e.fecha " +
			"FROM Evento e " +
			"JOIN EventoTematica et ON et.id_evento = e.id " +
			"JOIN ReporteroTematica rt ON rt.id_tematica = et.id_tematica " +
			"WHERE rt.id_reportero = ? " +
			"ORDER BY e.fecha, e.nombre";

		return db.executeQueryArray(sql, idReportero);
	}

	public String getDecisionActual(int idEvento, int idReportero) {
		String sql =
			"SELECT decision " +
			"FROM DecisionFreelance " +
			"WHERE id_evento = ? AND id_reportero = ?";

		List<Object[]> res = db.executeQueryArray(sql, idEvento, idReportero);

		if (res.isEmpty() || res.get(0)[0] == null) {
			return null;
		}

		return res.get(0)[0].toString();
	}

	public boolean existeDecision(int idEvento, int idReportero) {
		String sql =
			"SELECT COUNT(*) " +
			"FROM DecisionFreelance " +
			"WHERE id_evento = ? AND id_reportero = ?";

		Long count = db.executeQueryScalar(Long.class, sql, idEvento, idReportero);
		return count != null && count > 0;
	}

	public void guardarDecision(int idEvento, int idReportero, String decision) {
		if (existeDecision(idEvento, idReportero)) {
			String sql =
				"UPDATE DecisionFreelance " +
				"SET decision = ? " +
				"WHERE id_evento = ? AND id_reportero = ?";
			db.executeUpdate(sql, decision, idEvento, idReportero);
		} else {
			String sql =
				"INSERT INTO DecisionFreelance (id_evento, id_reportero, decision) " +
				"VALUES (?, ?, ?)";
			db.executeUpdate(sql, idEvento, idReportero, decision);
		}
	}
}
