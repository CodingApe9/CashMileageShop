package org.codingape9.cashmileageshop.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.codingape9.cashmileageshop.dto.UserDto;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM user WHERE uuid = #{uuid}")
    UserDto selectUser(String uuid);

    @Insert("INSERT INTO user (uuid, cash, mileage) VALUES (#{uuid}, #{cash}, #{mileage})")
    int insertUser(UserDto userDto);

//    @Update("UPDATE minecraft.obt_userdata SET received_date_time=#{receivedDateTime} WHERE uuid = #{uuid}")
//    int update(ObtUserdataDTO userData);
//
//    @Update("UPDATE minecraft.obt_userdata SET received_date_time = #{receivedDateTime} WHERE uuid = #{uuid}")
//    int updateReceivedDateTime(@Param("uuid") String uuid, @Param("receivedDateTime") LocalDateTime receivedDateTime);
}
