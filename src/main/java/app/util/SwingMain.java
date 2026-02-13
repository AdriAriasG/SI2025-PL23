package app.util;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.EmptyBorder;
import java.awt.Dimension;
import app.dto.AgenciaDTO;

public class SwingMain {

    private JFrame frame;
    private AgenciaDTO agencia;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Ahora el punto de entrada es el Login
                    app.model.LoginModel model = new app.model.LoginModel();
                    app.view.LoginView view = new app.view.LoginView();
                    app.controller.LoginController controller = new app.controller.LoginController(model, view);
                    controller.initController();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public SwingMain(AgenciaDTO agencia) {
        this.agencia = agencia;
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Gestión de Reportajes - " + agencia.getNombre());
        frame.setBounds(100, 100, 500, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        ((JPanel)frame.getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- AGENCIA DE PRENSA (ADRIAN / IVAN) ---
        addLabel("AGENCIA DE PRENSA (Adrian / Ivan)");
        addButton("HU #33537: Asignación de reporteros (ADRIAN)");
        addButton("HU #33543: Modificar asignación (ADRIAN)");
        addButton("HU #33548: Informe de un evento (ADRIAN)");
        addButton("HU #33539: Ofrecer reportajes (IVAN)");
        addButton("HU #33544: Modificar ofrecimiento (IVAN)");
        addButton("HU #33541: Distribuir reportaje (IVAN)");

        // --- REPORTERO (DIEGO) ---
        addLabel("REPORTERO (Diego)");
        addButton("HU #33538: Entrega de reportaje (DIEGO)");
        addButton("HU #33545: Modificar entrega (DIEGO)");
        addButton("HU #33547: Restaurar versión previa (DIEGO)");

        // --- EMPRESA DE COMUNICACIÓN (IRENE) ---
        addLabel("EMPRESA DE COMUNICACIÓN (Irene)");
        addButton("HU #33540: Gestionar ofrecimientos (IRENE)");
        addButton("HU #33546: Modificar decisión (IRENE)");
        addButton("HU #33542: Acceder a reportaje (IRENE)");

        // --- MANTENIMIENTO BD ---
        addLabel("MANTENIMIENTO BD");
        JButton btnInit = new JButton("Inicializar Base de Datos en Blanco");
        btnInit.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnInit.addActionListener(e -> {
            new Database().createDatabase(false);
        });
        frame.getContentPane().add(btnInit);
        frame.getContentPane().add(javax.swing.Box.createRigidArea(new Dimension(0, 5)));

        JButton btnLoad = new JButton("Cargar Datos Iniciales para Pruebas");
        btnLoad.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLoad.addActionListener(e -> {
            Database db = new Database();
            db.createDatabase(false);
            db.loadDatabase();
        });
        frame.getContentPane().add(btnLoad);
    }

    private void addLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setFont(label.getFont().deriveFont(java.awt.Font.BOLD));
        label.setBorder(new EmptyBorder(15, 0, 5, 0));
        frame.getContentPane().add(label);
    }

    private void addButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(450, 30));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                javax.swing.JOptionPane.showMessageDialog(frame, "Acción no implementada: " + text);
            }
        });
        frame.getContentPane().add(button);
        frame.getContentPane().add(javax.swing.Box.createRigidArea(new Dimension(0, 2)));
    }

    public JFrame getFrame() { return this.frame; }
}
