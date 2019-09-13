package soda.cantfind.apply.model;

import lombok.Data;

import java.util.List;

/**
 * @Describe
 * @Author soda
 * @Create 2019/8/25 14:23
 **/
@Data
public class ZwdDocInfo {
    private String fileName;
    private Integer urlCount;
    private List<String> urlList;
}
