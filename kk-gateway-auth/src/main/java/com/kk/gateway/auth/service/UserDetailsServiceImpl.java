package com.kk.gateway.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 用户管理业务类
 * Created by macro on 2020/6/19.
 */
public class UserDetailsServiceImpl  {

	@Autowired
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