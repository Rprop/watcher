package com.jd.jmi.escort.service.impl;

import com.alibaba.fastjson.JSON;
import com.jd.common.util.PaginatedList;
import com.jd.common.util.StringUtils;
import com.jd.common.util.base.PaginatedArrayList;
import com.jd.jmi.escort.dao.BlackUserDao;
import com.jd.jmi.escort.dao.WhiteUserDao;
import com.jd.jmi.escort.model.BlackUserQuery;
import com.jd.jmi.escort.model.Result;
import com.jd.jmi.escort.model.enums.SourceEnum;
import com.jd.jmi.escort.pojo.BlackUser;
import com.jd.jmi.escort.pojo.WhiteUser;
import com.jd.jmi.escort.service.SyncRCSService;
import com.jd.jmi.escort.service.WhiteUserService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by changpan on 2016/2/23.
 */
@Service
public class WhiteUserServiceImpl implements WhiteUserService {

    private static final Logger logger = LoggerFactory.getLogger(BlackUserServiceImpl.class);

    @Resource
    private WhiteUserDao whiteUserDao;

    @Resource
    private SyncRCSService syncRCSService;

    @Override
    public PaginatedList<WhiteUser> list(BlackUserQuery blackUserQuery) {
        try {
            return whiteUserDao.list(blackUserQuery);
        } catch (Exception e) {
            logger.error("获取白名单用户列表失败query=" + JSON.toJSONString(blackUserQuery), e);
        }
        return new PaginatedArrayList<WhiteUser>(blackUserQuery.getPage(),
                blackUserQuery.getPageSize());
    }

    private WhiteUser getById(long id) {
        return whiteUserDao.getById(id);
    }

    @Override
    public Result delete(long id, String uname) {
        Result result = new Result(false, "删除失败");
        try {
            WhiteUser model = getById(id);
            if (model == null) {
                return result.setMes("该白名单用户不存在");
            }
            logger.info("删白名单用户,uname={},blackUser={}", uname, JSON.toJSONString(model));

            int r = whiteUserDao.delete(id);
            if (r == 1) {
                result.setSuccessAndMes(true, "删除成功");
                syncRCSService.syncDeleteWhiteUser(model.getUserPin(),model.getOrderType());
            }
        } catch (Exception e) {
            logger.error("删除白名单用户失败id=" + id, e);
        }
        return result;
    }

    /**
     * 添加白名单用户
     *
     * @param user
     * @return
     */
    @Override
    public Result insert(WhiteUser user) {
        Result result = new Result(false, "添加失败");
        try {
            if(whiteUserDao.insert(user) > 0){
                result.setSuccessAndMes(true,"添加成功");
                syncRCSService.syncAddWhiteUser(user.getUserPin(),user.getOrderType());
            }
        } catch (Exception e) {
            logger.error("添加白名单用户失败", e);
        }
        return result;
    }

    /**
     * 批量插入用户
     *
     * @param userPin
     * @param orderTypes
     * @param sourceEnum
     * @return
     */
    public Result insertBatch(String userPin, Integer[] orderTypes, SourceEnum sourceEnum, String operateUser) {
        Result result = new Result(false, "添加失败");
        try {
            BlackUserQuery blackUserQuery = new BlackUserQuery();
            blackUserQuery.setPage(1);
            blackUserQuery.setPageSize(1000);
            blackUserQuery.setUserPin(userPin);
            PaginatedList<WhiteUser> list = this.list(blackUserQuery);
            List<WhiteUser> users = new ArrayList<WhiteUser>();

            for (Integer orderType : orderTypes) {
                boolean isHas = false;
                for (WhiteUser u : list) {
                    if (orderType.compareTo(u.getOrderType()) == 0) {
                        isHas = true;
                        break;
                    }
                }
                if (!isHas) {
                    WhiteUser user = new WhiteUser();
                    user.setUserPin(userPin);
                    user.setModifyUser(operateUser);
                    user.setOrderType(orderType);
                    user.setSource(sourceEnum.getCode());
                    users.add(user);
                }
            }
            if (CollectionUtils.isNotEmpty(users)) {
                whiteUserDao.insertBatch(users);
                int [] orderTypeArray = new int [users.size()];
                int i = 0;
                for(WhiteUser user:users){
                    orderTypeArray[i++] = user.getOrderType();
                }
                syncRCSService.syncAddWhiteUser(userPin,orderTypeArray);
            }
            result.setSuccessAndMes(true, "添加成功");
        } catch (Exception e) {
            logger.error("添加白名单用户失败", e);
        }
        return result;
    }

    /**
     * 通过用户名和订单类型获取白名单用户
     *
     * @param userPin
     * @param orderType
     * @return
     */
    public WhiteUser selectByNameOrderType(String userPin, Integer orderType){
        if(StringUtils.isEmpty(userPin) || orderType==null){
            return null;
        }
        WhiteUser whiteUser = new WhiteUser();
        whiteUser.setUserPin(userPin);
        whiteUser.setOrderType(orderType);
        try{
            return whiteUserDao.selectByNameOrderType(whiteUser);
        }catch (Exception e){
            logger.error(String.format("获取白名单用户失败userPin=%s,orderType=%d",userPin,orderType), e);
            return null;
        }
    }
}