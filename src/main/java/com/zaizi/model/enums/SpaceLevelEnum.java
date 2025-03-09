package com.zaizi.model.enums;


import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum SpaceLevelEnum {
    COMMON("普通版", 0, 100, 100L * 1024 * 1024),
    PROFESSIONAL("专业版", 1, 1000, 1000L * 1024 * 1024),
    FLAGSHIP("旗舰版", 2, 10000, 10000L * 1024 * 1024);

    private final String text;

    private final int value;

    private final long maxCount;

    private final long maxSize;

    /**
     *
     * @param text text文本
     * @param value value值
     * @param maxCount 图片总大小 最大值
     * @param maxSize 图片数量 最大值
     */
    SpaceLevelEnum(String text, int value, long maxCount, long maxSize) {
        this.text = text;
        this.value = value;
        this.maxCount = maxCount;
        this.maxSize = maxSize;
    }

    private static final Map<Integer, SpaceLevelEnum> valueToEnumMap = new HashMap<>();

    static {
        for(SpaceLevelEnum spaceLevelEnum: SpaceLevelEnum.values()) {
            valueToEnumMap.put(spaceLevelEnum.getValue(), spaceLevelEnum);
        }
    }


    // 根据value 获取枚举
    public static SpaceLevelEnum getEnumByValue(Integer value) {
        if(ObjUtil.isEmpty(value)) {
            return null;
        }
        return valueToEnumMap.get(value);
    }

}
