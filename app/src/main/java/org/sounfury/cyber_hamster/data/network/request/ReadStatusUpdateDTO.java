package org.sounfury.cyber_hamster.data.network.request;

import org.jetbrains.annotations.NotNull;
import org.sounfury.cyber_hamster.data.enums.ReadStatusEnum;

public class ReadStatusUpdateDTO {
    
    /**
     * 用户图书ID
     */
    private Long id;

    public Long getId() {
        return id;
    }

    public ReadStatusUpdateDTO(Long id, Integer targetStatus) {
        this.id = id;
        this.targetStatus = targetStatus;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTargetStatus() {
        return targetStatus;
    }

    public void setTargetStatus(Integer targetStatus) {
        this.targetStatus = targetStatus;
    }

    /**
     * 目标阅读状态
     */

    private Integer targetStatus;

}
