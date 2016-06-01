package com.jd.jmi.escort.service.impl;

import com.jd.common.util.PaginatedList;
import com.jd.common.util.base.PaginatedArrayList;
import com.jd.jmi.escort.dao.WhiteUserDao;
import com.jd.jmi.escort.model.BlackUserQuery;
import com.jd.jmi.escort.model.Result;
import com.jd.jmi.escort.model.enums.SourceEnum;
import com.jd.jmi.escort.pojo.WhiteUser;
import com.jd.jmi.escort.service.SyncRCSService;
import com.jd.jmi.escort.util.DateUtils;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author baozhaonasita
 * @version 1.0
 * @date 2016/3/25
 */
public class WhiteUserServiceImplTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(WhiteUserServiceImplTest.class);

    @Tested
    private WhiteUserServiceImpl whiteUserService;
    @Injectable
    private WhiteUserDao whiteUserDao;
    @Injectable
    private SyncRCSService syncRCSService;

    @Test
    public void testInsert() throws Exception {
        final WhiteUser user = new WhiteUser();
        user.setUserPin("test123");
        user.setOrderType(35);
        user.setSource(SourceEnum.BACKGROUND.getCode());
        user.setModifyUser("test");

        final int r = 1;
        new Expectations() {{
            whiteUserDao.insert((WhiteUser) any);
            result = r;
        }};

        Result result = whiteUserService.insert(user);
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void testInsertBatch() throws Exception {
        String userPin = "test123";
        Integer[] orderTypes = {87, 44, 46, 34, 93};
        SourceEnum sourceEnum = SourceEnum.BACKGROUND;
        String operateUser = "test";

        final int r = 1;
        new Expectations() {{
            whiteUserDao.insertBatch((List<WhiteUser>) any);
            result = r;
        }};

        Result result = whiteUserService.insertBatch(userPin, orderTypes, sourceEnum, operateUser);
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void testSelectByNameOrderType() throws Exception {
        final WhiteUser whiteUser = new WhiteUser();
        whiteUser.setUserPin("test123");
        whiteUser.setOrderType(35);

        new Expectations() {{
            whiteUserDao.selectByNameOrderType((WhiteUser) any);
            result = whiteUser;
        }};

        WhiteUser user = whiteUserService.selectByNameOrderType(whiteUser.getUserPin(), whiteUser.getOrderType());
        Assert.assertEquals(whiteUser.getUserPin(), user.getUserPin());
    }

    @Test
    public void testList() throws Exception {
        WhiteUser user = new WhiteUser();
        user.setOrderType(35);
        user.setUserPin("test123");
        user.setModified(DateUtils.getDateTime("2016-03-25 15:35:29"));
        final PaginatedList<WhiteUser> list = new PaginatedArrayList<WhiteUser>();
        list.add(user);
        list.setTotalItem(1);
        new Expectations() {{
            whiteUserDao.list((BlackUserQuery) any);
            result = list;
        }};

        BlackUserQuery whiteUserQuery = new BlackUserQuery();
        whiteUserQuery.setOrderType(35);
        whiteUserQuery.setUserPin("test123");
        whiteUserQuery.setStartTimeStr("2016-03-25 15:34:29");
        whiteUserQuery.setEndTimeStr("2016-03-25 15:36:29");

        PaginatedList<WhiteUser> resultList = whiteUserService.list(whiteUserQuery);
        Assert.assertEquals(1, resultList.getTotalItem());
    }

    @Test
    public void testDelete() throws Exception {
        final WhiteUser user = new WhiteUser();
        user.setId(123L);
        user.setUserPin("test123");
        user.setOrderType(35);

        new Expectations() {{
            whiteUserDao.getById(anyLong);
            result = user;
            whiteUserDao.delete(anyLong);
            result = 1;
            syncRCSService.syncDeleteWhiteUser(anyString, anyInt);
            result = null;
        }};
        Result result = whiteUserService.delete(user.getId(), "test");
        Assert.assertTrue(result.isSuccess());
    }
}
