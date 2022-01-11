package com.kai.mybatis.dao;

import com.kai.mybatis.bean.Employee;

import java.util.List;

/**
 * @description:
 * @author: kai.lv
 * @date: 2022/1/11
 **/
public interface EmployeeMapperDynamicSQL {

    public List<Employee> getEmpsByConditionIf(Employee employee);


}
