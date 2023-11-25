package org.codingape9.cashmileageshop.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.codingape9.cashmileageshop.dto.ItemDto;

@Mapper
public interface ItemMapper {

    @Insert("INSERT INTO item (‪item_info, name) VALUES (#{itemInfo}, #{name})")
    int insertItem(ItemDto itemDto);
}
