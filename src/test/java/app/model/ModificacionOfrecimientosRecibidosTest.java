package app.model;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import app.dto.OfrecimientoDTO;
import app.model.ModificacionOfrecimientosRecibidosModel;
import app.util.Database;

public class ModificacionOfrecimientosRecibidosTest {
    private ModificacionOfrecimientosRecibidosModel model;
    private Database db = new Database();

    @Before
    public void setUp() {
        model = new ModificacionOfrecimientosRecibidosModel();
        // Limpieza e inserción de datos iniciales para cada test
        db.executeUpdate("DELETE FROM Ofrecimiento");
        db.executeUpdate("INSERT INTO Ofrecimiento (id, estado, acceso_concedido, acceso_especial, id_empresa, id_evento, precio) " +
                         "VALUES (999, 'PENDIENTE', 0, 0, 1, 1, 100.0)");
    }

    // CP_01: Acceso no concedido
    @Test
    public void testCambioEstadoSinRestriccion() {
        model.actualizarEstadoDecision(999, "ACEPTADO", true);
        String estadoActual = db.executeQueryPojo(OfrecimientoDTO.class, "SELECT estado FROM Ofrecimiento WHERE id = 999").get(0).getEstado();
        assertEquals("ACEPTADO", estadoActual);
    }

    // CP_02: Acceso especial
    @Test
    public void testCambioPermitidoAccesoEspecial() {
        db.executeUpdate("UPDATE Ofrecimiento SET estado = 'ACEPTADO', acceso_concedido = 1, acceso_especial = 1 WHERE id = 999");
        model.actualizarEstadoDecision(999, "PENDIENTE", false);
        String estadoActual = db.executeQueryPojo(OfrecimientoDTO.class, "SELECT estado FROM Ofrecimiento WHERE id = 999").get(0).getEstado();
        assertEquals("PENDIENTE", estadoActual);
    }

    // CP_03: Sin acceso especial
    @Test
    public void testCambioNoPermitidoSinAccesoEspecial() {
        db.executeUpdate("UPDATE Ofrecimiento SET estado = 'ACEPTADO', acceso_concedido = 1, acceso_especial = 0 WHERE id = 999");
 
        model.actualizarEstadoDecision(999, "RECHAZADO", false);
        String estadoActual = db.executeQueryPojo(OfrecimientoDTO.class, "SELECT estado FROM Ofrecimiento WHERE id = 999").get(0).getEstado();
        assertEquals("ACEPTADO", estadoActual); 
    }

    // CP_04: ID Inexistente
    @Test
    public void testActualizarOfrecimientoInexistente() {
        model.actualizarEstadoDecision(888, "ACEPTADO", true);
        assertTrue("El sistema debe manejar IDs inexistentes sin lanzar error", true);
    }

    // CP_05: Estado redundante
    @Test
    public void testActualizarMismoEstado() {
        model.actualizarEstadoDecision(999, "PENDIENTE", false);
        String estadoActual = db.executeQueryPojo(OfrecimientoDTO.class, "SELECT estado FROM Ofrecimiento WHERE id = 999").get(0).getEstado();
        assertEquals("PENDIENTE", estadoActual);
    }

 // CP_06: Estado no válido
    @Test
    public void testEstadoInvalido() {
        //Se espera que la BD lance un error
        try {
            model.actualizarEstadoDecision(999, "ESTADO_INVENTADO", false);
            // Si no lanza error el test falla
            fail("Debería haber lanzado un error en la base de datos");
        } catch (Exception e) {
            // Si salta la excepción, el test pasa
            assertTrue(true);
        }
    }
}