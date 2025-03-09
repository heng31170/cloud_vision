package com.zaizi.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

import java.util.HashMap;

@Getter
public enum SpaceTypeEnum {
    PRIVATE("私有空间", 0),
    TEAM("团队空间", 1);

    private final String text;
    private final int value;

    SpaceTypeEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    private static final HashMap<Integer, SpaceTypeEnum> valueToEnumMap = new HashMap<>();

    static {
        for(SpaceTypeEnum spaceTypeEnum: SpaceTypeEnum.values()) {
            valueToEnumMap.put(spaceTypeEnum.getValue(), spaceTypeEnum);
        }
    }

    // 根据value 获取枚举
    public static SpaceTypeEnum getEnumByValue(Integer value) {
        if(ObjUtil.isEmpty(value)) {
            return null;
        }
        return valueToEnumMap.get(value);
    }

}
