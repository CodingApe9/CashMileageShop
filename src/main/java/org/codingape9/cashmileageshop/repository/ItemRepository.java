package org.codingape9.cashmileageshop.repository;


import org.apache.ibatis.session.SqlSession;
import org.codingape9.cashmileageshop.dto.ItemDto;
import org.codingape9.cashmileageshop.manager.MyBatisManager;
import org.codingape9.cashmileageshop.mapper.ItemMapper;

public class ItemRepository {

    private final MyBatisManager sqlSessionFactory;

    public ItemRepository(MyBatisManager sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public int insertItem(ItemDto itemDto) {
        int insertRowCount;
        try (SqlSession session = sqlSessionFactory.getSession()) {
            ItemMapper mapper = session.getMapper(ItemMapper.class);
            insertRowCount = mapper.insertItem(itemDto);
        } catch (Exception sqlException) {
            return 0;
        }

        return insertRowCount;
    }
}