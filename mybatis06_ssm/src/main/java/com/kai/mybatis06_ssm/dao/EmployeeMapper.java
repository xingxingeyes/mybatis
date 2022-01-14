package com.kai.mybatis06_ssm.dao;


import com.kai.mybatis06_ssm.bean.Employee;

import java.util.List;

/**
 * @description:描述
 * @author: kai.lv
 * @date: 2021/12/12
 **/
public interface EmployeeMapper {


    public Employee getEmpById(Integer id);

    public List<Employee> getEmps();


}
