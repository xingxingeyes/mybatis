package com.kai.mybatis.typehandler;

import com.kai.mybatis.bean.EmpStatus;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @description: 1、实现TypeHandler接口
 * @author: kai.lv
 * @date: 2022/1/18
 **/
public class MyEnumStatusTypeHandler implements TypeHandler<EmpStatus> {

    /**
     * 定义当前数据如何保存到数据库中
     *
     * @param preparedStatement
     * @param i
     * @param empStatus
     * @param jdbcType
     * @throws SQLException
     */
    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, EmpStatus empStatus, JdbcType jdbcType) throws SQLException {
        System.out.println("要保存的状态码："+empStatus.getCode());
        preparedStatement.setString(i, empStatus.getCode().toString());
    }

    @Override
    public EmpStatus getResult(ResultSet resultSet, String s) throws SQLException {
        // 需要根据从数据库中拿到的枚举的状态码返回一个枚举对象
        int code = resultSet.getInt(s);
        System.out.println("从数据库中获取的状态码：" + code);
        EmpStatus status = EmpStatus.getEmpStatusByCode(code);
        return status;
    }

    @Override
    public EmpStatus getResult(ResultSet resultSet, int i) throws SQLException {
        // 需要根据从数据库中拿到的枚举的状态码返回一个枚举对象
        int code = resultSet.getInt(i);
        System.out.println("从数据库中获取的状态码：" + code);
        EmpStatus status = EmpStatus.getEmpStatusByCode(code);
        return status;
    }

    @Override
    public EmpStatus getResult(CallableStatement callableStatement, int i) throws SQLException {
        // 需要根据从数据库中拿到的枚举的状态码返回一个枚举对象
        int code = callableStatement.getInt(i);
        System.out.println("从数据库中获取的状态码：" + code);
        EmpStatus status = EmpStatus.getEmpStatusByCode(code);
        return status;
    }
}
