package integration.com.jd.jmi.escort.service.impl;

import com.jd.common.util.PaginatedList;
import com.jd.jmi.escort.model.BlackUserQuery;
import com.jd.jmi.escort.model.Result;
import com.jd.jmi.escort.model.enums.SourceEnum;
import com.jd.jmi.escort.pojo.WhiteUser;
import com.jd.jmi.escort.service.WhiteUserService;
import org.junit.Assert;
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
 * @date 2016/3/30
 */
@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration({"/spring-config.xml"}) //加载配置文件
public class WhiteUserServiceImplTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(WhiteUserServiceImplTest.class);

    @Resource
    private WhiteUserService whiteUserService;

    @Test
    public void testInsert() throws Exception {
        WhiteUser user = new WhiteUser();
        user.setUserPin("test123");
        user.setOrderType(35);
        user.setSource(SourceEnum.BACKGROUND.getCode());
        user.setModifyUser("test");

        Result result = whiteUserService.insert(user);
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void testInsertBatch() throws Exception {
        String userPin = "test123";
        Integer[] orderTypes = {87, 44, 46, 34, 93};
        SourceEnum sourceEnum = SourceEnum.BACKGROUND;
        String operateUser = "test";

        Result result = whiteUserService.insertBatch(userPin, orderTypes, sourceEnum, operateUser);
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void testSelectByNameOrderType() throws Exception {
        String userPin = "test123";
        int orderType = 35;
        WhiteUser user = whiteUserService.selectByNameOrderType(userPin, orderType);
        Assert.assertEquals(userPin, user.getUserPin());
    }

    @Test
    public void testList() throws Exception {
        BlackUserQuery blackUserQuery = new BlackUserQuery();
        blackUserQuery.setOrderType(35);
        blackUserQuery.setUserPin("test123");
        blackUserQuery.setStartTimeStr("2016-03-25 15:34:29");
        blackUserQuery.setEndTimeStr("2016-03-25 15:36:29");

        PaginatedList<WhiteUser> list = whiteUserService.list(blackUserQuery);
        Assert.assertEquals(1, list.getTotalItem());
    }

    @Test
    public void testDelete() throws Exception {
        WhiteUser user = whiteUserService.selectByNameOrderType("test123", 35);
        Result result = whiteUserService.delete(user.getId(), "test");
        Assert.assertTrue(result.isSuccess());
    }
}
