package org.codingape9.cashmileageshop.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.codingape9.cashmileageshop.dto.ItemDto;

import java.util.List;

@Mapper
public interface ItemMapper {
    @Insert("INSERT INTO item (‪item_info, name) VALUES (#{itemInfo}, #{name})")
    int insertItem(ItemDto itemDto);

    @Select("SELECT * FROM item")
    List<ItemDto> selectItem();

    @Select("SELECT * FROM item WHERE name = #{itemName}")
    ItemDto selectItemByName(String itemName);
}
