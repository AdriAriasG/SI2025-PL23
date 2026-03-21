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
        view.getBtnAceptar().addActionListener(e -> procesarCambio("ACEPTADO", true));
        view.getBtnRechazar().addActionListener(e -> procesarCambio("RECHAZADO", false));
        view.getBtnEliminar().addActionListener(e -> procesarCambio("PENDIENTE", false));
        cargarDatos();
    }

    public void mostrarVista() { view.getFrame().setVisible(true); }

    private void actualizarComboTematicas() {
        cargandoCombo = true;
        view.getCbTematicas().removeAllItems();
        
        // 1. Creamos "Todas" y la MARCAMOS
        CheckItem todas = new CheckItem("--- Todas ---");
        todas.setSelected(true); 
        view.getCbTematicas().addItem(todas);

        List<TematicaDTO> tematicas;
        int seleccion = view.getCbTipoFiltro().getSelectedIndex();
        if (seleccion == 1) tematicas = model.getTematicasEmpresa(idEmpresa);
        else tematicas = model.getTodasTematicas();

        // 2. Marcamos todas las individuales para que coincidan con el maestro
        if (tematicas != null) {
            for (TematicaDTO t : tematicas) {
                CheckItem item = new CheckItem(t.getNombre());
                item.setSelected(true); 
                view.getCbTematicas().addItem(item);
            }
        }
        cargandoCombo = false;
    }

    private void cargarDatos() {
        if (cargandoCombo) return;
        int seleccion = view.getCbTipoFiltro().getSelectedIndex();
        Boolean yaDecididos = (seleccion == 2) ? Boolean.TRUE : (seleccion == 0 ? Boolean.FALSE : null);

        List<String> seleccionadas = new ArrayList<>();
        for (int i = 1; i < view.getCbTematicas().getItemCount(); i++) {
            CheckItem item = view.getCbTematicas().getItemAt(i);
            if (item != null && item.isSelected()) seleccionadas.add(item.toString());
        }

        datosActuales = model.getOfrecimientosFiltrados(idEmpresa, yaDecididos, seleccionadas);
        view.getModel().setRowCount(0);
        if (datosActuales != null && !datosActuales.isEmpty()) {
            view.mostrarMensajeVacio(false);
            for (OfrecimientoDTO o : datosActuales) {
                view.getModel().addRow(new Object[]{o.getNombre(), o.getFecha(), o.getEstado()});
            }
        } else view.mostrarMensajeVacio(true);
    }

    private void procesarCambio(String nuevoEstado, boolean acceso) {
        int fila = view.getTable().getSelectedRow();
        if (fila == -1) return;
        OfrecimientoDTO sel = datosActuales.get(fila);
        if ("ACEPTADO".equals(sel.getEstado()) && !nuevoEstado.equals("PENDIENTE")) return;
        try {
            model.actualizarEstadoOfrecimiento(sel.getId(), nuevoEstado, acceso);
            cargarDatos();
        } catch (Exception e) { e.printStackTrace(); }
    }
}