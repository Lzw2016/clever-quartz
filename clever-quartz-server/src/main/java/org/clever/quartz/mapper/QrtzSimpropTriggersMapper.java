package org.clever.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.clever.quartz.entity.QrtzSimpropTriggers;
import org.springframework.stereotype.Repository;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-09 18:43 <br/>
 */
@Repository
@Mapper
public interface QrtzSimpropTriggersMapper extends BaseMapper<QrtzSimpropTriggers> {
}
