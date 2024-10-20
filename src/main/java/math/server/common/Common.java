package math.server.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Common {

    private static final Logger log = LoggerFactory.getLogger(Common.class);

    public static <T> void objectMapper(ResultSet resultSet, T object) throws SQLException {
        Field[] fields = object.getClass().getDeclaredFields();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        Map<String, Object> resultSetMap = new HashMap<>();

        // Populate the ResultSet map
        for (int i = 1; i <= columnCount; i++) {
            resultSetMap.put(metaData.getColumnName(i).toLowerCase(), resultSet.getObject(i));
        }

        // Map fields to the object
        for (Field field : fields) {
            field.setAccessible(true); // Allow access to private fields
            Object value = resultSetMap.get(field.getName().toLowerCase());

            if (value != null) {
                try {
                    field.set(object, value);
                } catch (IllegalAccessException e) {
                    log.error("Failed to set value for field: " + field.getName(), e);
                }
            }
        }
    }

    public static Map<String, Object> getObjectFields(Object object) throws IllegalAccessException {
        Field[] fields = object.getClass().getDeclaredFields();
        Map<String, Object> objectMap = new HashMap<>();

        for (Field field : fields) {
            field.setAccessible(true);  // Allow access to private fields

            String fieldName = field.getName();
            Object value = field.get(object);

            objectMap.put(fieldName, value);
        }

        return objectMap;
    }
}
