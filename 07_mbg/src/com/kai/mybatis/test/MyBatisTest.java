package com.kai.mybatis.test;


import com.kai.mybatis.bean.Employee;
import com.kai.mybatis.bean.EmployeeExample;
import com.kai.mybatis.dao.EmployeeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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


    @Test
    public void testMbg() throws Exception{
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        File configFile = new File("mbg.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,
                callback, warnings);
        myBatisGenerator.generate(null);
    }


    // @Test
    // public void testSimple() throws IOException {
    //     SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
    //     SqlSession openSession = sqlSessionFactory.openSession();
    //
    //     try {
    //         EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
    //         List<Employee> list = mapper.selectAll(null);
    //         for (Employee emp : list) {
    //             System.out.println(emp.getId());
    //         }
    //         openSession.commit();
    //     } finally {
    //         openSession.close();
    //     }
    // }

    @Test
    public void testMyBatis3() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();

        try {
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            // xxxExample就是封装查询条件得
            // 1、查询所有
            // List<Employee> list = mapper.selectByExample(null);
            // 2、查询员工名字有e的字母，和员工性别是1的
            EmployeeExample example = new EmployeeExample();
            // 创建一个Criteria，这个Criteria就是拼装查询条件
            // select id, last_name, email, gender, d_id from tbl_employee
            // WHERE ( last_name like ? and gender = ? or email like ?)
            EmployeeExample.Criteria criteria = example.createCriteria();
            criteria.andLastNameLike("%e%");
            criteria.andGenderEqualTo("1");

            EmployeeExample.Criteria criteria1 = example.createCriteria();
            criteria1.andEmailLike("%e%");
            example.or(criteria1);

            List<Employee> list = mapper.selectByExample(example);
            for (Employee emp : list) {
                System.out.println(emp.getId());
            }
            openSession.commit();
        } finally {
            openSession.close();
        }
    }





}
