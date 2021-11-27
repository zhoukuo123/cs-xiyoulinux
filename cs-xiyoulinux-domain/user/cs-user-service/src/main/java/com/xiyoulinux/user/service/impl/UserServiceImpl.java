package com.xiyoulinux.user.service.impl;

import com.xiyoulinux.enums.Sex;
import com.xiyoulinux.user.mapper.CsUserMapper;
import com.xiyoulinux.user.pojo.CsUser;
import com.xiyoulinux.user.pojo.bo.UserBO;
import com.xiyoulinux.user.service.UserService;
import com.xiyoulinux.utils.MD5Utils;
import org.apache.dubbo.config.annotation.DubboService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

/**
 * @author CoderZk
 */
@DubboService
public class UserServiceImpl implements UserService {

    @Autowired
    private CsUserMapper csUserMapper;

    @Autowired
    private Sid sid;

    private static final String DEFAULT_USER_FACE = "https://cn.gravatar.com/avatar/a4a51e4c01cf84726aa7a9cf3df50d9f?d=mm&s=150";


    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public boolean queryPhoneIsExist(String phone) {
        Example userExample = new Example(CsUser.class);
        Example.Criteria userCriteria = userExample.createCriteria();

        userCriteria.andEqualTo("phone", phone);

        return csUserMapper.selectOneByExample(userExample) != null;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public CsUser createUser(UserBO userBO) {
        String userId = sid.nextShort();

        CsUser user = new CsUser();
        user.setUid(userId);
        user.setName(userBO.getUsername());
        try {
            user.setPassword(MD5Utils.getMD5Str(userBO.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 默认头像
        user.setFace(DEFAULT_USER_FACE);
        user.setSex(Sex.SECRET.type);
        user.setPhone(userBO.getPhone());

        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

//        csUserMapper.insert(user);
        csUserMapper.insertSelective(user);
        return user;
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public CsUser queryUserForLogin(String phone, String password) {
        Example userExample = new Example(CsUser.class);
        Example.Criteria userCriteria = userExample.createCriteria();

        userCriteria.andEqualTo("phone", phone);
        userCriteria.andEqualTo("password", password);

        CsUser result = csUserMapper.selectOneByExample(userExample);
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public Integer retrievePassword(UserBO userBO) {
        Example userExample = new Example(CsUser.class);
        Example.Criteria userCriteria = userExample.createCriteria();

        userCriteria.andEqualTo("phone", userBO.getPhone());

        CsUser user = new CsUser();
        try {
            user.setPassword(MD5Utils.getMD5Str(userBO.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        user.setUpdatedTime(new Date());

        int result = csUserMapper.updateByExampleSelective(user, userExample);
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public CsUser queryUserByUid(String uid) {

        return csUserMapper.selectByPrimaryKey(uid);
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public String queryUserNameByUid(String uid) {
        CsUser csUser = csUserMapper.selectByPrimaryKey(uid);
        return csUser.getName();
    }
}
