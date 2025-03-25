package com.kk.gateway.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zal
 */
@RestController
public class HomeController {

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(HttpServletRequest request, HttpServletResponse response) {
		return "/home is here.";
	}

	@RequestMapping(value = "/admin/info", method = RequestMethod.GET)
	public String adminInfo(HttpServletRequest request, HttpServletResponse response) {
		return "/admin/info is here.";
	}

}
