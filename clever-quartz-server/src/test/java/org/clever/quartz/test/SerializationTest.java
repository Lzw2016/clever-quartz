package org.clever.quartz.test;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.mapper.JacksonMapper;
import org.clever.quartz.model.HttpJobConfig;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-24 17:57 <br/>
 */
@Slf4j
public class SerializationTest {

// 使用Json存储JobData
// deserialization
// serialization
//
// getJobDataFromBlob
// serializeJobData
// org.quartz.impl.jdbcjobstore.StdJDBCDelegate

    @SuppressWarnings("unchecked")
    @Test
    public void test() {
        HttpJobConfig httpJobConfig = new HttpJobConfig();
        httpJobConfig.setUrl("/path");
        httpJobConfig.setMethod("GET");
        httpJobConfig.setJsonBody("{}");
        Map<String, Object> map = new HashMap<>();
        map.put("httpJobData", httpJobConfig);
        map.put("int", 1);
        map.put("String", "abc");
        String json = JacksonMapper.nonEmptyMapper().toJson(map);
        log.info("### {}", json);
        map = JacksonMapper.nonEmptyMapper().fromJson(json, Map.class);
        log.info("### {}", map.get("httpJobConfig").getClass().getName());
    }
}
