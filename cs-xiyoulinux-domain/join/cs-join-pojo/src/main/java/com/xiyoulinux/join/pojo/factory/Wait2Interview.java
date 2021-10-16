package com.xiyoulinux.join.pojo.factory;

/**
 * @author CoderZk
 */
public class Wait2Interview implements InterviewStatus {
    @Override
    public String getInterviewStatus() {
        return "1面已通过, 等待2面";
    }
}
