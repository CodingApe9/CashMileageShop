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

    @Select("SELECT * FROM mileage_item WHERE mileage_shop_id = (SELECT id FROM mileage_shop WHERE name = #{mileageShopName})")
    List<ShopItemDto> selectMileageShopItemList(@Param("mileageShopName") String mileageShopName);

    @Insert("INSERT INTO mileage_shop (name, line_num) VALUES (#{mileageShopName}, #{lineNum})")
    int insertMileageShop(@Param("shopName") String mileageShopName, @Param("lineNum") int lineNum);

    @Update("UPDATE mileage_shop SET state = #{state} WHERE name = #{mileageShopName}")
    int updateMileageShopState(@Param("mileageShopName") String mileageShopName, @Param("state") int state);

    @Insert("INSERT INTO mileage_item (max_buyable_cnt, price, item_id, mileage_shop_id, max_buyable_cnt_server, slot_num, state) " +
            "VALUES (#{maxBuyableCnt}, #{price}, #{itemId}, #{shopId}, #{maxBuyableCntServer}, #{slotNum}, #{state})")
    int insertMileageShopItem(@Param("maxBuyableCnt") int maxBuyableCnt,
                              @Param("price") int price,
                              @Param("itemId") int itemId,
                              @Param("shopId") int shopId,
                              @Param("maxBuyableCntServer") int maxBuyableCntServer,
                              @Param("slotNum") int slotNum,
                              @Param("state") int state);


    @Update("UPDATE mileage_item SET state = #{state} WHERE id = #{itemId}")
    int updateMileageShopItemState(@Param("itemId") int itemId, @Param("state") int state);
}