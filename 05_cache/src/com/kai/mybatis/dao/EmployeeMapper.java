package com.kai.mybatis.dao;

import com.kai.mybatis.bean.Employee;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @description:描述
 * @author: kai.lv
 * @date: 2021/12/12
 **/
public interface EmployeeMapper {

    //多条记录封装成map: Map<Integer, Employee>键是这条记录的主键，值是记录封装后的javaBean
    @MapKey("lastName") // 告诉mybatis封装这个Map的时候使用哪个属性作为主键
    public Map<String, Employee> getEmpByLastNameLikeReturnMap(String lastName);
    // 返回一天记录的map;key就是列名，值就是对应的值
    public Map<String, Object> getEmpByIdReturnMap(Integer id);

    public List<Employee> getEmpsByLastNameLike(String lastName);

    public Employee getEmpById(Integer id);

    public Employee getEmpByMap(Map<String,Object> map);

    public Employee getEmpByIdAndName(@Param("id") Integer id, String name);

    public void addEmp(Employee employee);

    public void updateEmp(Employee employee);

    public void deleteEmpById(Integer id);
}
