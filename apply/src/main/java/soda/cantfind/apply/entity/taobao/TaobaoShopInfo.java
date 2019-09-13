package soda.cantfind.apply.entity.taobao;

import lombok.Data;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @Describe
 * @Author soda
 * @Create 2019/9/2 9:47
 **/
@Data
public class TaobaoShopInfo {
    private BigDecimal goodsPrice;
    private String imgUrl;
}
