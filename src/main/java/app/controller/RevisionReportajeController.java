package app.controller;

import java.util.List;

import javax.swing.JOptionPane;

import app.dto.MultimediaDTO;
import app.dto.ReportajeRevisionResumenDTO;
import app.dto.RevisionDTO;
import app.dto.VersionDTO;
import app.model.RevisionReportajeModel;
import app.view.RevisionReportajeView;
import giis.demo.util.SwingUtil;

public class RevisionReportajeController {

    private RevisionReportajeModel model;
    private RevisionReportajeView view;
    private int idReportero;

    private List<ReportajeRevisionResumenDTO> listaReportajes;
    private List<MultimediaDTO> listaMultimedia;

    public RevisionReportajeController(
            RevisionReportajeModel model,
            RevisionReportajeView view,
            int idReportero) {

        this.model = model;
        this.view = view;
        this.idReportero = idReportero;

        initView();
        initController();
    }

    // ======================================================
    // INICIALIZACIÓN
    // ======================================================

    private void initView() {
        cargarReportajesPendientes();
        view.getFrame().setVisible(true);
    }

    private void initController() {

        view.getTablaReportajes().getSelectionModel()
            .addListSelectionListener(e ->
                SwingUtil.exceptionWrapper(() -> seleccionarReportaje())
            );

        view.getBtnGuardarComentario().addActionListener(e ->
            SwingUtil.exceptionWrapper(() -> guardarComentario())
        );

        view.getBtnFinalizarRevision().addActionListener(e ->
            SwingUtil.exceptionWrapper(() -> finalizarRevision())
        );
    }

    // ======================================================
    // CARGAR REPORTAJES
    // ======================================================

    private void cargarReportajesPendientes() {

        listaReportajes =
            model.getReportajesEnRevisionPendientes(idReportero);

        String[] columnas = {
            "idReportaje",
            "titulo",
            "nombreEvento",
            "fecha"
        };

        view.getTablaReportajes().setModel(
            SwingUtil.getTableModelFromPojos(listaReportajes, columnas)
        );

        SwingUtil.autoAdjustColumns(view.getTablaReportajes());
    }

    // ======================================================
    // SELECCIONAR REPORTAJE
    // ======================================================

    private void seleccionarReportaje() {

        int fila = view.getTablaReportajes().getSelectedRow();
        if (fila < 0) return;

        ReportajeRevisionResumenDTO seleccionado = listaReportajes.get(fila);
        int idReportaje = seleccionado.getIdReportaje();

        // Cargar versión actual
        VersionDTO version = model.getVersionActual(idReportaje);

        if (version != null) {
            view.getTxtTitulo().setText(seleccionado.getTitulo());
            view.getTxtSubtitulo().setText(version.getSubtitulo());
            view.getTxtCuerpo().setText(version.getCuerpo());
        }

        view.actualizarEstadoReportaje("EN_REVISION");

        // Cargar multimedia
        listaMultimedia = model.getMultimedia(idReportaje);

        String[] columnasMultimedia = {
            "id",
            "ruta",
            "tipo",
            "estado"
        };

        view.getTablaMultimedia().setModel(
            SwingUtil.getTableModelFromPojos(
                listaMultimedia,
                columnasMultimedia)
        );

        SwingUtil.autoAdjustColumns(view.getTablaMultimedia());

        // Cargar revisión del reportero
        RevisionDTO revision =
            model.getRevision(idReportaje, idReportero);

        if (revision == null) {
            view.getTxtComentario().setText("");
            view.mostrarRevisionPendiente();
        } else {
            view.getTxtComentario().setText(revision.getComentario());

            if (revision.isFinalizada()) {
                view.mostrarRevisionFinalizada();
            } else {
                view.mostrarRevisionPendiente();
            }
        }
    }

    // ======================================================
    // GUARDAR COMENTARIO
    // ======================================================

    private void guardarComentario() {

        int fila = view.getTablaReportajes().getSelectedRow();
        if (fila < 0)
            throw new IllegalArgumentException(
                "Debe seleccionar un reportaje");

        ReportajeRevisionResumenDTO seleccionado = listaReportajes.get(fila);
        int idReportaje = seleccionado.getIdReportaje();

        String comentario =
            view.getTxtComentario().getText().trim();

        if (comentario.isEmpty())
            throw new IllegalArgumentException(
                "El comentario no puede estar vacío");

        model.guardarComentario(
            idReportaje,
            idReportero,
            comentario
        );

        JOptionPane.showMessageDialog(
            view.getFrame(),
            "Comentario guardado correctamente"
        );
    }

    // ======================================================
    // FINALIZAR REVISIÓN
    // ======================================================

    private void finalizarRevision() {

        int fila = view.getTablaReportajes().getSelectedRow();
        if (fila < 0)
            throw new IllegalArgumentException(
                "Debe seleccionar un reportaje");

        ReportajeRevisionResumenDTO seleccionado = listaReportajes.get(fila);
        int idReportaje = seleccionado.getIdReportaje();

        model.finalizarRevision(
            idReportaje,
            idReportero
        );

        view.mostrarRevisionFinalizada();

        JOptionPane.showMessageDialog(
            view.getFrame(),
            "Revisión finalizada correctamente"
        );

        // Recargar tabla para eliminarlo de pendientes
        cargarReportajesPendientes();
    }
}