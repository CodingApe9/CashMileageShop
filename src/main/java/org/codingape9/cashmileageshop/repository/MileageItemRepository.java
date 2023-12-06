package org.codingape9.cashmileageshop.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.codingape9.cashmileageshop.dto.ShopItemDto;
import org.codingape9.cashmileageshop.manager.MyBatisManager;
import org.codingape9.cashmileageshop.mapper.MileageItemMapper;

public class MileageItemRepository {
    private final MyBatisManager sqlSessionFactory;

    public MileageItemRepository(MyBatisManager sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public int insertMileageItem(ShopItemDto mileageShopItem) {
        int insertMileageItemCount;

        try (SqlSession session = sqlSessionFactory.getSession()) {
            MileageItemMapper mapper = session.getMapper(MileageItemMapper.class);
            insertMileageItemCount = mapper.insertMileageShopItem(mileageShopItem);
        } catch (Exception sqlException) {
            return 0;
        }

        return insertMileageItemCount;
    }

    public int updateMileageItemState(String mileageShopName, int slotNumber, int state) {
        int updateMileageItemCount;

        try (SqlSession session = sqlSessionFactory.getSession()) {
            MileageItemMapper mapper = session.getMapper(MileageItemMapper.class);
            updateMileageItemCount = mapper.updateMileageShopItemState(mileageShopName, slotNumber, state);
        } catch (Exception sqlException) {
            return 0;
        }

        return updateMileageItemCount;
    }

    public List<ShopItemDto> selectMileageItemList(String mileageShopName) {
        List<ShopItemDto> mileageItemList;

        try (SqlSession session = sqlSessionFactory.getSession()) {
            MileageItemMapper mapper = session.getMapper(MileageItemMapper.class);
            mileageItemList = mapper.selectMileageShopItemList(mileageShopName);
        } catch (Exception sqlException) {
            return null;
        }

        return mileageItemList;
    }
}
