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
		
		// NUEVO: Listener para el Checkbox de embargo
		view.getChkEmbargo().addActionListener(e -> cargarDatos());
		
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

	public void mostrarVista() {
		view.getFrame().setVisible(true);
	}

	private void gestionarEstadoBotones() {
		int fila = view.getTable().getSelectedRow();
		if (fila != -1 && datosActuales != null) {
			OfrecimientoDTO sel = datosActuales.get(fila);
			
			// REQUISITO: Se puede modificar si es PENDIENTE o si tiene ACCESO ESPECIAL (embargo)
			boolean bloqueado = (sel.getAccesoConcedido() == 1 && sel.getAccesoEspecial() == 0);
			
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

		// NUEVO: Leemos el estado del checkbox
		boolean soloEmbargados = view.getChkEmbargo().isSelected();

		List<String> seleccionadas = new ArrayList<>();
		for (int i = 1; i < view.getCbTematicas().getItemCount(); i++) {
			CheckItem item = view.getCbTematicas().getItemAt(i);
			if (item != null && item.isSelected()) seleccionadas.add(item.toString());
		}

		// NUEVO: Pasamos el filtro de embargo al modelo
		datosActuales = model.getOfrecimientosFiltrados(idEmpresa, yaDecididos, seleccionadas, pMin, pMax, soloEmbargados);
		
		view.getModel().setRowCount(0);
		if (datosActuales != null && !datosActuales.isEmpty()) {
			view.mostrarMensajeVacio(false);
			for (OfrecimientoDTO o : datosActuales) {
				// NUEVO: Lógica para la fecha de embargo (guion si es null)
				String validez = (o.getFechaFinEmbargo() == null) ? "   -" : o.getFechaFinEmbargo();
				
				view.getModel().addRow(new Object[]{
					o.getNombre(), 
					o.getFecha(), 
					String.format("%.2f €", o.getPrecio()), 
					o.getEstado(),
					validez // NUEVO: Quinta columna
				});
			}
		} else view.mostrarMensajeVacio(true);
		
		gestionarEstadoBotones();
	}

	private void procesarCambio(String nuevoEstado) {
		int fila = view.getTable().getSelectedRow();
		if (fila == -1) return;
		OfrecimientoDTO sel = datosActuales.get(fila);
		
		// REQUISITO: Permitir si es PENDIENTE o tiene ACCESO ESPECIAL
		if (sel.getAccesoConcedido() == 1 && sel.getAccesoEspecial() == 0) {
			JOptionPane.showMessageDialog(view.getFrame(), "Operación no permitida: Acceso ya concedido.");
			return;
		}

		try {
			// NUEVO: Pasamos si es aceptado para el flag de acceso_concedido
			boolean acceso = nuevoEstado.equals("ACEPTADO");
			model.actualizarEstadoDecision(sel.getId(), nuevoEstado, acceso);
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