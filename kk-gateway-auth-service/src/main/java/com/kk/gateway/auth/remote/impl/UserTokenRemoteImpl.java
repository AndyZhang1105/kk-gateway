package com.kk.gateway.auth.remote.impl;

import com.kk.arch.remote.dto.UserDto;
import com.kk.arch.remote.exception.BusinessException;
import com.kk.gateway.auth.remote.UserTokenRemote;
import com.kk.gateway.common.JwtResponse;
import com.kk.gateway.common.util.JwtUtils;
import org.apache.dubbo.config.annotation.DubboService;

import static com.kk.gateway.common.JwtResponse.JWT_RESPONSE_CODE_SUCCESS;

/**
 * @author Zal
 */
@DubboService
public class UserTokenRemoteImpl implements UserTokenRemote {

	@Override
	public UserDto getUserByToken(String token) {
		final JwtResponse jwtResponse = JwtUtils.validateToken(token);
		if(jwtResponse.getCode() != JWT_RESPONSE_CODE_SUCCESS) {
			throw new BusinessException(jwtResponse.getCode(), jwtResponse.getMsg());
		}

		return UserDto.builder().tenantId(9901L).userId(1001001L).userName("tst001").build();
	}

}
