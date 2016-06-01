package integration.com.jd.jmi.escort.service;

import com.jd.cachecloud.common.HttpClient;

/**
 * Created by changpan on 2016/3/17.
 */
public class T {
    public static void main(String[] args) throws  Exception{
        HttpClient.RequestOption ro = HttpClient.RequestOption.post().addParameter("node", "/redis/cluster/13333").addParameter("data", "content").charset("utf-8");
        System.out.println((new HttpClient()).text("http://cfs.cache.jd.local/cachecloud-cfs/cfs/update", ro));
    }
}
