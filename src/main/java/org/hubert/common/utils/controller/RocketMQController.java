package org.hubert.common.utils.controller;

import lombok.RequiredArgsConstructor;
import org.hubert.common.utils.rocketmq.msg.UserDeviceOpMsg;
import org.hubert.common.utils.rocketmq.producer.PackageBindOpProducerHandler;
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
    private final PackageBindOpProducerHandler packageBindOpProducerHandler;

    @PostMapping("/sendAsync")
    public UserDeviceOpMsg send(@RequestBody UserDeviceOpMsg userDeviceOpMsg) {
        packageBindOpProducerHandler.sendAsync(userDeviceOpMsg);
        return userDeviceOpMsg;
    }

    @GetMapping("/test")
    public String test() {
        return "This is a test response";
    }
}
