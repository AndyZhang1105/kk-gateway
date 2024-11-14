package com.kk.gateway.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Zal
 */
@Data
@Builder
public class UserDto implements Serializable {

    private Long tenantId;
    private Long userId;
    private String userName;

}
