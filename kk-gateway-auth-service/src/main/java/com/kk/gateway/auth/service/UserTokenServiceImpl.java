package com.kk.gateway.auth.service;

import com.kk.gateway.auth.dto.UserDto;
import com.kk.gateway.auth.remote.UserTokenService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author Zal
 */
@DubboService
public class UserTokenServiceImpl implements UserTokenService {

	@Override
	public UserDto getUserByToken(String token) {
		return UserDto.builder().tenantId(9901L).userId(1001001L).userName("tst001").build();
	}

}
