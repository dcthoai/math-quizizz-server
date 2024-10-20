package main.java.math.server.repository.utils;

import main.java.math.server.common.Common;
import main.java.math.server.config.DataSourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class BaseRepository {

    private static final Logger log = LoggerFactory.getLogger(BaseRepository.class);
    protected final Connection connection;

    public BaseRepository() {
        this.connection = DataSourceProvider.getInstance().getConnection();
    }

    public ResultSet query(String sql, List<Object> params) {
        try {
            PreparedStatement query = connection.prepareStatement(sql);
            log.info("Execute query: " + sql);

            for (int i = 0; i < params.size(); i++) {
                query.setObject(i + 1, params.get(i));  // JDBC index start with 1
            }

            return query.executeQuery();
        } catch (SQLException e) {
            log.error("Failed to query data: ", e);
        }

        return null;
    }

    public <T> Object findOne(Integer ID, T object) {
        String tableName = object.getClass().getSimpleName();
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";

        try {
            PreparedStatement query = connection.prepareStatement(sql);
            query.setInt(1, ID);
            log.info("Execute query: " + sql);

            ResultSet resultSet = query.executeQuery();

            if (resultSet.next()) {
                Common.objectMapper(resultSet, object);

                return object;
            }
        } catch (SQLException e) {
            log.error("Failed to find one object by ID {}", ID, e);
        }

        return null;
    }

    public Integer insert(String tableName, Object object) {
        try {
            Map<String, Object> objectFields = Common.getObjectFields(object);
            List<Object> params = (List<Object>) objectFields.values();
            StringBuilder columns = new StringBuilder();
            StringBuilder values = new StringBuilder();
            StringBuilder sql = new StringBuilder("INSERT INTO " + tableName);

            for (String key : objectFields.keySet()) {
                columns.append(key).append(", ");
                values.append("?, ");
            }

            columns.delete(columns.length() - 2, columns.length()); // Remove last comma
            values.delete(values.length() - 2, values.length()); // Remove last comma

            sql.append(" (").append(columns).append(") VALUES (").append(values).append(")");
            PreparedStatement statement = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
            log.info("Execute query: " + sql);

            for (int i = 0; i < params.size(); ++i) {
                statement.setObject(i + 1, params.get(i)); // JDBC index start with 1
            }

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                log.error("Inserting data failed, no rows affected");
            } else {
                ResultSet generatedKeys = statement.getGeneratedKeys();

                if (generatedKeys.next())
                    return generatedKeys.getInt(1); // Return ID of new record which is insert in database
                else
                    log.error("Inserting data failed, no ID obtained");
            }
        } catch (IllegalAccessException e) {
            log.error("Failed to get params from object: ", e);
        } catch (SQLException e) {
            log.error("Failed to insert data into table {}: ", tableName, e);
        }

        return 0;
    }

    public boolean update(String tableName, String conditions, Map<String, Object> columnsData) {
        List<Object> params = (List<Object>) columnsData.values();
        StringBuilder columns = new StringBuilder();
        StringBuilder sql = new StringBuilder("UPDATE " + tableName + "SET ");

        columnsData.keySet().forEach(key -> columns.append(key).append(" = ?,"));
        columns.delete(columns.length() - 1, columns.length()); // Remove last comma
        sql.append(columns + " ").append(conditions);

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
            log.info("Execute query: " + sql);

            for (int i = 0; i < params.size(); ++i) {
                preparedStatement.setObject(i + 1, params.get(i)); // JDBC index start with 1
            }

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Failed to update this object: ", e);
        }

        return false;
    }

    public boolean delete(String tableName, Integer ID) {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, ID);
            log.info("Execute query: " + sql);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Delete by ID failed: ", e);
            return false;
        }
    }

    public boolean delete(String tableName, String conditions) {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, conditions);
            log.info("Execute query: " + sql);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Delete by conditions failed: ", e);
            return false;
        }
    }
}
