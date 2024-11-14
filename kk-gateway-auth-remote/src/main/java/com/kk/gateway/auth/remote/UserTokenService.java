package com.kk.gateway.auth.remote;

import com.kk.gateway.auth.dto.UserDto;

import java.util.Map;

/**
 * @author Zal
 */
public interface UserTokenService {

    /**
     * get user info by token
     */
    UserDto getUserByToken(String token);

}
