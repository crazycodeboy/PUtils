package com.jph.putils.db.table;

import android.text.TextUtils;
import com.jph.putils.db.annotation.Id;
import com.jph.putils.db.annotation.Table;
import com.jph.putils.db.converter.ColumnConverterFactory;
import com.jph.putils.util.LogUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class TableUtils {

    private TableUtils() {
    }

    public static String getTableName(Class<?> entityType) {
        Table table = entityType.getAnnotation(Table.class);
        if (table == null || TextUtils.isEmpty(table.name())) {
            return entityType.getName().replace('.', '_');
        }
        return table.name();
    }

    public static String getExecAfterTableCreated(Class<?> entityType) {
        Table table = entityType.getAnnotation(Table.class);
        if (table != null) {
            return table.execAfterTableCreated();
        }
        return null;
    }

    /**
     * key: entityType.name
     */
    private static ConcurrentHashMap<String, HashMap<String, Column>> entityColumnsMap = new ConcurrentHashMap<String, HashMap<String, Column>>();

    /* package */
    static synchronized HashMap<String, Column> getColumnMap(Class<?> entityType) {

        if (entityColumnsMap.containsKey(entityType.getName())) {
            return entityColumnsMap.get(entityType.getName());
        }

        HashMap<String, Column> columnMap = new HashMap<String, Column>();
        String primaryKeyFieldName = getPrimaryKeyFieldName(entityType);
        addColumns2Map(entityType, primaryKeyFieldName, columnMap);
        entityColumnsMap.put(entityType.getName(), columnMap);

        return columnMap;
    }

    private static void addColumns2Map(Class<?> entityType, String primaryKeyFieldName, HashMap<String, Column> columnMap) {
        if (Object.class.equals(entityType)) return;
        try {
            Field[] fields = entityType.getDeclaredFields();
            for (Field field : fields) {
                if (ColumnUtils.isTransient(field) || Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                if (ColumnConverterFactory.isSupportColumnConverter(field.getType())) {
                    if (!field.getName().equals(primaryKeyFieldName)) {
                        Column column = new Column(entityType, field);
                        if (!columnMap.containsKey(column.getColumnName())) {
                            columnMap.put(column.getColumnName(), column);
                        }
                    }
                } else if (ColumnUtils.isForeign(field)) {
                    Foreign column = new Foreign(entityType, field);
                    if (!columnMap.containsKey(column.getColumnName())) {
                        columnMap.put(column.getColumnName(), column);
                    }
                } else if (ColumnUtils.isFinder(field)) {
                    Finder column = new Finder(entityType, field);
                    if (!columnMap.containsKey(column.getColumnName())) {
                        columnMap.put(column.getColumnName(), column);
                    }
                }
            }

            if (!Object.class.equals(entityType.getSuperclass())) {
                addColumns2Map(entityType.getSuperclass(), primaryKeyFieldName, columnMap);
            }
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
        }
    }

    /* package */
    static Column getColumnOrId(Class<?> entityType, String columnName) {
        if (getPrimaryKeyColumnName(entityType).equals(columnName)) {
            return getId(entityType);
        }
        return getColumnMap(entityType).get(columnName);
    }

    /**
     * key: entityType.name
     */
    private static ConcurrentHashMap<String, com.jph.putils.db.table.Id> entityIdMap = new ConcurrentHashMap<String, com.jph.putils.db.table.Id>();

    /* package */
    static synchronized com.jph.putils.db.table.Id getId(Class<?> entityType) {
        if (Object.class.equals(entityType)) {
            throw new RuntimeException("field 'id' not found");
        }

        if (entityIdMap.containsKey(entityType.getName())) {
            return entityIdMap.get(entityType.getName());
        }

        Field primaryKeyField = null;
        Field[] fields = entityType.getDeclaredFields();
        if (fields != null) {

            for (Field field : fields) {
                if (field.getAnnotation(Id.class) != null) {
                    primaryKeyField = field;
                    break;
                }
            }

            if (primaryKeyField == null) {
                for (Field field : fields) {
                    if ("id".equals(field.getName()) || "_id".equals(field.getName())) {
                        primaryKeyField = field;
                        break;
                    }
                }
            }
        }

        if (primaryKeyField == null) {
            return getId(entityType.getSuperclass());
        }

        com.jph.putils.db.table.Id id = new com.jph.putils.db.table.Id(entityType, primaryKeyField);
        entityIdMap.put(entityType.getName(), id);
        return id;
    }

    private static String getPrimaryKeyFieldName(Class<?> entityType) {
        com.jph.putils.db.table.Id id = getId(entityType);
        return id == null ? null : id.getColumnField().getName();
    }

    private static String getPrimaryKeyColumnName(Class<?> entityType) {
        com.jph.putils.db.table.Id id = getId(entityType);
        return id == null ? null : id.getColumnName();
    }
}
