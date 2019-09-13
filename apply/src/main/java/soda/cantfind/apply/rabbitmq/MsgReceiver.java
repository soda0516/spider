package soda.cantfind.apply.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import soda.module.core.web.config.RabbitConfig;

import java.util.concurrent.TimeUnit;

/**
 * @Describe
 * @Author soda
 * @Create 2019/8/30 15:36
 **/
//@Component
//@RabbitListener(queues = RabbitConfig.QUEUE_A)
public class MsgReceiver {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

//    @RabbitHandler
    public void process(String content) {
        for (int i = 0; i < 60; i++) {
            System.out.println("接收处理队列A当中的消息： " + content);
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.info("接收处理队列A当中的消息： " + content);
    }
}
