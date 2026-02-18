package app.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import app.dto.AgenciaDTO;
import java.awt.*;

public class LoginView {
    private JFrame frame;
    private JComboBox<AgenciaDTO> cbAgencias;
    private JButton btnContinuar;

   public LoginView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Identificación - Gestión de Reportajes");
        frame.setBounds(100, 100, 400, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Centrar en pantalla
        
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new GridLayout(4, 1, 10, 10));

        JLabel lblBienvenida = new JLabel("Bienvenido al Sistema de Reportajes", JLabel.CENTER);
        lblBienvenida.setFont(new Font("Tahoma", Font.BOLD, 14));
        panel.add(lblBienvenida);

        JLabel lblSeleccione = new JLabel("Seleccione su Agencia de Prensa:");
        panel.add(lblSeleccione);

        cbAgencias = new JComboBox<>();
        panel.add(cbAgencias);

        btnContinuar = new JButton("Continuar");
        panel.add(btnContinuar);
    }

    public JFrame getFrame() { return frame; }
    public JComboBox<AgenciaDTO> getCbAgencias() { return cbAgencias; }
    public JButton getBtnContinuar() { return btnContinuar; }
}
