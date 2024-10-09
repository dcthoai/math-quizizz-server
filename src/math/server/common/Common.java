package math.server.common;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Common {

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
                    throw new RuntimeException("Failed to set value for field: " + field.getName(), e);
                }
            }
        }
    }
}
