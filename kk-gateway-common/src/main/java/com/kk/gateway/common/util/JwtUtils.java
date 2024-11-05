package com.kk.gateway.common.util;

import com.kk.gateway.common.CustomJWSKeySelector;
import com.kk.gateway.common.JwtResponse;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import static com.kk.gateway.common.JwtResponse.JWT_RESPONSE_CODE_FAIL;
import static com.kk.gateway.common.JwtResponse.JWT_RESPONSE_CODE_SUCCESS;

public class JwtUtils {
	private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

	public static final String SECRET_KEY = "this_is_your_secret_key_for_sign"; // 密钥
	public static final long JWT_EXPIRATION_IN_MS = 24 * 60 * 60 * 1000; // 令牌过期时间，单位毫秒

	// 生成 JWT 令牌
	public static String generateToken(String subject) {
		try {
			// 创建 JWT 的载荷
			JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
			builder.subject(subject);
			builder.issuer("your_issuer");
			builder.expirationTime(new Date(new Date().getTime() + JWT_EXPIRATION_IN_MS));
			builder.issueTime(new Date());
			JWTClaimsSet claimsSet = builder.build();

			// 创建签名对象
			SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

			// 使用 HMAC SHA-256 算法和密钥对 JWT 进行签名
			JWSSigner signer = new MACSigner(SECRET_KEY.getBytes());
			signedJWT.sign(signer);

			// 返回序列化的 JWT
			return signedJWT.serialize();
		} catch (JOSEException e) {
			log.error(e.getMessage());
			throw new RuntimeException("Error while signing JWT", e);
		}
	}

	public static JwtResponse validateToken(String token) {
		return validateToken(token, null);
	}

	// 验证 JWT 令牌
	public static JwtResponse validateToken(String token, String subject) {
		try {
			SignedJWT signedJWT = SignedJWT.parse(token);
			JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());

			// 验证签名
			if (!signedJWT.verify(verifier)) {
				return new JwtResponse(JWT_RESPONSE_CODE_FAIL, "验证签名失败", "");
			}

			// 验证载荷
			final JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
			final String claimsSubject = Optional.of(claimsSet.getSubject()).orElse("").trim();
			if (subject != null && !subject.trim().equals(claimsSubject)) {
				return new JwtResponse(JWT_RESPONSE_CODE_FAIL, "验证签名载荷失败", "");
			} else if (claimsSubject.isBlank()) {
				return new JwtResponse(JWT_RESPONSE_CODE_FAIL, "验证签名载荷不合法", "");
			}

			// 检查过期时间
			Date expirationTime = claimsSet.getExpirationTime();
			if (expirationTime != null && expirationTime.before(new Date())) {
				return new JwtResponse(JWT_RESPONSE_CODE_FAIL, "验证签名已过期", "");
			}

			return new JwtResponse(JWT_RESPONSE_CODE_SUCCESS, "", token);
		} catch (ParseException | JOSEException e) {
			log.error(e.getMessage());
			return new JwtResponse(JWT_RESPONSE_CODE_FAIL, "验证签名异常", "");
		}
	}

	// 从 JWT 令牌中获取主题（通常是用户名）
	public static String getSubjectFromToken(String token) {
		try {
			SignedJWT signedJWT = SignedJWT.parse(token);
			JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
			return claimsSet.getSubject();
		} catch (ParseException e) {
			log.error(e.getMessage());
			throw new RuntimeException("Error while parsing JWT", e);
		}
	}

	// 创建一个 ConfigurableJWTProcessor 实例
	public static ConfigurableJWTProcessor<SecurityContext> createJwtProcessor() {
		DefaultJWTProcessor<SecurityContext> processor = new DefaultJWTProcessor<>();

		// 创建 SecretKey 对象
		SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "HMACSHA256");

		// 设置 JWSKeySelector 为 CustomJWSKeySelector对象
		processor.setJWSKeySelector(new CustomJWSKeySelector());

		return processor;
	}

}
