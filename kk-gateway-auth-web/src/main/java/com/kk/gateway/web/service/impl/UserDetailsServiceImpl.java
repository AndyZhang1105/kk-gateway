package com.kk.gateway.web.service.impl;

import com.kk.arch.remote.dto.UserDto;
import com.kk.arch.remote.exception.BusinessException;
import com.kk.gateway.auth.remote.UserQueryRemote;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 用户管理业务类
 * @author Zal
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Resource
	private PasswordEncoder passwordEncoder;

	@DubboReference
	private UserQueryRemote userQueryRemote;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 1. 查询数据库是否存在此用户
		final String password = passwordEncoder.encode("admin");
		final UserDto userDto = userQueryRemote.queryByNameAndPwd(username, password);
		if(userDto == null) {
			throw new BusinessException(-1, "用户不存在!");
		}

		//  commaSeparatedStringToAuthorityList方法: 将字符串分割，转化为权限列表，默认是用 逗号 作为分隔符
		// final User user = new User("admin", password, AuthorityUtils.commaSeparatedStringToAuthorityList("ADMIN"));

		return User.builder().username(username).password(password).roles("ADMIN").authorities("ADMIN").build();
	}

}