package org.codingape9.cashmileageshop.mapper;

import org.apache.ibatis.annotations.*;
import org.codingape9.cashmileageshop.dto.ShopItemDto;

import java.util.List;

@Mapper
public interface CashItemMapper {

    @Insert("INSERT INTO cash_item (item_id, cash_shop_id, price, slot_num, max_buyable_cnt, max_buyable_cnt_server) " +
            "VALUES (#{itemId}, #{shopId}, #{price}, #{slotNum}, #{maxBuyableCnt}, #{maxBuyableCntServer})")
    int insertCashItem(ShopItemDto cashShopItem);

    @Update("UPDATE cash_item SET state = #{state} " +
            "WHERE cash_shop_id = (SELECT id FROM cash_shop WHERE name = #{cashShopName}) AND slot_num = #{slotNumber}")
    int updateCashItemState(@Param("cashShopName") String cashShopName, @Param("slotNumber") int slotNumber, @Param("state") int state);

    @Select("SELECT * FROM cash_item WHERE cash_shop_id = (SELECT id FROM cash_shop WHERE name = #{cashShopName})")
    List<ShopItemDto> selectCashItemList(String cashShopName);
}
