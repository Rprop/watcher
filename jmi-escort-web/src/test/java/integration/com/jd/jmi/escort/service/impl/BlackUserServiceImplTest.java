package integration.com.jd.jmi.escort.service.impl;

import com.jd.jmi.escort.common.enums.MemberGradeEnum;
import com.jd.jmi.escort.model.Result;
import com.jd.jmi.escort.pojo.BlackUser;
import com.jd.jmi.escort.service.BlackUserService;
import com.jd.jmi.escort.service.UserService;
import com.jd.user.sdk.export.domain.UserBaseInfoVo;
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
 * @date 2016/3/28
 */
@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration({"/spring-config.xml"})
public class BlackUserServiceImplTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlackUserServiceImplTest.class);
    @Resource
    private BlackUserService blackUserService;
    @Resource
    private UserService userService;

    /**
     * 删除
     *
     * @throws Exception
     */
    @Test
    public void testDelete() throws Exception {
        BlackUser user = blackUserService.selectByNameOrderType("YYY9", 35);
        Result result = blackUserService.delete(user.getId(), "bjdiqi");
        Assert.assertTrue(result.isSuccess());
    }

    /**
     * 获取会员等级信息
     *
     * @throws Exception
     */
    @Test
    public void testUserinfo() throws Exception {
        final String userPin = "baota";
        UserBaseInfoVo infoVo = userService.getUserinfo(userPin);
        if (infoVo != null) {
            System.out.println("userLevel==" + MemberGradeEnum.getEnumByCode(infoVo.getUserLevel() + "").getName());
        }
    }
}
