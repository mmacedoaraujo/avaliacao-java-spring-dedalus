package com.mmacedoaraujo.userapirabbitmqconsumer.listener;

import com.mmacedoaraujo.userapirabbitmqconsumer.config.RabbitMQConfig;
import com.mmacedoaraujo.userapirabbitmqconsumer.domain.User;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    //Method to consume user-api queue and print out what has been in the queue
    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void listener(User user) {
        System.out.println(user);
    }
}
