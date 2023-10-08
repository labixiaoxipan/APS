package com.benewake.system.entity.kingdee;

import lombok.Data;
/*
 * 生产订单列表数据类型*/
@Data
public class KingdeeProductionOrder {
    //("单据编号")
    private String FBillNo;

    //("单据类型")
    private String FBillType;
    //("单据类型")
    private String FBILLTYPEID;


    //("物料编码")
    private String FMaterialId;

    //("数量")
    private String FQty;

    //("业务状态")
    private String FStatus;

    //("领料状态")
    private String FPickMtrlStatus;

    //("合格品入库数量")
    private String FStockInQuaAuxQty;

    //("BOM版本")
    private String FBomId;


    //("生产车间ID")
    private String FWorkShopID;





}
