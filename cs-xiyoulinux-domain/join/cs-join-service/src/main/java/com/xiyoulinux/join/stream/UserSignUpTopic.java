package com.xiyoulinux.join.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author CoderZk
 */
public interface UserSignUpTopic {
    String INPUT = "user-signup-consumer";
    String OUTPUT = "user-signup-producer";

    @Input(INPUT)
    SubscribableChannel input();

    @Output(OUTPUT)
    MessageChannel output();
}
