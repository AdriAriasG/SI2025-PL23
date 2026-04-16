package app;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import app.model.AsignacionModel;
import app.util.Database;

/**
 * Pruebas JUnit del proceso de negocio: Validación y finalización de la asignación de reporteros.
 *
 * HU #34426 - Para poder finalizar la asignación de un evento deben cumplirse dos condiciones:
 *   1. Debe haber un Reportero Responsable (RR) designado.
 *   2. Debe haber al menos un reportero de tipo BASE asignado que NO sea el RR.
 *      (Si el RR es de tipo BASE, igualmente se requiere otro BASE adicional no-RR.)
 *
 * Métodos bajo prueba en AsignacionModel:
 *   - setReporteroResponsable(idEvento, idReportero)
 *   - getReporteroResponsable(idEvento)          → -1 si no hay RR
 *   - tieneReporteroBaseNoRR(idEvento)            → true si hay BASE que no es RR
 *   - finalizarAsignacion(idEvento)
 *   - isAsignacionFinalizada(idEvento)
 */
public class TestFinalizacionAsignacion {

    private static final Database db = new Database();
    private AsignacionModel model;

    private static final int ID_AGENCIA  = 1;
    private static final int ID_EVENTO   = 1;
    private static final int ID_GRAFICO  = 1; // tipo GRAFICO
    private static final int ID_BASE_1   = 2; // tipo BASE (primer BASE)
    private static final int ID_BASE_2   = 3; // tipo BASE (segundo BASE adicional)

    @BeforeEach
    void setUp() {
        db.createDatabase(false); // siempre recrea el esquema limpio
        model = new AsignacionModel();
        db.executeBatch(new String[]{
            "INSERT INTO Pais(id,nombre,precio_manutencion) VALUES (1,'España',50.0)",
            "INSERT INTO Provincia(id,nombre,precio_alojamiento,id_pais) VALUES (1,'Asturias',80.0,1)",
            "INSERT INTO AgenciaPrensa(id,nombre,email) VALUES (1,'Agencia Test','ag@test.com')",
            "INSERT INTO Reportero(id,nombre,tipo,id_agencia,id_provincia) VALUES (1,'Rep Grafico','GRAFICO',1,1)",
            "INSERT INTO Reportero(id,nombre,tipo,id_agencia,id_provincia) VALUES (2,'Rep Base 1','BASE',1,1)",
            "INSERT INTO Reportero(id,nombre,tipo,id_agencia,id_provincia) VALUES (3,'Rep Base 2','BASE',1,1)",
            "INSERT INTO Evento(id,nombre,fecha,fecha_inicio,fecha_fin,id_agencia,id_provincia) VALUES (1,'Evento Test','2026-06-10','2026-06-10','2026-06-15',1,1)"
        });
    }

    /**
     * CP1 (CE1): RR=GRAFICO + reportero BASE asignado → todas las precondiciones se cumplen → finalización correcta.
     * Verifica: getRR ≠ -1, tieneBaseNoRR = true, isAsignacionFinalizada = true tras llamar a finalizarAsignacion.
     */
    @Test
    void cp1_rrGraficoMasBase_finalizaCorrectamente() {
        db.executeBatch(new String[]{
            "INSERT INTO Asignacion(id_evento,id_reportero) VALUES (1,1)", // GRAFICO
            "INSERT INTO Asignacion(id_evento,id_reportero) VALUES (1,2)"  // BASE
        });
        model.setReporteroResponsable(ID_EVENTO, ID_GRAFICO);

        assertAll("Precondiciones antes de finalizar (CE1)",
            () -> assertEquals(ID_GRAFICO, model.getReporteroResponsable(ID_EVENTO),
                    "El RR debe ser el reportero gráfico"),
            () -> assertTrue(model.tieneReporteroBaseNoRR(ID_EVENTO),
                    "Debe haber al menos un BASE que no sea el RR"),
            () -> assertFalse(model.isAsignacionFinalizada(ID_EVENTO),
                    "El evento no debe estar finalizado antes de llamar a finalizarAsignacion")
        );

        model.finalizarAsignacion(ID_EVENTO);

        assertTrue(model.isAsignacionFinalizada(ID_EVENTO),
            "Tras finalizarAsignacion(), el evento debe quedar marcado como finalizado");
    }

    /**
     * CP2 (CE2): Sin ningún RR designado → getReporteroResponsable devuelve -1.
     * La asignación no puede finalizarse (condición 1 no cumplida).
     */
    @Test
    void cp2_sinRrDesignado_getRrDevuelveMenosUno() {
        db.executeUpdate("INSERT INTO Asignacion(id_evento,id_reportero) VALUES (?,?)",
            ID_EVENTO, ID_BASE_1);
        assertEquals(-1, model.getReporteroResponsable(ID_EVENTO),
            "Sin RR designado, getReporteroResponsable debe devolver -1");
    }

    /**
     * CP3 (CE3): RR=GRAFICO designado, pero ningún reportero BASE asignado → tieneReporteroBaseNoRR = false.
     * La asignación no puede finalizarse (condición 2 no cumplida).
     */
    @Test
    void cp3_rrGraficoSinBase_tieneBaseNoRRFalse() {
        db.executeUpdate("INSERT INTO Asignacion(id_evento,id_reportero) VALUES (?,?)",
            ID_EVENTO, ID_GRAFICO);
        model.setReporteroResponsable(ID_EVENTO, ID_GRAFICO);

        assertFalse(model.tieneReporteroBaseNoRR(ID_EVENTO),
            "Sin reportero BASE asignado (solo el RR de tipo GRAFICO), tieneReporteroBaseNoRR debe ser false");
    }

    /**
     * CP4 (CE4): RR=BASE, pero es el único BASE asignado → tieneReporteroBaseNoRR = false.
     * El método excluye al RR del conteo, por lo que aunque el RR sea BASE no satisface la condición.
     */
    @Test
    void cp4_rrBaseUnico_tieneBaseNoRRFalse() {
        db.executeUpdate("INSERT INTO Asignacion(id_evento,id_reportero) VALUES (?,?)",
            ID_EVENTO, ID_BASE_1);
        model.setReporteroResponsable(ID_EVENTO, ID_BASE_1);

        assertAll("CP4: RR=BASE sin otro BASE adicional",
            () -> assertEquals(ID_BASE_1, model.getReporteroResponsable(ID_EVENTO),
                    "El RR debe ser el BASE 1"),
            () -> assertFalse(model.tieneReporteroBaseNoRR(ID_EVENTO),
                    "Con RR de tipo BASE pero sin otro BASE adicional, tieneReporteroBaseNoRR debe ser false")
        );
    }

    /**
     * CP5 (CE5): RR=BASE + un segundo BASE adicional no-RR → todas las condiciones se cumplen → finalización correcta.
     */
    @Test
    void cp5_rrBaseMasOtroBase_finalizaCorrectamente() {
        db.executeBatch(new String[]{
            "INSERT INTO Asignacion(id_evento,id_reportero) VALUES (1,2)", // BASE 1 → será RR
            "INSERT INTO Asignacion(id_evento,id_reportero) VALUES (1,3)"  // BASE 2 → no RR
        });
        model.setReporteroResponsable(ID_EVENTO, ID_BASE_1);

        assertAll("Precondiciones antes de finalizar (CE5)",
            () -> assertEquals(ID_BASE_1, model.getReporteroResponsable(ID_EVENTO),
                    "El RR debe ser el BASE 1"),
            () -> assertTrue(model.tieneReporteroBaseNoRR(ID_EVENTO),
                    "BASE 2 satisface la condición de BASE no-RR")
        );

        model.finalizarAsignacion(ID_EVENTO);

        assertTrue(model.isAsignacionFinalizada(ID_EVENTO),
            "Con RR=BASE y otro BASE adicional, la asignación debe poder finalizarse correctamente");
    }
}
