package app.model;

import java.util.List;

import app.dto.MultimediaDTO;
import app.dto.ReportajeRevisionResumenDTO;
import app.dto.RevisionDTO;
import app.dto.VersionDTO;
import app.util.ApplicationException;
import app.util.Database;

public class RevisionReportajeModel {

    private Database db = new Database();

    // ======================================================
    // 1️⃣ Reportajes en revisión pendientes para el reportero
    // ======================================================
    public List<ReportajeRevisionResumenDTO> getReportajesEnRevisionPendientes(int idReportero){

        String sql = """
            SELECT r.id AS idReportaje,
                   r.titulo,
                   e.nombre AS nombreEvento,
                   e.fecha
            FROM Reportaje r
            JOIN Evento e ON r.id_evento = e.id
            JOIN Asignacion a ON a.id_evento = e.id
            LEFT JOIN RevisionReportaje rev
                ON rev.id_reportaje = r.id
                AND rev.id_reportero = ?
            WHERE r.estado = 'EN_REVISION'
              AND a.id_reportero = ?
              AND (rev.estado IS NULL OR rev.estado = 'PENDIENTE')
            ORDER BY e.fecha
            """;

        return db.executeQueryPojo(
                ReportajeRevisionResumenDTO.class,
                sql,
                idReportero,
                idReportero
        );
    }

    // ======================================================
    // 2️⃣ Obtener versión actual del reportaje
    // ======================================================
    public VersionDTO getVersionActual(int idReportaje) {

        String sql = """
            SELECT subtitulo,
                   cuerpo
            FROM VersionReportaje
            WHERE id_reportaje = ?
            ORDER BY id DESC
            LIMIT 1
            """;

        return db.executeQueryPojo(VersionDTO.class, sql, idReportaje)
                .stream()
                .findFirst()
                .orElse(null);
    }

    // ======================================================
    // 3️⃣ Obtener multimedia
    // ======================================================
    public List<MultimediaDTO> getMultimedia(int idReportaje) {

        String sql = """
            SELECT id,
                   ruta,
                   tipo,
                   estado
            FROM Multimedia
            WHERE id_reportaje = ?
            ORDER BY id DESC
            """;

        return db.executeQueryPojo(MultimediaDTO.class, sql, idReportaje);
    }

    // ======================================================
    // 4️⃣ Obtener revisión del reportero
    // ======================================================
    public RevisionDTO getRevision(int idReportaje, int idReportero) {

        String sql = """
            SELECT id_reportaje AS idReportaje,
                   id_reportero AS idReportero,
                   comentario,
                   estado
            FROM RevisionReportaje
            WHERE id_reportaje = ?
              AND id_reportero = ?
            """;

        return db.executeQueryPojo(
                RevisionDTO.class,
                sql,
                idReportaje,
                idReportero
        )
        .stream()
        .findFirst()
        .orElse(null);
    }

    // ======================================================
    // 5️⃣ Guardar o actualizar comentario
    // ======================================================
    public void guardarComentario(int idReportaje,
                                  int idReportero,
                                  String comentario) {

        RevisionDTO revision = getRevision(idReportaje, idReportero);

        if (revision == null) {

            String insert = """
                INSERT INTO RevisionReportaje
                (id_reportaje, id_reportero, comentario, estado)
                VALUES (?, ?, ?, 'PENDIENTE')
                """;

            db.executeUpdate(
                    insert,
                    idReportaje,
                    idReportero,
                    comentario
            );

        } else {

            if (revision.isFinalizada())
                throw new ApplicationException("La revisión ya está finalizada");

            String update = """
                UPDATE RevisionReportaje
                SET comentario = ?
                WHERE id_reportaje = ?
                  AND id_reportero = ?
                """;

            db.executeUpdate(
                    update,
                    comentario,
                    idReportaje,
                    idReportero
            );
        }
    }

    // ======================================================
    // 6️⃣ Finalizar revisión
    // ======================================================
    public void finalizarRevision(int idReportaje, int idReportero) {

        RevisionDTO revision = getRevision(idReportaje, idReportero);

        if (revision == null)
            throw new ApplicationException("Debe guardar un comentario antes de finalizar");

        if (revision.isFinalizada())
            throw new ApplicationException("La revisión ya está finalizada");

        String update = """
            UPDATE RevisionReportaje
            SET estado = 'FINALIZADA'
            WHERE id_reportaje = ?
              AND id_reportero = ?
            """;

        db.executeUpdate(update, idReportaje, idReportero);
    }
}