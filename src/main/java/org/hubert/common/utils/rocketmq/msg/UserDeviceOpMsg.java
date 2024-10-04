package org.hubert.common.utils.rocketmq.msg;

import lombok.Data;

/**
 * This is an example of the message body<p>
 * User device package binding/unbinding message body
 *
 * @author hubertwong
 * @version 1.0
 * @since 2024/9/13 09:46
 */
@Data
public class UserDeviceOpMsg {
    /**
     * App ID
     */
    private String appId;
    /**
     * 用户名(手机号)
     */
    private String userCode;
    /**
     * 设备码
     */
    private String deviceCode;
    /**
     * 套餐ID
     */
    private String packageId;
    /**
     * 套餐来源
     */
    private String packageSource;
    /**
     * 订单类型
     */
    private Integer packageType;
    /**
     * 套餐变更类型：
     * 0:解绑
     * 1:绑定
     */
    private Integer bindType;
}