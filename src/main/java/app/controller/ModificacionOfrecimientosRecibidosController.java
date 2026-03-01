package app.controller;

import java.util.List;
import javax.swing.JOptionPane;
import app.dto.OfrecimientoDTO;
import app.model.ModificacionOfrecimientosRecibidosModel;
import app.view.ModificacionOfrecimientosRecibidosView;

public class ModificacionOfrecimientosRecibidosController {

	private ModificacionOfrecimientosRecibidosModel model;
	private ModificacionOfrecimientosRecibidosView view;
	private int idEmpresa;
	private List<OfrecimientoDTO> datosActuales;

	public ModificacionOfrecimientosRecibidosController(ModificacionOfrecimientosRecibidosModel model, 
			ModificacionOfrecimientosRecibidosView view, int idEmpresa) {
		this.model = model;
		this.view = view;
		this.idEmpresa = idEmpresa;
		initController();
	}

	private void initController() {
		// Evento del botón filtrar
		view.getBtnFiltrar().addActionListener(e -> cargarDatos());

		// Eventos de decisión
		view.getBtnAceptar().addActionListener(e -> procesarCambio("ACEPTADO", true));
		view.getBtnRechazar().addActionListener(e -> procesarCambio("RECHAZADO", false));
		view.getBtnEliminar().addActionListener(e -> procesarCambio("PENDIENTE", false));

		// Carga inicial
		cargarDatos();
	}

	public void mostrarVista() {
		view.getFrame().setVisible(true);
	}

	private void cargarDatos() {
		// Miramos qué opción está seleccionada en el combo (0 = Pendientes, 1 = Decididos)
		boolean yaDecididos = view.getCbFiltro().getSelectedIndex() == 1;

		datosActuales = model.getOfrecimientosFiltrados(idEmpresa, yaDecididos);
		view.getModel().setRowCount(0);

		if (datosActuales.isEmpty()) {
			view.mostrarMensajeVacio(true);
		} else {
			view.mostrarMensajeVacio(false);
			for (OfrecimientoDTO o : datosActuales) {
				view.getModel().addRow(new Object[]{o.getNombre(), o.getFecha(), o.getEstado()});
			}
		}
	}

	private void procesarCambio(String nuevoEstado, boolean acceso) {
		// 1. Obtener la fila seleccionada
		int fila = view.getTable().getSelectedRow();

		if (fila == -1) {
			JOptionPane.showMessageDialog(view.getFrame(), 
					"Por favor, seleccione un registro de la tabla.", 
					"Aviso", 
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		// 2. Obtener el objeto de la lista
		OfrecimientoDTO seleccionado = datosActuales.get(fila);

		// 3. REGLA DE NEGOCIO: Bloqueo total si ya está ACEPTADO
		if ("ACEPTADO".equals(seleccionado.getEstado())) {
			JOptionPane.showMessageDialog(view.getFrame(), 
					"No se puede modificar la decisión: Los ofrecimientos ya aceptados no admiten cambios.", 
					"Acción Denegada", 
					JOptionPane.ERROR_MESSAGE);
			return; 
		}

		// 4. Confirmación extra si se va a "Eliminar" (PENDIENTE)
		if ("PENDIENTE".equals(nuevoEstado)) {
			int confirmar = JOptionPane.showConfirmDialog(view.getFrame(),
					"¿Desea eliminar la decisión actual y volver a dejar este evento como PENDIENTE?",
					"Confirmar eliminación",
					JOptionPane.YES_NO_OPTION);
			if (confirmar != JOptionPane.YES_OPTION) return;
		}

		// 5. Ejecutar la actualización en el Modelo
		try {
			model.actualizarEstadoOfrecimiento(seleccionado.getId(), nuevoEstado, acceso);

			// 6. Mensaje de éxito y refrescar
			String msg = nuevoEstado.equals("PENDIENTE") ? "Decisión eliminada." : "Estado actualizado a " + nuevoEstado;
			JOptionPane.showMessageDialog(view.getFrame(), msg);

			cargarDatos(); 

		} catch (Exception e) {
			JOptionPane.showMessageDialog(view.getFrame(), 
					"Error al actualizar la base de datos: " + e.getMessage(), 
					"Error", 
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
