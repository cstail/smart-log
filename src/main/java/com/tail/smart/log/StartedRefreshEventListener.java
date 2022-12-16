package com.tail.smart.log;

import ch.qos.logback.classic.spi.TurboFilterList;
import ch.qos.logback.classic.turbo.TurboFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;

@Order(Integer.MAX_VALUE)
public class StartedRefreshEventListener implements ApplicationListener<RefreshScopeRefreshedEvent>, ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(StartedRefreshEventListener.class);
    @Autowired
    protected SmartLogConfig smartLogConfig;


    //配置刷新时会自动移除相关的filter，必须再次添加(不要问我为什么不在logback.xml中添加该组件，我讨厌xml)
    @Override
    public void onApplicationEvent(RefreshScopeRefreshedEvent event) {
        updateFilter();
    }

    //启动完成
    @Override
    public void run(ApplicationArguments args) throws Exception {
        updateFilter();
    }

    protected void updateFilter() {
        if (logger instanceof ch.qos.logback.classic.Logger) {
            logger.info("smart   log config {}", smartLogConfig);//此行万万不能删除
            ch.qos.logback.classic.Logger logBack = (ch.qos.logback.classic.Logger) logger;
            TurboFilterList list = logBack.getLoggerContext().getTurboFilterList();
            if (list.size() == 0) {
                list.add(new SmartLogFilter(smartLogConfig));
            } else {
                for (TurboFilter turboFilter : list) {
                    if (turboFilter instanceof SmartLogFilter) {
                        return;
                    }
                }
                list.add(new SmartLogFilter(smartLogConfig));
            }
        }
    }
}
