package com.kai.mybatis.dao;

import com.kai.mybatis.bean.Employee;

/**
 * @description:
 * @author: kai.lv
 * @date: 2021/12/12
 **/
public interface EmployeeMapper {

    public Employee getEmpById(Integer id);
}
