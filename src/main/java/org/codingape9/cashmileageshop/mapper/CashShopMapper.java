package org.codingape9.cashmileageshop.mapper;

import org.apache.ibatis.annotations.*;
import org.codingape9.cashmileageshop.dto.ShopDto;
import org.codingape9.cashmileageshop.dto.ShopItemDto;

import java.util.List;

@Mapper
public interface CashShopMapper {
    @Select({
            "<script>",
            "SELECT * FROM cash_shop WHERE state IN",
            "<foreach item='state' collection='stateList' open='(' separator=',' close=')'>",
            "#{state}",
            "</foreach>",
            "</script>"
    })
    List<ShopDto> selectCashShopList(@Param("stateList") List<Integer> stateList);

    @Select("SELECT * FROM cash_shop WHERE name = #{cashShopName}")
    ShopDto selectCashShop(String cashShopName);


    @Select("select cash_item.* " +
            "from cash_item join (select id from cash_shop where name = #{cashShopName}) as cash_shop_id " +
            "on cash_shop_id.id = cash_item.cash_shop_id and state = 1;")
    List<ShopItemDto> selectCashShopItemList(String cashShopName);

    @Insert("INSERT INTO cash_shop (name, line_num) VALUES (#{cashShopName}, #{lineNum})")
    int insertCashShop(@Param("cashShopName") String cashShopName, @Param("lineNum") int lineNum);

    @Update("UPDATE cash_shop SET state = #{state} WHERE name = #{cashShopName}")
    int updateCashShopState(@Param("cashShopName") String cashShopName, @Param("state") int state);
}
