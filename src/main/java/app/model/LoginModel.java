package app.model;

import java.util.List;
import app.dto.AgenciaDTO;
import app.dto.ReporteroDTO;
import app.dto.EmpresaComunicacionDTO;
import app.util.Database;

public class LoginModel {
    private Database db = new Database();

    public List<AgenciaDTO> getAgencias() {
        String sql = "SELECT id, nombre, email FROM AgenciaPrensa ORDER BY nombre";
        return db.executeQueryPojo(AgenciaDTO.class, sql);
    }

    public List<ReporteroDTO> getReporteros() {
        String sql = "SELECT id, nombre, id_agencia FROM Reportero ORDER BY nombre";
        return db.executeQueryPojo(ReporteroDTO.class, sql);
    }

    public List<EmpresaComunicacionDTO> getEmpresas() {
        String sql = "SELECT id, nombre FROM EmpresaComunicacion ORDER BY nombre";
        return db.executeQueryPojo(EmpresaComunicacionDTO.class, sql);
    }
    
    public List<ReporteroDTO> getReporterosFreelance() {
		String sql = """
				SELECT id, nombre
				FROM Reportero
				WHERE id_agencia IS NULL
				ORDER BY nombre
				""";
		return db.executeQueryPojo(ReporteroDTO.class, sql);
	}

}
