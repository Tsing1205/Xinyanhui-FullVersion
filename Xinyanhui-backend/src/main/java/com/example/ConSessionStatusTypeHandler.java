package com.example;

import com.example.pojo.ConSessionStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(ConSessionStatus.class)
public class ConSessionStatusTypeHandler extends BaseTypeHandler<ConSessionStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ConSessionStatus parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getValue()); // 写入数据库时用枚举的 value
    }

    @Override
    public ConSessionStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return ConSessionStatus.fromValue(value); // 读取数据库字段映射回枚举
    }

    @Override
    public ConSessionStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return ConSessionStatus.fromValue(rs.getString(columnIndex));
    }

    @Override
    public ConSessionStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return ConSessionStatus.fromValue(cs.getString(columnIndex));
    }
}