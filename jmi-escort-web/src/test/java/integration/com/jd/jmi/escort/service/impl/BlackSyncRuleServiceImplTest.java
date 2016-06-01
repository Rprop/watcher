package integration.com.jd.jmi.escort.service.impl;

import com.jd.jim.cli.Cluster;
import com.jd.jmi.cache.JimDBUtils;
import com.jd.jmi.escort.common.constant.CacheConstants;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author baozhaonasita
 * @version 1.0
 * @date 2016/3/28
 */
@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration({"/spring-config.xml"}) //加载配置文件
public class BlackSyncRuleServiceImplTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlackSyncRuleServiceImplTest.class);

    @Resource
    private JimDBUtils jimDBClient;
    @Resource
    private Cluster jimClient;
    /**
     * 删除缓存
     *
     * @throws Exception
     */
    @Test
    public void testDeleteCache() throws Exception {
        int[] orderTypeArr = {93, 34, 62, 35, 83, 36, 53, 43, 37, 87, 74, 47, 44, 39};
        for (int i = 0; i < orderTypeArr.length; i++) {
            String cacheKey = CacheConstants.BLACK_SYNC_RULE_KEY_PREFIX + orderTypeArr[i];
            String value = jimDBClient.get(cacheKey);
            System.out.println("value==" + value);
            if (StringUtils.isNotEmpty(value)) {
                System.out.println("cacheKey:" + cacheKey + "被删除");
                jimClient.del(cacheKey);
            }
        }
    }
}
