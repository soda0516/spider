package soda.cantfind.apply.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import soda.cantfind.apply.entity.tusou.TusouSpiderTask;
import soda.module.core.web.config.RabbitConfig;

/**
 * @Describe
 * @Author soda
 * @Create 2019/9/8 16:09
 **/
@Component
@RabbitListener(queues = RabbitConfig.TUSOU_SPIDER_QUEUE_A)
public class TuSouReceiverC {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final TusouProcessUtil tusouProcessUtil;

    TuSouReceiverC(TusouProcessUtil tusouProcessUtil){
        this.tusouProcessUtil = tusouProcessUtil;
    }
    @RabbitHandler
    public void process(TusouSpiderTask content) {
        logger.info("后台采集工作开始进行");
        tusouProcessUtil.process(content);
    }
}
