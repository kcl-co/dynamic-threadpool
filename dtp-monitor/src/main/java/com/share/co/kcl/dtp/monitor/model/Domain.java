package com.share.co.kcl.dtp.monitor.model;

import org.springframework.context.ApplicationContext;

public class Domain {

    /**
     * the spring application context
     */
    protected final ApplicationContext applicationContext;

    protected Domain(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
