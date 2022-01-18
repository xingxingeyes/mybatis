package com.kai.mybatis.test;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kai.mybatis.bean.EmpStatus;
import com.kai.mybatis.bean.Employee;
import com.kai.mybatis.bean.OraclePage;
import com.kai.mybatis.dao.EmployeeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

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
            Page<Object> page = PageHelper.startPage(5, 1);


            List<Employee> emps = mapper.getEmps();
            // 传入要连续显示多少页
            PageInfo<Employee> info = new PageInfo<>(emps, 5);
            for (Employee emp : emps) {
                System.out.println(emp);
            }
            // System.out.println("当前页码："+page.getPageNum());
            // System.out.println("总记录数："+page.getTotal());
            // System.out.println("每页的记录数："+page.getPageSize());
            // System.out.println("总页码："+page.getPages());

            System.out.println("当前页码：" + info.getPageNum());
            System.out.println("总记录数：" + info.getTotal());
            System.out.println("每页的记录数：" + info.getPageSize());
            System.out.println("总页码：" + info.getPages());
            System.out.println("是否第一页：" + info.isIsFirstPage());
            int[] nums = info.getNavigatepageNums();
            for (int i = 0; i < nums.length; i++) {
                System.out.println(nums[i]);
            }
        } finally {
            openSession.close();
        }
    }


    @Test
    public void testBatch() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        // 可以执行批量操作的sqlSession
        SqlSession openSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        try {
            long start = System.currentTimeMillis();
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            for (int i = 0; i < 10000; i++) {
                mapper.addEmp(new Employee(UUID.randomUUID().toString().substring(0, 5), "0", "b"));
            }
            openSession.commit();
            long end = System.currentTimeMillis();
            //批量：（预编译sql一次==>设置参数===>10000次===>执行（1次））
            //Parameters: 616c1(String), b(String), 1(String)==>4598
            //非批量：（预编译sql=设置参数=执行）==》10000    10200
            System.out.println("耗时：" + (end - start));
        } finally {
            openSession.close();
        }

    }

    /**
     * oracle分页：
     * 借助rownum：行号；子查询；
     * 存储过程包装分页逻辑
     *
     * @throws IOException
     */
    @Test
    public void testProcedure() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            OraclePage page = new OraclePage();
            page.setStart(1);
            page.setEnd(5);
            mapper.getPageByProcedure(page);

            System.out.println("总记录数：" + page.getCount());
            System.out.println("查出的数据：" + page.getEmps().size());
            System.out.println("查出的数据：" + page.getEmps());
        } finally {
            openSession.close();
        }
    }

    @Test
    public void testEnumUse() {
        EmpStatus login = EmpStatus.LOGIN;
        System.out.println("枚举的索引：" + login.ordinal());
        System.out.println("枚举的名字：" + login.name());

        System.out.println("枚举的状态码" + login.getCode());
        System.out.println("枚举的提示消息：" + login.getMsg());

    }


    /**
     * 默认mybatis在处理枚举对象的时候保存的是枚举的名字；EnumTypeHandler
     * 改变使用：EmumOrdinalTypeHandler
     *
     * @throws IOException
     */
    @Test
    public void testEnum() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();

        try {
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            // Employee employee = new Employee("test_enum", "1", "enum@kai.com");
            // mapper.addEmp(employee);
            // System.out.println("保存成功" + employee.getId());
            Employee empById = mapper.getEmpById(30029);
            System.out.println(empById.getEmpStatus());
            openSession.commit();
        } finally {
            openSession.close();
        }

    }


}
