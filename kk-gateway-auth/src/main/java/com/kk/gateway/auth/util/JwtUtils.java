package com.kk.gateway.auth.util;

import com.kk.gateway.auth.dto.CustomJWSKeySelector;
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

public class JwtUtils {
	private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

	public static final String SECRET_KEY = "this_is_your_secret_key_for_sign"; // 密钥
	public static final long JWT_EXPIRATION_IN_MS = 20L * 1000; // 令牌过期时间，单位毫秒

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

	// 验证 JWT 令牌
	public static boolean validateToken(String token, String subject) {
		try {
			SignedJWT signedJWT = SignedJWT.parse(token);
			JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());

			// 验证签名
			if (!signedJWT.verify(verifier)) {
				return false;
			}

			// 验证载荷
			JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
			if (subject != null && !subject.equals(claimsSet.getSubject())) {
				return false;
			}

			// 检查过期时间
			Date expirationTime = claimsSet.getExpirationTime();
			if (expirationTime != null && expirationTime.before(new Date())) {
				return false;
			}

			return true;
		} catch (ParseException | JOSEException e) {
			log.error(e.getMessage());
			return false;
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
