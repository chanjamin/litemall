package org.linlinjava.litemall.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 简单缓存的数据
 */
@ConfigurationProperties(prefix = "cachemanager")
@Component
public class CacheManager {

    Log log = LogFactory.getLog(this.getClass());

    //是否启用缓存

    private boolean enable = true;
    //@过期时间：xxx秒

    private int expiretime = 600;

    public static final String CATALOG = "catalog";

    public static final String INDEX = "index";

//    private static ConcurrentHashMap<String, Map<String, Object>> cacheDataList = new ConcurrentHashMap<>();

    @Autowired
    private Jedis jedis;


    /**
     * 缓存首页数据
     *
     * @param data
     */
    public void saveData(String cacheKey, Map<String, Object> data) {

        Map<String, String> cacheData = jedis.hgetAll(cacheKey);
//        有记录，则先丢弃
        if (cacheData != null) {
            cacheData.remove(cacheKey);
        }

        cacheData = new HashMap<>();
        //Map<String, Object> --> Map<String, String>
        Set<String> dataKeySet = data.keySet();
        for (String str : dataKeySet) {
            //转成json
            String value = JSON.toJSONString(data.get(str));
            cacheData.put(str, value);
        }
        //设置过期时间
        jedis.expire(cacheKey, expiretime);
        //存入jedis
        jedis.hmset(cacheKey, cacheData);
    }

    public Map<String, Object> getHomeCacheData(String cacheKey) {
        Map<String, String> cacheData = jedis.hgetAll(cacheKey);
        HashMap<String, Object> resultMap = new HashMap<>();
        for (String key:cacheData.keySet()
             ) {
            resultMap.put(key, JSONArray.parse(cacheData.get(key)));
        }
        return resultMap;
    }

    /**
     * 判断缓存中是否有数据
     *
     * @return
     */
    public boolean hasData(String cacheKey) {
        if (!enable)
            return false;
        Map<String, String> cacheData = jedis.hgetAll(cacheKey);
        if (cacheData == null || cacheData.size() == 0)
            return false;
        return true;
    }


    /**
     * 清除缓存数据
     */
    public void delCache(String cacheKey) {
        jedis.del(cacheKey);
    }

}