package math.server.repository.utils;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public interface IEntityManager<T> {

    List<T> query(String sql, List<Object> params);

    List<T> query(String sql, List<Object> params, Class<T> clazz);

    T findOne(Integer ID);

    T findOne(String conditions, List<Object> params);

    List<T> findAll();

    List<T> findAll(String conditions, List<Object> params);

    Integer insert(Object object);

    void saveAll(List<T> objects);

    boolean update(String conditions, Map<String, Object> columnsData);

    boolean delete(Integer ID);

    boolean delete(String conditions, List<Object> params);
}
