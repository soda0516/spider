package soda.cantfind.apply.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author soda
 * @since 2019-08-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ZwdShopInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("goods_name")
    private String goodsName;

    @TableField("shop_url")
    private String shopUrl;

    @TableField("goods_price")
    private BigDecimal goodsPrice;

    @TableField("on_sale_time")
    private String onSaleTime;

    @TableField("shop_address")
    private String shopAddress;

    @TableField("shop_name")
    private String shopName;


}
