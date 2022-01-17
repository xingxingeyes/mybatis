package com.kai.mybatis.test;


import com.kai.mybatis.bean.Employee;
import com.kai.mybatis.dao.EmployeeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class MyBatisTest {

    public SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }


    /**
     * 1.获取sqlSessionFactory对象
     *      解析文件的每一个信息保存在Configuration中，返回包含Configuration的DefaultStatement
     *      注意：【MappedStatement】:代表一个增删改查的信息
     * 2.获取sqlSession对象
     *      返回sqlSession的实现类DefaultSqlSession对象，包含Executor和Configuration;
     *      这一步会创建Executor对象
     * 3.获取接口的代理对象(MapperProxy)
     * 4.执行增删改查方法
     *
     *
     * @throws IOException
     */
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

}
