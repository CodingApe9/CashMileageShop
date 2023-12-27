package org.codingape9.cashmileageshop.state;

import java.util.List;

public enum ShopState {
    CLOSE_STATE(1, "닫힘"),
    OPEN_STATE(2, "오픈"),
    DELETE_STATE(3, "삭제됨");

    public static final List<ShopState> UNOPEN_SHOP_STATE_LIST = List.of(CLOSE_STATE);
    public static final List<ShopState> OPEN_SHOP_STATE_LIST = List.of(OPEN_STATE);
    public static final List<ShopState> UNDELETED_SHOP_STATE_LIST = List.of(CLOSE_STATE, OPEN_STATE);
    public static final List<ShopState> ALL_STATE_LIST = List.of(CLOSE_STATE, OPEN_STATE, DELETE_STATE);

    private final int stateNumber;
    private final String stateName;

    ShopState(int stateNumber, String stateName) {
        this.stateNumber = stateNumber;
        this.stateName = stateName;
    }

    public static ShopState of(int state) {
        return switch (state) {
            case 1 -> CLOSE_STATE;
            case 2 -> OPEN_STATE;
            case 3 -> DELETE_STATE;
            default -> null;
        };
    }

    public int getStateNumber() {
        return stateNumber;
    }

    public String getStateName() {
        return stateName;
    }
}
