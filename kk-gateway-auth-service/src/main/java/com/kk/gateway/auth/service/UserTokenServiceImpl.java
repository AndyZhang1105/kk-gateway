package com.kk.gateway.auth.service;

import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.kk.gateway.auth.dto.UserDto;
import com.kk.gateway.auth.remote.UserTokenService;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.Map;

/**
 * @author Zal
 */
@DubboService
public class UserTokenServiceImpl implements UserTokenService {

	@Override
	public UserDto getUserByToken(String token) {
		return UserDto.builder().tenantId(1L).userId(1L).userName("tst").build();
	}

}
