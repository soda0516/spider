package soda.cantfind.apply.util.spider;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import soda.cantfind.apply.constant.TuSouTaskStateConstant;
import soda.cantfind.apply.entity.taobao.TaobaoShopInfo;
import soda.cantfind.apply.entity.tusou.TusouSpiderTask;
import soda.module.core.constant.PublicResultConstant;
import soda.module.core.web.exception.BusinessException;
import soda.module.core.web.exception.ForbiddenException;
import soda.module.core.web.response.ResponseBuilder;
import soda.module.core.web.response.ResponseModel;
import soda.module.core.web.util.JsonUtil;
import soda.module.user.model.JwtUser;
import soda.module.user.util.SecurityContextUtil;

import java.io.*;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Describe
 * @Author soda
 * @Create 2019/8/29 11:20
 **/
public class SpiderTaobaoUtil {
    private static Logger logger = LoggerFactory.getLogger(SpiderTaobaoUtil.class);
    private static RequestConfig requestConfig;
    private CloseableHttpClient client;
    private static CloseableHttpClient httpClient = HttpClientBuilder.create().build();
    static {
        requestConfig = RequestConfig
                .custom()
                .setCircularRedirectsAllowed(true)
                .build();
    }
    public SpiderTaobaoUtil(){}
    public SpiderTaobaoUtil(CloseableHttpClient closeableHttpClient){
        this.client = closeableHttpClient;
    }
    public TaobaoShopInfo getShopInfoByUrl(String url){
        TaobaoShopInfo taobaoShopInfo = null;
        //        从淘宝的连接中获取相关图片的get请求
        String body = this.getResponsebodyFromUrl(url);
        if (!StringUtils.isBlank(body)){
            Document document = Jsoup.parse(body);
            Elements elements = document.body().getElementById("J_UlThumb").getElementsByTag("img");
            Elements elementPrice = document.body().getElementsByClass("tb-rmb-num");
            if (!elements.isEmpty()){
                String img ;
                if (!StringUtils.isBlank(elements.get(0).attr("data-src"))){
                    img = elements.get(0).attr("data-src").split(".jpg_")[0] + ".jpg";
                }else {
                    img = elements.get(0).attr("src").split(".jpg_")[0] + ".jpg";
                }
                if (!img.startsWith("https:")){
                    img = "https:"+img;
                }
                if (!StringUtils.isBlank(img)){
                    taobaoShopInfo = new TaobaoShopInfo();
                    taobaoShopInfo.setImgUrl(img);
                }
            }
            if (null != taobaoShopInfo &&!elementPrice.isEmpty()){
                String priceStr = elementPrice.first().text();
                if (StringUtils.isNumeric(priceStr.replace(".",""))){
                    taobaoShopInfo.setGoodsPrice(new BigDecimal(priceStr));
                }
            }
        }
        return taobaoShopInfo;
    }
    private String getResponsebodyFromUrl(String url){
        String result = null;
        //        从淘宝的连接中获取相关图片的get请求
        HttpGet getFromTabao = new HttpGet(url);
        getFromTabao.setConfig(requestConfig);
        try (
                CloseableHttpResponse responseTaobao = httpClient.execute(getFromTabao)
        ) {
            if (responseTaobao.getStatusLine().getStatusCode() == 200){
                HttpEntity entity = responseTaobao.getEntity();
                result = EntityUtils.toString(entity);
            }else {
                logger.warn("淘宝店铺:"+url+":没有返回相关店铺及产品信息");
            }
        } catch (SocketTimeoutException e) {
            return this.getResponsebodyFromUrl(url);
//            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static TusouSpiderTask uploadTxtDoc(MultipartFile file){
        JwtUser jwtUser = SecurityContextUtil.getJwtUserFromContext();
        if (null == jwtUser){
            throw new ForbiddenException("当前用户信息无效");
        }
        TusouSpiderTask task = null;
        String fileName = file.getOriginalFilename();
        String filePath = "c:/taobao/";
        File dest = new File(filePath + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + (int)(Math.random() * 10)+(int)(Math.random() * 10)+ fileName);
//        加上了日期，和随机数，这样要是还能有重复的，我就特么的认了
        if (!dest.getParentFile().exists()){
            if (dest.getParentFile().mkdir()){
                while (dest.exists()){
                    dest.renameTo(new File(filePath + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))+ (int)(Math.random() * 10)+(int)(Math.random() * 10)+ fileName));
                }
            }
        }
        try {
            file.transferTo(dest);
        } catch (Exception e) {
            logger.error(e.toString());
            throw new BusinessException("写入文档过程中出现异常");
        }
//        判断文件是不是存在
        if (dest.exists()){
            int count = 0;
            try (
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(dest))
            ) {
                while (bufferedReader.ready()){
                    String line = bufferedReader.readLine();
                    if (!StringUtils.isBlank(line)){
                        if (line.startsWith("https://item.taobao.com/") || line.startsWith("https://detail.tmall.com")){
                            count++;
                        }else {
                            throw new BusinessException("上传文档中，存在非淘宝连接，请核对文档信息，重新上传");
                        }
                    }
                }
            } catch (IOException e) {
                logger.error(e.toString());
                throw new BusinessException("上传文档过程中出现异常");
            }
            task = new TusouSpiderTask();
            task.setUserId(jwtUser.getId());
            task.setCreateTime(LocalDateTime.now());
            task.setFileName(dest.getName());
            task.setLocalAddress(dest.getPath());
            task.setUrlCount(count);
            task.setTaskState(TuSouTaskStateConstant.ready);
        }
        return task;
    }
}
