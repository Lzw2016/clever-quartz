package org.clever.quartz.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.io.*;
import java.sql.*;
import java.util.Map;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-30 11:42 <br/>
 */
@MappedTypes({Map.class})
@MappedJdbcTypes({JdbcType.BLOB})
public class JobDataTypeHandlerToObject extends BaseTypeHandler<Map> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Map parameter, JdbcType jdbcType) throws SQLException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(byteArray)) {
            out.writeObject(parameter);
        } catch (IOException e) {
            throw new SQLException("对象序列化异常", e);
        }
        byte[] data = byteArray.toByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ps.setBinaryStream(i, bis, data.length);
        try {
            bis.close();
        } catch (IOException ignored) {
        }
    }

    private Map getObject(Blob blob) throws SQLException {
        if (null == blob) {
            return null;
        }
        byte[] returnValue = blob.getBytes(1, (int) blob.length());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(returnValue);
        Object result;
        try (ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            result = objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new SQLException("对象反序列化异常", e);
        }
        try {
            inputStream.close();
        } catch (IOException ignored) {
        }
        if (result != null && !(result instanceof Map)) {
            throw new RuntimeException("对象反序列化异常,类型不匹配");
        }
        return (Map) result;
    }

    @Override
    public Map getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Blob blob = rs.getBlob(columnName);
        return getObject(blob);
    }

    @Override
    public Map getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Blob blob = rs.getBlob(columnIndex);
        return getObject(blob);
    }

    @Override
    public Map getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Blob blob = cs.getBlob(columnIndex);
        return getObject(blob);
    }
}
