package org.hubert.common.demo.properties;

import lombok.Data;

/**
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/4 14:48
 */
@Data
public class MQConsumerOperationProperties {
    private String nameServer;
    private String group;
    private String topic;
    private String tag;
}
