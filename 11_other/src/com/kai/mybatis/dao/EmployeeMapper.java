package com.kai.mybatis.dao;

import com.kai.mybatis.bean.Employee;
import com.kai.mybatis.bean.OraclePage;

import java.util.List;

/**
 * @description:
 * @author: kai.lv
 * @date: 2021/12/12
 **/
public interface EmployeeMapper {

    public Employee getEmpById(Integer id);

    public List<Employee> getEmps();

    public void addEmp(Employee employee);

    public void getPageByProcedure(OraclePage page);
}
