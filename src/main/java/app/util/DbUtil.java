package app.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

public abstract class DbUtil {
	public abstract String getUrl();
	
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(getUrl());
	}

	public <T> List<T> executeQueryPojo(Class<T> pojoClass, String sql, Object... params) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			BeanListHandler<T> beanListHandler = new BeanListHandler<>(pojoClass);
			QueryRunner runner = new QueryRunner();
			return runner.query(conn, sql, beanListHandler, params);
		} catch (SQLException e) {
			throw new UnexpectedException(e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}

	public List<Object[]> executeQueryArray(String sql, Object... params) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			ArrayListHandler beanListHandler = new ArrayListHandler();
			QueryRunner runner = new QueryRunner();
			return runner.query(conn, sql, beanListHandler, params);
		} catch (SQLException e) {
			throw new UnexpectedException(e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}

	public List<Map<String, Object>> executeQueryMap(String sql, Object... params) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			MapListHandler beanListHandler = new MapListHandler();
			QueryRunner runner = new QueryRunner();
			return runner.query(conn, sql, beanListHandler, params);
		} catch (SQLException e) {
			throw new UnexpectedException(e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}

	public void executeUpdate(String sql, Object... params) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			QueryRunner runner = new QueryRunner();
			runner.update(conn, sql, params);
		} catch (SQLException e) {
			throw new UnexpectedException(e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}
	
	public void executeScript(String fileName) {
		List<String> lines;
		try {
			lines = Files.readAllLines(Paths.get(fileName));
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
		List<String> batchUpdate = new ArrayList<>();
		List<String> batchDrop = new ArrayList<>();
		StringBuilder previousLines = new StringBuilder();
		for (String line : lines) {
			line = line.trim();
			if (line.length() == 0 || line.startsWith("--"))
				continue;
			if (line.endsWith(";")) {
				String sql = previousLines.toString() + line;
				if (line.toLowerCase().startsWith("drop"))
					batchDrop.add(sql);
				else
					batchUpdate.add(sql);
				previousLines = new StringBuilder();
			} else {
				previousLines.append(line + " ");
			}
		}
		if (!batchDrop.isEmpty())
			this.executeBatchNoFail(batchDrop);
		if (!batchUpdate.isEmpty())
			this.executeBatch(batchUpdate);
	}

	public void executeBatch(String[] sqls) {
		executeBatch(Arrays.asList(sqls));
	}
	
	public void executeBatch(List<String> sqls) {
		try (Connection cn = DriverManager.getConnection(getUrl()); Statement stmt = cn.createStatement()) {
			for (String sql : sqls)
				stmt.addBatch(sql);
			stmt.executeBatch();
		} catch (SQLException e) {
			throw new UnexpectedException(e);
		}
	}

	public void executeBatchNoFail(List<String> sqls) {
		try (Connection cn = DriverManager.getConnection(getUrl()); Statement stmt = cn.createStatement()) {
			for (String sql : sqls)
				executeWithoutException(stmt, sql);
		} catch (SQLException e) {
			throw new UnexpectedException(e);
		}
	}

	private void executeWithoutException(Statement stmt, String sql) {
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
		}
	}
	
	public <T> T executeQueryScalar(Class<T> type, String sql, Object... params) {
	    Connection conn = null;
	    try {
	        conn = this.getConnection();
	        QueryRunner runner = new QueryRunner();

	        return runner.query(conn, sql, rs -> {
	            if (rs.next()) {
	                Object value = rs.getObject(1);

	                if (value == null)
	                    return null;

	                if (type == Long.class)
	                    return type.cast(((Number) value).longValue());

	                if (type == Integer.class)
	                    return type.cast(((Number) value).intValue());

	                return type.cast(value);
	            }
	            return null;
	        }, params);

	    } catch (SQLException e) {
	        throw new UnexpectedException(e);
	    } finally {
	        DbUtils.closeQuietly(conn);
	    }
	}
	
}
