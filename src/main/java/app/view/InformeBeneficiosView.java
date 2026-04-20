package app.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class InformeBeneficiosView {
    private JFrame frame;
    private JComboBox<String> cbTematicas;
    private JButton btnGenerar;
    private JTable tableEventos, tableTarifaPlana, tablePagosIndividuales;
    private DefaultTableModel modelEventos, modelTarifaPlana, modelPagosIndividuales;
    private JLabel lblSumaTarifa, lblSumaPagos, lblTotal;

    public InformeBeneficiosView() {
        frame = new JFrame("INFORME DE INGRESOS");
        frame.setBounds(100, 100, 800, 750);
        frame.getContentPane().setLayout(new BorderLayout(10, 10));

        // NORTE: Filtro y botón
        JPanel pnlNorte = new JPanel(new FlowLayout());
        cbTematicas = new JComboBox<>();
        btnGenerar = new JButton("GENERAR INFORME");
        pnlNorte.add(new JLabel("Elija las temáticas deseadas:"));
        pnlNorte.add(cbTematicas);
        pnlNorte.add(btnGenerar);
        frame.add(pnlNorte, BorderLayout.NORTH);

        // CENTRO: Las tres listas
        JPanel pnlCentral = new JPanel();
        pnlCentral.setLayout(new BoxLayout(pnlCentral, BoxLayout.Y_AXIS));

        modelEventos = new DefaultTableModel(new String[]{"Eventos (Seleccione uno)"}, 0);
        tableEventos = new JTable(modelEventos);
        pnlCentral.add(crearPanelScroll(tableEventos, "EVENTOS"));

        modelTarifaPlana = new DefaultTableModel(new String[]{"Empresa", "Importe Tarifa"}, 0);
        tableTarifaPlana = new JTable(modelTarifaPlana);
        pnlCentral.add(crearPanelScroll(tableTarifaPlana, "EMPRESAS CON TARIFA PLANA"));

        modelPagosIndividuales = new DefaultTableModel(new String[]{"Empresa", "Precio Pagado"}, 0);
        tablePagosIndividuales = new JTable(modelPagosIndividuales);
        pnlCentral.add(crearPanelScroll(tablePagosIndividuales, "EMPRESAS SIN TARIFA PLANA"));

        frame.add(pnlCentral, BorderLayout.CENTER);

        // SUR: Balance de ingresos
        JPanel pnlSur = new JPanel(new GridLayout(3, 1));
        pnlSur.setBorder(new TitledBorder("INGRESOS OBTENIDOS"));
        lblSumaTarifa = new JLabel("Tarifa plana: ----------- 0.00€");
        lblSumaPagos = new JLabel("Pagos individuales: ----------- 0.00€");
        lblTotal = new JLabel("TOTAL: ----------- 0.00€");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 14));
        pnlSur.add(lblSumaTarifa); pnlSur.add(lblSumaPagos); pnlSur.add(lblTotal);
        frame.add(pnlSur, BorderLayout.SOUTH);
    }

    private JScrollPane crearPanelScroll(JTable t, String titulo) {
        JScrollPane sp = new JScrollPane(t);
        sp.setBorder(BorderFactory.createTitledBorder(titulo));
        sp.setPreferredSize(new Dimension(750, 150));
        return sp;
    }

    // Getters
    public JFrame getFrame() { return frame; }
    public JComboBox<String> getCbTematicas() { return cbTematicas; }
    public JButton getBtnGenerar() { return btnGenerar; }
    public JTable getTableEventos() { return tableEventos; }
    public DefaultTableModel getModelEventos() { return modelEventos; }
    public DefaultTableModel getModelTarifaPlana() { return modelTarifaPlana; }
    public DefaultTableModel getModelPagosIndividuales() { return modelPagosIndividuales; }
    public void setBalance(double t, double p) {
        lblSumaTarifa.setText(String.format("Tarifa plana: ----------- %.2f€", t));
        lblSumaPagos.setText(String.format("Pagos individuales: ----------- %.2f€", p));
        lblTotal.setText(String.format("TOTAL: ----------- %.2f€", t + p));
    }
}