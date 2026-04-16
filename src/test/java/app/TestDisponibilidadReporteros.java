package app;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import app.dto.ReporteroDTO;
import app.model.AsignacionModel;
import app.util.Database;

/**
 * Pruebas JUnit del proceso de negocio: Disponibilidad de reporteros con solapamiento de rangos de fechas.
 *
 * HU #34430 - Un reportero está disponible para un evento si no tiene ningún otro evento asignado
 * cuyo rango de fechas se solape en algún día con el rango del nuevo evento.
 * Condición de solape: inicio_A <= fin_B AND fin_A >= inicio_B.
 *
 * Método principal bajo prueba: AsignacionModel.getReporterosDisponibles(idEvento, idAgencia)
 */
public class TestDisponibilidadReporteros {

    private static final Database db = new Database();
    private AsignacionModel model;

    // IDs de entidades de test
    private static final int ID_AGENCIA_1        = 1;
    private static final int ID_REP_LIBRE        = 1; // reportero sin asignaciones al comenzar cada test
    private static final int ID_REP_OTRA_AGENCIA = 2; // reportero de agencia distinta
    private static final int ID_EVENTO_OBJETIVO      = 1; // 2026-06-10 → 2026-06-15
    private static final int ID_EVENTO_NO_SOLAPA     = 2; // 2026-06-01 → 2026-06-09 (termina el día anterior)
    private static final int ID_EVENTO_SOLAPA_INICIO = 3; // 2026-06-08 → 2026-06-10 (termina el mismo día que empieza el objetivo)
    private static final int ID_EVENTO_SOLAPA_INT    = 4; // 2026-06-12 → 2026-06-20 (dentro del rango del objetivo)

    @BeforeEach
    void setUp() {
        db.createDatabase(false); // siempre recrea el esquema limpio
        model = new AsignacionModel();
        db.executeBatch(new String[]{
            "INSERT INTO Pais(id,nombre,precio_manutencion) VALUES (1,'España',50.0)",
            "INSERT INTO Provincia(id,nombre,precio_alojamiento,id_pais) VALUES (1,'Asturias',80.0,1)",
            "INSERT INTO AgenciaPrensa(id,nombre,email) VALUES (1,'Agencia Test 1','ag1@test.com')",
            "INSERT INTO AgenciaPrensa(id,nombre,email) VALUES (2,'Agencia Test 2','ag2@test.com')",
            // Reportero libre (agencia 1) y reportero de otra agencia
            "INSERT INTO Reportero(id,nombre,tipo,id_agencia,id_provincia) VALUES (1,'Rep Sin Asignacion','BASE',1,1)",
            "INSERT INTO Reportero(id,nombre,tipo,id_agencia,id_provincia) VALUES (2,'Rep Otra Agencia','BASE',2,1)",
            // Evento objetivo: 10/06 – 15/06
            "INSERT INTO Evento(id,nombre,fecha,fecha_inicio,fecha_fin,id_agencia,id_provincia) VALUES (1,'Evento Objetivo','2026-06-10','2026-06-10','2026-06-15',1,1)",
            // Termina el 09/06 → no solapa con inicio 10/06
            "INSERT INTO Evento(id,nombre,fecha,fecha_inicio,fecha_fin,id_agencia,id_provincia) VALUES (2,'Evento No Solapa','2026-06-01','2026-06-01','2026-06-09',1,1)",
            // Termina el 10/06 = primer día del objetivo → solapa exactamente 1 día
            "INSERT INTO Evento(id,nombre,fecha,fecha_inicio,fecha_fin,id_agencia,id_provincia) VALUES (3,'Evento Solapa Inicio','2026-06-08','2026-06-08','2026-06-10',1,1)",
            // 12/06–20/06 → contenido dentro del rango del objetivo → solapa
            "INSERT INTO Evento(id,nombre,fecha,fecha_inicio,fecha_fin,id_agencia,id_provincia) VALUES (4,'Evento Solapa Interior','2026-06-12','2026-06-12','2026-06-20',1,1)"
        });
    }

    /** Auxiliar: comprueba si un reportero con el id dado aparece en la lista. */
    private boolean contieneReportero(List<ReporteroDTO> lista, int idReportero) {
        return lista.stream().anyMatch(r -> r.getId() == idReportero);
    }

    /**
     * CP1 (CE1): Reportero sin ningún evento asignado → debe aparecer como disponible.
     */
    @Test
    void cp1_sinAsignacionesPrevias_reporteroDisponible() {
        List<ReporteroDTO> disponibles = model.getReporterosDisponibles(ID_EVENTO_OBJETIVO, ID_AGENCIA_1);
        assertTrue(contieneReportero(disponibles, ID_REP_LIBRE),
            "Reportero sin asignaciones previas debe aparecer en la lista de disponibles");
    }

    /**
     * CP2 (CE2): Reportero asignado a un evento que termina el día anterior al inicio del objetivo → disponible.
     * Evento previo: 01/06–09/06. Objetivo: 10/06–15/06. No hay día en común → no solapa.
     */
    @Test
    void cp2_eventoPrevioTerminaElDiaAnterior_reporteroDisponible() {
        db.executeUpdate("INSERT INTO Asignacion(id_evento,id_reportero) VALUES (?,?)",
            ID_EVENTO_NO_SOLAPA, ID_REP_LIBRE);
        List<ReporteroDTO> disponibles = model.getReporterosDisponibles(ID_EVENTO_OBJETIVO, ID_AGENCIA_1);
        assertTrue(contieneReportero(disponibles, ID_REP_LIBRE),
            "Evento previo que termina el día anterior al inicio del objetivo no debe bloquear la disponibilidad");
    }

    /**
     * CP3 (CE3): Reportero asignado a un evento cuyo último día coincide con el primer día del objetivo → NO disponible.
     * Evento previo: 08/06–10/06. Objetivo: 10/06–15/06. Comparten el 10/06 → solape de 1 día.
     */
    @Test
    void cp3_eventoPrevioTerminaEnInicioObjetivo_reporteroNoDisponible() {
        db.executeUpdate("INSERT INTO Asignacion(id_evento,id_reportero) VALUES (?,?)",
            ID_EVENTO_SOLAPA_INICIO, ID_REP_LIBRE);
        List<ReporteroDTO> disponibles = model.getReporterosDisponibles(ID_EVENTO_OBJETIVO, ID_AGENCIA_1);
        assertFalse(contieneReportero(disponibles, ID_REP_LIBRE),
            "Evento previo cuyo último día coincide con el inicio del objetivo debe bloquear la disponibilidad (solape de 1 día)");
    }

    /**
     * CP4 (CE4): Reportero asignado a un evento que solapa en el interior del rango del objetivo → NO disponible.
     * Evento previo: 12/06–20/06 (dentro del objetivo 10/06–15/06). Solapamiento parcial.
     */
    @Test
    void cp4_eventoSolapaInterior_reporteroNoDisponible() {
        db.executeUpdate("INSERT INTO Asignacion(id_evento,id_reportero) VALUES (?,?)",
            ID_EVENTO_SOLAPA_INT, ID_REP_LIBRE);
        List<ReporteroDTO> disponibles = model.getReporterosDisponibles(ID_EVENTO_OBJETIVO, ID_AGENCIA_1);
        assertFalse(contieneReportero(disponibles, ID_REP_LIBRE),
            "Evento previo solapado en el interior del rango objetivo debe bloquear la disponibilidad");
    }

    /**
     * CP5 (CE5): Reportero ya asignado al propio evento objetivo → NO aparece en disponibles.
     */
    @Test
    void cp5_yaAsignadoAlEventoObjetivo_reporteroNoDisponible() {
        db.executeUpdate("INSERT INTO Asignacion(id_evento,id_reportero) VALUES (?,?)",
            ID_EVENTO_OBJETIVO, ID_REP_LIBRE);
        List<ReporteroDTO> disponibles = model.getReporterosDisponibles(ID_EVENTO_OBJETIVO, ID_AGENCIA_1);
        assertFalse(contieneReportero(disponibles, ID_REP_LIBRE),
            "Reportero ya asignado al evento objetivo no debe aparecer en la lista de disponibles");
    }

    /**
     * CP6 (CE6): Reportero perteneciente a una agencia distinta → no aparece en la lista.
     */
    @Test
    void cp6_reporteroOtraAgencia_noApareceEnDisponibles() {
        List<ReporteroDTO> disponibles = model.getReporterosDisponibles(ID_EVENTO_OBJETIVO, ID_AGENCIA_1);
        assertFalse(contieneReportero(disponibles, ID_REP_OTRA_AGENCIA),
            "Reportero de una agencia distinta no debe aparecer en la lista de disponibles de la agencia 1");
    }
}
