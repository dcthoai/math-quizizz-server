package math.server.repository.utils;

import math.server.common.Common;
import math.server.config.DataSourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A class that provides basic CRUD methods for manipulating the database
 * @param <T> Name of the entity that wants to operate with the database
 * @author dcthoai
 */
public class EntityManager<T> implements IEntityManager<T> {

    private static final Logger log = LoggerFactory.getLogger(EntityManager.class);
    protected final Connection connection;

    public EntityManager() {
        this.connection = DataSourceProvider.getInstance().getConnection();
    }

    @Override
    public List<T> query(Class<T> entityName, String sql, List<Object> params) {
        try {
            PreparedStatement query = connection.prepareStatement(sql);
            log.info("Execute query: " + sql);

            for (int i = 0; i < params.size(); i++) {
                query.setObject(i + 1, params.get(i));  // JDBC index start with 1
            }

            ResultSet resultSet = query.executeQuery();
            List<T> results = new ArrayList<>();

            while(resultSet.next()) {
                T object = entityName.getDeclaredConstructor().newInstance();
                Common.objectMapper(resultSet, object);
                results.add(object);
            }

            resultSet.close();
            query.close();

            return results;
        } catch (SQLException e) {
            log.error("Failed to query data", e);
        } catch (InvocationTargetException e) {
            log.error("InvocationTargetException", e.getCause());
        } catch (InstantiationException e) {
            log.error("Query failed. Cannot get instance of entity mapper", e);
        } catch (IllegalAccessException e) {
            log.error("Query failed. Cannot access entity field to mapper", e);
        } catch (NoSuchMethodException e) {
            log.error("Query failed. Cannot find constructor to get entity instance", e);
        }

        return new ArrayList<>();
    }

    @Override
    public T findOne(Class<T> entityName, Integer ID) {
        String sql = "SELECT * FROM `" + entityName.getSimpleName() + "` WHERE `id` = ?";
        List<T> results = query(entityName, sql, List.of(ID));

        if (results.isEmpty())
            return null;

        return results.get(0);
    }

    @Override
    public T findOne(Class<T> entityName, String conditions, List<Object> params) {
        String sql = "SELECT * FROM `" + entityName.getSimpleName() + "` WHERE " + conditions;
        List<T> results = query(entityName, sql, params);

        if (results.isEmpty())
            return null;

        return results.get(0);
    }

    @Override
    public List<T> findAll(Class<T> entityName) {
        String sql = "SELECT * FROM `" + entityName.getSimpleName() + "`";
        return query(entityName, sql, Collections.emptyList());
    }

    @Override
    public List<T> findAll(Class<T> entityName, String conditions, List<Object> params) {
        String sql = "SELECT * FROM `" + entityName.getSimpleName() + "` WHERE " + conditions;
        return query(entityName, sql, params);
    }

    @Override
    public Integer insert(Class<T> entityName, Object object) {
        String tableName = entityName.getSimpleName();

        try {
            Map<String, Object> objectFields = Common.getObjectFields(object);
            List<Object> params = new ArrayList<>(objectFields.values());
            StringBuilder columns = new StringBuilder();
            StringBuilder values = new StringBuilder();
            StringBuilder sql = new StringBuilder("INSERT INTO " + tableName);

            for (String key : objectFields.keySet()) {
                columns.append("`").append(key).append("`, ");
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
                statement.close();
                log.error("Inserting data failed, no rows affected");
            } else {
                ResultSet generatedKeys = statement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    int recordID = generatedKeys.getInt(1); // Return ID of new record which is insert in database
                    generatedKeys.close();
                    statement.close();

                    return recordID;
                } else {
                    generatedKeys.close();
                    statement.close();
                    log.error("Inserting data failed, no ID obtained");
                }
            }
        } catch (IllegalAccessException e) {
            log.error("Failed to get params from object", e);
        } catch (SQLException e) {
            log.error("Failed to insert data into table {}", tableName, e);
        }

        return 0;
    }

    private boolean execute(String sql, List<Object> params) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            log.info("Execute query: " + sql);

            for (int i = 0; i < params.size(); ++i) {
                preparedStatement.setObject(i + 1, params.get(i)); // JDBC index start with 1
            }

            int affectedRows = preparedStatement.executeUpdate();
            preparedStatement.close();

            return affectedRows > 0;
        } catch (SQLException e) {
            log.error("Update or delete entity failed", e);
            return false;
        }
    }

    @Override
    public boolean update(Class<T> entityName, String conditions, Map<String, Object> columnsData) {
        List<Object> params = new ArrayList<>(columnsData.values());
        StringBuilder columns = new StringBuilder();
        StringBuilder sql = new StringBuilder("UPDATE `" + entityName.getSimpleName() + "` SET ");

        columnsData.keySet().forEach(key -> columns.append("`").append(key).append("` = ?,"));
        columns.delete(columns.length() - 1, columns.length()); // Remove last comma
        sql.append(columns).append(" ").append(conditions);

        return execute(sql.toString(), params);
    }

    @Override
    public boolean delete(Class<T> entityName, Integer ID) {
        String sql = "DELETE FROM `" + entityName.getSimpleName() + "` WHERE `id` = ?";
        return execute(sql, List.of(ID));
    }

    @Override
    public boolean delete(Class<T> entityName, String conditions, List<Object> params) {
        String sql = "DELETE FROM `" + entityName.getSimpleName() + "` WHERE " + conditions;
        return execute(sql, params);
    }
}
