package app.model;

import java.util.List;
import app.dto.AgenciaDTO;
import app.util.Database;

public class LoginModel {
    private Database db = new Database();

    public List<AgenciaDTO> getAgencias() {
        String sql = "SELECT id, nombre, email FROM AgenciaPrensa ORDER BY nombre";
        return db.executeQueryPojo(AgenciaDTO.class, sql);
    }
}
