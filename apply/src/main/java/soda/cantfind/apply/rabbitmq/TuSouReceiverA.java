package soda.cantfind.apply.rabbitmq;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import soda.cantfind.apply.constant.SpiderBaseUrlConstant;
import soda.cantfind.apply.constant.SpiderSourceWebSite;
import soda.cantfind.apply.constant.TuSouTaskStateConstant;
import soda.cantfind.apply.entity.taobao.TaobaoShopInfo;
import soda.cantfind.apply.entity.tusou.Identical;
import soda.cantfind.apply.entity.tusou.TuSouResult;
import soda.cantfind.apply.entity.tusou.TusouSpiderTask;
import soda.cantfind.apply.entity.tusou.TusouSpiderTaskResult;
import soda.cantfind.apply.service.ISpiderService;
import soda.cantfind.apply.service.ITusouSpiderTaskResultService;
import soda.cantfind.apply.service.ITusouSpiderTaskService;
import soda.cantfind.apply.util.spider.SpiderTaobaoUtil;
import soda.module.core.web.config.RabbitConfig;
import soda.module.core.web.util.JsonUtil;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Describe
 * @Author soda
 * @Create 2019/9/1 18:38
 **/
@Component
@RabbitListener(queues = RabbitConfig.TUSOU_SPIDER_QUEUE_A)
public class TuSouReceiverA {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final TusouProcessUtil tusouProcessUtil;

    TuSouReceiverA(TusouProcessUtil tusouProcessUtil){
        this.tusouProcessUtil = tusouProcessUtil;
    }
    @RabbitHandler
    public void process(TusouSpiderTask content) {
        logger.info("后台采集工作开始进行");
        tusouProcessUtil.process(content);
    }
}
