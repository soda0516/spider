package soda.cantfind.apply.entity.tusou;

import io.swagger.models.auth.In;
import lombok.Data;

import java.util.List;

/**
 * @Describe
 * @Author soda
 * @Create 2019/8/31 19:52
 **/
@Data
public class OriData {
    private boolean isSupportPangge;
    private String oriUrl;
    private Integer actionId;
    private boolean isSupportRank;
    private List<Identical> identicalList;
}
