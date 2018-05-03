package cn.luischen.service.user.impl;

import cn.luischen.constant.ErrorConstant;
import cn.luischen.dao.UserDao;
import cn.luischen.dto.cond.UserCond;
import cn.luischen.exception.BusinessException;
import cn.luischen.model.UserDomain;
import cn.luischen.service.user.UserService;
import cn.luischen.utils.TaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Donghua.Chen on 2018/4/20.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;//这里会报错，但是并不会影响


    @Transactional
    @Override
    public int updateUserInfo(UserDomain user) {
        if (null == user.getUid())
            throw BusinessException.withErrorCode("用户编号不可能为空");
        return userDao.updateUserInfo(user);
    }

    @Override
    public UserDomain getUserInfoById(Integer uId) {
        return userDao.getUserInfoById(uId);
    }

    @Override
    public UserDomain login(String username, String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password))
            throw BusinessException.withErrorCode(ErrorConstant.Auth.USERNAME_PASSWORD_IS_EMPTY);

        String pwd = TaleUtils.MD5encode(username + password);
        UserDomain user = userDao.getUserInfoByCond(username, pwd);
        if (null == user)
            throw BusinessException.withErrorCode(ErrorConstant.Auth.USERNAME_PASSWORD_ERROR);

        return user;
    }


}
