package com.kk.gateway.api.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

	private List<String> skipPaths;

	public List<String> getSkipPaths() {
		return skipPaths;
	}

	public void setSkipPaths(List<String> skipPaths) {
		this.skipPaths = skipPaths;
	}

}