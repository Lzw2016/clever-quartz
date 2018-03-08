package org.clever.quartz.mapper;

import org.apache.ibatis.annotations.Param;
import org.clever.quartz.entity.QrtzTriggers;

/**
 * 作者：lizw <br/>
 * 创建时间：2017/6/1 11:56 <br/>
 */
public interface QrtzTriggersMapper extends tk.mybatis.mapper.common.Mapper<QrtzTriggers> {
    /**
     * 查询触发器数据
     *
     * @param schedName    Scheduler名称
     * @param triggerName  Trigger key
     * @param triggerGroup Trigger group名称
     * @return 不存在返回null
     */
    QrtzTriggers getQrtzTriggers(@Param("schedName") String schedName, @Param("triggerGroup") String triggerGroup, @Param("triggerName") String triggerName);
}
