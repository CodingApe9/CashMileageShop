package org.codingape9.cashmileageshop.manager;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.codingape9.cashmileageshop.mapper.UserMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class MyBatisManager {
    private final SqlSessionFactory sessionFactory;

    public MyBatisManager() {
        String userDir = System.getProperty("user.dir");
        String relativePath = "plugins" + File.separator + "CashMileageShop" + File.separator + "mybatis-config.xml";
        String fullPath = userDir + File.separator + relativePath;

        FileReader fileReader;
        try {
            fileReader = new FileReader(fullPath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        sessionFactory = new SqlSessionFactoryBuilder().build(fileReader);
        sessionFactory.getConfiguration().addMapper(UserMapper.class);
    }

    public SqlSession getSession() {
        return sessionFactory.openSession(true);
    }
}
