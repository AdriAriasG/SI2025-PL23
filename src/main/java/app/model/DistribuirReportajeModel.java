package app.model;

import java.util.List;

import app.dto.EmpresaComunicacionDTO;
import app.dto.EventoDTO;
import app.util.ApplicationException;
import app.util.Database;

public class DistribuirReportajeModel {

	private Database db = new Database();

	public List<EventoDTO> getEventosConReportaje(int idAgencia) {
		String sql =
				"SELECT e.id, e.nombre, e.fecha, e.id_agencia " +
						"FROM Evento e " +
						"WHERE e.id_agencia = ? " +
						"AND EXISTS (" +
						"   SELECT 1 FROM Reportaje r " +
						"   WHERE r.id_evento = e.id " +
						"   AND r.estado = 'TERMINADO'" +
						") " +
						"ORDER BY e.fecha, e.nombre";

		return db.executeQueryPojo(EventoDTO.class, sql, idAgencia);
	}

	public List<EmpresaComunicacionDTO> getEmpresasAceptadasSinAcceso(int idEvento) {
		String sql =
				"SELECT ec.id, ec.nombre " +
						"FROM EmpresaComunicacion ec " +
						"JOIN Ofrecimiento o ON ec.id = o.id_empresa " +
						"WHERE o.id_evento = ? " +
						"AND o.estado = 'ACEPTADO' " +
						"AND o.acceso_concedido = FALSE " +
						"ORDER BY ec.nombre";

		return db.executeQueryPojo(EmpresaComunicacionDTO.class, sql, idEvento);
	}

	public List<EmpresaComunicacionDTO> getEmpresasAceptadasConAcceso(int idEvento) {
		String sql =
				"SELECT ec.id, ec.nombre " +
						"FROM EmpresaComunicacion ec " +
						"JOIN Ofrecimiento o ON ec.id = o.id_empresa " +
						"WHERE o.id_evento = ? " +
						"AND o.estado = 'ACEPTADO' " +
						"AND o.acceso_concedido = TRUE " +
						"ORDER BY ec.nombre";

		return db.executeQueryPojo(EmpresaComunicacionDTO.class, sql, idEvento);
	}

	public void concederAcceso(int idEvento, int idEmpresa) {

		// Comprobar que el reportaje está TERMINADO
		String estado = db.executeQueryScalar(
				String.class,
				"""
				SELECT r.estado
				FROM Reportaje r
				WHERE r.id_evento = ?
				""",
				idEvento
				);

		if (!"TERMINADO".equals(estado)) {
			throw new ApplicationException(
					"Solo pueden distribuirse reportajes TERMINADOS"
					);
		}

		// Obtener si tiene tarifa plana (SQLite devuelve 0 o 1)
		Integer tieneTarifaPlanaInt = db.executeQueryScalar(
				Integer.class,
				"SELECT tiene_tarifa_plana FROM EmpresaComunicacion WHERE id = ?",
				idEmpresa
				);

		boolean tieneTarifaPlana = tieneTarifaPlanaInt != null && tieneTarifaPlanaInt == 1;

		if (tieneTarifaPlana) {

			Integer alCorrienteInt = db.executeQueryScalar(
					Integer.class,
					"SELECT al_corriente_pago FROM EmpresaComunicacion WHERE id = ?",
					idEmpresa
					);

			boolean alCorriente = alCorrienteInt != null && alCorrienteInt == 1;

			if (!alCorriente) {
				throw new ApplicationException(
						"La empresa no está al corriente de pago de la tarifa plana"
						);
			}

		} else {

			Integer pagadoInt = db.executeQueryScalar(
					Integer.class,
					"""
					SELECT pagado
					FROM Ofrecimiento
					WHERE id_evento = ?
					  AND id_empresa = ?
					""",
					idEvento,
					idEmpresa
					);

			boolean pagado = pagadoInt != null && pagadoInt == 1;

			if (!pagado) {
				throw new ApplicationException(
						"El reportaje no está pagado por esta empresa"
						);
			}
		}

		// Conceder acceso
		db.executeUpdate(
				"""
				UPDATE Ofrecimiento
				SET acceso_concedido = TRUE
				WHERE id_evento = ? AND id_empresa = ?
				""",
				idEvento,
				idEmpresa
				);
	}
	public void quitarAcceso(int idEvento, int idEmpresa) {
		String sql =
				"UPDATE Ofrecimiento " +
						"SET acceso_concedido = FALSE " +
						"WHERE id_evento = ? AND id_empresa = ?";

		db.executeUpdate(sql, idEvento, idEmpresa);
	}

	public boolean empresaHaDescargadoReportaje(int idEvento, int idEmpresa) {
		String sql =
				"SELECT COUNT(*) AS total " +
						"FROM Ofrecimiento " +
						"WHERE id_evento = ? " +
						"AND id_empresa = ? " +
						"AND descargado = TRUE";

		List<ContadorDTO> resultado =
				db.executeQueryPojo(ContadorDTO.class, sql, idEvento, idEmpresa);

		if (resultado.isEmpty()) {
			return false;
		}

		return resultado.get(0).getTotal() > 0;
	}

	public static class ContadorDTO {
		private int total;

		public int getTotal() {
			return total;
		}

		public void setTotal(int total) {
			this.total = total;
		}
	}
}