package com.kk.gateway.auth.remote;

import com.kk.arch.remote.dto.*;
import com.kk.gateway.auth.remote.dto.JwtRequestDto;

/**
 * @author Zal
 */
public interface UserTokenRemote {

    /**
     * get user info by token
     */
    UserDto getUserByToken(String token);

    /**
     * create token
     * @param requestDto
     * @return
     */
    String createToken(JwtRequestDto requestDto);
}
