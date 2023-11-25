package org.codingape9.cashmileageshop.mapper;

import org.apache.ibatis.annotations.*;
import org.codingape9.cashmileageshop.dto.UserDto;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM user WHERE uuid = #{uuid}")
    UserDto selectUser(String uuid);

    @Insert("INSERT INTO user (uuid, cash, mileage) VALUES (#{uuid}, #{cash}, #{mileage})")
    int insertUser(UserDto userDto);

    @Update("UPDATE user SET cash = #{cash} WHERE uuid = #{uuid}")
    int updateUserCash(@Param("uuid") String uuid, @Param("cash") int cash);

    @Update("UPDATE user SET cash = cash + #{cash} WHERE uuid = #{uuid}")
    int updateUserAddCash(@Param("uuid") String uuid, @Param("cash") int cash);

    @Update("UPDATE user SET cash = cash - #{cash} WHERE uuid = #{uuid}")
    int updateUserSubCash(@Param("uuid") String uuid, @Param("cash") int cash);

    @Update("UPDATE user SET mileage = #{mileage} WHERE uuid = #{uuid}")
    int updateUserMileage(@Param("uuid") String uuid, @Param("mileage") int mileage);

    @Update("UPDATE user SET mileage = mileage + #{mileage} WHERE uuid = #{uuid}")
    int updateUserAddMileage(@Param("uuid") String uuid, @Param("mileage") int mileage);

    @Update("UPDATE user SET mileage = mileage - #{mileage} WHERE uuid = #{uuid}")
    int updateUserSubMileage(@Param("uuid") String uuid, @Param("mileage") int mileage);
}
