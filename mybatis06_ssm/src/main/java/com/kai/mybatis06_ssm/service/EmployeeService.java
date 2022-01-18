package com.kai.mybatis06_ssm.service;

import com.kai.mybatis06_ssm.bean.Employee;
import com.kai.mybatis06_ssm.dao.EmployeeMapper;
import org.apache.ibatis.session.SqlSession;
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
     EmployeeMapper employeeMapper;

    @Autowired // 批量操作
    private SqlSession sqlSession;

    public List<Employee> getEmps() {
        // EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);

        return employeeMapper.getEmps();
    }

}
