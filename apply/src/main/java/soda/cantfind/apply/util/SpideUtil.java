package soda.cantfind.apply.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import soda.cantfind.apply.entity.ZwdShopInfo;
import soda.cantfind.apply.entity.zwd.stu.Same;
import soda.cantfind.apply.entity.zwd.stu.SearchResult;
import soda.cantfind.apply.util.spider.SpiderTaobaoUtil;
import soda.cantfind.apply.util.spider.SpiderZwdUtil;
import soda.module.core.web.util.JsonUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Describe
 * @Author soda
 * @Create 2019/8/25 18:57
 **/
public class SpideUtil {
    public static void main(String[] args) {
////        WebDriver webDriver = new FirefoxDriver();
////        System.out.println("https://img.alicdn.com/imgextra/i3/705821169/O1CN01UD8XpR1KVRYHRkkUj_!!705821169.jpg_50x50.jpg".startsWith("https:"));
//        long startTime = System.currentTimeMillis(); //获取开始时间
//
//        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
//        SpiderTaobaoUtil spiderTaobaoUtil = new SpiderTaobaoUtil(closeableHttpClient);
//        String url = spiderTaobaoUtil.getOneImageUrlWithShopUrl("https://item.taobao.com/item.htm?id=596310961711");
//        CookieStore cookieStore = new BasicCookieStore();
//        CloseableHttpClient httpClient = HttpClients
//                .custom()
//                .setDefaultCookieStore(cookieStore)
//                .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0")
//                .build();
//        WebDriver webDriver = new FirefoxDriver();
//        SpiderZwdUtil spiderZwdUtil = new SpiderZwdUtil(httpClient,webDriver).setCookieStore(cookieStore);
//        try {
//            System.out.println(spiderZwdUtil.getInfoByImgUrlWithHttoClient(url));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            webDriver.quit();
//        }
//
//        long endTime = System.currentTimeMillis(); //获取结束时间
//        webDriver.quit();
//        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");
//        WebDriver webDriver = new FirefoxDriver();
//        for (int i = 0; i < 50; i++) {
//            System.out.println();
//        }
//        getZwdInfoNoWebDriver("https://item.taobao.com/item.htm?id=596310961711");
    }
    public static List<ZwdShopInfo> getInfoFromTaobaoUrl(String url){
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);
        WebDriver driver = new FirefoxDriver(options);
        List<ZwdShopInfo> zwdShopInfoList = null;
        try {
            driver.get(url);
            Document documentTaobao = Jsoup.parse(driver.getPageSource());
            Elements elements = documentTaobao.body().getElementById("J_UlThumb").getElementsByTag("img");
            if (elements.isEmpty()){
                return null;
            }
            String imgUrl = "https:" + elements.get(0).attr("src").split(".jpg_")[0] + ".jpg";
            driver.get("https://stu.17zwd.com/");
            driver.findElement(By.className("tip-confirm")).click();
            driver.findElement(By.id("search-input")).findElement(By.tagName("input")).sendKeys(imgUrl);
            driver.findElement(By.id("search-btn")).click();
//            TimeUnit.SECONDS.sleep(5);
            if (null != driver.findElement(By.className("guide-btn"))){
                driver.findElement(By.className("guide-btn")).click();
                driver.findElement(By.className("guide-btn")).click();
            }
            TimeUnit.MILLISECONDS.sleep(500);
            String body = driver.getPageSource();
            Document document = Jsoup.parse(body);
            Elements elementsByClass = document.body().getElementsByClass("wrap-rs-item");
            if (!elementsByClass.isEmpty()){
                zwdShopInfoList = new ArrayList<>();
                for (Element e:elementsByClass
                     ) {

                    ZwdShopInfo zwdShopInfo = new ZwdShopInfo();
                    zwdShopInfo.setGoodsName(e.getElementsByClass("link-rs-item-name").attr("title"));
                    zwdShopInfo.setGoodsPrice(BigDecimal.valueOf(Double.parseDouble(e.getElementsByClass("word-rs-item-price").text().replace("￥",""))));
                    zwdShopInfo.setOnSaleTime(e.getElementsByClass("word-rs-item-count-view").text());
                    zwdShopInfo.setShopUrl(e.getElementsByClass("link-rs-item-name").attr("href"));
                    zwdShopInfo.setShopName(e.getElementsByClass("link-rs-item-shop-name icon-service").text());
                    zwdShopInfoList.add(zwdShopInfo);
                }
//                Element e = elementsByClass.get(0);
//                zwdShopInfoList = new ArrayList<>();
//                ZwdShopInfo zwdShopInfo = new ZwdShopInfo();
//                zwdShopInfo.setGoodsName(e.getElementsByClass("link-rs-item-name").attr("title"));
//                zwdShopInfo.setGoodsPrice(BigDecimal.valueOf(Double.parseDouble(e.getElementsByClass("word-rs-item-price").text().replace("￥",""))));
////                zwdShopInfo.setOnSaleTime(LocalDateTime.parse(e.getElementsByClass("word-rs-item-count-view").text()));
//                zwdShopInfo.setShopUrl(e.getElementsByClass("link-rs-item-name").attr("href"));
//                zwdShopInfoList.add(zwdShopInfo);
//                System.out.println("商品名称："+e.getElementsByClass("link-rs-item-name").attr("title"));
//                System.out.println("店铺链接："+e.getElementsByClass("link-rs-item-name").attr("href"));
//                System.out.println("商品价格："+e.getElementsByClass("word-rs-item-price").text().replace("￥",""));
//                System.out.println("上架时间："+e.getElementsByClass("word-rs-item-count-view").text());
//                //System.out.println("店铺地址："+e.getElementsByClass("data-route").text());
//                System.out.println("店铺名称："+e.getElementsByClass("link-rs-item-shop-name icon-service").text());
            }
//            TimeUnit.SECONDS.sleep(5);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
        return zwdShopInfoList;
    }
    @Deprecated
    private static List<ZwdShopInfo> getZwdInfoNoWebDriver(String taobaoShopUrl){
        List<ZwdShopInfo> zwdShopInfoList = null;
        CloseableHttpClient closeableHttpClient = HttpClientBuilder
                .create()
                .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0")
                .build();
        RequestConfig requestConfig = RequestConfig
                .custom()
                .setCircularRedirectsAllowed(true)
                .build();
//        从淘宝的连接中获取相关图片的get请求
        HttpGet getFromTabao = new HttpGet(taobaoShopUrl);
        getFromTabao.setConfig(requestConfig);
        String imgUrl = null;
        try (
                CloseableHttpResponse responseTaobao = closeableHttpClient.execute(getFromTabao)
        ) {
            if (responseTaobao.getStatusLine().getStatusCode() == 200){
                HttpEntity entity = responseTaobao.getEntity();
                String taobaoBody = EntityUtils.toString(entity);
                Document documentTaobao = Jsoup.parse(taobaoBody);
                Elements elements = documentTaobao.body().getElementById("J_UlThumb").getElementsByTag("img");
                if (!elements.isEmpty()){
                    if (!StringUtils.isBlank(elements.get(0).attr("data-src"))){
                        String img = elements.get(0).attr("data-src").split(".jpg_")[0] + ".jpg";
                        if (!img.startsWith("https:")){
                            img = "https:"+img;
                        }
                        imgUrl = img;
                        System.out.println("data-src" + imgUrl);
                    }else {
                        String img = elements.get(0).attr("src").split(".jpg_")[0] + ".jpg";
                        if (!img.startsWith("https:")){
                            img = "https:" + img;
                        }
                        imgUrl = img;
                        System.out.println("src" + imgUrl);
                    }
                }
            }
            System.out.println(imgUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Integer> sameIdList = new ArrayList<>();
////        如果图片连接不是空的，则进行下面的步骤
//        if (!StringUtils.isBlank(imgUrl)){
//            String zwdUrl = "https://stu.17zwd.com/SearchSimilar?img_url=" + imgUrl;
//            System.out.println("能进读取gids的代码"+zwdUrl);
//            HttpGet getFromZwd = new HttpGet(zwdUrl);
//            try (
//                    CloseableHttpResponse response17Zwd = closeableHttpClient.execute(getFromZwd)
//            ) {
//                if (response17Zwd.getStatusLine().getStatusCode() == 200) {
//                    String body = EntityUtils.toString(response17Zwd.getEntity());
//                    Document document = Jsoup.parse(body);
//                    document.head().getElementsByTag("script");
//                    for (Element e:document.head().getElementsByTag("script")
//                    ) {
//                        String str = e.html().replace("\n", ""); //这里是为了解决 无法多行匹配的问题
////                String pattern = "var renderInfo = \\{(.*?)\\};"; //()必须加，
//                        String pattern = "searchResult: \\{(.*?)\\};"; //()必须加，
//                        Pattern r = Pattern.compile(pattern,Pattern.MULTILINE);// Pattern.MULTILINE 好像没有什么用，所以才使用上面的replace
//                        Matcher m = r.matcher(str);
//                        if(m.find()) {
//                            String option = m.group();
////                    System.out.println(option_1.replace("searchResult: ",""));
//                            String result = option.replace("searchResult: ","");
//                            System.out.println("返回的result结果是："+result);
//                            SearchResult root = JsonUtil.json2Obj(result, SearchResult.class);
//                            if (null != root && root.getData() != null){
//                                for (Same same :root.getData().getSame()
//                                ) {
//                                    sameIdList.add(same.getGid());
//                                }
//                            }
//                        }else {
//                            System.out.println("找不到返回结果searchResult");
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
////        https://stu.17zwd.com/api/searchByGids?gids=111720921
////        如果同款的列表，不是空的，则通过id进行同款信息的查询
//        if (sameIdList.size() > 0){
//            StringBuilder gidParams = new StringBuilder();
//            for (int id:sameIdList
//                 ) {
//                gidParams.append(id);
//                gidParams.append(",");
//            }
//            String url = "";
//            if (gidParams.toString().length() > 0){
//                url ="https://stu.17zwd.com/api/searchByGids?gids=" + gidParams.substring(0,gidParams.toString().length() - 1);
//            }
////            stringBuilder.append()
//            HttpGet httpGet = new HttpGet(url);
//            try {
//                CloseableHttpResponse responseByGid = closeableHttpClient.execute(httpGet);
//                String body = EntityUtils.toString(responseByGid.getEntity());
//                Document document = Jsoup.parse(body);
//                System.out.println(document.body().html());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        return zwdShopInfoList;
    }
}
