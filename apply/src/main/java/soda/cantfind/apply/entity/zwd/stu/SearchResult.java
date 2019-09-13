package soda.cantfind.apply.entity.zwd.stu;

import java.util.List;

/**
 * @Describe
 * @Author soda
 * @Create 2019/8/28 17:38
 **/
@lombok.Data
public class SearchResult {
    private Data data;

    private List<ContainedProducts> containedProducts;
}
