package org.codingape9.cashmileageshop.repository;

import org.apache.ibatis.session.SqlSession;
import org.codingape9.cashmileageshop.dto.ShopDto;
import org.codingape9.cashmileageshop.dto.ShopItemDto;
import org.codingape9.cashmileageshop.manager.MyBatisManager;
import org.codingape9.cashmileageshop.mapper.CashShopMapper;

import java.util.List;

public class CashShopRepository implements ShopRepository {
    private final MyBatisManager sqlSessionFactory;

    public CashShopRepository(MyBatisManager sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public List<ShopDto> selectShopList(List<Integer> stateList) {
        List<ShopDto> shopDtoList;

        try (SqlSession session = sqlSessionFactory.getSession()) {
            CashShopMapper mapper = session.getMapper(CashShopMapper.class);
            shopDtoList = mapper.selectCashShopList(stateList);
        } catch (Exception sqlException) {
            return null;
        }

        return shopDtoList;
    }

    @Override
    public List<ShopItemDto> selectShopItemList(String shopName) {
        List<ShopItemDto> shopItemDtoList;

        try (SqlSession session = sqlSessionFactory.getSession()) {
            CashShopMapper mapper = session.getMapper(CashShopMapper.class);
            shopItemDtoList = mapper.selectCashShopItemList(shopName);
        } catch (Exception sqlException) {
            return null;
        }

        return shopItemDtoList;
    }

    @Override
    public ShopDto selectShop(String shopName) {
        ShopDto shopDto;

        try (SqlSession session = sqlSessionFactory.getSession()) {
            CashShopMapper mapper = session.getMapper(CashShopMapper.class);
            shopDto = mapper.selectCashShop(shopName);
        } catch (Exception sqlException) {
            return null;
        }

        return shopDto;
    }

    @Override
    public int insertShop(String shopName, int lineNum) {
        int insertCashShopCount;

        try (SqlSession session = sqlSessionFactory.getSession()) {
            CashShopMapper mapper = session.getMapper(CashShopMapper.class);
            insertCashShopCount = mapper.insertCashShop(shopName, lineNum);
        } catch (Exception sqlException) {
            return 0;
        }

        return insertCashShopCount;
    }

    @Override
    public int updateShopState(String shopName, int state) {
        int deleteCashShopCount;

        try (SqlSession session = sqlSessionFactory.getSession()) {
            CashShopMapper mapper = session.getMapper(CashShopMapper.class);
            deleteCashShopCount = mapper.updateCashShopState(shopName, state);
        } catch (Exception sqlException) {
            return 0;
        }

        return deleteCashShopCount;
    }
}
