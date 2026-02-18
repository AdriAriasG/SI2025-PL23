package app.controller;


import java.util.List;

import app.dto.OfrecimientoDTO;
import app.model.OfrecimientosRecibidosModel;
import app.view.OfrecimientosRecibidosView;


public class OfrecimientosRecibidosController {
	
	private OfrecimientosRecibidosModel model;
	private OfrecimientosRecibidosView view;
	
	public OfrecimientosRecibidosController(OfrecimientosRecibidosModel model,
											OfrecimientosRecibidosView view) {
		this.model = model;
		this.view = view;
	}
	
	public void initController() {
		//Cargamos los datos de la base de datos en la tabla
		cargarDatosTabla();
		
		//Eventos de los botones
		view.getBtnAceptar().addActionListener(e -> aceptarOfrecimiento());
		view.getBtnRechazar().addActionListener(e -> rechazarOfrecimiento());
		
		//Mostrar la ventana
		view.getFrame().setVisible(true);
	}
	
	private void cargarDatosTabla() {
		
		view.getTableModel().setRowCount(0);
		
		List<OfrecimientoDTO> lista = model.getOfrecimientos();
		
		for(OfrecimientoDTO o : lista) {
			view.getTableModel().addRow(new Object[] {
					o.getNombre(),
					o.getFecha()
			});
		}
	}
	
	
	private void aceptarOfrecimiento() {
		
	}
	
	private void rechazarOfrecimiento() {
		
	}
	
	

}
