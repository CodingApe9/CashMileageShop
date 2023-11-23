package org.codingape9.cashmileageshop.repository;


import org.apache.ibatis.session.SqlSession;
import org.codingape9.cashmileageshop.dto.UserDto;
import org.codingape9.cashmileageshop.mapper.UserMapper;
import org.codingape9.cashmileageshop.manager.MyBatisManager;

public class UserRepository {

    private final MyBatisManager sqlSessionFactory;

    public UserRepository(MyBatisManager sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public UserDto getUser(String uuid) {
        UserDto userDto;
        try (SqlSession session = sqlSessionFactory.getSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            userDto = mapper.selectUser(uuid);
        }

        return userDto;
    }

    //    public int save(ObtUserdataDTO dto) {
//        int affectedRow = 0;
//        try (SqlSession session = sqlSessionFactory.getSession()) {
//            ObtUserDataMapper mapper = session.getMapper(ObtUserDataMapper.class);
//            affectedRow = mapper.save(dto);
//        }
//
//        return affectedRow;
//    }
//
//    public int update(ObtUserdataDTO dto) {
//        int affectedRow = 0;
//        try (SqlSession session = sqlSessionFactory.getSession()) {
//            ObtUserDataMapper mapper = session.getMapper(ObtUserDataMapper.class);
//            affectedRow = mapper.update(dto);
//        }
//
//        return affectedRow;
//    }
}
