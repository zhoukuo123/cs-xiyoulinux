package com.xiyoulinux.join.pojo.vo;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.Objects;

/**
 * @author CoderZk
 */
@ApiModel(value = "用户面试状态VO", description = "用户面试状态VO")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InterviewStatusVO {
    private Integer round;
    private Integer status;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InterviewStatusVO that = (InterviewStatusVO) o;
        return Objects.equals(round, that.round) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(round, status);
    }
}
