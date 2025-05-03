package com.kk.gateway.auth.remote.impl;

import com.kk.arch.remote.dto.UserDto;
import com.kk.arch.remote.exception.BusinessException;
import com.kk.gateway.auth.remote.UserQueryRemote;
import com.kk.gateway.auth.remote.UserTokenRemote;
import com.kk.gateway.auth.remote.dto.JwtRequestDto;
import com.kk.gateway.common.JwtRequest;
import com.kk.gateway.common.JwtResponse;
import com.kk.gateway.common.util.JwtUtils;
import jakarta.annotation.Resource;
import org.apache.dubbo.common.utils.JsonUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.http.HttpStatus;

import static com.kk.gateway.common.JwtResponse.JWT_RESPONSE_CODE_SUCCESS;

/**
 * @author Zal
 */
@DubboService
public class UserTokenRemoteImpl implements UserTokenRemote {

	@Resource
	private UserQueryRemote userQueryRemote;

	@Override
	public UserDto getUserByToken(String token) {
		final JwtResponse jwtResponse = JwtUtils.validateToken(token);
		if(jwtResponse.getCode() != JWT_RESPONSE_CODE_SUCCESS) {
			throw new BusinessException(jwtResponse.getCode(), jwtResponse.getMsg());
		}

		return UserDto.builder().tenantId(9901L).userId(1001001L).userName("tst001").build();
	}

	@Override
	public String createToken(JwtRequestDto requestDto) {
		try {
			final UserDto userDto =  userQueryRemote.queryByNameAndPwd(requestDto.getUsername(), requestDto.getPassword());
            return JwtUtils.generateToken(JsonUtils.toJson(userDto));
		} catch (Exception e) {
			throw new BusinessException(HttpStatus.UNAUTHORIZED.value(), "Invalid credentials");
		}
	}

}
