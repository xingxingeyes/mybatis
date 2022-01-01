package com.kai.mybatis.dao;

import com.kai.mybatis.bean.Employee;
import org.apache.ibatis.annotations.Param;

/**
 * @description:
 * @author: kai.lv
 * @date: 2021/12/12
 **/
public interface EmployeeMapper {

    public Employee getEmpById(Integer id);

    public Employee getEmpByIdAndName(@Param("id") Integer id, @Param("lastName") String name);

    public void addEmp(Employee employee);

    public void updateEmp(Employee employee);

    public void deleteEmpById(Integer id);
}
