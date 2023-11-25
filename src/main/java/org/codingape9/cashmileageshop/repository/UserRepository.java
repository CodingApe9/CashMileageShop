package org.codingape9.cashmileageshop.repository;


import org.apache.ibatis.session.SqlSession;
import org.codingape9.cashmileageshop.dto.UserDto;
import org.codingape9.cashmileageshop.manager.MyBatisManager;
import org.codingape9.cashmileageshop.mapper.UserMapper;

import java.util.UUID;

public class UserRepository {

    private final MyBatisManager sqlSessionFactory;

    public UserRepository(MyBatisManager sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public UserDto selectUser(UUID uuid) {
        UserDto userDto;
        try (SqlSession session = sqlSessionFactory.getSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            userDto = mapper.selectUser(uuid.toString());
        }

        return userDto;
    }

    public int insertUser(UserDto userDto) {
        int insertRowCount;
        try (SqlSession session = sqlSessionFactory.getSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            insertRowCount = mapper.insertUser(userDto);
        }

        return insertRowCount;
    }
}
