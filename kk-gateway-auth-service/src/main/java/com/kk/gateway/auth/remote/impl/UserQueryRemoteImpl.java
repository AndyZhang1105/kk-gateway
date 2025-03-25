package com.kk.gateway.auth.remote.impl;

import com.kk.arch.remote.dto.UserDto;
import com.kk.gateway.auth.remote.UserQueryRemote;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class UserQueryRemoteImpl implements UserQueryRemote {

    @Override
    public UserDto queryByNameAndPwd(String loginName, String pwd) {
        return UserDto.builder().userId(1001L).userName(loginName).tenantId(9901L).build();
    }

}
