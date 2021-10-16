package com.xiyoulinux.join.pojo.factory;

import com.xiyoulinux.join.pojo.vo.InterviewStatusVO;

/**
 * @author CoderZk
 */
public class Wait3Interview implements InterviewStatus {
    @Override
    public String getInterviewStatus() {
        return "2面已通过, 等待3面";
    }
}
