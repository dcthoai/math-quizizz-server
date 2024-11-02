package math.server.repository.utils;

import math.server.common.Common;

import math.server.config.DataSourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A class that provides basic CRUD methods for manipulating the database
 * @param <T> Name of the entity that wants to operate with the database
 * @author dcthoai
 */
@SuppressWarnings("unused")
public class EntityManager<T> implements IEntityManager<T> {

    private static final Logger log = LoggerFactory.getLogger(EntityManager.class);
    private final Class<T> entityType;
    private final String entityName;

    @SuppressWarnings("unchecked")
    public EntityManager() {
        this.entityType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

        if (entityType.isAnnotationPresent(Entity.class)) {
            Entity entityAnnotation = entityType.getAnnotation(Entity.class);
            this.entityName = entityAnnotation.value();
        } else {
            this.entityName = null;
        }

        if (Objects.isNull(entityName) || entityName.isEmpty()) {
            log.error("Not found entity");
        }
    }

    private <K> List<K> executeQuery(String sql, List<Object> params, Class<K> clazz) {
        try {
            Connection connection = DataSourceProvider.getInstance().getConnection();
            PreparedStatement query = connection.prepareStatement(sql);
            log.info("Execute query: " + sql);

            for (int i = 0; i < params.size(); i++) {
                query.setObject(i + 1, params.get(i));  // JDBC index start with 1
            }

            ResultSet resultSet = query.executeQuery();
            List<K> results = new ArrayList<>();

            while(resultSet.next()) {
                K object = clazz.getDeclaredConstructor().newInstance();
                Common.objectMapper(resultSet, object);
                results.add(object);
            }

            resultSet.close();
            query.close();
            connection.close();

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
    public List<T> query(String sql, List<Object> params) {
        return executeQuery(sql, params, entityType);
    }

    @Override
    public <EntityType> List<EntityType> query(String sql, List<Object> params, Class<EntityType> entityTypeClass) {
        return executeQuery(sql, params, entityTypeClass);
    }

    @Override
    public T findOne(Integer ID) {
        String sql = "SELECT * FROM `" + entityName + "` WHERE `id` = ?";
        List<T> results = query(sql, List.of(ID));

        if (results.isEmpty())
            return null;

        return results.get(0);
    }

    @Override
    public <EntityType> EntityType findOne(Integer ID, Class<EntityType> entityTypeClass) {
        String sql = "SELECT * FROM `" + entityName + "` WHERE `id` = ?";
        List<EntityType> results = query(sql, List.of(ID), entityTypeClass);

        if (results.isEmpty())
            return null;

        return results.get(0);
    }

    @Override
    public T findOne(String conditions, List<Object> params) {
        String sql = "SELECT * FROM `" + entityName + "` WHERE " + conditions;
        List<T> results = query(sql, params);

        if (results.isEmpty())
            return null;

        return results.get(0);
    }

    @Override
    public <EntityType> EntityType findOne(String conditions, List<Object> params, Class<EntityType> entityTypeClass) {
        String sql = "SELECT * FROM `" + entityName + "` WHERE " + conditions;
        List<EntityType> results = query(sql, params, entityTypeClass);

        if (results.isEmpty())
            return null;

        return results.get(0);
    }

    @Override
    public List<T> findAll() {
        String sql = "SELECT * FROM `" + entityName + "`";
        return query(sql, Collections.emptyList());
    }

    @Override
    public List<T> findAll(String conditions, List<Object> params) {
        String sql = "SELECT * FROM `" + entityName + "` WHERE " + conditions;
        return query(sql, params);
    }

    @Override
    public <EntityType> List<EntityType> findAll(Class<EntityType> entityTypeClass) {
        String sql = "SELECT * FROM `" + entityName + "`";
        return query(sql, Collections.emptyList(), entityTypeClass);
    }

    @Override
    public <EntityType> List<EntityType> findAll(String conditions, List<Object> params, Class<EntityType> entityTypeClass) {
        String sql = "SELECT * FROM `" + entityName + "` WHERE " + conditions;
        return query(sql, params, entityTypeClass);
    }

    @Override
    public int save(Object object) {
        try {
            Map<String, Object> objectFields = Common.getObjectFields(object);
            List<Object> params = new ArrayList<>(objectFields.values());
            StringBuilder columns = new StringBuilder();
            StringBuilder values = new StringBuilder();
            StringBuilder sql = new StringBuilder("INSERT INTO " + entityName);

            for (String key : objectFields.keySet()) {
                columns.append("`").append(key).append("`, ");
                values.append("?, ");
            }

            columns.delete(columns.length() - 2, columns.length()); // Remove last comma
            values.delete(values.length() - 2, values.length()); // Remove last comma
            sql.append(" (").append(columns).append(") VALUES (").append(values).append(")");

            Connection connection = DataSourceProvider.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
            log.info("Execute query: " + sql);

            for (int i = 0; i < params.size(); ++i) {
                statement.setObject(i + 1, params.get(i)); // JDBC index start with 1
            }

            int affectedRows = statement.executeUpdate();
            int recordID = 0;

            if (affectedRows == 0) {
                log.error("Inserting data failed, no rows affected");
            } else {
                ResultSet generatedKeys = statement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    recordID = generatedKeys.getInt(1); // Return ID of new record which is insert in database
                } else {
                    log.error("Inserting data failed, no ID obtained");
                }

                generatedKeys.close();
            }

            statement.close();
            connection.close();

            return recordID;
        } catch (IllegalAccessException e) {
            log.error("Failed to get params from object", e);
        } catch (SQLException e) {
            log.error("Failed to insert data into table {}", entityName, e);
        }

        return 0;
    }

    @Override
    public void saveAll(List<T> objects) {
        if (Objects.isNull(objects) || objects.isEmpty())
            return;

        try {
            Map<String, Object> objectFields = Common.getObjectFields(objects.get(0));
            List<String> columns = new ArrayList<>(objectFields.keySet());
            StringBuilder sql = new StringBuilder("INSERT INTO " + entityName + " (");

            for (String column : columns) {
                sql.append("`").append(column).append("`, ");
            }

            sql.delete(sql.length() - 2, sql.length());
            sql.append(") VALUES ");

            String placeholders = "(" + "?, ".repeat(columns.size());
            placeholders = placeholders.substring(0, placeholders.length() - 2) + ")";
            sql.append(placeholders.repeat(objects.size()).replace(")(", "), ("));

            Connection connection = DataSourceProvider.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
            log.info("Execute batch insert query: " + sql);

            int paramIndex = 1;

            for (Object object : objects) {
                List<Object> params = new ArrayList<>(Common.getObjectFields(object).values());

                for (Object param : params) {
                    statement.setObject(paramIndex++, param);
                }
            }

            statement.executeUpdate();
            statement.close();
            connection.close();

        } catch (IllegalAccessException e) {
            log.error("Failed to get params from object", e);
        } catch (SQLException e) {
            log.error("Failed to insert data into table {}", entityName, e);
        }
    }

    @Override
    public boolean update(Object object) {
        try {
            Map<String, Object> columnsData = Common.getObjectFields(object);

            StringBuilder columns = new StringBuilder();
            StringBuilder sql = new StringBuilder("UPDATE `" + entityName + "` SET ");

            columnsData.keySet().forEach(key -> columns.append("`").append(key).append("` = ?,"));
            columns.delete(columns.length() - 1, columns.length()); // Remove last comma
            sql.append(columns);

            return execute(sql.toString(), Arrays.asList(columnsData.values().toArray()));
        } catch (IllegalAccessException e) {
            log.error("Cannot update entity: {}", object.getClass(), e);
        }

        return false;
    }

    @Override
    public boolean update(String sql, List<Object> params) {
        return execute(sql, params);
    }

    @Override
    public boolean delete(Integer ID) {
        String sql = "DELETE FROM `" + entityName + "` WHERE `id` = ?";
        return execute(sql, List.of(ID));
    }

    @Override
    public boolean delete(String conditions, List<Object> params) {
        String sql = "DELETE FROM `" + entityName + "` WHERE " + conditions;
        return execute(sql, params);
    }

    private boolean execute(String sql, List<Object> params) {
        try {
            Connection connection = DataSourceProvider.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            log.info("Execute query: " + sql);

            for (int i = 0; i < params.size(); ++i) {
                preparedStatement.setObject(i + 1, params.get(i)); // JDBC index start with 1
            }

            int affectedRows = preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();

            return affectedRows > 0;
        } catch (SQLException e) {
            log.error("Update or delete entity failed", e);
            return false;
        }
    }
}
