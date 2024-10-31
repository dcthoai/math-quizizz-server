package math.server.repository.utils;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public interface IEntityManager<T> {

    List<T> query(Class<T> entityName, String sql, List<Object> params);

    T findOne(Class<T> entityName, Integer ID);

    T findOne(Class<T> entityName, String conditions, List<Object> params);

    List<T> findAll(Class<T> entityName);

    List<T> findAll(Class<T> entityName, String conditions, List<Object> params);

    Integer insert(Class<T> entityName, Object object);

    boolean update(Class<T> entityName, String conditions, Map<String, Object> columnsData);

    boolean delete(Class<T> entityName, Integer ID);

    boolean delete(Class<T> entityName, String conditions, List<Object> params);
}
