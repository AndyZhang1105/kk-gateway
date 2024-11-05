package com.kk.gateway.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

	public static int JWT_RESPONSE_CODE_FAIL = -1;
	public static int JWT_RESPONSE_CODE_SUCCESS = 0;

	private int code;
	private String msg;
	private String token;

}
