package org.sounfury.cyber_hamster.data.enums;

import java.util.Objects;

/**
 * 图书阅读状态枚举
 */
public enum ReadStatusEnum {
    
    /**
     * 未读
     */
    UNREAD(0, "未读"),
    
    /**
     * 在读
     */
    READING(1, "在读"),
    
    /**
     * 已读
     */
    READ(2, "已读");
    
    private final Integer code;
    private final String desc;
    
    ReadStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 根据状态码获取枚举
     * 
     * @param code 状态码
     * @return 对应的枚举值，如果不存在则返回null
     */
    public static ReadStatusEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        
        for (ReadStatusEnum status : ReadStatusEnum.values()) {
            if (Objects.equals(status.getCode(), code)) {
                return status;
            }
        }
        
        return null;
    }
}
