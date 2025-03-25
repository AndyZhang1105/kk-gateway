package com.kk.gateway.auth.remote;

import com.kk.arch.remote.dto.UserDto;

public interface UserQueryRemote {

    /**
     * 通过账号和密码查询用户信息
     */
    UserDto queryByNameAndPwd(String loginName, String pwd);

}
