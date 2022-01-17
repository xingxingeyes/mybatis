package com.kai.mybatis06_ssm.controller;

import com.kai.mybatis06_ssm.bean.Employee;
import com.kai.mybatis06_ssm.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: kai.lv
 * @date: 2022/1/14
 **/
@Controller
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @RequestMapping("/emps")
    public String emps(Map<String,Object> map) {
        List<Employee> emps = employeeService.getEmps();
        map.put("allEmps", emps);
        return "list";
    }


}
