package com.benzolamps.dict.service.base;

import java.util.List;

/**
 * 局域网操作Service接口
 * @author Benzolamps
 * @version 2.2.1
 * @datetime 2018-10-9 09:02:43
 */
public interface LocalAreaNetworkService {

    /**
     * 打开防火墙
     */
    void openFireWall();

    /**
     * 添加规则
     */
    void addRule();

    /**
     * 获取ipv4
     * @return ipv4
     */
    List<String> getIpv4();
}
