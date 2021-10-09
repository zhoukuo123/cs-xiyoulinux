package com.xiyoulinux.mapper;

import com.xiyoulinux.entity.CsUserActivity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * @Entity com.xiyoulinux.dynamic.entity.CsUserDynamic
 * @author qkm
 */
@Repository
public interface CsUserActivityMapper extends BaseMapper<CsUserActivity> {
    List<CsUserActivity> selectOneWeek();
    List<CsUserActivity> selectByUserId(@Param(("userId"))String userId);
}




