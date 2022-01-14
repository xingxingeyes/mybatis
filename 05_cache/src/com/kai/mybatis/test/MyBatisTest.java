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
import java.util.*;

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
     * 两级缓存
     * 一级缓存：（本地缓存）: sqlSession级别的缓存，一级缓存是一直开启的；
     *          与数据库同义词会话期间查询到的数据会凡在本地缓存中。
     *          以后如果需要获取相同的数据，直接从缓存中拿，没必要再去查询数据库
     *
     *          一级缓存失效情况（没有使用到当前一级缓存的情况，效果就是，还需要再向数据库发出查询）
     *          1、sqlSession不同。
     *          2、sqlSession相同，查询条件不同。
     *          3、sqlSession相同，两次查询之间执行了增删改操作
     *          4、sqlSession相同，手动清除一级缓存
     *
     *
     * 二级缓存：（全局缓存） 基于namespace级别的缓存：一个namespace对应一个二级缓存；
     *          工作机制：
     *          1、一个会话，查询一条数据，这个数据就会被放在当前会话的一级缓存中；
     *          2、如果会话关闭，一级缓存中的数据会被保存到二级缓存中；新的会话就可以参照耳机缓存中的内容；
     *          3、sqlSession===EmployeeMapper==>Employee
     *                          DepartmentMapper===>Department
     *             不同namespace查询的数据会放在自己对应的缓存中(map)
     *             效果：数据会从二级缓存中获取
     *                  查出的数据都会被默认放在一级缓存中
     *                  只有会话提交或者关闭后，一级缓存中的数据才会转移到二级缓存中
     *          使用:
     *              1、开启全局二级缓存配置 <setting name="cacheEnabled" value="true"/>
     *              2、去mapper.xml中配置使用二级缓存:
     *                  <cache></cache>
     *              3、我们的POJO需要实现反序列化接口
     *
     * 和缓存有关的设置/属性：
     *              1、cacheEnabled=true,false:关闭缓存（二级缓存关闭）（一级缓存一直可用）
     *              2、每个select标签都有useCache="true",
     *                      false:不使用缓存（一级缓存依然使用，二级缓存不使用）
     *              3、每个增删改标签的 flushCache="true"（一级二级缓存都会清除）,
     *                  增删改执行完成后就会清除缓存；
     *                  测试：flushCache="true":一级缓存就清空了；耳机也会被清除。
     *                  查询标签：flushCache="true";每次查询之后都会清空缓存，缓存是没有被使用的。
     *              4、sqlSession.clearCache();只是清除当前session的一级缓存；
     *              5、localCacheScope:本地缓存作用域：（一级缓存SESSION):当前会话的所有数据保存在会话缓存中；
     *                                                      STATEMENT:可以禁用一级缓存；
     *
     * 第三方缓存整合：
     *              1、导入第三方缓存包即可
     *              2、导入第三方缓存整合适配包；官方有
     *              3、mapper.xml中使用自定义缓存
     *                  <cache type="org.mybatis.caches.ehcache.EhcacheCache">
     *
     *
     *
     */

    @Test
    public void testFirstLevelCache() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();

        try {
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            Employee emp1 = mapper.getEmpById(1);
            System.out.println(emp1);

            // mapper.addEmp(new Employee(null,"Anna","aaa@kai.com","1"));

            openSession.clearCache();

            // SqlSession openSession2 = sqlSessionFactory.openSession();
            // EmployeeMapper mapper2 = openSession2.getMapper(EmployeeMapper.class);
            Employee emp2 = mapper.getEmpById(1);
            System.out.println(emp2);
            //
            // openSession2.close();
            // openSession.commit();
        } finally {
            openSession.close();
        }
    }

    @Test
    public void testSecondLevelCache() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();

        try {
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            Employee emp1 = mapper.getEmpById(1);
            System.out.println(emp1);
            openSession.commit();
            openSession.close();



            SqlSession openSession2 = sqlSessionFactory.openSession();
            EmployeeMapper mapper2 = openSession2.getMapper(EmployeeMapper.class);
            mapper2.addEmp(new Employee(null,"Anna","aaa@kai.com","1"));
            Employee emp2 = mapper2.getEmpById(1);
            System.out.println(emp2);


            openSession2.commit();
            openSession2.close();
        } finally {

        }
    }

    @Test
    public void testSecondLevelCache02() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();

        try {
            DepartmentMapper mapper = openSession.getMapper(DepartmentMapper.class);
            Department dept1 = mapper.getDeptById(1);
            System.out.println(dept1);
            openSession.commit();
            openSession.close();

            SqlSession openSession2 = sqlSessionFactory.openSession();
            DepartmentMapper mapper2 = openSession2.getMapper(DepartmentMapper.class);
            Department dept2 = mapper2.getDeptById(1);
            System.out.println(dept2);


            openSession2.commit();
            openSession2.close();
        } finally {

        }
    }








}
