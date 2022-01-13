package com.kai.mybatis.dao;

import com.kai.mybatis.bean.Department;

/**
 * @description:
 * @author: kai.lv
 * @date: 2022/1/10
 **/
public interface DepartmentMapper {

    public Department getDeptById(Integer id);

    public Department getDepByIdPlus(Integer id);

    public Department getDeptByIdStep(Integer id);



}
