package org.codingape9.cashmileageshop.repository;

import org.apache.ibatis.session.SqlSession;
import org.codingape9.cashmileageshop.dto.ShopItemDto;
import org.codingape9.cashmileageshop.manager.MyBatisManager;
import org.codingape9.cashmileageshop.mapper.CashItemMapper;

import java.util.Collections;
import java.util.List;

public class CashItemRepository implements ShopItemRepository {
    private final MyBatisManager sqlSessionFactory;

    public CashItemRepository(MyBatisManager sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public int insertShopItem(ShopItemDto shopItem) {
        int insertCashItemCount;

        try (SqlSession session = sqlSessionFactory.getSession()) {
            CashItemMapper mapper = session.getMapper(CashItemMapper.class);
            insertCashItemCount = mapper.insertCashItem(shopItem);
        } catch (Exception sqlException) {
            return 0;
        }

        return insertCashItemCount;
    }

    @Override
    public int updateShopItemState(String cashShopName, int slotNumber, int state) {
        int updateCashItemCount;

        try (SqlSession session = sqlSessionFactory.getSession()) {
            CashItemMapper mapper = session.getMapper(CashItemMapper.class);
            updateCashItemCount = mapper.updateCashItemState(cashShopName, slotNumber, state);
        } catch (Exception sqlException) {
            return 0;
        }

        return updateCashItemCount;
    }

    @Override
    public List<ShopItemDto> selectShopItemList(String cashShopName) {
        List<ShopItemDto> cashItemList;

        try (SqlSession session = sqlSessionFactory.getSession()) {
            CashItemMapper mapper = session.getMapper(CashItemMapper.class);
            cashItemList = mapper.selectCashItemList(cashShopName);
        } catch (Exception sqlException) {
            return Collections.emptyList();
        }

        return cashItemList;
    }
}
