package soda.cantfind.apply.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import soda.cantfind.apply.constant.SpiderSourceWebSite;
import soda.cantfind.apply.constant.TuSouTaskStateConstant;
import soda.cantfind.apply.entity.taobao.TaobaoShopInfo;
import soda.cantfind.apply.entity.tusou.Identical;
import soda.cantfind.apply.entity.tusou.TuSouResult;
import soda.cantfind.apply.entity.tusou.TusouSpiderTask;
import soda.cantfind.apply.entity.tusou.TusouSpiderTaskResult;
import soda.cantfind.apply.rabbitmq.TuSouProducer;
import soda.cantfind.apply.service.ISpiderService;
import soda.cantfind.apply.service.ITusouSpiderTaskResultService;
import soda.cantfind.apply.service.ITusouSpiderTaskService;
import soda.cantfind.apply.util.spider.SpiderTaobaoUtil;
import soda.module.core.web.exception.BusinessException;
import soda.module.core.web.response.ResponseBuilder;
import soda.module.core.web.response.ResponseModel;
import soda.module.core.web.util.JsonUtil;
import soda.module.user.model.JwtUser;
import soda.module.user.util.SecurityContextUtil;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Describe
 * @Author soda
 * @Create 2019/8/31 20:12
 **/
@Slf4j
@RestController
@RequestMapping("/spider")
public class SpiderController {
    @Resource(name = SpiderSourceWebSite.TUSOU_VVIC)
    ISpiderService iSpiderService;
    private final ITusouSpiderTaskService iTusouSpiderTaskService;
    private final ITusouSpiderTaskResultService iTusouSpiderTaskResultService;

    SpiderController( ITusouSpiderTaskService iTusouSpiderTaskService,
                      TuSouProducer tuSouProducer,
                      ITusouSpiderTaskResultService iTusouSpiderTaskResultService){
        this.iTusouSpiderTaskService = iTusouSpiderTaskService;
        this.iTusouSpiderTaskResultService = iTusouSpiderTaskResultService;
    }
    @GetMapping("/tusou")
    public ResponseModel getInfoFromTaobaoShopUrl(@RequestParam("address") String address){
        TuSouResult tuSouResult = new TuSouResult();
        CloseableHttpClient client = HttpClientBuilder.create().build();
        SpiderTaobaoUtil spiderTaobaoUtil = new SpiderTaobaoUtil(client);
        TaobaoShopInfo imageUrl = spiderTaobaoUtil.getShopInfoByUrl(address);
        String md5 = iSpiderService.getStringFromUrl(imageUrl.getImgUrl());
        tuSouResult = JsonUtil.json2Obj(iSpiderService.getJsonFromUrl(md5), TuSouResult.class);
        if (null != tuSouResult && null != tuSouResult.getData()){
            List<Identical> identicals = tuSouResult.getData().getIdenticalList()
                    .stream()
                    .filter(item -> item.getIs_df().equals(1))
                    .filter(item -> item.getIs_sp().equals(1))
                    .filter(item -> item.getIs_tx().equals(1))
                    .collect(Collectors.toList());
            tuSouResult.getData().setIdenticalList(identicals);
        }
        return ResponseBuilder.success(tuSouResult);
    }
    @GetMapping("/tusou/task/list")
    public ResponseModel getTaskList(){
        JwtUser jwtUser = SecurityContextUtil.getJwtUserFromContext();
        return ResponseBuilder.success(iTusouSpiderTaskService.lambdaQuery()
                .eq(TusouSpiderTask::getUserId,jwtUser.getId())
                .list()
                .stream()
                .sorted(Comparator.comparing(TusouSpiderTask::getCreateTime).reversed()));
    }
    @GetMapping("/tusou/task/list/page")
    public ResponseModel getTaskListPage(@RequestParam(value = "current") Integer current,@RequestParam(value = "pages") Integer pages){
        JwtUser jwtUser = SecurityContextUtil.getJwtUserFromContext();
        //分页设置
        IPage<TusouSpiderTask> taskIPage = new Page<>();
        taskIPage.setCurrent(current);
        taskIPage.setSize(pages);
        //从数据库里，直接拿出来倒叙
        LambdaQueryWrapper<TusouSpiderTask> taskLambdaQueryWrapper = new LambdaQueryWrapper<>();
        taskLambdaQueryWrapper.eq(TusouSpiderTask::getUserId,jwtUser.getId()).orderByDesc(TusouSpiderTask::getCreateTime);
        return ResponseBuilder.success(iTusouSpiderTaskService.page(taskIPage,taskLambdaQueryWrapper));
    }
    @GetMapping("/tusou/task/recent")
    public ResponseModel getRecentTask(){
        JwtUser jwtUser = SecurityContextUtil.getJwtUserFromContext();
        Optional<TusouSpiderTask> task =iTusouSpiderTaskService.lambdaQuery()
                .eq(TusouSpiderTask::getUserId,jwtUser.getId())
                .ne(TusouSpiderTask::getTaskState , TuSouTaskStateConstant.ready)
                .list()
                .stream()
                .max(Comparator.comparing(TusouSpiderTask::getCreateTime));
        if (task.isPresent()){
            return ResponseBuilder.success(task.get());
        }
        return ResponseBuilder.success();
    }
    @GetMapping("/tusou/task/result")
    public ResponseModel getTaskResult(@RequestParam(value ="task",required = false) String content,@RequestParam(value = "current",required = false) Integer current,@RequestParam(value = "pages",required = false) Integer pages){
        TusouSpiderTask task = JsonUtil.json2Obj(content,TusouSpiderTask.class);
        IPage<TusouSpiderTaskResult> iPage = new Page<>();
        QueryWrapper<TusouSpiderTaskResult> queryWrapper = new QueryWrapper<>();
        if (null != task && null != task.getId() && task.getTaskState().equals(TuSouTaskStateConstant.complete)){
            iPage.setCurrent(null == current?0:current);
            iPage.setSize(null == pages?20:pages);
            queryWrapper.lambda().eq(TusouSpiderTaskResult::getTaskId,task.getId());
//            System.out.println("/tusou/task/result"+JsonUtil.obj2Json(iTusouSpiderTaskResultService.page(iPage,queryWrapper)));
        } else {
            queryWrapper.lambda().eq(TusouSpiderTaskResult::getTaskId,0);
        }
        return ResponseBuilder.success(iTusouSpiderTaskResultService.page(iPage,queryWrapper));
    }
    /**
     * 通过一个上传的文档，立即开启一个任务
     * @param file
     * @return
     */
    @PostMapping("tusou/task/active")
    public ResponseModel activeOneTask(@RequestParam("file") MultipartFile file){
        if (null == file){
            throw new BusinessException("没有检查到文档，请选择一个上传文件");
        }
        TusouSpiderTask task = SpiderTaobaoUtil.uploadTxtDoc(file);
        return ResponseBuilder.success(iSpiderService.activateOneTask(task));
    }
    @PostMapping("/tusou/task/download")
    public ResponseEntity index(@RequestParam("task") String content){
        return iSpiderService.downloadExcel(content);
    }
    @DeleteMapping("/tusou/task")
    public ResponseModel deleteTask(@RequestParam("task") String content){
        TusouSpiderTask task = JsonUtil.json2Obj(content,TusouSpiderTask.class);
        if (null != task && null != task.getId()){
            if (iTusouSpiderTaskService.removeById(task.getId())){
                File file = new File("c:/"+ task.getFileName());
                file.deleteOnExit();
                return ResponseBuilder.success();
            }
        }
        return ResponseBuilder.warning("删除对应任务失败！");
    }
 }
