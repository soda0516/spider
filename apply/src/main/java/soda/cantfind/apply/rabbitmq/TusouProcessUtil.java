package soda.cantfind.apply.rabbitmq;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
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
import soda.module.core.web.util.JsonUtil;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Describe
 * @Author soda
 * @Create 2019/9/8 16:05
 **/
@Component
public class TusouProcessUtil {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource(name = SpiderSourceWebSite.TUSOU_VVIC)
    ISpiderService iSpiderService;

    private final ITusouSpiderTaskResultService iTusouSpiderTaskResultService;
    private final ITusouSpiderTaskService iTusouSpiderTaskService;

    TusouProcessUtil(ITusouSpiderTaskResultService iTusouSpiderTaskResultService,
                   ITusouSpiderTaskService iTusouSpiderTaskService){
        this.iTusouSpiderTaskResultService = iTusouSpiderTaskResultService;
        this.iTusouSpiderTaskService = iTusouSpiderTaskService;
    }
    void process(TusouSpiderTask content) {
        List<String> taobaoShopUrlList = new ArrayList<>();
        File file = new File(content.getLocalAddress());
//        首先从txt文档中，读取淘宝店铺的链接
        try (
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file))
        ) {
            while (bufferedReader.ready()){
                String line = bufferedReader.readLine();
                if (!StringUtils.isBlank(line.trim())){
                    taobaoShopUrlList.add(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            iTusouSpiderTaskService.saveOrUpdate(content.setTaskState(TuSouTaskStateConstant.destroy));
            return;
        }
        SpiderTaobaoUtil spiderTaobaoUtil = new SpiderTaobaoUtil();
//            对获取的每个店铺链接进行遍历
        for (String shopUrl:taobaoShopUrlList
        ) {
            TusouSpiderTaskResult taskResult = null;
            try {
                logger.info("采集正在进行中"+content.getFileName()+ shopUrl);
                //每次请求完成，需要休眠一会儿，方式弹出来滑块
                try {
                    TimeUnit.SECONDS.sleep((long) (Math.random()+1.5));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                TaobaoShopInfo shopInfo = spiderTaobaoUtil.getShopInfoByUrl(shopUrl);
                if (null == shopInfo || null == shopInfo.getImgUrl()){
                    logger.warn("采集正在进行中:"+content.getFileName()+ shopUrl+"没有返回店铺或商品详情信息");
                }else {
                    String md5 = iSpiderService.getStringFromUrl(shopInfo.getImgUrl());
                    if (!StringUtils.isBlank(md5)){
                        //这个链接特殊，需要把md5的值传进去
                        String json = iSpiderService.getJsonFromUrl(md5);
                        TuSouResult tuSouResult = JsonUtil.json2Obj(json, TuSouResult.class);
                        if (null != tuSouResult && null != tuSouResult.getData()){
                            List<Identical> identicalList = tuSouResult
                                    .getData()
                                    .getIdenticalList();
//                            OptionalInt max = identicalList.stream().mapToInt(Identical::getDiscount_price).max();
//                            OptionalInt min = identicalList.stream().mapToInt(Identical::getDiscount_price).min();
                            if (null != identicalList && identicalList.size() > 0){
                                List<Identical> identicals = identicalList
                                        .stream()
                                        .filter(item -> item.getIs_df().equals(1))
                                        .filter(item -> item.getIs_sp().equals(1))
                                        .filter(item -> item.getIs_tx().equals(1))
                                        .collect(Collectors.toList());
                                if (identicals.size() > 0){
                                    taskResult = new TusouSpiderTaskResult();
                                    taskResult.setOriGoodsUrl(shopUrl);
                                    taskResult.setOriGoodsPrice(shopInfo.getGoodsPrice());
                                    taskResult.setSameStyle(true);
                                    taskResult.setThreeTag(true);
                                    //取列表里的第一个值
                                    Identical first = identicals.get(0);
                                    taskResult.setSamePageUrl(String.format(SpiderBaseUrlConstant.TUSOU_VVIC_ITEM_PIC_URL, first.getItem_id(),first.getIndex_img_name()));
                                    taskResult.setTaskId(content.getId());
                                    //如果两个价格都不是空的，则进行差价计算
                                    if (null != shopInfo.getGoodsPrice() && null != first.getDiscount_price()){
                                        taskResult.setPriceDifferencesRange(shopInfo.getGoodsPrice().subtract(BigDecimal.valueOf(identicalList.get(0).getDiscount_price())).toString());
                                    }
                                    taskResult.setPriceRange(String.valueOf(first.getDiscount_price()));
                                    taskResult.setStatus(true);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.error(shopUrl+"采集过程出现外的错误"+e);
//                e.printStackTrace();
            }
//            判断任务结果，有没有信息，如果有插入，如果没有，就是采集失败了，就得把当前这条失败
            if (null == taskResult){
                taskResult = new TusouSpiderTaskResult();
                taskResult.setTaskId(content.getId());
                taskResult.setOriGoodsUrl(shopUrl);
                taskResult.setStatus(false);
                iTusouSpiderTaskResultService.save(taskResult);
            }else {
                taskResult.setStatus(true);
                iTusouSpiderTaskResultService.save(taskResult);
            }
        }
        iTusouSpiderTaskService.saveOrUpdate(content.setCompleteTime(LocalDateTime.now()).setTaskState(TuSouTaskStateConstant.complete));
        logger.info("文档采集完成"+ LocalDateTime.now());
    }
}
