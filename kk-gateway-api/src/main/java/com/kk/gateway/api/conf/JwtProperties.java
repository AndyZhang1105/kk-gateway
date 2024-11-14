package com.kk.gateway.api.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Zal
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

	private List<String> skipPaths;

	public void setSkipPaths(List<String> skipPaths) {
		this.skipPaths = skipPaths;
	}

}