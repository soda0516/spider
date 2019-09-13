package soda.cantfind.apply.util.spider;

import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import soda.cantfind.apply.entity.ZwdShopInfo;
import soda.cantfind.apply.entity.zwd.stu.Same;
import soda.cantfind.apply.entity.zwd.stu.SearchResult;
import soda.module.core.web.util.JsonUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Describe
 * @Author soda
 * @Create 2019/8/29 11:48
 **/
public class SpiderZwdUtil {
    private CloseableHttpClient closeableHttpClient;
    private WebDriver webDriver;
    private static RequestConfig requestConfig;
    private CookieStore cookieStore;

    public SpiderZwdUtil setCookieStore(CookieStore cookieStore) {
        this.cookieStore = cookieStore;
        return this;
    }

    public CookieStore getCookieStore() {
        return cookieStore;
    }

    static {
        requestConfig = RequestConfig
                .custom()
                .setCircularRedirectsAllowed(true)
                .build();
    }
    private SpiderZwdUtil(){}
    public SpiderZwdUtil(CloseableHttpClient closeableHttpClient){
        this.closeableHttpClient = closeableHttpClient;
    }
    public SpiderZwdUtil(WebDriver webDriver){
        this.webDriver = webDriver;
    }
    public SpiderZwdUtil(CloseableHttpClient closeableHttpClient,WebDriver webDriver){
        this.webDriver = webDriver;
        this.closeableHttpClient = closeableHttpClient;
    }

    public ZwdShopInfo getInfoByImgUrlWithHttoClient(String imgUrl){
        List<Integer> sameIdList = new ArrayList<>();
        String zwdUrl = "https://stu.17zwd.com/SearchSimilar?img_url=" + imgUrl;
        System.out.println("从17网上获取的连接是：" + zwdUrl);
        BasicClientCookie clientCookie = new BasicClientCookie("laravel_session","SetYkNE7PFjYT7zNhk97bR7WDCgfYY9ftYJIQn1E");
        clientCookie.setPath("/");
        clientCookie.setSecure(false);
        cookieStore.addCookie(clientCookie);
        HttpGet getFromZwd = new HttpGet("https://stu.17zwd.com/search/bc0acb75016bd128276f1e81b1299e6a");

        getFromZwd.setHeader("Host","stu.17zwd.com");
        getFromZwd.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0");
//        getFromZwd.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//        getFromZwd.setHeader("Accept-Language","zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
//        getFromZwd.setHeader("Accept-Encoding","gzip, deflate, br");
//        getFromZwd.setHeader("Cookie","stu_token=nque3s90-8LejIR_cPeH0d64_rMxzDNyZ70s; daifa_tip_shown=true; w_popover_shown=1; Hm_lvt_d4ab127f10dee8544952f68326109a9a=1567055351; Hm_lpvt_d4ab127f10dee8544952f68326109a9a=1567055366; Hm_lvt_cdb4a077a2e6221083922d763bdbfa18=1567055351; Hm_lpvt_cdb4a077a2e6221083922d763bdbfa18=1567055365; stu_crop_guide_shown=true; daifa_session=eyJpdiI6IlNVblhVQ0JLSE45UFB0MWFjVVZzRVE9PSIsInZhbHVlIjoiXC9LSUxiSm1PQkV5c25cL2FtcDhreWhlUkVrOTAwSDVpQVFkZGtLN2lPOTFVbjl5bDNhcHdna1wvR0FtS0xORnozbSIsIm1hYyI6ImIwM2IxZTY0NTJlYWRkNzVhMjBkMDIxODhlMWY4NDExZjcxYThhY2QyN2M5Yjc2ZmM5ODgyN2U1MzMzY2M0ODUifQ%3D%3D");
//        getFromZwd.setHeader("daifa_session","eyJpdiI6IjVxZzA2RlBHY0syZEpGaHNBT1k4Q3c9PSIsInZhbHVlIjoiZktZbzIydWRNZDR1THVPcXQ4bFBPR3k2WXBYN1B4bE9nVjM4V1dpcjRRNmd1N2VXbFR3ZVJrUlRqZmRxUFV2NyIsIm1hYyI6IjBkZDUwZGIxNWQ5ZGFhNWExNDlmY2M0MjEwYmNhZjZmM2NiMGY1OGYzYzI1ZGQxOTI2MGQyYWE1MWUwYjU3MTUifQ%3D%3D");
//        getFromZwd.setHeader("Connection","keep-alive");
//        getFromZwd.setHeader("Upgrade-Insecure-Requests","1");
//        getFromZwd.setHeader("Hm_lpvt_d4ab127f10dee8544952f68326109a9a","1567055351");
//        getFromZwd.setHeader("Hm_lvt_cdb4a077a2e6221083922d763bdbfa18","1567055351");
//        getFromZwd.setHeader("Hm_lpvt_cdb4a077a2e6221083922d763bdbfa18","1567055351");

        System.out.println(Arrays.toString(getFromZwd.getAllHeaders()));
//        getFromZwd.setHeader("Cookie","stu_token=K2RDwcnO-0IBnr9WPXF7rbszDTtPvXrN-qA8; daifa_tip_shown=true; w_popover_shown=1; daifa_session=eyJpdiI6IlpWV1RxckJSK2JMSHo1dGh3bmxWaEE9PSIsInZhbHVlIjoiRndJa3J2a0ZPV2hjdjl1eGZOamx0bEt2SzlBY3p6Vk1ZMGxJZ0FadG9hdm5pcWdCNDdGOTIwdncyVDZic2hnVSIsIm1hYyI6ImZmNDBkZTRkOTVjNzA1Njk2MDcyOWJmNzAxZmI2NGM1NGNlNTIyYzFhNDUyZWQ2NDcwYjRlYTRjN2Y2YWE3NzAifQ%3D%3D; Hm_lvt_d4ab127f10dee8544952f68326109a9a=1567053949; Hm_lpvt_d4ab127f10dee8544952f68326109a9a=1567053949; Hm_lvt_cdb4a077a2e6221083922d763bdbfa18=1567053949; Hm_lpvt_cdb4a077a2e6221083922d763bdbfa18=1567053949; stu_crop_guide_shown=true");
//        getFromZwd.setHeader("Host","stu.17zwd.com");
        getFromZwd.setConfig(requestConfig);
        try (
                CloseableHttpResponse response17Zwd = closeableHttpClient.execute(getFromZwd)
        ) {
            if (response17Zwd.getStatusLine().getStatusCode() == 200) {
                String body = EntityUtils.toString(response17Zwd.getEntity());
                System.out.println(body);
                Document document = Jsoup.parse(body);
                document.head().getElementsByTag("script");
                for (Element e:document.head().getElementsByTag("script")
                ) {
                    String str = e.html().replace("\n", ""); //这里是为了解决 无法多行匹配的问题
//                String pattern = "var renderInfo = \\{(.*?)\\};"; //()必须加，
                    String pattern = "searchResult: \\{(.*?)\\};"; //()必须加，
                    Pattern r = Pattern.compile(pattern,Pattern.MULTILINE);// Pattern.MULTILINE 好像没有什么用，所以才使用上面的replace
                    Matcher m = r.matcher(str);
                    if(m.find()) {
                        String option = m.group();
//                    System.out.println(option_1.replace("searchResult: ",""));
                        String result = option.replace("searchResult: ","");
                        System.out.println("返回的result结果是："+result);
                        SearchResult root = JsonUtil.json2Obj(result, SearchResult.class);
                        if (null != root && root.getData() != null){
                            for (Same same :root.getData().getSame()
                            ) {
                                sameIdList.add(same.getGid());
                            }
                        }
                    }else {
                        System.out.println("找不到返回结果searchResult");
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //        如果同款的列表，不是空的，则通过id进行同款信息的查询
        if (sameIdList.size() > 0){
            StringBuilder gidParams = new StringBuilder();
            for (int id:sameIdList
            ) {
                gidParams.append(id);
                gidParams.append(",");
            }
            String url = "";
            if (gidParams.toString().length() > 0){
                url ="https://stu.17zwd.com/api/searchByGids?gids=" + gidParams.substring(0,gidParams.toString().length() - 1);
            }
//            stringBuilder.append()
            HttpGet httpGet = new HttpGet(url);
            try {
                CloseableHttpResponse responseByGid = closeableHttpClient.execute(httpGet);
                String body = EntityUtils.toString(responseByGid.getEntity());
                Document document = Jsoup.parse(body);
                System.out.println(document.body().html());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ZwdShopInfo().setShopName("成功了哈哈哈");
    }
    public List<ZwdShopInfo> getInfoByImgUrlWithWebDriver(String imgUrl){
        String zwdUrl = "https://stu.17zwd.com/SearchSimilar?img_url=" + imgUrl;
        List<ZwdShopInfo> zwdShopInfoList = null;
        webDriver.get(zwdUrl);
        WebDriverWait webDriverWait = new WebDriverWait(webDriver,1);
        webDriverWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("wrap-rs-item")));
        String body = webDriver.getPageSource();
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
        return zwdShopInfoList;
    }
}
