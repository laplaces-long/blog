package com.blog.Realm;

import com.blog.model.User;
import com.blog.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

public class ShiroRealm extends AuthorizingRealm {
	@Autowired
	private UserService userService;

    //身份认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
		String username = (String) authenticationToken.getPrincipal(); // 获取用户登录账号
		User userInfo = userService.findByName(username); // 后期修改成从redis缓存读取
        if(null == userInfo){
            return null;
        }
		Object principal = username;
		Object credentials = userInfo.getPassword();
		String realmName = getName();
		ByteSource credentialsSalt = ByteSource.Util.bytes(userInfo.getSalt());
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(principal, credentials, credentialsSalt, realmName);
		return info;
    }


	//授权, 后期数据库导入权限表再修改
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		return null;
	}

}