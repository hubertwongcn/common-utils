package org.hubert.common.utils.properties;

import lombok.Data;

/**
 * @author hubertwong
 * @version 1.0
 * @since 2024/9/20 14:48
 */
@Data
public class MQProducerOperationProperties {
    private String nameServer;
    private String group;
    private String topic;
    private String tag;
}