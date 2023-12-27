package org.codingape9.cashmileageshop.repository;

import java.util.Collections;
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
            return Collections.emptyList();
        }

        return shopDtoList;
    }

    @Override
    public List<ShopItemDto> selectShopItemList(String mileageShopName) {
        List<ShopItemDto> shopItemDtoList;

        try (SqlSession session = sqlSessionFactory.getSession()) {
            MileageShopMapper mapper = session.getMapper(MileageShopMapper.class);
            shopItemDtoList = mapper.selectMileageShopItemList(mileageShopName);
        } catch (Exception sqlException) {
            return Collections.emptyList();
        }

        return shopItemDtoList;
    }

    @Override
    public ShopDto selectShop(String mileageShopName) {
        ShopDto shopDto;

        try (SqlSession session = sqlSessionFactory.getSession()) {
            MileageShopMapper mapper = session.getMapper(MileageShopMapper.class);
            shopDto = mapper.selectMileageShopByName(mileageShopName);
        } catch (Exception sqlException) {
            return null;
        }

        return shopDto;
    }

    @Override
    public int insertShop(String mileageShopName, int lineNum) {
        int insertMileageShopCount;

        try (SqlSession session = sqlSessionFactory.getSession()) {
            MileageShopMapper mapper = session.getMapper(MileageShopMapper.class);
            insertMileageShopCount = mapper.insertMileageShop(mileageShopName, lineNum);
        } catch (Exception sqlException) {
            return 0;
        }

        return insertMileageShopCount;
    }

    @Override
    public int updateShopState(String mileageShopName, int state) {
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
