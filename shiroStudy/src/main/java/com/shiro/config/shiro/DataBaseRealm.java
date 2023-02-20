package com.shiro.config.shiro;


import com.shiro.entity.User;
import com.shiro.exception.BusinessException;
import com.shiro.service.PermissionService;
import com.shiro.service.RoleService;
import com.shiro.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Set;

@Configuration
public class DataBaseRealm  extends AuthorizingRealm {
    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;
    @Resource
    private PermissionService permissionService;


    /**
     * 登录认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 获取账号密码
        UsernamePasswordToken t = (UsernamePasswordToken) authenticationToken;
        String userName = authenticationToken.getPrincipal().toString();
        // 获取数据库中的密码
        User user = userService.lambdaQuery().eq(User::getUserName, userName).one();
        if (user==null){
            throw new BusinessException("用户不存在");
        }
        String passwordInDB = user.getPassword();
        String salt = user.getSalt();
        // 认证信息里存放账号密码, getName() 是当前Realm的继承方法,通常返回当前类名 :databaseRealm
        // 盐也放进去
        // 这样通过ShiroConfig里配置的 HashedCredentialsMatcher 进行自动校验
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userName, passwordInDB, ByteSource.Util.bytes(salt), getName());
        return authenticationInfo;
    }





    /**
     * 权限验证
     * 只有用到org.apache.shiro.web.filter.authz包里默认的过滤器才会走到这里。本项目里，实际不会走到这里。
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 能进入到这里，表示账号已经通过认证了
        String username = (String) principalCollection.getPrimaryPrincipal();
        // 通过service获取角色和权限
        Set<String> permissions = permissionService.getPermissionsByUserName(username);
        Set<String> roles = roleService.getRolesByUserName(username);
        // 授权对象
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        // 把通过service获取到的角色和权限放进去
        simpleAuthorizationInfo.setStringPermissions(permissions);
        simpleAuthorizationInfo.setRoles(roles);
        return simpleAuthorizationInfo;
    }


}
