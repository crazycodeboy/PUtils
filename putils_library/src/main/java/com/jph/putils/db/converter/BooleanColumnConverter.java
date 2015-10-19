package com.jph.putils.db.converter;

import android.database.Cursor;
import android.text.TextUtils;
import com.jph.putils.db.sqlite.ColumnDbType;
public class BooleanColumnConverter implements ColumnConverter<Boolean> {
    @Override
    public Boolean getFieldValue(final Cursor cursor, int index) {
        return cursor.isNull(index) ? null : cursor.getInt(index) == 1;
    }

    @Override
    public Boolean getFieldValue(String fieldStringValue) {
        if (TextUtils.isEmpty(fieldStringValue)) return null;
        return fieldStringValue.length() == 1 ? "1".equals(fieldStringValue) : Boolean.valueOf(fieldStringValue);
    }

    @Override
    public Object fieldValue2ColumnValue(Boolean fieldValue) {
        if (fieldValue == null) return null;
        return fieldValue ? 1 : 0;
    }

    @Override
    public ColumnDbType getColumnDbType() {
        return ColumnDbType.INTEGER;
    }
}
