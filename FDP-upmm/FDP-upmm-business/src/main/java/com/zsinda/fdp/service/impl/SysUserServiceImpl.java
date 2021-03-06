package com.zsinda.fdp.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsinda.fdp.dto.UserInfo;
import com.zsinda.fdp.entity.SysRole;
import com.zsinda.fdp.entity.SysUser;
import com.zsinda.fdp.mapper.SysUserMapper;
import com.zsinda.fdp.service.SysMenuService;
import com.zsinda.fdp.service.SysRoleService;
import com.zsinda.fdp.service.SysUserService;
import com.zsinda.fdp.vo.MenuVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService{

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public UserInfo findUserInfo(SysUser sysUser) {
        UserInfo userInfo = new UserInfo();
        userInfo.setSysUser(sysUser);
        //查询该用户的角色
        List<SysRole> sysRoles = sysRoleService.findRoleById(sysUser.getUserId());
        List<Integer> roleIds = sysRoles.stream().map(SysRole::getRoleId)
                .collect(Collectors.toList());

        userInfo.setRoles(ArrayUtil.toArray(roleIds, Integer.class));

        //设置权限列表（menu.permission）
        Set<String> permissions = new HashSet<>();

        roleIds.forEach(roleId -> {
            List<String> permissionList = sysMenuService.findMenuByRoleId(roleId)
                    .stream()
                    .filter(menu -> StringUtils.isNotEmpty(menu.getPermission()))
                    .map(MenuVO::getPermission)
                    .collect(Collectors.toList());
            permissions.addAll(permissionList);
        });
        userInfo.setPermissions(ArrayUtil.toArray(permissions, String.class));
        return userInfo;
    }


}
