package org.codingape9.cashmileageshop.repository;

import java.util.Collections;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.codingape9.cashmileageshop.dto.ShopItemDto;
import org.codingape9.cashmileageshop.manager.MyBatisManager;
import org.codingape9.cashmileageshop.mapper.MileageItemMapper;

public class MileageItemRepository implements ShopItemRepository {
    private final MyBatisManager sqlSessionFactory;

    public MileageItemRepository(MyBatisManager sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public int insertShopItem(ShopItemDto shopItem) {
        int insertMileageItemCount;

        try (SqlSession session = sqlSessionFactory.getSession()) {
            MileageItemMapper mapper = session.getMapper(MileageItemMapper.class);
            insertMileageItemCount = mapper.insertMileageShopItem(shopItem);
        } catch (Exception sqlException) {
            return 0;
        }

        return insertMileageItemCount;
    }

    @Override
    public int updateShopItemState(String mileageShopName, int slotNumber, int state) {
        int updateMileageItemCount;

        try (SqlSession session = sqlSessionFactory.getSession()) {
            MileageItemMapper mapper = session.getMapper(MileageItemMapper.class);
            updateMileageItemCount = mapper.updateMileageShopItemState(mileageShopName, slotNumber, state);
        } catch (Exception sqlException) {
            return 0;
        }

        return updateMileageItemCount;
    }

    @Override
    public List<ShopItemDto> selectShopItemList(String mileageShopName) {
        List<ShopItemDto> mileageItemList;

        try (SqlSession session = sqlSessionFactory.getSession()) {
            MileageItemMapper mapper = session.getMapper(MileageItemMapper.class);
            mileageItemList = mapper.selectMileageShopItemList(mileageShopName);
        } catch (Exception sqlException) {
            return Collections.emptyList();
        }

        return mileageItemList;
    }
}
