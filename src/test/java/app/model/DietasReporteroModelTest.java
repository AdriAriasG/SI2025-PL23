package app.model;

import app.dto.DietaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DietasReporteroModelTest {

    private DietasReporteroModel model;

    @BeforeEach
    void setUp() {
        model = new DietasReporteroModel();
    }

    // CP1 — Evento 10, Reportero 1 (misma provincia)
    @Test
    void calcularDietas_mismaProvincia_noCobraAlojamiento() {

        DietaDTO dieta = model.calcularDietas(10, 1);

        assertEquals(62, dieta.getNumeroDias());
        assertEquals(0.0, dieta.getImporteAlojamiento());
        assertEquals(30.0, dieta.getImporteManutencion());
        assertEquals(1860.0, dieta.getTotal());
    }

    // CP2 — Evento 12 (provincia 3 Paris 120€), reportero 1 (provincia 1)
    @Test
    void calcularDietas_distintaProvincia_cobraAlojamiento() {

        DietaDTO dieta = model.calcularDietas(12, 1);

        // Evento 12 → 2026-03-01 a 2026-05-01 → 62 días
        // Provincia 3 → 120€
        // País Francia → 40€

        assertEquals(62, dieta.getNumeroDias());
        assertEquals(120.0, dieta.getImporteAlojamiento());
        assertEquals(40.0, dieta.getImporteManutencion());
        assertEquals((120.0 + 40.0) * 62, dieta.getTotal());
    }

    // CP3 — Evento largo (ej. 420)
    @Test
    void calcularDietas_eventoAgosto_correcto() {

        DietaDTO dieta = model.calcularDietas(420, 420);

        // Evento 420 → 2026-08-01 a 2026-10-01
        // Agosto (31) + Septiembre (30) + 1 Octubre = 62 días
        // Provincia 3 → 120€
        // País Francia → 40€
        // Reportero 420 vive en provincia 6 → distinta → cobra alojamiento

        assertEquals(62, dieta.getNumeroDias());
        assertEquals(120.0, dieta.getImporteAlojamiento());
        assertEquals(40.0, dieta.getImporteManutencion());
        assertEquals((120.0 + 40.0) * 62, dieta.getTotal());
    }

    // CP4 — Fechas inválidas (debe fallar si existe alguno así en BD)
    @Test
    void calcularDietas_idEventoInexistente_lanzaExcepcion() {

        assertThrows(Exception.class, () ->
                model.calcularDietas(9999, 1)
        );
    }
}