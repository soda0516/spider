//package soda.cantfind.apply.util;
//
//import org.apache.http.Header;
//import org.apache.http.HttpClientConnection;
//import org.apache.http.HttpEntity;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.CookieStore;
//import org.apache.http.client.RedirectStrategy;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.client.params.ClientPNames;
//import org.apache.http.cookie.Cookie;
//import org.apache.http.entity.BasicHttpEntity;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.DefaultBHttpClientConnection;
//import org.apache.http.impl.client.BasicCookieStore;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.DefaultRedirectStrategy;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.impl.cookie.BasicClientCookie;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.protocol.HTTP;
//import org.apache.http.util.EntityUtils;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.firefox.FirefoxOptions;
//import org.springframework.web.client.RestTemplate;
//import soda.cantfind.apply.entity.zwd.stu.Same;
//import soda.cantfind.apply.entity.zwd.stu.SearchResult;
//import soda.cantfind.apply.entity.zwd.stu.ZwdStuRoot;
//import soda.module.core.web.util.JsonUtil;
//import sun.plugin2.os.windows.Windows;
//
//import java.io.UnsupportedEncodingException;
//import java.nio.charset.Charset;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.concurrent.TimeUnit;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
///**
// * @Describe
// * @Author soda
// * @Create 2019/8/28 9:51
// **/
//// TODO: 2019/8/28 这个只是测试，正式代码需要判断很多的东西的
//@Deprecated
//public class TestHttpClient {
//    public static void main(String[] args) {
////        FirefoxOptions options = new FirefoxOptions();
////        options.setHeadless(false);
////        WebDriver driver = new FirefoxDriver(options);
////        driver.get("https://stu.17zwd.com/SearchSimilar?img_url=https://gd1.alicdn.com/imgextra/i2/0/O1CN01tOB9lv1lmNmhXQWBg_!!0-item_pic.jpg");
////        CookieStore cookieStore = new BasicCookieStore();
////        for (org.openqa.selenium.Cookie c:driver.manage().getCookies()
////        ) {
////            Cookie cookie = new BasicClientCookie(c.getName(),c.getValue());
////            cookieStore.addCookie(cookie);
////        }
////        System.out.println("请求头的Header");
////        for (Cookie c:cookieStore.getCookies()
////             ) {
////            System.out.println(c.getName()+":"+c.getValue());
////        }
////        System.out.println("\n");
////        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
//        RequestConfig requestConfig = RequestConfig
//                .custom()
//                .setCircularRedirectsAllowed(true)
//                .setRedirectsEnabled(true)
//                .build();
//////        HttpGet httpGet = new HttpGet("https://item.taobao.com/item.htm?id=600327254888");
////        HttpGet httpGet = new HttpGet("https://stu.17zwd.com/SearchSimilar?img_url=https://gd1.alicdn.com/imgextra/i2/0/O1CN01tOB9lv1lmNmhXQWBg_!!0-item_pic.jpg");
//////      https://stu.17zwd.com/SearchSimilar?img_url=https://gd1.alicdn.com/imgextra/i2/0/O1CN01tOB9lv1lmNmhXQWBg_!!0-item_pic.jpg
//        CookieStore store = new BasicCookieStore();
////        store.addCookie(new BasicClientCookie("stu_token","TKbHBTEZ-YKpZbbxdLzaIzEn3T8XTsISiMho"));
////        store.addCookie(new BasicClientCookie("path","/"));
//        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().setDefaultCookieStore(store).build();
//
////        HttpGet httpGet1 = new HttpGet("https://stu.17zwd.com/SearchSimilar?img_url=https://gd1.alicdn.com/imgextra/i2/0/O1CN01tOB9lv1lmNmhXQWBg_!!0-item_pic.jpg");
////        HttpGet httpGet1 = new HttpGet("https://tusou.vvic.com/api/uploadImage?url=https:%2F%2Fgd1.alicdn.com%2Fimgextra%2Fi1%2F1654031624%2FO1CN01wuvACi1NrpkKvLTiX_!!1654031624.jpg&searchCity=gz");
//
//        HttpGet httpGet1 = new HttpGet("https://www.vvic.com/apic/loginUserInfo?user_key=&city=gz&cross=1&xy=2");
////        httpGet1.setHeader(" Cookie","acw_tc=71e5fc2115672298592308477eceb445aa3d42d305af77fbf5d3cd7e42; route=d736ac764983ff2b8247885a547d9fc9; SERVERID=397c8c6c407931d30f0d178e34342c89|1567229859|1567229859; vvic_token=26ac7780-16e4-4a03-9a49-04c4d0ee563e; _ga=GA1.2.1744376214.1567229859; _gid=GA1.2.398151838.1567229859; _gat=1");
////        请求 URL: https://tusou.vvic.com/list/d1b620a292fbcaa7d6f4a9ffd7364b51?searchCity=gz
//        httpGet1.setHeader("Accept","application/json, text/plain, */*");
//        httpGet1.setHeader("Accept-Encoding","gzip, deflate");
//        httpGet1.setHeader("Referer","https://tusou.vvic.com/list/d1b620a292fbcaa7d6f4a9ffd7364b51?searchCity=gz");
//        httpGet1.setHeader("Host","tusou.vvic.com");
//        httpGet1.setHeader("Content-Type","application/x-www-form-urlencoded");
//        httpGet1.setHeader("Origin","https://tusou.vvic.com");
////
////   Content-Type: application/x-www-form-urlencoded
////        Origin: https://tusou.vvic.com
////        Host: tusou.vvic.com
//        HttpPost httpPost = new HttpPost("https://tusou.vvic.com/api/searchImage");
////        Accept: application/json, text/plain, */*
//        httpPost.setHeader("Accept","application/json, text/plain, */*");
////        Accept-Encoding: gzip, deflate
////        httpPost.setHeader("Accept-Encoding","gzip, deflate, br");
////        Accept-Language: zh-Hans-CN, zh-Hans; q=0.5
//        httpPost.setHeader("Accept-Language","zh-CN,zh;q=0.9");
////        Cache-Control: no-cache
////        httpPost.setHeader("Cache-Control","no-cache");
////        Connection: Keep-Alive
////        httpPost.setHeader("Connection","Keep-Alive");
//        //        Content-Type: application/x-www-form-urlencoded
////        Content-Length: 101
////        httpPost.setHeader("Content-Length","101");
//        httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");
////        httpPost.setHeader("Cookie","acw_tc=76b20f6015671770202992025e16c16a42cf1ab71a74733e5cbd1930af97f3; _ga=GA1.2.392023443.1567177020; _gid=GA1.2.1886844029.1567177020; vvic_token=1ae5765d-307f-4009-b0e8-0e11c6695e35; _gat=1");
//        httpPost.setHeader("Host","tusou.vvic.com");
////        httpPost.setHeader("Referer","https://tusou.vvic.com/list/d1b620a292fbcaa7d6f4a9ffd7364b51?searchCity=gz");
//////        User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 10.0; WOW64; Trident/7.0; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.30729; .NET CLR 3.5.30729)
//        httpPost.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0");
////
//////        https://tusou.vvic.com
////        httpPost.setHeader("Origin","https://tusou.vvic.com");
////        httpPost.setHeader("Remote Address","112.124.157.87:443");
//
//
//        List<NameValuePair> nameValuePairs = new ArrayList<>();
//        nameValuePairs.add(new BasicNameValuePair("md5","d1b620a292fbcaa7d6f4a9ffd7364b51"));
//        nameValuePairs.add(new BasicNameValuePair("isTheft","0"));
//        nameValuePairs.add(new BasicNameValuePair("mergeSpam","0"));
//        nameValuePairs.add(new BasicNameValuePair("searchCity","gz"));
//        nameValuePairs.add(new BasicNameValuePair("sort","default"));
//        nameValuePairs.add(new BasicNameValuePair("img",""));
//        nameValuePairs.add(new BasicNameValuePair("strength","0"));
//
//        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, Charset.defaultCharset()));
////        httpGet1.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36");
//        httpGet1.setHeader("Cookie","Webstorm-9f32a31c=d9556838-5178-45ca-b8aa-2c9c9a5f17b9; Idea-7fc6de43=7cbc6b1a-ae31-4642-a006-5777ad9f3c0d; daifa_tip_shown=true; w_popover_shown=1; stu_crop_guide_shown=true; pnm_cku822=098%23E1hvcQvUvbpvUvCkvvvvvjiPRFdyljEVnLcpAjnEPmPy6jYPP2FhzjYRnLdW1j3WRphvCvvvvvvCvpvVvvpvvhCvKphv8vvvvvCvpvvvvvv2vhCvmh%2BvvvWvphvW9pvvvQCvpvs9vvv2vhCv2RmEvpvVmvvC9jaCuphvmvvv92473KV4mphvLv366QvjR%2BLw%2BnezrmphQRAn3feAOHjyTWexRdIAcUmxfwoOd5lwQbmDYCI4JZKQ0f0DW3CQog0HsXZpVcC8AXcBlLyzOvxr1WCl5dUPvpvhvv2MMTwCvvpvvUmm; Hm_lvt_cdb4a077a2e6221083922d763bdbfa18=1566960057,1566968857,1567070331,1567070354; Hm_lvt_d4ab127f10dee8544952f68326109a9a=1566960057,1566968857,1567070331,1567070354; _ga=GA1.1.1559170518.1567177272; _gid=GA1.1.1007670381.1567177272; sidebarStatus=0; vue_admin_template_token=ceshitoken");
////        httpGet1.setHeader("Referer","https://tusou.vvic.com/list/d1b620a292fbcaa7d6f4a9ffd7364b51?searchCity=gz");
////        httpGet1.setHeader("Upgrade-Insecure-Requests","1");
////        httpGet1.setHeader("Referrer Policy","no-referrer-when-downgrade");
//
//        httpGet1.setConfig(requestConfig);
////        HttpGet httpGet1 = new HttpGet("https://stu.17zwd.com/api/searchByGids?gids=111720921");
//        try (
//                CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost)
//        ) {
//            for (Header h:closeableHttpResponse.getAllHeaders()
//                 ) {
//                System.out.println(h.toString());
//            }
////            System.out.println(closeableHttpResponse.getAllHeaders().toString());
//            String body = EntityUtils.toString(closeableHttpResponse.getEntity());
////            System.out.println("返回的结果是"+body);
//            System.out.println(body);
////            Document document = Jsoup.parse(body);
////            document.head().getElementsByTag("script");
////            for (Element e:document.head().getElementsByTag("script")
////                 ) {
////                String str = e.html().replace("\n", ""); //这里是为了解决 无法多行匹配的问题
//////                String pattern = "var renderInfo = \\{(.*?)\\};"; //()必须加，
////                String pattern = "searchResult: \\{(.*?)\\};"; //()必须加，
////                Pattern r = Pattern.compile(pattern,Pattern.MULTILINE);// Pattern.MULTILINE 好像没有什么用，所以才使用上面的replace
////                Matcher m = r.matcher(str);
////                if(m.find())
////                {
////                    String option_1 = m.group();
//////                    System.out.println(option_1.replace("searchResult: ",""));
////                    String result = option_1.replace("searchResult: ","");
////                    SearchResult root = JsonUtil.json2Obj(result, SearchResult.class);
//////                    System.out.println(option_1);
////                    for (Same same :root.getData().getSame()
////                         ) {
////                        System.out.println(same.getGid());
////                    }
////                }
////            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
////        httpGet.setConfig(requestConfig);
////        try (
////                CloseableHttpResponse response = httpClient.execute(httpGet)
////        ) {
////            HttpEntity entity = response.getEntity();
////            for (Header h:response.getAllHeaders()
////                 ) {
//////                System.out.println(h.toString());
////            }
////            String taobaoBody = EntityUtils.toString(entity);
//////            Document documentTaobao = Jsoup.parse(taobaoBody);
//////            Elements elements = documentTaobao.body().getElementById("J_UlThumb").getElementsByTag("img");
//////            if (elements.isEmpty()){
//////            }
//////            String imgUrl = "https:" + elements.get(0).attr("src").split(".jpg_")[0] + ".jpg";
//////            System.out.println(imgUrl);
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//    }
//}
