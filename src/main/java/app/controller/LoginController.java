package app.controller;

import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import app.dto.AgenciaDTO;
import app.model.LoginModel;
import app.view.LoginView;
import app.util.SwingMain;

public class LoginController {
    private LoginModel model;
    private LoginView view;

    public LoginController(LoginModel model, LoginView view) {
        this.model = model;
        this.view = view;
        this.initView();
    }

    public void initController() {
        view.getBtnContinuar().addActionListener(e -> intentarEntrar());
        view.getFrame().setVisible(true);
    }

    private void initView() {
        // Cargar agencias en el combo
        List<AgenciaDTO> agencias = model.getAgencias();
        DefaultComboBoxModel<AgenciaDTO> cbModel = new DefaultComboBoxModel<>();
        
        // El requisito dice que por defecto no aparece ninguna elegida
        // Añadimos un elemento nulo o indicativo al principio
        view.getCbAgencias().setModel(cbModel);
        for (AgenciaDTO a : agencias) {
            cbModel.addElement(a);
        }
        view.getCbAgencias().setSelectedIndex(-1); // No seleccionado por defecto
    }

    private void intentarEntrar() {
        AgenciaDTO seleccionada = (AgenciaDTO) view.getCbAgencias().getSelectedItem();
        if (seleccionada == null) {
            JOptionPane.showMessageDialog(view.getFrame(), 
                "Debe seleccionar una agencia para continuar.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
        } else {
            // Cerramos login y abrimos main
            view.getFrame().dispose();
            SwingMain main = new SwingMain(seleccionada);
            main.getFrame().setVisible(true);
        }
    }
}
