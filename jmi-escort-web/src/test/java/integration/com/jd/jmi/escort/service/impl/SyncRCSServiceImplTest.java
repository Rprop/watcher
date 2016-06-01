package integration.com.jd.jmi.escort.service.impl;

import com.jd.jmi.escort.service.SyncRCSService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author baozhaonasita
 * @version 1.0
 * @date 2016/3/29
 */
@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration({"/spring-config.xml"})
public class SyncRCSServiceImplTest {

    @Resource
    private SyncRCSService syncRCSService;

    @Test
    public void testSyncAddBlackUser() throws Exception {
        String userPin = "test";
        int orderType = 35;//国内机票
        syncRCSService.syncAddBlackUser(userPin, orderType);
    }

    @Test
    public void testSyncAddBlackUserList() throws Exception {
        String userPin = "test";
        int[] orderTypes = {34, 35, 36, 37, 39, 43, 44, 45, 46, 47, 53, 62, 74, 83, 87, 93, 98};
        syncRCSService.syncAddBlackUser(userPin, orderTypes);
    }

    @Test
    public void testSyncDeleteBlackUser() throws Exception {
        String userPin = "test";
        int orderType = 35;//国内机票
        syncRCSService.syncDeleteBlackUser(userPin, orderType);
    }

    @Test
    public void testSyncDeleteBlackUserList() throws Exception {
        String userPin = "test";
        int[] orderTypes = {34, 35, 36, 37, 39, 43, 44, 45, 46, 47, 53, 62, 74, 83, 87, 93, 98};
        syncRCSService.syncDeleteBlackUser(userPin, orderTypes);
    }

    @Test
    public void testSyncAddWhiteUser() throws Exception {
        String userPin = "testWhite";
        int orderType = 35;//国内机票
        syncRCSService.syncAddWhiteUser(userPin, orderType);
    }

    @Test
    public void testSyncAddWhiteUserList() throws Exception {
        String userPin = "testWhite";
        int[] orderTypes = {34, 35, 36, 37, 39, 43, 44, 45, 46, 47, 53, 62, 74, 83, 87, 93, 98};
        syncRCSService.syncAddWhiteUser(userPin, orderTypes);
    }

    @Test
    public void testSyncDeleteWhiteUser() throws Exception {
        String userPin = "testWhite";
        int orderType = 35;//国内机票
        syncRCSService.syncDeleteWhiteUser(userPin, orderType);
    }

    @Test
    public void testSyncDeleteWhiteUserList() throws Exception {
        String userPin = "testWhite";
        int[] orderTypes = {34, 35, 36, 37, 39, 43, 44, 45, 46, 47, 53, 62, 74, 83, 87, 93, 98};
        syncRCSService.syncDeleteWhiteUser(userPin, orderTypes);
    }
}
