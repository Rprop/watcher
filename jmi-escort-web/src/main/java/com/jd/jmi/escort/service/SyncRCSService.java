package com.jd.jmi.escort.service;

/**
 * @author baozhaonasita
 * @version 1.0
 * @date 2016/3/4
 */
public interface SyncRCSService {

    /**
     * 增加黑名单同步
     *
     * @param userPin
     * @param orderType
     */
    public void syncAddBlackUser(String userPin, int orderType);

    /**
     * 增加黑名单同步
     *
     * @param userPin
     * @param orderTypes
     */
    public void syncAddBlackUser(String userPin, int[] orderTypes);

    /**
     * 删除黑名单同步
     *
     * @param userPin
     * @param orderType
     */
    public void syncDeleteBlackUser(String userPin, int orderType);

    /**
     * 删除黑名单同步
     *
     * @param userPin
     * @param orderTypes
     */
    public void syncDeleteBlackUser(String userPin, int[] orderTypes);

    /**
     * 增加白名单同步
     *
     * @param userPin
     * @param orderType
     */
    public void syncAddWhiteUser(String userPin, int orderType);

    /**
     * 增加白名单同步
     *
     * @param userPin
     * @param orderTypes
     */
    public void syncAddWhiteUser(String userPin, int[] orderTypes);

    /**
     * 删除白名单同步
     *
     * @param userPin
     * @param orderType
     */
    public void syncDeleteWhiteUser(String userPin, int orderType);

    /**
     * 删除白名单同步
     *
     * @param userPin
     * @param orderTypes
     */
    public void syncDeleteWhiteUser(String userPin, int[] orderTypes);
}
