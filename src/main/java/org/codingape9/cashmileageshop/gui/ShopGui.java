package org.codingape9.cashmileageshop.gui;

import java.util.ArrayList;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.codingape9.cashmileageshop.dto.ItemInfoDto;
import org.codingape9.cashmileageshop.util.ItemUtil;

import java.util.List;
import java.util.UUID;

public abstract class ShopGui {
    public static String PRICE;
    public static String SERVER_LIMITED;
    public static String USER_LIMITED;

    public static final List<String> PRICE_VARIABLE = List.of("%price%");
    public static final List<String> SERVER_LIMITED_VARIABLE = List.of("%server_remain%", "%server_purchases_limited%");
    public static final List<String> USER_LIMITED_VARIABLE = List.of("%user_remain%", "%user_purchases_limited%");
    private static final List<Component> SHOP_ITEM_INFO_HEADER = List.of(
            Component.text(""),
            Component.text("§7§m                                                §7"),
            Component.text("")
    );
    private static final List<Component> SHOP_ITEM_INFO_FOOTER = List.of(
            Component.text(""),
            Component.text("§7§m                                                §7")
    );
    private static final int NO_BUYABLE_LIMTED = -1;
    private static final String NO_INFO = "";

    private String name;
    private Inventory inventory;

    public ShopGui(String name, Inventory inventory) {
        this.name = name;
        this.inventory = inventory;
    }

    public void setShopItems(UUID uuid) {
        getShopItemInfoList().forEach(itemInfoDto -> setShopItem(uuid, itemInfoDto));
    }

    private void setShopItem(UUID uuid, ItemInfoDto itemInfoDto) {
        ItemStack shopItemStack = createItemStack(uuid, itemInfoDto);
        inventory.setItem(itemInfoDto.slotNum(), shopItemStack);
    }

    private ItemStack createItemStack(UUID uuid, ItemInfoDto itemInfoDto) {
        ItemStack itemStack = ItemUtil.deserialize(itemInfoDto.itemInfo());
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<Component> itemLore = createItemLore(uuid, itemInfoDto);
        itemMeta.lore(itemLore);
        return itemStack;
    }

    private List<Component> createItemLore(UUID uuid, ItemInfoDto itemInfoDto) {
        String priceInfo = getPriceInfo(itemInfoDto);
        String buyableInfo = getBuyableStateInfo(itemInfoDto, uuid);
        String serverBuyableInfo = getServerBuyableStateInfo(itemInfoDto, uuid);

        List<Component> itemLore = new ArrayList<>(SHOP_ITEM_INFO_HEADER);
        itemLore.add(Component.text(priceInfo));
        itemLore.add(Component.text(buyableInfo));
        itemLore.add(Component.text(serverBuyableInfo));
        itemLore.addAll(SHOP_ITEM_INFO_FOOTER);
        return itemLore;
    }

    private String getPriceInfo(ItemInfoDto itemInfoDto) {
        return PRICE.replace(PRICE_VARIABLE.get(0), String.valueOf(itemInfoDto.price()));
    }

    private String getBuyableStateInfo(ItemInfoDto itemInfoDto, UUID uuid) {
        if (itemInfoDto.maxBuyableCnt() == NO_BUYABLE_LIMTED) {
            return NO_INFO;
        }
        int buyCnt = getBuyCnt(uuid, itemInfoDto);
        return USER_LIMITED
                .replace(USER_LIMITED_VARIABLE.get(0), String.valueOf(itemInfoDto.maxBuyableCnt() - buyCnt))
                .replace(USER_LIMITED_VARIABLE.get(1), String.valueOf(itemInfoDto.maxBuyableCnt()));
    }

    private String getServerBuyableStateInfo(ItemInfoDto itemInfoDto, UUID uuid) {
        if (itemInfoDto.maxBuyableCntServer() == NO_BUYABLE_LIMTED) {
            return NO_INFO;
        }
        int buyCnt = getServerBuyCnt(uuid, itemInfoDto);
        return SERVER_LIMITED
                .replace(SERVER_LIMITED_VARIABLE.get(0), String.valueOf(itemInfoDto.maxBuyableCntServer() - buyCnt))
                .replace(SERVER_LIMITED_VARIABLE.get(1), String.valueOf(itemInfoDto.maxBuyableCntServer()));
    }

    abstract List<ItemInfoDto> getShopItemInfoList();

    abstract int getBuyCnt(UUID uuid, ItemInfoDto itemInfoDto);

    abstract int getServerBuyCnt(UUID uuid, ItemInfoDto itemInfoDto);
}
