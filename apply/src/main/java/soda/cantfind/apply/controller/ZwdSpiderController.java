package soda.cantfind.apply.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import soda.cantfind.apply.entity.ZwdShopInfo;
import soda.cantfind.apply.entity.ZwdUrlList;
import soda.cantfind.apply.model.ZwdDocInfo;
import soda.cantfind.apply.service.IZwdUrlListService;
import soda.cantfind.apply.util.SpideUtil;
import soda.cantfind.apply.util.TxtReaderUtil;
import soda.cantfind.apply.util.spider.SpiderTaobaoUtil;
import soda.cantfind.apply.util.spider.SpiderZwdUtil;
import soda.module.core.web.response.ResponseBuilder;
import soda.module.core.web.response.ResponseModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Describe
 * @Author soda
 * @Create 2019/8/24 18:50
 **/
@RestController
@RequestMapping("/spider")
public class ZwdSpiderController {

    private final IZwdUrlListService zwdUrlListService;

    ZwdSpiderController(IZwdUrlListService zwdUrlListService){
        this.zwdUrlListService = zwdUrlListService;

    }
    @PostMapping("/file")
    public ResponseModel spider(@RequestParam(value = "file",required = false) MultipartFile file){
        if (null == file || file.isEmpty()){
            return ResponseBuilder.failure("服务器接收不到文件！");
        }
        String fileName = file.getOriginalFilename();
        String filePath = "c:/taobao/";
        File dest = new File(filePath + fileName);
        if (dest.exists()){
            dest.deleteOnExit();
        }
        if (!dest.getParentFile().exists()){
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> list = TxtReaderUtil.readTaobaoUrl(dest.getPath());

        List<ZwdUrlList> zwdUrlLists = new ArrayList<>();

        list.forEach( v -> {
            ZwdUrlList zwdUrlList = new ZwdUrlList();
            zwdUrlList.setFileName(fileName);
            zwdUrlList.setUrlName(v);
            zwdUrlLists.add(zwdUrlList);
        });
        if (zwdUrlListService.saveBatch(zwdUrlLists)) {
            ZwdDocInfo zwdDocInfo = new ZwdDocInfo();
            zwdDocInfo.setFileName(fileName);
            zwdDocInfo.setUrlCount(list.size());
            return ResponseBuilder.success(zwdDocInfo);
        }else {
            return ResponseBuilder.failure();
        }
    }
    @GetMapping("/zwd")
    public ResponseModel getInfoFromZwd(@RequestParam(value = "fileName") String fileName){
        if (StringUtils.isBlank(fileName)){
            return ResponseBuilder.failure("传入的文件名参数是空的！");
        }
        LambdaQueryWrapper<ZwdUrlList> zwdUrlListLambdaQueryWrapper = new LambdaQueryWrapper<>();
        zwdUrlListLambdaQueryWrapper.eq(ZwdUrlList::getFileName,fileName);
        IPage<ZwdUrlList> page = new Page<>(0,20);
        List<ZwdUrlList> lists = zwdUrlListService.page(page,zwdUrlListLambdaQueryWrapper).getRecords();
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);
        // TODO: 2019/8/24 目前虽然传过来的是列表，但是只读取列表里的第一个数据，用来测试
        List<ZwdShopInfo> zwdShopInfo = SpideUtil.getInfoFromTaobaoUrl(lists.get(9).getUrlName());
        System.out.println(zwdShopInfo);
        if (null == zwdShopInfo){
            return ResponseBuilder.failure();
        }else {
            return ResponseBuilder.success(zwdShopInfo);
        }
    }
}
