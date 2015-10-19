package com.jph.putils.db.table;

import android.text.TextUtils;
import com.jph.putils.DbUtils;

import java.util.HashMap;
import java.util.Map;


public class Table {

    public final DbUtils db;
    public final String tableName;
    public final Id id;

    /**
     * key: columnName
     */
    public final HashMap<String, Column> columnMap;

    /**
     * key: columnName
     */
    public final HashMap<String, Finder> finderMap;

    /**
     * key: dbName#className
     */
    private static final HashMap<String, Table> tableMap = new HashMap<String, Table>();

    private Table(DbUtils db, Class<?> entityType) {
        this.db = db;
        this.tableName = TableUtils.getTableName(entityType);
        this.id = TableUtils.getId(entityType);
        this.columnMap = TableUtils.getColumnMap(entityType);

        finderMap = new HashMap<String, Finder>();
        for (Column column : columnMap.values()) {
            column.setTable(this);
            if (column instanceof Finder) {
                finderMap.put(column.getColumnName(), (Finder) column);
            }
        }
    }

    public static synchronized Table get(DbUtils db, Class<?> entityType) {
        String tableKey = db.getDaoConfig().getDbName() + "#" + entityType.getName();
        Table table = tableMap.get(tableKey);
        if (table == null) {
            table = new Table(db, entityType);
            tableMap.put(tableKey, table);
        }

        return table;
    }

    public static synchronized void remove(DbUtils db, Class<?> entityType) {
        String tableKey = db.getDaoConfig().getDbName() + "#" + entityType.getName();
        tableMap.remove(tableKey);
    }

    public static synchronized void remove(DbUtils db, String tableName) {
        if (tableMap.size() > 0) {
            String key = null;
            for (Map.Entry<String, Table> entry : tableMap.entrySet()) {
                Table table = entry.getValue();
                if (table != null && table.tableName.equals(tableName)) {
                    key = entry.getKey();
                    if (key.startsWith(db.getDaoConfig().getDbName() + "#")) {
                        break;
                    }
                }
            }
            if (TextUtils.isEmpty(key)) {
                tableMap.remove(key);
            }
        }
    }

    private boolean checkedDatabase;

    public boolean isCheckedDatabase() {
        return checkedDatabase;
    }

    public void setCheckedDatabase(boolean checkedDatabase) {
        this.checkedDatabase = checkedDatabase;
    }

}
