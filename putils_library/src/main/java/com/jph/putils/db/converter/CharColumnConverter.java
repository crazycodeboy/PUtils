package com.jph.putils.db.converter;

import android.database.Cursor;
import android.text.TextUtils;

import com.jph.putils.db.sqlite.ColumnDbType;
public class CharColumnConverter implements ColumnConverter<Character> {
    @Override
    public Character getFieldValue(final Cursor cursor, int index) {
        return cursor.isNull(index) ? null : (char) cursor.getInt(index);
    }

    @Override
    public Character getFieldValue(String fieldStringValue) {
        if (TextUtils.isEmpty(fieldStringValue)) return null;
        return fieldStringValue.charAt(0);
    }

    @Override
    public Object fieldValue2ColumnValue(Character fieldValue) {
        if (fieldValue == null) return null;
        return (int) fieldValue;
    }

    @Override
    public ColumnDbType getColumnDbType() {
        return ColumnDbType.INTEGER;
    }
}
