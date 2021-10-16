package com.xiyoulinux.join.pojo.factory;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.InterningXmlVisitor;
import com.xiyoulinux.join.pojo.vo.InterviewStatusVO;

import java.util.HashMap;
import java.util.Map;

/**
 * @author CoderZk
 */
public class InterviewStatusFactory {
    private static Map<InterviewStatusVO, InterviewStatus> interviewStatusMap = new HashMap<>();

    static {
        InterviewStatusVO wait1Interview = new InterviewStatusVO(1, 0);
        InterviewStatusVO wait2Interview = new InterviewStatusVO(2, 0);
        InterviewStatusVO oneInterviewOut = new InterviewStatusVO(1, -1);
        InterviewStatusVO wait3Interview = new InterviewStatusVO(3, 0);
        InterviewStatusVO twoInterviewOut = new InterviewStatusVO(2, -1);
        InterviewStatusVO wait4Interview = new InterviewStatusVO(3, 1);
        InterviewStatusVO threeInterviewOut = new InterviewStatusVO(3, -1);

        interviewStatusMap.put(wait1Interview, new Wait1Interview());
        interviewStatusMap.put(wait2Interview, new Wait2Interview());
        interviewStatusMap.put(oneInterviewOut, new OneInterviewOut());
        interviewStatusMap.put(wait3Interview, new Wait3Interview());
        interviewStatusMap.put(twoInterviewOut, new TwoInterviewOut());
        interviewStatusMap.put(wait4Interview, new Wait4Interview());
        interviewStatusMap.put(threeInterviewOut, new ThreeInterviewOut());
    }

    public static  InterviewStatus getInterviewStatus(InterviewStatusVO interviewStatusVO) {
        return interviewStatusMap.get(interviewStatusVO);
    }


}
