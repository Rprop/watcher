package com.jd.jmi.escort.service.impl;

import com.jd.common.util.PaginatedList;
import com.jd.common.util.base.PaginatedArrayList;
import com.jd.jmi.escort.common.constant.JmiOrderTypeEnum;
import com.jd.jmi.escort.common.enums.BlackUserLevelEnum;
import com.jd.jmi.escort.dao.BlackUserDao;
import com.jd.jmi.escort.dao.RiskEventDao;
import com.jd.jmi.escort.model.BlackUserQuery;
import com.jd.jmi.escort.model.Result;
import com.jd.jmi.escort.model.enums.SourceEnum;
import com.jd.jmi.escort.pojo.BlackUser;
import com.jd.jmi.escort.service.SyncRCSService;
import com.jd.jmi.escort.service.UserService;
import com.jd.jmi.escort.util.DateUtils;
import com.jd.user.sdk.export.UserInfoExportService;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.util.List;

/**
 * @author baozhaonasita
 * @version 1.0
 * @date 2016/2/24
 */
public class BlackUserServiceImplTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlackUserServiceImplTest.class);

    @Tested
    private BlackUserServiceImpl blackUserService;
    @Injectable
    private BlackUserDao blackUserDao;
    @Injectable
    private RiskEventDao riskEventDao;
    @Injectable
    private SyncRCSService syncRCSService;
    @Injectable
    private DataSourceTransactionManager transactionManager;
    @Injectable
    private UserService userService;
    @Injectable
    private UserInfoExportService userInfoExportService;

    /**
     * 根据userPin和订单类型查询
     *
     * @throws Exception
     */
    @Test
    public void testSelectByNameOrderType() throws Exception {
        final BlackUser blackUser = new BlackUser();
        blackUser.setUserPin("test");
        blackUser.setOrderType(35);
        new Expectations() {{
            blackUserDao.selectByNameOrderType((BlackUserQuery) any);
            result = blackUser;
        }};
        BlackUser rtBlackUser = blackUserService.selectByNameOrderType(blackUser.getUserPin(), blackUser.getOrderType());
        Assert.assertEquals("test", rtBlackUser.getUserPin());
    }

    /**
     * 根据条件查询
     *
     * @throws Exception
     */
    @Test
    public void testList() throws Exception {
        BlackUser user = new BlackUser();
        user.setOrderType(35);
        user.setUserPin("test");
        user.setLevel(2);
        user.setModified(DateUtils.getDateTime("2016-03-25 10:53:36"));
        final PaginatedList<BlackUser> list = new PaginatedArrayList<BlackUser>();
        list.add(user);
        new Expectations() {{
            blackUserDao.list((BlackUserQuery) any);
            result = list;
        }};

        BlackUserQuery blackUserQuery = new BlackUserQuery();
        blackUserQuery.setOrderType(user.getOrderType());
        blackUserQuery.setLevel(user.getLevel());
        blackUserQuery.setUserPin(user.getUserPin());
        blackUserQuery.setStartTime(user.getModified());
        blackUserQuery.setEndTime(user.getModified());

        PaginatedList<BlackUser> rtList = blackUserService.list(blackUserQuery);
        Assert.assertEquals(user.getUserPin(), rtList.get(0).getUserPin());
    }

    /**
     * 新增
     *
     * @throws Exception
     */
    @Test
    public void testInsert() throws Exception {
        final BlackUser blackUser = new BlackUser();

        blackUser.setUserPin("test123");
        blackUser.setOrderType(35);
        blackUser.setLevel(1);
        blackUser.setModifyUser("");
        blackUser.setSource(1);

        final int r = 1;
        new Expectations() {{
            blackUserDao.insert((BlackUser) any);
            result = r;
        }};

        Result result = blackUserService.insert(blackUser);
        Assert.assertTrue(result.isSuccess());
    }

    /**
     * 批量新增
     *
     * @throws Exception
     */
    @Test
    public void testInsertBatch() throws Exception {
        String userPin = "test456";
        Integer[] orderTypes = {87, 44, 46, 34, 93};
        SourceEnum sourceEnum = SourceEnum.BACKGROUND;
        String operateUser = "";
        final int r = 1;
        new Expectations() {{
            blackUserDao.insertBatch((List<BlackUser>) any);
            result = r;
        }};
        Result result = blackUserService.insertBatch(userPin, orderTypes, sourceEnum, operateUser);
        Assert.assertTrue(result.isSuccess());
    }

    /**
     * 修改
     *
     * @throws Exception
     */
    @Test
    public void testUpdate() throws Exception {
        BlackUser blackUser = new BlackUser();
        blackUser.setUserPin("test");
        blackUser.setOrderType(35);
        blackUser.setTriggerCount(1L);
        final int r = 1;
        new Expectations() {{
            blackUserDao.update((BlackUser) any);
            result = r;
        }};
        Result result = blackUserService.update(blackUser);
        Assert.assertTrue(result.isSuccess());
    }

    /**
     * 合并
     *
     * @throws Exception
     */
    @Test
    public void testMerge() throws Exception {
        final BlackUser user = new BlackUser();
        user.setUserPin("baota");
        user.setOrderType(JmiOrderTypeEnum.LOTTERY.getOrderType());
        user.setLevel(BlackUserLevelEnum.RISKUSER.getCode());

        final int r = 1;
        new Expectations() {{
            blackUserDao.selectByNameOrderType((BlackUserQuery) any);
            result = null;
            blackUserDao.insert((BlackUser) any);
            result = r;
        }};
        Result result = blackUserService.merge(user.getUserPin(), user.getOrderType(), user.getLevel());
        Assert.assertTrue(result.isSuccess());
    }

    /**
     * 合并
     *
     * @throws Exception
     */
    @Test
    public void testMergeToBlackUser() throws Exception {
        final BlackUser user = new BlackUser();
        user.setUserPin("baota");
        user.setOrderType(JmiOrderTypeEnum.AIR_TICKET.getOrderType());
        user.setLevel(BlackUserLevelEnum.BLACKUSER.getCode());

        final int r = 1;
        new Expectations() {{
            blackUserDao.selectByNameOrderType((BlackUserQuery) any);
            result = user;
            blackUserDao.updateTriggerCountById((BlackUser) any);
            result = r;
        }};
        Result result = blackUserService.merge(user.getUserPin(), user.getOrderType(), user.getLevel());
        Assert.assertTrue(result.isSuccess());

    }
}
