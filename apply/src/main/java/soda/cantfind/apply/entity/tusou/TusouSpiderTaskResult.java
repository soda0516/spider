package soda.cantfind.apply.entity.tusou;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author soda
 * @since 2019-09-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TusouSpiderTaskResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("task_id")
    private Integer taskId;

    @TableField("ori_goods_url")
    private String oriGoodsUrl;

    @TableField("ori_goods_price")
    private BigDecimal oriGoodsPrice;

    @TableField("same_style")
    private Boolean sameStyle;

    @TableField("three_tag")
    private Boolean threeTag;

    @TableField("price_range")
    private String priceRange;

    @TableField("price_differences_range")
    private String priceDifferencesRange;

    @TableField("same_page_url")
    private String samePageUrl;

    @TableField("status")
    private boolean status;


}
