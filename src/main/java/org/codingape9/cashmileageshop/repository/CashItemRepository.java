package org.codingape9.cashmileageshop.repository;

import org.apache.ibatis.session.SqlSession;
import org.codingape9.cashmileageshop.dto.ShopItemDto;
import org.codingape9.cashmileageshop.manager.MyBatisManager;
import org.codingape9.cashmileageshop.mapper.CashItemMapper;

import java.util.List;

public class CashItemRepository {
    private final MyBatisManager sqlSessionFactory;

    public CashItemRepository(MyBatisManager sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public int insertCashItem(ShopItemDto cashShopItem) {
        int insertCashItemCount;

        try (SqlSession session = sqlSessionFactory.getSession()) {
            CashItemMapper mapper = session.getMapper(CashItemMapper.class);
            insertCashItemCount = mapper.insertCashItem(cashShopItem);
        } catch (Exception sqlException) {
            return 0;
        }

        return insertCashItemCount;
    }

    public int updateCashItemState(String cashShopName, int slotNumber, int state) {
        int updateCashItemCount;

        try (SqlSession session = sqlSessionFactory.getSession()) {
            CashItemMapper mapper = session.getMapper(CashItemMapper.class);
            updateCashItemCount = mapper.updateCashItemState(cashShopName, slotNumber, state);
        } catch (Exception sqlException) {
            return 0;
        }

        return updateCashItemCount;
    }

    public List<ShopItemDto> selectCashItemList(String cashShopName) {
        List<ShopItemDto> cashItemList;

        try (SqlSession session = sqlSessionFactory.getSession()) {
            CashItemMapper mapper = session.getMapper(CashItemMapper.class);
            cashItemList = mapper.selectCashItemList(cashShopName);
        } catch (Exception sqlException) {
            return null;
        }

        return cashItemList;
    }
}
