package com.kk.gateway.security.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class IndexController {

	@GetMapping("/")
	public String home() {
		return "index";
	}

	@GetMapping("/index")
	public String index(Model model) {
		// 从 SecurityContextHolder 获取当前认证信息
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// 获取用户名
		String currentUserName = authentication.getName();

		// 将用户名添加到模型中
		model.addAttribute("currentUserName", currentUserName);

		return "index";  // 返回 index.html 视图
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		// 执行自定义登出逻辑
		performCustomLogoutLogic();

		// 使用 SecurityContextLogoutHandler 清除认证信息
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}

		// 重定向到登录页面
		return "redirect:/login?logout";
	}

	private void performCustomLogoutLogic() {
		// 在这里添加你的自定义登出逻辑
		// 例如：记录登出时间、清理缓存等
		System.out.println("Executing custom logout logic...");
	}

}
