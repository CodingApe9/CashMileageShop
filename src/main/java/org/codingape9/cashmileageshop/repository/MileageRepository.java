package org.codingape9.cashmileageshop.repository;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.codingape9.cashmileageshop.dto.ShopDto;
import org.codingape9.cashmileageshop.dto.ShopItemDto;
import org.codingape9.cashmileageshop.manager.MyBatisManager;
import org.codingape9.cashmileageshop.mapper.MileageShopMapper;

public class MileageRepository {
    private final MyBatisManager sqlSessionFactory;

    public MileageRepository(MyBatisManager sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public List<ShopDto> selectMileageShopList(List<Integer> stateList) {
        List<ShopDto> shopDtoList;

        try (SqlSession session = sqlSessionFactory.getSession()) {
            MileageShopMapper mapper = session.getMapper(MileageShopMapper.class);
            shopDtoList = mapper.selectMileageShopList(stateList);
        } catch (Exception sqlException) {
            return null;
        }

        return shopDtoList;
    }

    public List<ShopItemDto> selectMileageItemShop(String mileageShopName) {
        List<ShopItemDto> shopItemDtoList;

        try (SqlSession session = sqlSessionFactory.getSession()) {
            MileageShopMapper mapper = session.getMapper(MileageShopMapper.class);
            shopItemDtoList = mapper.selectCashShopItemList(mileageShopName);
        } catch (Exception sqlException) {
            return null;
        }

        return shopItemDtoList;
    }

    public ShopDto selectMileageShop(String mileageShopName) {
        ShopDto shopDto;

        try (SqlSession session = sqlSessionFactory.getSession()) {
            MileageShopMapper mapper = session.getMapper(MileageShopMapper.class);
            shopDto = mapper.selectMileageShopByName(mileageShopName);
        } catch (Exception sqlException) {
            return null;
        }

        return shopDto;
    }

    public int insertMileageShop(String mileageShopName, int lineNum) {
        int insertMileageShopCount;

        try (SqlSession session = sqlSessionFactory.getSession()) {
            MileageShopMapper mapper = session.getMapper(MileageShopMapper.class);
            insertMileageShopCount = mapper.insertMileageShop(mileageShopName, lineNum);
        } catch (Exception sqlException) {
            return 0;
        }

        return insertMileageShopCount;
    }

    public int updateMileageShopState(String mileageShopName, int state) {
        int updateMileageShopCount;

        try (SqlSession session = sqlSessionFactory.getSession()) {
            MileageShopMapper mapper = session.getMapper(MileageShopMapper.class);
            updateMileageShopCount = mapper.updateMileageShopState(mileageShopName, state);
        } catch (Exception sqlException) {
            return 0;
        }

        return updateMileageShopCount;
    }

}
