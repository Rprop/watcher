package com.jd.jmi.escort.service.impl;

import com.alibaba.fastjson.JSON;
import com.jd.common.util.PaginatedList;
import com.jd.common.util.StringUtils;
import com.jd.common.util.base.PaginatedArrayList;
import com.jd.jmi.escort.common.enums.BlackUserLevelEnum;
import com.jd.jmi.escort.common.enums.JmiRecordStatusEnum;
import com.jd.jmi.escort.common.enums.RiskEventDelUserEnum;
import com.jd.jmi.escort.dao.BlackUserDao;
import com.jd.jmi.escort.dao.RiskEventDao;
import com.jd.jmi.escort.model.BlackUserQuery;
import com.jd.jmi.escort.model.Result;
import com.jd.jmi.escort.model.enums.SourceEnum;
import com.jd.jmi.escort.pojo.BlackUser;
import com.jd.jmi.escort.service.BlackUserService;
import com.jd.jmi.escort.service.SyncRCSService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 黑名单用户
 * Created by changpan on 2016/2/22.
 */
@Service("blackUserService")
public class BlackUserServiceImpl implements BlackUserService {
    private static final Logger logger = LoggerFactory.getLogger(BlackUserServiceImpl.class);

    @Resource
    private BlackUserDao blackUserDao;
    @Resource
    private RiskEventDao riskEventDao;
    @Resource
    private SyncRCSService syncRCSService;
    @Resource
    private DataSourceTransactionManager transactionManager;

    @Override
    public PaginatedList<BlackUser> list(BlackUserQuery blackUserQuery) {
        try {
            return blackUserDao.list(blackUserQuery);
        } catch (Exception e) {
            logger.error("获取黑名单用户列表失败query=" + JSON.toJSONString(blackUserQuery), e);
        }
        return new PaginatedArrayList<BlackUser>(blackUserQuery.getPage(),
                blackUserQuery.getPageSize());
    }

    @Override
    public Result insert(BlackUser blackUser) {
        Result result = new Result(false);
        if(null == blackUser.getTriggerCount()){
            blackUser.setTriggerCount(0L);
        }
        int r = blackUserDao.insert(blackUser);
        if (r > 0) {
            result.setSuccess(true);
            result.setMes("添加成功");
        } else {
            result.setMes("添加失败");
        }
        return result;
    }

    @Override
    public Result update(BlackUser blackUser) {
        Result result = new Result(false);
        int r = blackUserDao.update(blackUser);
        if (r == 1) {
            result.setSuccess(true);
            result.setMes("更新成功");
        } else {
            result.setMes("更新失败");
        }
        return result;
    }

    @Override
    public Result insertBatch(final String userPin, Integer[] orderTypes, SourceEnum sourceEnum, String operateUser) {
        final Result result = new Result(false, "添加失败");
        BlackUserQuery blackUserQuery = new BlackUserQuery();
        blackUserQuery.setPage(1);
        blackUserQuery.setPageSize(1000);
        blackUserQuery.setUserPin(userPin);
        PaginatedList<BlackUser> list = this.list(blackUserQuery);
        final List<BlackUser> addUsers = new ArrayList<BlackUser>();
        final List<BlackUser> updateUsers = new ArrayList<BlackUser>();

        for (Integer orderType : orderTypes) {
            boolean isHas = false;
            for (BlackUser u : list) {
                if (orderType.compareTo(u.getOrderType()) == 0) {
                    isHas = true;
                    if (u.getLevel().intValue() == BlackUserLevelEnum.RISKUSER.getCode()) {
                        u.setLevel(BlackUserLevelEnum.BLACKUSER.getCode());
                        u.setSource(SourceEnum.BACKGROUND.getCode());
                        updateUsers.add(u);
                    }
                    break;
                }
            }
            if (!isHas) {
                BlackUser user = new BlackUser();
                user.setUserPin(userPin);
                user.setLevel(BlackUserLevelEnum.BLACKUSER.getCode());
                user.setTriggerCount(0L);
                user.setModifyUser(operateUser);
                user.setOrderType(orderType);
                user.setSource(sourceEnum.getCode());
                addUsers.add(user);
            }
        }
        int r = saveOrUpdate(addUsers, updateUsers, userPin);
        if (r == 1) {
            result.setSuccess(true);
            result.setMes("添加或更新成功");
        } else {
            result.setMes("添加或更新失败");
        }
        return result;
    }

    private int saveOrUpdate(final List<BlackUser> addUsers, final List<BlackUser> updateUsers, final String userPin) {
        TransactionTemplate template = new TransactionTemplate(transactionManager);
        int result = (Integer) template.execute(new TransactionCallback() {
            @Override
            public Object doInTransaction(TransactionStatus transactionStatus) {
                int rt = 1;
                int urt = 1;
                try {
                    if (CollectionUtils.isNotEmpty(addUsers)) {
                        rt = blackUserDao.insertBatch(addUsers);
                    }
                    if (CollectionUtils.isNotEmpty(updateUsers)) {
                        /**
                         * 更新风险等级
                         */
                        for (BlackUser user : updateUsers) {
                            urt = blackUserDao.update(user);
                            if (urt != 1) {
                                logger.error("更新风险等级失败,urt={},user={}", urt, JSON.toJSONString(user));
                                break;
                            }
                        }
                    }
                    if (rt > 0) {
                        if (urt > 0) {
                            /**
                             * 同步RCS
                             */
                            List<BlackUser> rcsUsers = new ArrayList<BlackUser>();
                            rcsUsers.addAll(addUsers);
                            rcsUsers.addAll(updateUsers);
                            if (CollectionUtils.isNotEmpty(rcsUsers) && rt > 0 && urt > 0) {
                                int[] orderTypeArray = new int[rcsUsers.size()];
                                int i = 0;
                                for (BlackUser user : rcsUsers) {
                                    orderTypeArray[i++] = user.getOrderType();
                                }
                                syncRCSService.syncAddBlackUser(userPin, orderTypeArray);
                            }
                        } else {
                            transactionStatus.setRollbackOnly();
                            logger.error("更新风险用户失败,urt={}", urt);
                        }
                    } else {
                        transactionStatus.setRollbackOnly();
                        logger.error("更新风险用户失败,rt={}", rt);
                    }
                } catch (Exception e) {
                    transactionStatus.setRollbackOnly();
                    logger.error("数据库保存出错", e);
                    return -1;
                }
                if (rt > 0 && urt > 0) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        return result;
    }

    private BlackUser getById(long id) {
        return blackUserDao.getById(id);
    }

    @Override
    public Result delete(final long id, final String uname) {
        Result result = new Result(false, "删除失败");
        TransactionTemplate template = new TransactionTemplate(transactionManager);
        int rt = (Integer) template.execute(new TransactionCallback() {
            @Override
            public Object doInTransaction(TransactionStatus transactionStatus) {
                try {
                    BlackUser blackUser = getById(id);
                    logger.info("删除风险用户,uname={},blackUser={}", uname, JSON.toJSONString(blackUser));
                    blackUser.setStatus(JmiRecordStatusEnum.DELETE.getCode());
                    blackUser.setModifyUser(uname);
                    int r = blackUserDao.update(blackUser);
                    int er = riskEventDao.updateForDelUser(blackUser.getUserPin(), RiskEventDelUserEnum.DELETE.getCode(), blackUser.getOrderType());
                    if (r > 0) {
                        if (er >= 0) {//风险事件记录可能会没有
                            syncRCSService.syncDeleteBlackUser(blackUser.getUserPin(), blackUser.getOrderType());
                            return 1;
                        } else {
                            transactionStatus.setRollbackOnly();
                            logger.error("更新风险事件失败，er=" + er);
                            return -1;
                        }
                    } else {
                        transactionStatus.setRollbackOnly();
                        logger.error("更新风险用户失败，r=" + r);
                        return -1;
                    }
                } catch (Exception e) {
                    transactionStatus.setRollbackOnly();
                    logger.error("删除风险用户失败id=" + id, e);
                    return -1;
                }
            }
        });
        if (rt > 0) {
            result.setSuccessAndMes(true, "删除成功");
        }
        return result;
    }

    /**
     * 合并
     *
     * @param blackUser
     * @return
     */
    private Result merge(BlackUser blackUser) {
        Result result = new Result(false);
        BlackUser oldBlackUser = selectByNameOrderType(blackUser.getUserPin(), blackUser.getOrderType());
        if (null == oldBlackUser) {
            if (null == blackUser.getTriggerCount()) {
                blackUser.setTriggerCount(1L);
            }
            blackUser.setSource(SourceEnum.SYNC.getCode());
            blackUser.setModifyUser("system");
            result = insert(blackUser);
        } else {
            if (blackUser.getLevel().intValue() == BlackUserLevelEnum.BLACKUSER.getCode()) {
                oldBlackUser.setLevel(BlackUserLevelEnum.BLACKUSER.getCode());
            }
            int r = blackUserDao.updateTriggerCountById(oldBlackUser);
            if (r > 0) {
                result.setSuccess(true);
            }
        }
        if (blackUser.getLevel().intValue() == BlackUserLevelEnum.BLACKUSER.getCode()) {
            syncRCSService.syncAddBlackUser(oldBlackUser.getUserPin(), oldBlackUser.getOrderType());
        }
        return result;
    }

    @Override
    public Result merge(String userPin, Integer orderType, Integer level) {
        BlackUser blackUser = new BlackUser();
        blackUser.setUserPin(userPin);
        blackUser.setOrderType(orderType);
        blackUser.setLevel(level);
        return merge(blackUser);
    }

    @Override
    public BlackUser selectByNameOrderType(String userPin, Integer orderType) {
        if (StringUtils.isEmpty(userPin) || orderType == null) {
            return null;
        }
        BlackUserQuery query = new BlackUserQuery();
        query.setUserPin(userPin);
        query.setOrderType(orderType);
        return blackUserDao.selectByNameOrderType(query);
    }
}
