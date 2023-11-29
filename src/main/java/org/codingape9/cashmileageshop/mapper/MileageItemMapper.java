package org.codingape9.cashmileageshop.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.codingape9.cashmileageshop.dto.ShopItemDto;

public interface MileageItemMapper {


    @Insert("INSERT INTO mileage_item (item_id, mileage_shop_id, price, slot_num, max_buyable_cnt, max_buyable_cnt_server) " +
            "VALUES (#{itemId}, #{shopId}, #{price}, #{slotNum}, #{maxBuyableCnt}, #{maxBuyableCntServer})")
    int insertMileageShopItem(ShopItemDto mileageShopItem);

    @Update("UPDATE mileage_item SET state = #{state} " +
            "WHERE mileage_shop_id = (SELECT id FROM mileage_shop WHERE name = #{mileageShopName}) AND slot_num = #{slotNumber}")
    int updateMileageShopItemState(@Param("mileageShopName") String mileageShopName, @Param("slotNumber") int slotNumber , @Param("state") int state);

    @Select("SELECT * FROM mileage_item WHERE mileage_shop_id = (SELECT id FROM mileage_shop WHERE name = #{mileageShopName})")
    List<ShopItemDto> selectMileageShopItemList(String mileageShopName);
}
