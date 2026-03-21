package app.controller;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import app.dto.OfrecimientoDTO;
import app.dto.TematicaDTO;
import app.model.ModificacionOfrecimientosRecibidosModel;
import app.view.ModificacionOfrecimientosRecibidosView;
import app.view.ModificacionOfrecimientosRecibidosView.CheckItem;

public class ModificacionOfrecimientosRecibidosController {
	private ModificacionOfrecimientosRecibidosModel model;
	private ModificacionOfrecimientosRecibidosView view;
	private int idEmpresa;
	private List<OfrecimientoDTO> datosActuales;
	private boolean cargandoCombo = false;

	public ModificacionOfrecimientosRecibidosController(ModificacionOfrecimientosRecibidosModel model, 
			ModificacionOfrecimientosRecibidosView view, int idEmpresa) {
		this.model = model;
		this.view = view;
		this.idEmpresa = idEmpresa;
		initController();
	}

	private void initController() {
		actualizarComboTematicas();
		view.getCbTipoFiltro().addActionListener(e -> actualizarComboTematicas());
		view.getBtnFiltrar().addActionListener(e -> cargarDatos());
		
		// Listener para bloquear botones según el acceso concedido
		view.getTable().getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				gestionarEstadoBotones();
			}
		});

		view.getBtnAceptar().addActionListener(e -> procesarCambio("ACEPTADO"));
		view.getBtnRechazar().addActionListener(e -> procesarCambio("RECHAZADO"));
		view.getBtnEliminar().addActionListener(e -> procesarCambio("PENDIENTE"));
		cargarDatos();
	}

	// ESTE ES EL MÉTODO QUE FALTABA
	public void mostrarVista() {
		view.getFrame().setVisible(true);
	}

	private void gestionarEstadoBotones() {
		int fila = view.getTable().getSelectedRow();
		if (fila != -1 && datosActuales != null) {
			OfrecimientoDTO sel = datosActuales.get(fila);
			boolean bloqueado = (sel.getAccesoConcedido() == 1);
			
			view.getBtnAceptar().setEnabled(!bloqueado);
			view.getBtnRechazar().setEnabled(!bloqueado);
			view.getBtnEliminar().setEnabled(!bloqueado);
			
			String msg = bloqueado ? "No se puede modificar: El acceso ya ha sido concedido." : null;
			view.getBtnAceptar().setToolTipText(msg);
		}
	}

	private void cargarDatos() {
		if (cargandoCombo) return;
		Double pMin = parsePrecio(view.getTxtPrecioMin().getText());
		Double pMax = parsePrecio(view.getTxtPrecioMax().getText());

		int seleccion = view.getCbTipoFiltro().getSelectedIndex();
		Boolean yaDecididos = (seleccion == 2) ? Boolean.TRUE : (seleccion == 0 ? Boolean.FALSE : null);

		List<String> seleccionadas = new ArrayList<>();
		for (int i = 1; i < view.getCbTematicas().getItemCount(); i++) {
			CheckItem item = view.getCbTematicas().getItemAt(i);
			if (item != null && item.isSelected()) seleccionadas.add(item.toString());
		}

		datosActuales = model.getOfrecimientosFiltrados(idEmpresa, yaDecididos, seleccionadas, pMin, pMax);
		
		view.getModel().setRowCount(0);
		if (datosActuales != null && !datosActuales.isEmpty()) {
			view.mostrarMensajeVacio(false);
			for (OfrecimientoDTO o : datosActuales) {
				view.getModel().addRow(new Object[]{
					o.getNombre(), 
					o.getFecha(), 
					String.format("%.2f €", o.getPrecio()), 
					o.getEstado()
				});
			}
		} else view.mostrarMensajeVacio(true);
		
		gestionarEstadoBotones();
	}

	private void procesarCambio(String nuevoEstado) {
		int fila = view.getTable().getSelectedRow();
		if (fila == -1) return;
		OfrecimientoDTO sel = datosActuales.get(fila);
		
		if (sel.getAccesoConcedido() == 1) {
			JOptionPane.showMessageDialog(view.getFrame(), "Operación no permitida: Acceso ya concedido.");
			return;
		}

		try {
			model.actualizarEstadoDecision(sel.getId(), nuevoEstado);
			cargarDatos();
		} catch (Exception e) { e.printStackTrace(); }
	}

	private Double parsePrecio(String texto) {
		if (texto == null || texto.trim().isEmpty()) return null;
		try { return Double.parseDouble(texto.replace(",", ".")); } catch (Exception e) { return null; }
	}

	private void actualizarComboTematicas() {
		cargandoCombo = true;
		view.getCbTematicas().removeAllItems();
		CheckItem todas = new CheckItem("--- Todas ---");
		todas.setSelected(true);
		view.getCbTematicas().addItem(todas);
		List<TematicaDTO> tematicas;
		int seleccion = view.getCbTipoFiltro().getSelectedIndex();
		if (seleccion == 1) tematicas = model.getTematicasEmpresa(idEmpresa);
		else tematicas = model.getTodasTematicas();
		if (tematicas != null) {
			for (TematicaDTO t : tematicas) {
				CheckItem item = new CheckItem(t.getNombre());
				item.setSelected(true);
				view.getCbTematicas().addItem(item);
			}
		}
		cargandoCombo = false;
	}
}