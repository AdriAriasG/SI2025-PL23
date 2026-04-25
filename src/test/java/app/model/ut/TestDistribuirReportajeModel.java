package app.model.ut;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import app.model.DistribuirReportajeModel;
import app.util.ApplicationException;
import app.util.Database;

public class TestDistribuirReportajeModel {

    private static Database db = new Database();

    @BeforeEach
    public void setUp() {
        db.createDatabase(false);
        loadCleanDatabase(db);
    }

    private static void loadCleanDatabase(Database db) {
        db.executeBatch(new String[] {
                // Datos de soporte necesarios para cumplir claves foráneas
                "INSERT INTO Pais (id, nombre, precio_manutencion) VALUES (1, 'España', 30.0)",
                "INSERT INTO Provincia (id, nombre, precio_alojamiento, id_pais) VALUES (1, 'Asturias', 60.0, 1)",
                "INSERT INTO AgenciaPrensa (id, nombre, email) VALUES (1, 'Agencia Test', 'agencia@test.com')",
                "INSERT INTO Reportero (id, nombre, tipo, id_agencia, id_provincia) VALUES (1, 'Reportero Test', 'BASE', 1, 1)",

                // Eventos
                "INSERT INTO Evento (id, nombre, fecha, fecha_inicio, fecha_fin, id_agencia, asignacion_finalizada, id_provincia) "
                        + "VALUES (100, 'Evento con reportaje terminado sin embargo', '2026-05-01', '2026-05-01', '2026-05-01', 1, TRUE, 1)",

                "INSERT INTO Evento (id, nombre, fecha, fecha_inicio, fecha_fin, id_agencia, asignacion_finalizada, id_provincia) "
                        + "VALUES (101, 'Evento con reportaje no terminado', '2026-05-02', '2026-05-02', '2026-05-02', 1, TRUE, 1)",

                "INSERT INTO Evento (id, nombre, fecha, fecha_inicio, fecha_fin, id_agencia, asignacion_finalizada, id_provincia) "
                        + "VALUES (102, 'Evento con embargo vigente', '2026-05-03', '2026-05-03', '2026-05-03', 1, TRUE, 1)",

                "INSERT INTO Evento (id, nombre, fecha, fecha_inicio, fecha_fin, id_agencia, asignacion_finalizada, id_provincia) "
                        + "VALUES (103, 'Evento con embargo caducado', '2026-05-04', '2026-05-04', '2026-05-04', 1, TRUE, 1)",

                // Reportajes
                "INSERT INTO Reportaje (id, id_evento, id_reportero_autor, titulo, estado, fecha_fin_embargo) "
                        + "VALUES (200, 100, 1, 'Reportaje terminado sin embargo', 'TERMINADO', NULL)",

                "INSERT INTO Reportaje (id, id_evento, id_reportero_autor, titulo, estado, fecha_fin_embargo) "
                        + "VALUES (201, 101, 1, 'Reportaje no terminado', 'EN_REVISION', '2030-12-31')",

                "INSERT INTO Reportaje (id, id_evento, id_reportero_autor, titulo, estado, fecha_fin_embargo) "
                        + "VALUES (202, 102, 1, 'Reportaje con embargo vigente', 'TERMINADO', '2030-12-31')",

                "INSERT INTO Reportaje (id, id_evento, id_reportero_autor, titulo, estado, fecha_fin_embargo) "
                        + "VALUES (203, 103, 1, 'Reportaje con embargo caducado', 'TERMINADO', '2020-01-01')",

                // Empresas de comunicación
                "INSERT INTO EmpresaComunicacion (id, nombre, email, tiene_tarifa_plana, al_corriente_pago, acepta_embargo) "
                        + "VALUES (300, 'Empresa tarifa plana al corriente', 'empresa300@test.com', TRUE, TRUE, TRUE)",

                "INSERT INTO EmpresaComunicacion (id, nombre, email, tiene_tarifa_plana, al_corriente_pago, acepta_embargo) "
                        + "VALUES (301, 'Empresa tarifa plana no al corriente', 'empresa301@test.com', TRUE, FALSE, TRUE)",

                "INSERT INTO EmpresaComunicacion (id, nombre, email, tiene_tarifa_plana, al_corriente_pago, acepta_embargo) "
                        + "VALUES (302, 'Empresa sin tarifa plana', 'empresa302@test.com', FALSE, FALSE, TRUE)",

                "INSERT INTO EmpresaComunicacion (id, nombre, email, tiene_tarifa_plana, al_corriente_pago, acepta_embargo) "
                        + "VALUES (303, 'Empresa no acepta embargo', 'empresa303@test.com', TRUE, TRUE, FALSE)",

                // Ofrecimientos para acceso normal
                "INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido, precio, descargado, acceso_especial, pagado) "
                        + "VALUES (400, 100, 300, 'ACEPTADO', FALSE, 0.0, FALSE, FALSE, FALSE)",

                "INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido, precio, descargado, acceso_especial, pagado) "
                        + "VALUES (401, 101, 300, 'ACEPTADO', FALSE, 0.0, FALSE, FALSE, FALSE)",

                "INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido, precio, descargado, acceso_especial, pagado) "
                        + "VALUES (402, 100, 301, 'ACEPTADO', FALSE, 0.0, FALSE, FALSE, FALSE)",

                "INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido, precio, descargado, acceso_especial, pagado) "
                        + "VALUES (403, 100, 302, 'ACEPTADO', FALSE, 0.0, FALSE, FALSE, FALSE)",

                // Ofrecimientos para acceso especial
                "INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido, precio, descargado, acceso_especial, pagado) "
                        + "VALUES (404, 102, 300, 'ACEPTADO', FALSE, 0.0, FALSE, FALSE, FALSE)",

                "INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido, precio, descargado, acceso_especial, pagado) "
                        + "VALUES (405, 102, 301, 'ACEPTADO', FALSE, 0.0, FALSE, FALSE, FALSE)",

                "INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido, precio, descargado, acceso_especial, pagado) "
                        + "VALUES (406, 102, 302, 'ACEPTADO', FALSE, 0.0, FALSE, FALSE, FALSE)",

                "INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido, precio, descargado, acceso_especial, pagado) "
                        + "VALUES (407, 103, 300, 'ACEPTADO', FALSE, 0.0, FALSE, FALSE, FALSE)",

                "INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido, precio, descargado, acceso_especial, pagado) "
                        + "VALUES (408, 102, 303, 'ACEPTADO', FALSE, 0.0, FALSE, FALSE, FALSE)"
        });
    }

    // PRUEBAS UNITARIAS concederAcceso(int idEvento, int idEmpresa)

    // CP1
    @Test
    public void testConcederAcceso_ReportajeTerminado_EmpresaTarifaPlanaAlCorriente() {
        DistribuirReportajeModel model = new DistribuirReportajeModel();

        model.concederAcceso(100, 300);

        assertEquals(1, getAccesoConcedido(100, 300));
    }

    // CP2
    @Test
    public void testConcederAcceso_ReportajeNoTerminado() {
        DistribuirReportajeModel model = new DistribuirReportajeModel();

        assertThrows(ApplicationException.class, () -> model.concederAcceso(101, 300));

        assertEquals(0, getAccesoConcedido(101, 300));
    }

    // CP3
    @Test
    public void testConcederAcceso_EmpresaTarifaPlanaNoAlCorriente() {
        DistribuirReportajeModel model = new DistribuirReportajeModel();

        assertThrows(ApplicationException.class, () -> model.concederAcceso(100, 301));

        assertEquals(0, getAccesoConcedido(100, 301));
    }

    // CP4
    @Test
    public void testConcederAcceso_EmpresaSinTarifaPlana() {
        DistribuirReportajeModel model = new DistribuirReportajeModel();

        model.concederAcceso(100, 302);

        assertEquals(1, getAccesoConcedido(100, 302));
    }

    
    // PRUEBAS UNITARIAS concederAccesoEspecial(int idEvento, int idEmpresa)

    // CP1
    @Test
    public void testConcederAccesoEspecial_ReportajeTerminado_TarifaPlanaAlCorriente_EmbargoVigente_AceptaEmbargo() {
        DistribuirReportajeModel model = new DistribuirReportajeModel();

        model.concederAccesoEspecial(102, 300);

        assertEquals(1, getAccesoEspecial(102, 300));
    }

    // CP2
    @Test
    public void testConcederAccesoEspecial_ReportajeNoTerminado() {
        DistribuirReportajeModel model = new DistribuirReportajeModel();

        assertThrows(ApplicationException.class, () -> model.concederAccesoEspecial(101, 300));

        assertEquals(0, getAccesoEspecial(101, 300));
    }

    // CP3
    @Test
    public void testConcederAccesoEspecial_EmpresaTarifaPlanaNoAlCorriente() {
        DistribuirReportajeModel model = new DistribuirReportajeModel();

        assertThrows(ApplicationException.class, () -> model.concederAccesoEspecial(102, 301));

        assertEquals(0, getAccesoEspecial(102, 301));
    }

    // CP4
    @Test
    public void testConcederAccesoEspecial_EmpresaSinTarifaPlana() {
        DistribuirReportajeModel model = new DistribuirReportajeModel();

        model.concederAccesoEspecial(102, 302);

        assertEquals(1, getAccesoEspecial(102, 302));
    }

    // CP5
    @Test
    public void testConcederAccesoEspecial_ReportajeSinEmbargo() {
        DistribuirReportajeModel model = new DistribuirReportajeModel();

        assertThrows(ApplicationException.class, () -> model.concederAccesoEspecial(100, 300));

        assertEquals(0, getAccesoEspecial(100, 300));
    }

    // CP6
    @Test
    public void testConcederAccesoEspecial_ReportajeConEmbargoCaducado() {
        DistribuirReportajeModel model = new DistribuirReportajeModel();

        assertThrows(ApplicationException.class, () -> model.concederAccesoEspecial(103, 300));

        assertEquals(0, getAccesoEspecial(103, 300));
    }

    // CP7
    @Test
    public void testConcederAccesoEspecial_EmpresaNoAceptaEmbargo() {
        DistribuirReportajeModel model = new DistribuirReportajeModel();

        assertThrows(ApplicationException.class, () -> model.concederAccesoEspecial(102, 303));

        assertEquals(0, getAccesoEspecial(102, 303));
    }

    private int getAccesoConcedido(int idEvento, int idEmpresa) {
        return db.executeQueryScalar(
                Integer.class,
                """
                SELECT acceso_concedido
                FROM Ofrecimiento
                WHERE id_evento = ?
                  AND id_empresa = ?
                """,
                idEvento,
                idEmpresa
        );
    }

    private int getAccesoEspecial(int idEvento, int idEmpresa) {
        return db.executeQueryScalar(
                Integer.class,
                """
                SELECT acceso_especial
                FROM Ofrecimiento
                WHERE id_evento = ?
                  AND id_empresa = ?
                """,
                idEvento,
                idEmpresa
        );
    }
}