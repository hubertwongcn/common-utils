package org.hubert.common.controller;

import lombok.RequiredArgsConstructor;
import org.hubert.common.rocketmq.msg.DeviceOpMsg;
import org.hubert.common.rocketmq.producer.PackageBindOpProducerHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This is an example class for rocketmq.
 *
 * @author hubertwong
 * @version 1.0
 * @since 2024/9/30 23:24
 */
@RequestMapping("/rocketmq")
@RestController
@RequiredArgsConstructor
public class RocketMQController {
    /**
     * Handler responsible for sending asynchronous messages
     * related to user device package binding operations using RocketMQ.
     * <p>
     * Utilizes a {@link PackageBindOpProducerHandler} to handle the details
     * of message production.
     */
    private final PackageBindOpProducerHandler packageBindOpProducerHandler;

    /**
     * Sends an asynchronous message for user device operations.
     *
     * @param deviceOpMsg the user device operation message that needs to be sent
     * @return the user device operation message that was sent
     */
    @PostMapping("/sendAsync")
    public DeviceOpMsg send(@RequestBody DeviceOpMsg deviceOpMsg) {
        packageBindOpProducerHandler.sendAsync(deviceOpMsg);
        return deviceOpMsg;
    }

    /**
     * Handles GET requests to the "/test" endpoint.
     *
     * @return a test response message
     */
    @GetMapping("/test")
    public String test() {
        return "This is a test response";
    }
}
