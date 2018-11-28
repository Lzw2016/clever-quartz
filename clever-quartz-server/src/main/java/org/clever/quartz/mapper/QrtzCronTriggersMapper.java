package org.clever.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.clever.quartz.entity.QrtzCronTriggers;
import org.springframework.stereotype.Repository;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-09 18:40 <br/>
 */
@Repository
@Mapper
public interface QrtzCronTriggersMapper extends BaseMapper<QrtzCronTriggers> {

}
