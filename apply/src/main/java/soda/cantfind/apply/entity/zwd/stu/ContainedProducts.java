package soda.cantfind.apply.entity.zwd.stu;

import lombok.Data;

import java.util.List;

/**
 * @Describe
 * @Author soda
 * @Create 2019/8/28 17:37
 **/
@Data
public class ContainedProducts {
    private String type;
    private List<String> attributes;
    private double score;
    private List<Integer> box;
}
