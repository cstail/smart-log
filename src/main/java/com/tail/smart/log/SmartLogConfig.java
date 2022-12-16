package com.tail.smart.log;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
public class SmartLogConfig {
    @Value("${trace.header}")
    protected String header;
    @Value("${trace.prefixPackage}")
    protected String prefixPackage;
    @Value("${trace.excludePackage}")
    protected String excludePackage;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getPrefixPackage() {
        return prefixPackage;
    }

    public void setPrefixPackage(String prefixPackage) {
        this.prefixPackage = prefixPackage;
    }

    public String getExcludePackage() {
        return excludePackage;
    }

    public void setExcludePackage(String excludePackage) {
        this.excludePackage = excludePackage;
    }

    @Override
    public String toString() {
        return "SmartLogConfig{" +
                "header='" + header + '\'' +
                ", prefixPackage='" + prefixPackage + '\'' +
                ", excludePackage='" + excludePackage + '\'' +
                '}';
    }
}
