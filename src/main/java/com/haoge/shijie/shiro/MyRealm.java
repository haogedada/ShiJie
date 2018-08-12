package com.haoge.shijie.shiro;

import com.haoge.shijie.pojo.AuxiliaryUserBean;
import com.haoge.shijie.pojo.UserBean;
import com.haoge.shijie.service.AuxiliaryUserService;
import com.haoge.shijie.service.LoginService;
import com.haoge.shijie.util.JWTUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class MyRealm extends AuthorizingRealm {
    @Autowired
    private LoginService loginService;
    @Autowired
    private AuxiliaryUserService auxiliaryUserService;

    @Autowired
    public void setUserService(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = JWTUtil.getUsername(principals.toString());
        UserBean user = loginService.findUserByName(username);

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        AuxiliaryUserBean auxiliaryUserBean = auxiliaryUserService.findAuxiliaryUserById(user.getUserId());
        simpleAuthorizationInfo.addRole(auxiliaryUserBean.getUserRole());
        Set<String> permission = new HashSet<>(Arrays.asList(auxiliaryUserBean.getUserPermission().split(",")));
        simpleAuthorizationInfo.addStringPermissions(permission);
        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        // 解密获得username，用于和数据库进行对比
        String username = JWTUtil.getUsername(token);
        if (username == null) {
            throw new AuthenticationException("token invalid");
        }
        UserBean userBean = loginService.findUserByName(username);
        if (userBean == null) {
            throw new AuthenticationException("User didn't existed!");
        }
        if (!JWTUtil.verify(token, username, userBean.getUserPassword())) {
            throw new AuthenticationException("Username or password error");
        }
        return new SimpleAuthenticationInfo(token, token, "my_realm");
    }
}
