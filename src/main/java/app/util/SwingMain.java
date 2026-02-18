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
import app.model.AsignacionModel;
// import app.model.InformeModel;  // HU #33548 - Descomentar mañana
import app.view.AsignacionView;
// import app.view.InformeView;  // HU #33548 - Descomentar mañana
import app.controller.AsignacionController;
// import app.controller.InformeController;  // HU #33548 - Descomentar mañana

public class SwingMain {

    private JFrame frame;
    private AgenciaDTO agencia;

    public static void main(String[] args) {
        // Inicializar la base de datos automáticamente al arrancar
        try {
            Database db = new Database();
            db.createDatabase(false);
            db.loadDatabase();
        } catch (Exception e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
        }

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
                handleButtonAction(text);
            }
        });
        frame.getContentPane().add(button);
        frame.getContentPane().add(javax.swing.Box.createRigidArea(new Dimension(0, 2)));
    }

    /**
     * Maneja las acciones de los botones según su texto
     */
    private void handleButtonAction(String buttonText) {
        // HU #33537: Asignación de reporteros (ADRIAN)
        if (buttonText.contains("#33537")) {
            AsignacionModel model = new AsignacionModel();
            AsignacionView view = new AsignacionView(false); // Modo no edición
            new AsignacionController(model, view, agencia);
        }
        // HU #33543: Modificar asignación (ADRIAN)
        else if (buttonText.contains("#33543")) {
            AsignacionModel model = new AsignacionModel();
            AsignacionView view = new AsignacionView(true); // Modo edición
            new AsignacionController(model, view, agencia);
        }
        // HU #33548: Informe de un evento (ADRIAN) - Descomentar mañana
        /*
        else if (buttonText.contains("#33548")) {
            InformeModel model = new InformeModel();
            InformeView view = new InformeView();
            new InformeController(model, view, agencia);
        }
        */
        // Resto de botones no implementados
        else {
            javax.swing.JOptionPane.showMessageDialog(frame, "Acción no implementada: " + buttonText);
        }
    }

    public JFrame getFrame() { return this.frame; }
}
