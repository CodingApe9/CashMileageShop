package org.codingape9.cashmileageshop.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.codingape9.cashmileageshop.dto.ShopDto;
import org.codingape9.cashmileageshop.dto.ShopItemDto;

@Mapper
public interface MileageShopMapper {
    @Select({
            "<script>",
            "SELECT * FROM mileage_shop WHERE state IN",
            "<foreach item='state' collection='stateList' open='(' separator=',' close=')'>",
            "#{state}",
            "</foreach>",
            "</script>"
    })
    List<ShopDto> selectMileageShopList(@Param("stateList") List<Integer> stateList);

    @Select("SELECT * FROM mileage_shop WHERE name = #{mileageShopName}")
    ShopDto selectMileageShopByName(@Param("mileageShopName") String mileageShopName);

    @Select("select mileage_item.* " +
            "from mileage_item join (select id from mileage_shop where name = #{mileageShopName}) as mileage_shop_id " +
            "on mileage_shop_id.id = mileage_item.mileage_shop_id and state = 1;")
    List<ShopItemDto> selectCashShopItemList(String mileageShopName);

    @Insert("INSERT INTO mileage_shop (name, line_num) VALUES (#{mileageShopName}, #{lineNum})")
    int insertMileageShop(@Param("shopName") String mileageShopName, @Param("lineNum") int lineNum);

    @Update("UPDATE mileage_shop SET state = #{state} WHERE name = #{mileageShopName}")
    int updateMileageShopState(@Param("mileageShopName") String mileageShopName, @Param("state") int state);


}