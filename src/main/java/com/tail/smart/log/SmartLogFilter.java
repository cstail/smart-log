package com.tail.smart.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.slf4j.Marker;

import java.util.HashMap;
import java.util.Map;

import static ch.qos.logback.core.spi.FilterReply.*;


public class SmartLogFilter extends TurboFilter {
    protected Map<String, Level> valueLevelMap = new HashMap<String, Level>();
    protected SmartLogConfig smartLogConfig;

    public SmartLogFilter(SmartLogConfig smartLogConfig) {
        this.smartLogConfig = smartLogConfig;
        valueLevelMap.put("OFF", Level.OFF);
        valueLevelMap.put("ERROR", Level.ERROR);
        valueLevelMap.put("WARN", Level.WARN);
        valueLevelMap.put("INFO", Level.INFO);
        valueLevelMap.put("DEBUG", Level.DEBUG);
        valueLevelMap.put("TRACE", Level.TRACE);
        valueLevelMap.put("ALL", Level.ALL);
    }

    @Override
    public FilterReply decide(Marker marker, Logger logger, Level level, String format, Object[] params, Throwable t) {

        String mdcValue = MDC.get(smartLogConfig.getHeader());
        if (StringUtils.isBlank(mdcValue)) {
            return NEUTRAL;
        }

        //如果不是指定包前缀，忽略当前的日志
        String name = logger.getName();
        if (StringUtils.isBlank(smartLogConfig.getPrefixPackage())) {
            return NEUTRAL;
        }
        if (!name.startsWith(smartLogConfig.getPrefixPackage())) {
            return NEUTRAL;
        }
        //如果当前包已经被排除，忽略
        if (StringUtils.isNotBlank(smartLogConfig.getExcludePackage())) {
            if (name.startsWith(smartLogConfig.getExcludePackage())) {
                return NEUTRAL;
            }
        }
        Level levelAssociatedWithMDCValue = null;
        if (mdcValue != null) {
            levelAssociatedWithMDCValue = valueLevelMap.get(mdcValue);
        }
        //找不到日志范围
        if (levelAssociatedWithMDCValue == null) {
            return NEUTRAL;
        }
        if (level.isGreaterOrEqual(levelAssociatedWithMDCValue)) {
            return ACCEPT;
        } else {
            return NEUTRAL;
        }
    }
}
