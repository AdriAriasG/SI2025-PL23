package app.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import app.dto.AgenciaDTO;
import app.dto.EmpresaComunicacionDTO;
import app.dto.EventoDTO;
import app.model.ModificarOfrecimientoModel;
import app.util.ApplicationException;
import app.util.EmailService;
import app.view.ModificarOfrecimientoView;

public class ModificarOfrecimientoController {

    private final ModificarOfrecimientoModel model;
    private final ModificarOfrecimientoView view;
    private final AgenciaDTO agenciaSeleccionada;

    private Integer idEventoSeleccionado = null;

    private final Set<Integer> empresasOfrecer = new HashSet<>();
    private final Set<Integer> empresasQuitar = new HashSet<>();

    public ModificarOfrecimientoController(
            ModificarOfrecimientoModel model,
            ModificarOfrecimientoView view,
            AgenciaDTO agenciaSeleccionada) {

        this.model = model;
        this.view = view;
        this.agenciaSeleccionada = agenciaSeleccionada;

        initView();
        initController();
    }

    private void initView() {
        limpiarTablasEmpresas();
        view.setLocationRelativeTo(null);
        view.setVisible(true);
        cargarEventos();
    }

    private void initController() {
        view.getBtnAceptar().addActionListener(e -> ejecutarSeguro(this::guardarCambios));
        view.getComboFiltro().addActionListener(e -> ejecutarSeguro(this::cargarEmpresas));
        view.getComboTematica().addActionListener(e -> ejecutarSeguro(this::cargarEmpresas));
        view.getComboTarifaPlana().addActionListener(e -> ejecutarSeguro(this::cargarEmpresas));
        view.getBtnOfrecer().addActionListener(e -> ejecutarSeguro(this::ofrecerEmpresa));
        view.getBtnQuitar().addActionListener(e -> ejecutarSeguro(this::quitarEmpresa));
        view.getBtnCancelar().addActionListener(e -> view.dispose());

        view.getTablaEventos().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                ejecutarSeguro(this::seleccionarEvento);
            }
        });
    }

    private void ejecutarSeguro(Runnable accion) {
        try {
            accion.run();
        } catch (ApplicationException ex) {
            JOptionPane.showMessageDialog(
                    view,
                    ex.getMessage(),
                    "Operación no permitida",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void guardarCambios() {
        if (idEventoSeleccionado == null) {
            throw new ApplicationException("Debes seleccionar un evento.");
        }

        // Prevalidar todo para evitar guardar cambios parciales por reglas de negocio.
        for (Integer idEmpresa : empresasOfrecer) {
            model.validarPuedeOfrecer(idEventoSeleccionado, idEmpresa);
        }
        for (Integer idEmpresa : empresasQuitar) {
            model.validarPuedeQuitar(idEventoSeleccionado, idEmpresa);
        }

        for (Integer idEmpresa : empresasOfrecer) {
            model.ofrecerEmpresa(idEventoSeleccionado, idEmpresa);
        }

        for (Integer idEmpresa : empresasQuitar) {
            boolean notificar = model.quitarOfrecimiento(idEventoSeleccionado, idEmpresa);
            if (notificar) {
                enviarEmailCancelacion(idEmpresa);
            }
        }

        empresasOfrecer.clear();
        empresasQuitar.clear();
        view.dispose();
    }

    private void enviarEmailCancelacion(int idEmpresa) {
        String emailAgencia = model.getEmailAgencia(agenciaSeleccionada.getId());
        String emailEmpresa = model.getEmailEmpresa(idEmpresa);
        String nombreEvento = model.getNombreEvento(idEventoSeleccionado);

        if (emailEmpresa == null || emailAgencia == null) {
            return;
        }

        EmailService emailService = new EmailService(emailAgencia, "ehpxhktyjwyfkjmm");
        String asunto = "Cancelación de ofrecimiento";
        String cuerpo = "Estimados,\n\n"
                + "La agencia " + agenciaSeleccionada.getNombre()
                + " ha cancelado el ofrecimiento del reportaje del evento '"
                + nombreEvento + "'.\n\nUn saludo.";

        emailService.enviarEmail(emailEmpresa, asunto, cuerpo, emailAgencia);
    }

    private void seleccionarEvento() {
        int fila = view.getTablaEventos().getSelectedRow();
        if (fila == -1) {
            return;
        }

        idEventoSeleccionado = (int) view.getTablaEventos().getValueAt(fila, 0);
        empresasOfrecer.clear();
        empresasQuitar.clear();
        cargarEmpresas();
    }

    private void cargarEmpresas() {
        if (idEventoSeleccionado == null) {
            limpiarTablasEmpresas();
            return;
        }

        boolean filtrarPorTematica = isFiltrarPorTematica();
        boolean soloTarifaPlana = isSoloTarifaPlana();

        List<EmpresaComunicacionDTO> con;
        List<EmpresaComunicacionDTO> sin;

        if (filtrarPorTematica) {
            con = new ArrayList<>(model.getEmpresasConOfrecimientoConTematicaCoincidente(
                    idEventoSeleccionado, soloTarifaPlana));
            sin = new ArrayList<>(model.getEmpresasSinOfrecimientoConTematicaCoincidente(
                    idEventoSeleccionado, soloTarifaPlana));
        } else {
            con = new ArrayList<>(model.getEmpresasConOfrecimiento(idEventoSeleccionado, soloTarifaPlana));
            sin = new ArrayList<>(model.getEmpresasSinOfrecimiento(idEventoSeleccionado, soloTarifaPlana));
        }

        for (Integer idEmpresa : empresasOfrecer) {
            sin.removeIf(emp -> emp.getId() == idEmpresa);
            if (cumpleFiltrosTablaOfrecidas(idEmpresa, filtrarPorTematica, soloTarifaPlana)
                    && con.stream().noneMatch(emp -> emp.getId() == idEmpresa)) {
                EmpresaComunicacionDTO empresa = model.getEmpresaById(idEmpresa);
                if (empresa != null) {
                    con.add(empresa);
                }
            }
        }

        for (Integer idEmpresa : empresasQuitar) {
            con.removeIf(emp -> emp.getId() == idEmpresa);
            if (cumpleFiltrosTablaDisponibles(idEmpresa, filtrarPorTematica, soloTarifaPlana)
                    && sin.stream().noneMatch(emp -> emp.getId() == idEmpresa)) {
                EmpresaComunicacionDTO empresa = model.getEmpresaById(idEmpresa);
                if (empresa != null) {
                    sin.add(empresa);
                }
            }
        }

        boolean modoSin = isModoSin();

        if (modoSin) {
            cargarTabla(view.getTablaDisponibles(), sin);

            List<EmpresaComunicacionDTO> soloMovidas = new ArrayList<>();
            for (EmpresaComunicacionDTO empresa : con) {
                if (empresasOfrecer.contains(empresa.getId())) {
                    soloMovidas.add(empresa);
                }
            }
            cargarTabla(view.getTablaOfrecidas(), soloMovidas);
        } else {
            cargarTabla(view.getTablaOfrecidas(), con);

            List<EmpresaComunicacionDTO> soloMovidas = new ArrayList<>();
            for (EmpresaComunicacionDTO empresa : sin) {
                if (empresasQuitar.contains(empresa.getId())) {
                    soloMovidas.add(empresa);
                }
            }
            cargarTabla(view.getTablaDisponibles(), soloMovidas);
        }
    }

    private boolean isFiltrarPorTematica() {
        String opcionTematica = (String) view.getComboTematica().getSelectedItem();
        return opcionTematica != null && opcionTematica.equals("Con temática coincidente");
    }

    private boolean isSoloTarifaPlana() {
        String opcionTarifa = (String) view.getComboTarifaPlana().getSelectedItem();
        return opcionTarifa != null && opcionTarifa.equals("Solo empresas con tarifa plana");
    }

    private boolean isModoSin() {
        String opcion = (String) view.getComboFiltro().getSelectedItem();
        return opcion != null && opcion.toLowerCase().contains("sin");
    }

    private boolean cumpleFiltrosTablaOfrecidas(int idEmpresa, boolean filtrarPorTematica, boolean soloTarifaPlana) {
        if (soloTarifaPlana && !model.tieneTarifaPlana(idEmpresa)) {
            return false;
        }
        if (filtrarPorTematica && !model.empresaTieneTematicaCoincidente(idEventoSeleccionado, idEmpresa)) {
            return false;
        }
        return true;
    }

    private boolean cumpleFiltrosTablaDisponibles(int idEmpresa, boolean filtrarPorTematica, boolean soloTarifaPlana) {
        if (!model.asignacionFinalizada(idEventoSeleccionado)) {
            return false;
        }
        if (soloTarifaPlana && !model.tieneTarifaPlana(idEmpresa)) {
            return false;
        }
        if (filtrarPorTematica && !model.empresaTieneTematicaCoincidente(idEventoSeleccionado, idEmpresa)) {
            return false;
        }
        if (model.tieneTarifaPlana(idEmpresa) && !model.estaAlCorrientePago(idEmpresa)) {
            return false;
        }
        return true;
    }

    private void cargarTabla(JTable tabla, List<EmpresaComunicacionDTO> datos) {
        DefaultTableModel modelTabla = (DefaultTableModel) tabla.getModel();
        modelTabla.setRowCount(0);

        Set<Integer> idsPintados = new HashSet<>();
        for (EmpresaComunicacionDTO empresa : datos) {
            if (idsPintados.add(empresa.getId())) {
                modelTabla.addRow(new Object[] { empresa });
            }
        }
    }

    private void ofrecerEmpresa() {
        if (idEventoSeleccionado == null) {
            throw new ApplicationException("Debes seleccionar un evento.");
        }

        int fila = view.getTablaDisponibles().getSelectedRow();
        if (fila == -1) {
            return;
        }

        EmpresaComunicacionDTO empresa =
                (EmpresaComunicacionDTO) view.getTablaDisponibles().getValueAt(fila, 0);
        int idEmpresa = empresa.getId();

        // Si estaba pendiente de quitar, simplemente se deshace el cambio temporal.
        if (empresasQuitar.contains(idEmpresa)) {
            empresasQuitar.remove(idEmpresa);
            cargarEmpresas();
            return;
        }

        model.validarPuedeOfrecer(idEventoSeleccionado, idEmpresa);
        empresasOfrecer.add(idEmpresa);
        empresasQuitar.remove(idEmpresa);
        cargarEmpresas();
    }

    private void quitarEmpresa() {
        if (idEventoSeleccionado == null) {
            throw new ApplicationException("Debes seleccionar un evento.");
        }

        int fila = view.getTablaOfrecidas().getSelectedRow();
        if (fila == -1) {
            return;
        }

        EmpresaComunicacionDTO empresa =
                (EmpresaComunicacionDTO) view.getTablaOfrecidas().getValueAt(fila, 0);
        int idEmpresa = empresa.getId();

        // Si era un ofrecimiento pendiente, simplemente se deshace.
        if (empresasOfrecer.contains(idEmpresa)) {
            empresasOfrecer.remove(idEmpresa);
            cargarEmpresas();
            return;
        }

        model.validarPuedeQuitar(idEventoSeleccionado, idEmpresa);
        empresasQuitar.add(idEmpresa);
        empresasOfrecer.remove(idEmpresa);
        cargarEmpresas();
    }

    private void limpiarTablasEmpresas() {
        ((DefaultTableModel) view.getTablaOfrecidas().getModel()).setRowCount(0);
        ((DefaultTableModel) view.getTablaDisponibles().getModel()).setRowCount(0);
    }

    private void cargarEventos() {
        List<EventoDTO> eventos = model.getEventosByAgencia(agenciaSeleccionada.getId());
        DefaultTableModel modelTabla = (DefaultTableModel) view.getTablaEventos().getModel();
        modelTabla.setRowCount(0);

        for (EventoDTO evento : eventos) {
            modelTabla.addRow(new Object[] { evento.getId(), evento.getNombre(), evento.getFecha() });
        }

        view.getTablaEventos().clearSelection();
    }
}