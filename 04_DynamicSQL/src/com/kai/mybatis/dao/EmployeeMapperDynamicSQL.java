package com.kai.mybatis.dao;

import com.kai.mybatis.bean.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description:
 * @author: kai.lv
 * @date: 2022/1/11
 **/
public interface EmployeeMapperDynamicSQL {

    public List<Employee> getEmpsByConditionIf(Employee employee);

    public List<Employee> getEmpsByConditionTrim(Employee employee);

    public List<Employee> getEmpsByConditionChoose(Employee employee);

    public void updateEmp(Employee employee);

    public List<Employee> getEmpsByConditionForeach(@Param("ids") List<Integer> ids);

    public void addEmps(@Param("emps") List<Employee> emps);

    public List<Employee> getEmpsInnerParameter(Employee employee);


}
