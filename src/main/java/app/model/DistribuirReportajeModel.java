package app.model;

import java.util.List;

import app.dto.EmpresaComunicacionDTO;
import app.dto.EventoDTO;
import app.util.ApplicationException;
import app.util.Database;

public class DistribuirReportajeModel {

    private Database db = new Database();

    public List<EventoDTO> getEventosConReportaje(int idAgencia) {
        return getEventosConReportaje(idAgencia, false);
    }

    public List<EventoDTO> getEventosConReportaje(int idAgencia, boolean soloConEmbargo) {
        String sql =
                "SELECT e.id, e.nombre, e.fecha, e.id_agencia " +
                "FROM Evento e " +
                "JOIN Reportaje r ON r.id_evento = e.id " +
                "WHERE e.id_agencia = ? " +
                "AND r.estado = 'TERMINADO' ";

        if (soloConEmbargo) {
            sql += "AND r.fecha_fin_embargo IS NOT NULL " +
                   "AND date(r.fecha_fin_embargo) >= date('now') ";
        }

        sql += "ORDER BY e.fecha, e.nombre";

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
                "AND o.acceso_especial = FALSE " +
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
                "AND (o.acceso_concedido = TRUE OR o.acceso_especial = TRUE) " +
                "ORDER BY ec.nombre";

        return db.executeQueryPojo(EmpresaComunicacionDTO.class, sql, idEvento);
    }

    public boolean eventoTieneEmbargoVigente(int idEvento) {
        Integer total = db.executeQueryScalar(
                Integer.class,
                """
                SELECT COUNT(*)
                FROM Reportaje
                WHERE id_evento = ?
                  AND fecha_fin_embargo IS NOT NULL
                  AND date(fecha_fin_embargo) >= date('now')
                """,
                idEvento
        );

        return total != null && total > 0;
    }

    public boolean empresaAceptaEmbargo(int idEmpresa) {
        Integer acepta = db.executeQueryScalar(
                Integer.class,
                """
                SELECT acepta_embargo
                FROM EmpresaComunicacion
                WHERE id = ?
                """,
                idEmpresa
        );

        return acepta != null && acepta == 1;
    }

    private void validarCondicionesEconomicas(int idEvento, int idEmpresa) {
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

        Integer tieneTarifaPlanaInt = db.executeQueryScalar(
                Integer.class,
                """
                SELECT tiene_tarifa_plana
                FROM EmpresaComunicacion
                WHERE id = ?
                """,
                idEmpresa
        );

        boolean tieneTarifaPlana = tieneTarifaPlanaInt != null && tieneTarifaPlanaInt == 1;

        if (tieneTarifaPlana) {
            Integer alCorrienteInt = db.executeQueryScalar(
                    Integer.class,
                    """
                    SELECT al_corriente_pago
                    FROM EmpresaComunicacion
                    WHERE id = ?
                    """,
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
    }

    public void concederAcceso(int idEvento, int idEmpresa) {
        validarCondicionesEconomicas(idEvento, idEmpresa);

        db.executeUpdate(
                """
                UPDATE Ofrecimiento
                SET acceso_concedido = TRUE,
                    acceso_especial = FALSE
                WHERE id_evento = ? AND id_empresa = ?
                """,
                idEvento,
                idEmpresa
        );
    }

    public void concederAccesoEspecial(int idEvento, int idEmpresa) {
        validarCondicionesEconomicas(idEvento, idEmpresa);

        if (!eventoTieneEmbargoVigente(idEvento)) {
            throw new ApplicationException(
                    "Solo se puede conceder acceso especial si el embargo sigue vigente."
            );
        }

        if (!empresaAceptaEmbargo(idEmpresa)) {
            throw new ApplicationException(
                    "La empresa no acepta condiciones de embargo."
            );
        }

        db.executeUpdate(
                """
                UPDATE Ofrecimiento
                SET acceso_concedido = TRUE,
                    acceso_especial = TRUE
                WHERE id_evento = ? AND id_empresa = ?
                """,
                idEvento,
                idEmpresa
        );
    }

    public void quitarAcceso(int idEvento, int idEmpresa) {
        if (empresaHaDescargadoReportaje(idEvento, idEmpresa)) {
            throw new ApplicationException(
                    "No se puede quitar el acceso porque la empresa ya ha descargado el reportaje."
            );
        }

        String sql =
                "UPDATE Ofrecimiento " +
                "SET acceso_concedido = FALSE, acceso_especial = FALSE " +
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