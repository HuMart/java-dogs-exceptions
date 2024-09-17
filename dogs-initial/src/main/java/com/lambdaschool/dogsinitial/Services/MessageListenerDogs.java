package com.lambdaschool.dogsinitial.Services;


import com.lambdaschool.dogsinitial.DogsinitialApplication;
import com.lambdaschool.dogsinitial.model.MessageDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MessageListenerDogs
{
    private static final Logger logger = LoggerFactory.getLogger(MessageListenerDogs.class);

    @RabbitListener(queues = DogsinitialApplication.QUEUE_NAME_DOGS)
    public void receiveMessage(MessageDetail msg)
    {
        logger.info(msg.toString());
        System.out.println("Received Dogs Queue message {" + msg.toString() + "}");
    }

}

