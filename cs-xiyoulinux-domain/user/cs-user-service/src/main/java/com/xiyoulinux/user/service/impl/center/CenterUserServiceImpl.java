package com.xiyoulinux.user.service.impl.center;

import com.xiyoulinux.user.mapper.CsUserMapper;
import com.xiyoulinux.user.pojo.CsUser;
import com.xiyoulinux.user.pojo.bo.center.CenterUserBO;
import com.xiyoulinux.user.service.center.CenterUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author CoderZk
 */
@RestController
public class CenterUserServiceImpl implements CenterUserService {

    @Resource
    private CsUserMapper usersMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public CsUser queryUserInfo(String userId) {
        CsUser user = usersMapper.selectByPrimaryKey(userId);
        user.setPassword(null);
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public CsUser updateUserInfo(String userId, CenterUserBO centerUserBO) {

        CsUser updateUser = new CsUser();
        BeanUtils.copyProperties(centerUserBO, updateUser);
        updateUser.setUid(userId);
        updateUser.setUpdatedTime(new Date());

        usersMapper.updateByPrimaryKeySelective(updateUser);

        return queryUserInfo(userId);

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public CsUser updateUserFace(String userId, String faceUrl) {

        CsUser updateUser = new CsUser();
        updateUser.setUid(userId);
        updateUser.setFace(faceUrl);
        updateUser.setUpdatedTime(new Date());

        usersMapper.updateByPrimaryKeySelective(updateUser);

        return queryUserInfo(userId);
    }
}
