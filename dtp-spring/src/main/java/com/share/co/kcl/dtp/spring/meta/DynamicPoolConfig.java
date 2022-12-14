package com.share.co.kcl.dtp.spring.meta;

import lombok.Data;

@Data
public class DynamicPoolConfig {

    /**
     * the server code
     */
    private String serverCode;

    /**
     * the server secret
     */
    private String serverSecret;

    /**
     * the server monitor domain
     */
    private String serverDomain;
}
