package com.jph.putils.db.converter;

import android.database.Cursor;
import android.text.TextUtils;
import com.jph.putils.db.sqlite.ColumnDbType;
public class ShortColumnConverter implements ColumnConverter<Short> {
    @Override
    public Short getFieldValue(final Cursor cursor, int index) {
        return cursor.isNull(index) ? null : cursor.getShort(index);
    }

    @Override
    public Short getFieldValue(String fieldStringValue) {
        if (TextUtils.isEmpty(fieldStringValue)) return null;
        return Short.valueOf(fieldStringValue);
    }

    @Override
    public Object fieldValue2ColumnValue(Short fieldValue) {
        return fieldValue;
    }

    @Override
    public ColumnDbType getColumnDbType() {
        return ColumnDbType.INTEGER;
    }
}
