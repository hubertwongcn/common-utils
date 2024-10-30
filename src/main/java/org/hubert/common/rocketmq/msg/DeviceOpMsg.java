package org.hubert.common.rocketmq.msg;

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
public class DeviceOpMsg {
    /**
     * App ID
     */
    private String appId;

    /**
     * Username (phone number)
     */
    private String userCode;

    /**
     * Device code
     */
    private String deviceCode;

    /**
     * Package ID
     */
    private String packageId;

    /**
     * Package source
     */
    private String packageSource;

    /**
     * Order type
     */
    private Integer packageType;

    /**
     * Package change type:
     * 0: Unbind
     * 1: Bind
     */
    private Integer bindType;
}