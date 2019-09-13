package soda.cantfind.apply.util;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.Random;

/**
 * @Describe
 * @Author soda
 * @Create 2019/9/2 7:45
 **/
public class TestTest {
    public static void main(String[] args) {
        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet("https://s.taobao.com/search?q=实拍2019新款港味chic纯色衬衫+气质V领针织毛衣马甲时尚两件套女&imgfile=&commend=all&ssid=s5-e&search_type=item&sourceId=tb.index&spm=a21bo.2017.201856-taobao-item.1&ie=utf8&initiative_id=tbindexz_20170306");
        try (
                CloseableHttpResponse response = closeableHttpClient.execute(get)
        ) {
            System.out.println(EntityUtils.toString(response.getEntity()));
//            Document document = Jsoup.parse(EntityUtils.toString(response.getEntity(), "GBK"));
//            Element element = document.body().getElementsByClass("main_cont").get(0);
//            Elements elementsP = element.getElementsByTag("p");
//            File file = new File("d:/texttext.txt");
//            try (
//                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))
//            ) {
//                for (Element e:elementsP
//                ) {
//                    bufferedWriter.write(e.text());
//                    bufferedWriter.newLine();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
