package com.benewake.system.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName aps_fim_priority
 */
@TableName(value ="aps_fim_priority")
@Data
public class ApsFimPriorityDto implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    @TableField(value = "f_production_id")
    private String fProductionId;

    /**
     * 
     */
    @TableField(value = "f_task_source_id")
    private String fTaskSourceId;

    /**
     * 
     */
    @TableField(value = "f_material_code")
    private String fMaterialCode;

    /**
     * 
     */
    @TableField(value = "f_material_name")
    private String fMaterialName;

    /**
     * 
     */
    @TableField(value = "f_replenishment_quantity")
    private String fReplenishmentQuantity;

    /**
     * 
     */
    @TableField(value = "f_expected_start_time")
    private String fExpectedStartTime;

    /**
     * 
     */
    @TableField(value = "f_required_delivery_time")
    private String fRequiredDeliveryTime;

    /**
     * 
     */
    @TableField(value = "f_priority")
    private String fPriority;

    /**
     * 
     */
    @TableField(value = "f_contained_sales_orders")
    private String fContainedSalesOrders;



    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}