package soda.cantfind.apply.model.spider;

import lombok.Data;

/**
 * @Describe
 * @Author soda
 * @Create 2019/8/31 18:43
 **/
@Data
public class TuSouUploadResultInfo {
    private int code;
    private boolean result;
    private String message;
    private String data;
    private String md5;
    private String appMd5;
}
