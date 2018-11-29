package org.clever.quartz.config;

import lombok.extern.slf4j.Slf4j;
import org.clever.quartz.utils.QuartzManager;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-29 11:36 <br/>
 */
@Component
@Slf4j
public class AppShutdown implements ApplicationListener<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.info("#### 开始关闭调度器......");
        QuartzManager.shutdown();
        log.info("#### 关闭调度器完成");
    }
}
