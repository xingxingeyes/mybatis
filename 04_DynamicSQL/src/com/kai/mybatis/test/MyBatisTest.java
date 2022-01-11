package com.kai.mybatis.test;


import com.kai.mybatis.bean.Department;
import com.kai.mybatis.bean.Employee;
import com.kai.mybatis.dao.*;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 根据XML配置文件（全局配置文件）创建一个SqlSessonFaction对象
 * 1.接口是编程
 * 原生：       Dao ===》 DaoImpl
 * mybatis:    Mapper ===> xxMapper.xml
 * 2.SqlSession代表数据库的一次会话：用完必须关闭
 * 3.SqlSession和connection一样都是非线程安全。每次使用都应该去获取新的对象。
 * 4.mapper接口没有实现类，但是mybatis会为这个接口生成一个代理对象。
 * 将接口和xml进行绑定
 * EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class)
 * 5.两个重要的配置文件：
 * mybatis的全局配置文件：包含数据库连接池信息，事务管理器等。。。凶弹运行环境信息
 * sql映射文件：保存了每一个sql语句的映射信息 将sql抽取出来
 * @author: kai.lv
 * @date: 2021/12/11
 **/
public class MyBatisTest {

    public SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }


    /**
     * 1.根据xml配置文件（全局配置文件）创建一个SqlSessionFactory对象有数据源一些运行环境信息
     * 2.sql映射文件；配置了么一个sql，以及sql的封装规则等。
     * 3.将sql映射文件注册在全局配置文件中
     * 4.写代码
     * 1).根据全局配置文件得到SqlSessionFacrory
     * 2).使用sqlSession工厂，获取到sqlSession对象使用来执行增删改查，一个sqlSession就是
     * 代表和数据库的一次会话，用完关闭
     * 3).使用sql的唯一标识来告诉Mybatis执行哪个sql，sql都是保存在sql映射文件中的。
     *
     * @throws IOException
     */
    @Test
    public void test() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        // 2.获取sqlSession实例，能直接执行已经映射的sql语句
        SqlSession openSession = sqlSessionFactory.openSession();

        try {
            Employee employee = openSession.selectOne("org.kai.mybatis.EmployeeMapper.selectEmp", 1);
            System.out.println(employee);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            openSession.close();
        }
    }

    @Test
    public void test01() throws IOException {
        // 1.获取sqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        // 2.获取sqlSession对象
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            // 3.获取接口的实现类对象
            // 会为接口自动创建一个代理对象，代理对象去执行增删改查
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            Employee employee = mapper.getEmpById(1);
            System.out.println(mapper.getClass());
            System.out.println(employee);
        } finally {
            openSession.close();
        }
    }

    @Test
    public void test2() throws IOException {
        // 1.获取sqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        // 2.获取sqlSession对象
        SqlSession openSession = sqlSessionFactory.openSession();

        try {
            EmployeeMapperAnnotation mapper = openSession.getMapper(EmployeeMapperAnnotation.class);
            Employee employee = mapper.getEmpById(1);
            System.out.println(employee);
        } finally {
            openSession.close();
        }
    }

    /**
     * 测试增删改
     *
     * @throws IOException
     */
    @Test
    public void test03() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        // 1.获取到的sqlsessionFactory不会自动提交数据
        SqlSession openSession = sqlSessionFactory.openSession();

        try {
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            Employee employee = new Employee(null, "jerry", "jerry@kai.com", "1");
            mapper.addEmp(employee);
            System.out.println(employee.getId());
            // mapper.updateEmp(new Employee(5,"jerry","jerry@kai.com","0"));
            // mapper.deleteEmpById(5);
            // 2.手动提交数据
            openSession.commit();
        } finally {
            openSession.close();
        }
    }

    /**
     * 测试增删改
     *
     * @throws IOException
     */
    @Test
    public void test04() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        // 1.获取到的sqlsessionFactory不会自动提交数据
        SqlSession openSession = sqlSessionFactory.openSession();

        try {
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            // Employee jerry = mapper.getEmpByIdAndName(6, "jerry");
            // System.out.println(jerry);
            // Map<String, Object> map = new HashMap<>();
            // map.put("id",1);
            // map.put("lastName", "tom");
            // map.put("tableName", "tbl_employee");
            // Employee empByMap = mapper.getEmpByMap(map);
            // System.out.println(empByMap);

            // List<Employee> emps = mapper.getEmpsByLastNameLike("%e%");
            // for (Employee emp: emps) {
            //     System.out.println(emp);
            // }

            // Map<String, Object> map = mapper.getEmpByIdReturnMap(1);
            // System.out.println(map);

            // Map<String, Employee> maps = mapper.getEmpByLastNameLikeReturnMap("%r%");
            // System.out.println(maps);

            openSession.commit();
        } finally {
            openSession.close();
        }
    }

    @Test
    public void test05() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapperPlus mapper = openSession.getMapper(EmployeeMapperPlus.class);
            // Employee emp = mapper.getEmpById(1);
            // System.out.println(emp);

            // Employee empAndDept = mapper.getEmpAndDept(1);
            // System.out.println(empAndDept);
            // System.out.println(empAndDept.getDept());

            // Employee empByIdStep = mapper.getEmpByIdStep(1);
            // System.out.println(empByIdStep);
            // System.out.println(empByIdStep.getDept());
            //
            Employee empByIdStep = mapper.getEmpByIdStep(6);
            System.out.println(empByIdStep);
            System.out.println(empByIdStep.getDept());

            openSession.commit();
        } finally {
            openSession.close();
        }
    }


    @Test
    public void test06() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            DepartmentMapper mapper = openSession.getMapper(DepartmentMapper.class);
            // Department dept = mapper.getDepByIdPlus(3);
            // System.out.println(dept);
            // System.out.println(dept.getEmps());

            Department deptByIdStep = mapper.getDeptByIdStep(3);
            System.out.println(deptByIdStep.getDepartmentName());
            // System.out.println(deptByIdStep.getEmps());
            openSession.commit();
        } finally {
            openSession.close();
        }
    }

    @Test
    public void testDynamicSql() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();

        try {
            EmployeeMapperDynamicSQL mapper = openSession.getMapper(EmployeeMapperDynamicSQL.class);
//            Employee employee = new Employee(6, "%e%", "jerry@kai.com", null);
            Employee employee = new Employee(null, "%e%", null, null);
            List<Employee> emps = mapper.getEmpsByConditionIf(employee);
            for (Employee emp : emps) {
                System.out.println(emp);
            }

            // 查询的时候如果某些条件没带可能sql拼装会有问题
            // 1、给where后面加上1=1，以后的条件都and xxx.
            // 2、mybatis使用where标签来将所有的查询条件包括在内。mybatis就会将where标签中拼装的sql，多出来的and或者or去掉。
                // where只会去掉第一个多出来的and或者or。

            openSession.commit();
        } finally {
            openSession.close();
        }


    }






}
