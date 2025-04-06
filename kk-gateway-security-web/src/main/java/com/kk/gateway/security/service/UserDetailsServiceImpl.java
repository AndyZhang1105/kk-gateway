package com.kk.gateway.security.service;

import jakarta.annotation.Resource;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 用户管理业务类
 * 这里暂进没用到，如果需要自定义用户的验证查询操作，在SecurityConfig里注释掉UserDetailsService的Bean，启用这个
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Resource
	private PasswordEncoder passwordEncoder;

	//@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//  1、模拟根据username 查询数据库
		if (!"admin".equals(username)) {
			throw new UsernameNotFoundException("用户名或密码错误!");
		}

		// 2、根据查询到的对象 比对 密码
		String password = passwordEncoder.encode("admin");

		//  commaSeparatedStringToAuthorityList方法: 将字符串分割，转化为权限列表，默认是用 逗号 作为分隔符
		return new User("admin", password, AuthorityUtils.commaSeparatedStringToAuthorityList("ADMIN"));
	}

}