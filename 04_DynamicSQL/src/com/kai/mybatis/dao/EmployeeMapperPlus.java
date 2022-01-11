package com.kai.mybatis.dao;

import com.kai.mybatis.bean.Employee;

import java.util.List;

/**
 * @description:
 * @author: kai.lv
 * @date: 2022/1/10
 **/
public interface EmployeeMapperPlus {

    public Employee getEmpById(Integer id);

    public Employee getEmpAndDept(Integer id);

    public Employee getEmpByIdStep(Integer id);

    public List<Employee> getEmpsByDeptId(Integer deptId);

}
