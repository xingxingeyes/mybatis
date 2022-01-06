package com.kai.mybatis.dao;

import com.kai.mybatis.bean.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @description:描述
 * @author: kai.lv
 * @date: 2021/12/12
 **/
public interface EmployeeMapper {

    public Employee getEmpById(Integer id);

    public Employee getEmpByMap(Map<String,Object> map);

    public Employee getEmpByIdAndName(@Param("id") Integer id, String name);

    public void addEmp(Employee employee);

    public void updateEmp(Employee employee);

    public void deleteEmpById(Integer id);
}
