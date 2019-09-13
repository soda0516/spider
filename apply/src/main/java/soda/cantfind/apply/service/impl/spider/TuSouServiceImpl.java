package soda.cantfind.apply.service.impl.spider;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.tomcat.util.digester.DocumentProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import soda.cantfind.apply.constant.SpiderBaseUrlConstant;
import soda.cantfind.apply.constant.SpiderSourceWebSite;
import soda.cantfind.apply.constant.TuSouTaskStateConstant;
import soda.cantfind.apply.entity.tusou.TusouSpiderTask;
import soda.cantfind.apply.entity.tusou.TusouSpiderTaskResult;
import soda.cantfind.apply.model.spider.TuSouUploadResultInfo;
import soda.cantfind.apply.rabbitmq.TuSouProducer;
import soda.cantfind.apply.service.ISpiderService;
import soda.cantfind.apply.service.ITusouSpiderTaskResultService;
import soda.cantfind.apply.service.ITusouSpiderTaskService;
import soda.cantfind.apply.util.spider.SpiderTaobaoUtil;
import soda.module.core.web.exception.BusinessException;
import soda.module.core.web.response.ResponseBuilder;
import soda.module.core.web.util.JsonUtil;
import soda.module.user.model.JwtUser;
import soda.module.user.util.SecurityContextUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @Describe 搜款网图搜 https://tusou.vvic.com
 * @Author soda
 * @Create 2019/8/31 16:49
 **/
@Slf4j
@Service(value = SpiderSourceWebSite.TUSOU_VVIC)
public class TuSouServiceImpl implements ISpiderService{
    private final ITusouSpiderTaskService iTusouSpiderTaskService;
    private final TuSouProducer tuSouProducer;
    private final ITusouSpiderTaskResultService iTusouSpiderTaskResultService;
    private static CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
    private static RequestConfig requestConfig;
    static {
        requestConfig = RequestConfig
                .custom()
                .setSocketTimeout(3000)
                .build();
    }
    public TuSouServiceImpl(ITusouSpiderTaskService iTusouSpiderTaskService,
                            TuSouProducer tuSouProducer,
                            ITusouSpiderTaskResultService iTusouSpiderTaskResultService){
        this.iTusouSpiderTaskService = iTusouSpiderTaskService;
        this.tuSouProducer = tuSouProducer;
        this.iTusouSpiderTaskResultService = iTusouSpiderTaskResultService;
    }
    /**
     * 从淘宝店铺的连接中，获取一个
     * @param md5
     * @return
     */
    @Override
    public String getJsonFromUrl(String md5) {
//        如果md5的值，不是空的，则通过md5的值，从图搜的api里面获取自己需要的json信息
        if (!StringUtils.isBlank(md5)){
            HttpPost httpPost = new HttpPost(SpiderBaseUrlConstant.TUSOU_VVIC_API_SEARCHIMAGE);
            httpPost.setHeader("Accept","application/json, text/plain, */*");
            httpPost.setHeader("Accept-Language","zh-CN,zh;q=0.9");
            httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");
            httpPost.setHeader("Host","tusou.vvic.com");
            httpPost.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0");
//            设置请求参数
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("md5",md5));
            nameValuePairs.add(new BasicNameValuePair("isTheft","0"));
            nameValuePairs.add(new BasicNameValuePair("mergeSpam","0"));
            nameValuePairs.add(new BasicNameValuePair("searchCity","gz"));
            nameValuePairs.add(new BasicNameValuePair("sort","default"));
            nameValuePairs.add(new BasicNameValuePair("img",""));
            nameValuePairs.add(new BasicNameValuePair("strength","0"));
            httpPost.setConfig(requestConfig);
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, CharEncoding.UTF_8));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try (
                    CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost)
            ) {
                if (closeableHttpResponse.getStatusLine().getStatusCode() == 200) {
//                    返回一个正确的json结果
                    return EntityUtils.toString( closeableHttpResponse.getEntity());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    @Override
    public String getStringFromUrl(String url) {
        //        https://tusou.vvic.com/api/uploadImage?url=https:%2F%2Fgd1.alicdn.com%2Fimgextra%2Fi1%2F1654031624%2FO1CN01wuvACi1NrpkKvLTiX_!!1654031624.jpg&searchCity=gz
        String uploadUrl = SpiderBaseUrlConstant.TUSOU_VVIC_UPLOAD_URL + url;
        HttpGet uploadHttpGet = new HttpGet(uploadUrl);
        String md5 = "";
        uploadHttpGet.setConfig(requestConfig);
        try (
                CloseableHttpResponse uploadResponse = closeableHttpClient.execute(uploadHttpGet)
        ) {
//            首先上传图片的连接，返回一个md5值
            if (uploadResponse.getStatusLine().getStatusCode() == 200) {
                String uploadJson = EntityUtils.toString(uploadResponse.getEntity());
                TuSouUploadResultInfo uploadResultInfo = JsonUtil.json2Obj(uploadJson, TuSouUploadResultInfo.class);
                if (null != uploadResultInfo && uploadResultInfo.getCode() == 200){
                    md5 = uploadResultInfo.getAppMd5();
                }
            }
        } catch (SocketTimeoutException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5;
    }
    @Override
    public TusouSpiderTask activateOneTask(TusouSpiderTask task) {
        if (null == task){
            throw new BusinessException("传入任务对象为空，启动任务失败，请尝试重新开启任务！");
        }
        int count = iTusouSpiderTaskService.lambdaQuery()
                .eq(TusouSpiderTask::getTaskState, TuSouTaskStateConstant.loading)
                .eq(TusouSpiderTask::getUserId,task.getUserId())
                .count();
        if (count > 0){
            throw new BusinessException("当前用户已经有一个正在采集的任务，不能重复开启！");
        }
//        System.out.println(content);
        task.setTaskState(TuSouTaskStateConstant.loading);
        if (iTusouSpiderTaskService.saveOrUpdate(task)){
            tuSouProducer.sendMsg(task);
        } else {
            throw new BusinessException("任务写入输入库过程中，出现异常！");
        }
        return task;
    }

    @Override
    public ResponseEntity downloadExcel(String content) {
        TusouSpiderTask task = JsonUtil.json2Obj(content,TusouSpiderTask.class);
        if (null !=task && null != task.getId()){
            if (!task.getTaskState().equals(TuSouTaskStateConstant.complete)){
                return ResponseEntity.notFound().build();
            }
            List<TusouSpiderTaskResult> list = iTusouSpiderTaskResultService.lambdaQuery().eq(TusouSpiderTaskResult::getTaskId,task.getId()).list();
            String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHSSMM"))+".xls";
            File file = new File("c:/taobao/"+fileName);
            try (
                    HSSFWorkbook hssfWorkbook = new HSSFWorkbook()
            ) {
                hssfWorkbook.createSheet();
                HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
                HSSFRow firstRow = sheet.createRow(0);
                firstRow.createCell(0).setCellValue("原商品链接");
                firstRow.createCell(1).setCellValue("是否存在同款");
                firstRow.createCell(2).setCellValue("判断代发、实拍、退款标签");
                firstRow.createCell(3).setCellValue("淘宝价格");
                firstRow.createCell(4).setCellValue("价格区间");
                firstRow.createCell(5).setCellValue("差价");
                firstRow.createCell(6).setCellValue("同款页面链接");
                for (int i = 0; i < list.size(); i++) {
                    TusouSpiderTaskResult result = list.get(i);
                    Row row = sheet.createRow(i+1);
                    if (null != result.getOriGoodsUrl()){
                        row.createCell(0).setCellValue(result.getOriGoodsUrl());
                    }
                    if (null != result.getSameStyle()){
                        row.createCell(1).setCellValue(result.getSameStyle()?"是":"否");
                    }
                    if (null != result.getThreeTag()){
                        row.createCell(2).setCellValue(result.getThreeTag()?"是":"否");
                    }
                    if (null != result.getOriGoodsPrice()){
                        row.createCell(3).setCellValue(result.getOriGoodsPrice().toString());
                    }
                    if (null != result.getPriceDifferencesRange()){
                        row.createCell(4).setCellValue(result.getPriceRange());
                    }
                    if (null != result.getSamePageUrl()){
                        row.createCell(5).setCellValue(result.getPriceDifferencesRange());
                    }
                    if (null != result.getSamePageUrl()){
                        row.createCell(6).setCellValue(result.getSamePageUrl());
                    }
                }
                hssfWorkbook.write(file);

            } catch (FileNotFoundException e) {
                throw new BusinessException("下载文档在服务器上不存在");
            } catch (IOException e){
                throw new RuntimeException(e);
            }
            ResourceLoader resource = new FileSystemResourceLoader();
            try {
                InputStreamResource inputStreamResource = new InputStreamResource(resource.getResource(file.getAbsolutePath()).getInputStream());
                return ResponseEntity
                        .ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName)
                        .body(inputStreamResource);
            } catch (IOException e) {
                log.error(e.toString());
                e.printStackTrace();
            }
        }
        return ResponseEntity.notFound().build();
    }
}
