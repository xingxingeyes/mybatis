package com.kai.mybatis06_ssm.service;

import com.kai.mybatis06_ssm.bean.Employee;
import com.kai.mybatis06_ssm.dao.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: kai.lv
 * @date: 2022/1/14
 **/
@Service
public class EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    public List<Employee> getEmps() {
        return employeeMapper.getEmps();
    }

}
