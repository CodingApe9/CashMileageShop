package org.codingape9.cashmileageshop.repository;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.codingape9.cashmileageshop.dto.ShopDto;
import org.codingape9.cashmileageshop.dto.ShopItemDto;
import org.codingape9.cashmileageshop.manager.MyBatisManager;
import org.codingape9.cashmileageshop.mapper.MileageShopMapper;

public class MileageShopRepository implements ShopRepository {
    private final MyBatisManager sqlSessionFactory;

    public MileageShopRepository(MyBatisManager sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public List<ShopDto> selectShopList(List<Integer> stateList) {
        List<ShopDto> shopDtoList;

        try (SqlSession session = sqlSessionFactory.getSession()) {
            MileageShopMapper mapper = session.getMapper(MileageShopMapper.class);
            shopDtoList = mapper.selectMileageShopList(stateList);
        } catch (Exception sqlException) {
            return null;
        }

        return shopDtoList;
    }

    @Override
    public List<ShopItemDto> selectShopItemList(String shopName) {
        List<ShopItemDto> shopItemDtoList;

        try (SqlSession session = sqlSessionFactory.getSession()) {
            MileageShopMapper mapper = session.getMapper(MileageShopMapper.class);
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
            MileageShopMapper mapper = session.getMapper(MileageShopMapper.class);
            shopDto = mapper.selectMileageShopByName(shopName);
        } catch (Exception sqlException) {
            return null;
        }

        return shopDto;
    }

    @Override
    public int insertShop(String shopName, int lineNum) {
        int insertMileageShopCount;

        try (SqlSession session = sqlSessionFactory.getSession()) {
            MileageShopMapper mapper = session.getMapper(MileageShopMapper.class);
            insertMileageShopCount = mapper.insertMileageShop(shopName, lineNum);
        } catch (Exception sqlException) {
            return 0;
        }

        return insertMileageShopCount;
    }

    @Override
    public int updateShopState(String shopName, int state) {
        int updateMileageShopCount;

        try (SqlSession session = sqlSessionFactory.getSession()) {
            MileageShopMapper mapper = session.getMapper(MileageShopMapper.class);
            updateMileageShopCount = mapper.updateMileageShopState(shopName, state);
        } catch (Exception sqlException) {
            return 0;
        }

        return updateMileageShopCount;
    }
}
