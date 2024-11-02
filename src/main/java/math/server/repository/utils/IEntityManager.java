package math.server.repository.utils;

import java.util.List;

@SuppressWarnings("unused")
public interface IEntityManager<T> {

    List<T> query(String sql, List<Object> params);

    <EntityType> List<EntityType> query(String sql, List<Object> params, Class<EntityType> entityTypeClass);

    T findOne(Integer ID);

    T findOne(String conditions, List<Object> params);

    <EntityType> EntityType findOne(Integer ID, Class<EntityType> entityTypeClass);

    <EntityType> EntityType findOne(String conditions, List<Object> params, Class<EntityType> entityTypeClass);

    List<T> findAll();

    List<T> findAll(String conditions, List<Object> params);

    <EntityType> List<EntityType> findAll(Class<EntityType> entityTypeClass);

    <EntityType> List<EntityType> findAll(String conditions, List<Object> params, Class<EntityType> entityTypeClass);

    int save(Object object);

    void saveAll(List<T> objects);

    boolean update(Object object);

    boolean update(String sql, List<Object> params);

    boolean delete(Integer ID);

    boolean delete(String conditions, List<Object> params);
}
