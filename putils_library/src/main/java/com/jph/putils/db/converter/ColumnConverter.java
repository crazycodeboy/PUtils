package com.jph.putils.db.converter;

import android.database.Cursor;

import com.jph.putils.db.sqlite.ColumnDbType;

public interface ColumnConverter<T> {

    T getFieldValue(final Cursor cursor, int index);

    T getFieldValue(String fieldStringValue);

    Object fieldValue2ColumnValue(T fieldValue);

    ColumnDbType getColumnDbType();
}
