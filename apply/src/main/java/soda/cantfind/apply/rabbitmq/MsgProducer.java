package soda.cantfind.apply.rabbitmq;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import soda.module.core.web.config.RabbitConfig;

import java.util.UUID;

/**
 * @Describe
 * @Author soda
 * @Create 2019/8/30 14:58
 **/
@Component
public class MsgProducer implements RabbitTemplate.ConfirmCallback {
    private RabbitTemplate rabbitTemplate;
    public MsgProducer(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }
    public void sendMsg(String content) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        //把消息放入ROUTINGKEY_A对应的队列当中去，对应的是队列A
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_A, RabbitConfig.ROUTINGKEY_A, content, correlationId);
    }
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {

    }
}
