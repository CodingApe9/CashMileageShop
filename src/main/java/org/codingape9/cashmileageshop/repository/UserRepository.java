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
        } catch (Exception sqlException) {
            return null;
        }

        return userDto;
    }

    public int insertUser(UserDto userDto) {
        int insertRowCount;
        try (SqlSession session = sqlSessionFactory.getSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            insertRowCount = mapper.insertUser(userDto);
        } catch (Exception sqlException) {
            return 0;
        }

        return insertRowCount;
    }

    public int updateUserCash(UUID uuid, int cash) {
        int updateRowCount;
        try (SqlSession session = sqlSessionFactory.getSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            updateRowCount = mapper.updateUserCash(uuid.toString(), cash);
        }

        return updateRowCount;
    }

    public int updateUserAddCash(UUID uuid, int cash) {
        int updateRowCount;
        try (SqlSession session = sqlSessionFactory.getSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            updateRowCount = mapper.updateUserAddCash(uuid.toString(), cash);
        } catch (Exception sqlException) {
            return 0;
        }

        return updateRowCount;
    }

    public int updateUserSubCash(UUID uuid, int cash) {
        int updateRowCount;
        try (SqlSession session = sqlSessionFactory.getSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            updateRowCount = mapper.updateUserSubCash(uuid.toString(), cash);
        } catch (Exception sqlException) {
            return 0;
        }

        return updateRowCount;
    }

    public int updateUserMileage(UUID uuid, int mileage) {
        int updateRowCount;
        try (SqlSession session = sqlSessionFactory.getSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            updateRowCount = mapper.updateUserMileage(uuid.toString(), mileage);
        } catch (Exception sqlException) {
            return 0;
        }

        return updateRowCount;
    }

    public int updateUserAddMileage(UUID uuid, int mileage) {
        int updateRowCount;
        try (SqlSession session = sqlSessionFactory.getSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            updateRowCount = mapper.updateUserAddMileage(uuid.toString(), mileage);
        } catch (Exception sqlException) {
            return 0;
        }

        return updateRowCount;
    }

    public int updateUserSubMileage(UUID uuid, int mileage) {
        int updateRowCount;
        try (SqlSession session = sqlSessionFactory.getSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            updateRowCount = mapper.updateUserSubMileage(uuid.toString(), mileage);
        } catch (Exception sqlException) {
            return 0;
        }

        return updateRowCount;
    }
}
