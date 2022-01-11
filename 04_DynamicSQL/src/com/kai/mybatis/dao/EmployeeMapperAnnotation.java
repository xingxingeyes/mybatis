package com.kai.mybatis.dao;

import com.kai.mybatis.bean.Employee;
import org.apache.ibatis.annotations.Select;

/**
 * @description:
 * @author: kai.lv
 * @date: 2021/12/12
 **/

public interface EmployeeMapperAnnotation {
    @Select("select * from tbl_employee where id =#{id} ")
    public Employee getEmpById(Integer id);
}
