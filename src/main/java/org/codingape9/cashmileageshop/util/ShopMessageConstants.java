package org.codingape9.cashmileageshop.util;

public enum ShopMessageConstants {
    WRONG_COMMAND("명령어를 잘못 입력했습니다."),
    EXISTING_SHOP("이미 존재하는 상점입니다. 상점 이름: "),
    UNDELETEABLE_SHOP("존재하지 않는 상점입니다. 상점 이름: "),
    UNDEFINED_SHOP("상점을 찾을 수 없습니다. 상점 이름: "),
    UNEXISTING_ITEM("존재하지 않는 아이템입니다. 아이템 이름: "),
    SUCCESS_INSERT_SHOP("상점을 생성했습니다. 상점 이름: "),
    SUCCESS_OPEN_SHOP("상점을 오픈했습니다. 상점 이름: "),
    SUCCESS_DELETE_SHOP("상점을 삭제했습니다. 상점 이름: "),
    SUCCESS_DISPLAY_ITEM("아이템을 진열했습니다. [상점 이름: %s 아이템 이름: %s]"),
    SUCCESS_DELETE_ITEM_IN_SHOP("아이템을 삭제했습니다. [상점 이름: %s 아이템 이름: %s]"),
    SUCCESS_CLOSE_SHOP("상점을 닫았습니다. 상점 이름: "),
    FAIL_INSERT_SHOP("상점을 생성하는데 실패했습니다. 상점 이름: %s"),
    FAIL_OPEN_SHOP("상점을 오픈하는데 실패했습니다. 상점 이름: %s"),
    FAIL_DELETE_SHOP("상점을 삭제하는데 실패했습니다. 상점 이름: %s"),
    FAIL_DISPLAY_ITEM("아이템을 진열하는데 실패했습니다. [상점 이름: %s 아이템 이름: %s]"),
    UNEXISTING_ITEM_IN_SHOP("존재하지 않는 아이템입니다. [상점 이름: %s 아이템 이름: %s]"),
    FAIL_DELETE_ITEM_IN_SHOP("아이템 삭제에 실패했습니다. [상점 이름: %s 아이템 이름: %s]"),
    FAIL_CLOSE_SHOP("상점을 닫는데 실패했습니다. 상점 이름: %s");

    private final String message;

    ShopMessageConstants(String message) {
        this.message = message;
    }

    public String formatted(Object... args) {
        return message.formatted(args);
    }

    @Override
    public String toString() {
        return message;
    }
}
