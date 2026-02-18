package app.controller;


import java.util.List;

import javax.swing.JOptionPane;

import app.dto.OfrecimientoDTO;
import app.model.OfrecimientosRecibidosModel;
import app.view.OfrecimientosRecibidosView;


public class OfrecimientosRecibidosController {
	
	private OfrecimientosRecibidosModel model;
	private OfrecimientosRecibidosView view;
	private List<OfrecimientoDTO> datosActuales;
	
	public OfrecimientosRecibidosController(OfrecimientosRecibidosModel model,
											OfrecimientosRecibidosView view) {
		this.model = model;
		this.view = view;
	}
	
	public void initController() {
		//Cargamos los datos de la base de datos en la tabla
		cargarDatosTabla();
		
		//Eventos de los botones
		view.getBtnAceptar().addActionListener(e -> gestionar("ACEPTADO", true));
		view.getBtnRechazar().addActionListener(e -> gestionar("RECHAZADO", false));
		
		//Mostrar la ventana
		view.getFrame().setVisible(true);
	}
	
	private void cargarDatosTabla() {
		
		
		view.getTableModel().setRowCount(0);
		
		datosActuales = model.getOfrecimientos();
		if(datosActuales.isEmpty()) {
			view.mostrarMensajeVacio(true);
		}
		else {
			view.mostrarMensajeVacio(false);
			for(OfrecimientoDTO o : datosActuales) {
				view.getTableModel().addRow(new Object[] {
						o.getNombre(),
						o.getFecha()
				});
		
			}
		}
	}
	
	
	private void gestionar(String nuevoEstado, boolean accesoConcedido) {
		
		int fila = view.getTable().getSelectedRow();
		
		if(fila == -1) {
			JOptionPane.showMessageDialog(view.getFrame(), "Por favor, seleccione un evento", "Ningún evento seleccionado",JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		int idOfrecimiento = datosActuales.get(fila).getId();
		model.actualizarEstadoOfrecimiento(idOfrecimiento, nuevoEstado, accesoConcedido);
		JOptionPane.showMessageDialog(view.getFrame(), "Operación realizada con éxito");
		cargarDatosTabla();
		
	}
	
}
