package com.kk.gateway.auth.remote;

import com.kk.arch.remote.dto.*;

/**
 * @author Zal
 */
public interface UserTokenRemote {

    /**
     * get user info by token
     */
    UserDto getUserByToken(String token);

}
