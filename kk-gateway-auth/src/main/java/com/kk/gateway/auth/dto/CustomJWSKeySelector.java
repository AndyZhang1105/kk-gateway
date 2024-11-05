package com.kk.gateway.auth.dto;

import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.SecurityContext;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomJWSKeySelector implements JWSKeySelector<SecurityContext> {

	private final Map<String, SecretKey> keyMap;

	public CustomJWSKeySelector() {
		keyMap = new HashMap<>();
		keyMap.put("key1", new SecretKeySpec("secret1".getBytes(), "HMACSHA256"));
		keyMap.put("key2", new SecretKeySpec("secret2".getBytes(), "HMACSHA256"));
	}

	@Override
	public List<? extends Key> selectJWSKeys(JWSHeader header, SecurityContext context) {
		// 根据 JWSHeader 中的 kid 获取相应的密钥
		String kid = header.getKeyID();
		if (keyMap.containsKey(kid)) {
			return Collections.singletonList(keyMap.get(kid));
		} else {
			// 如果没有找到相应的密钥，则返回空列表
			return Collections.emptyList();
		}
	}

}
