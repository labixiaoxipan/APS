package com.benewake.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.benewake.system.entity.ApsInventoryLock;
import com.benewake.system.entity.kingdee.KingdeeInventoryLock;
import com.benewake.system.entity.kingdee.transfer.MaterialIdToName;
import com.benewake.system.service.ApsInventoryLockService;
import com.benewake.system.mapper.ApsInventoryLockMapper;
import com.benewake.system.service.ApsTableVersionService;
import com.benewake.system.transfer.KingdeeToApsInventoryLock;
import com.kingdee.bos.webapi.entity.QueryParam;
import com.kingdee.bos.webapi.sdk.K3CloudApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author ASUS
* @description 针对表【aps_inventory_lock】的数据库操作Service实现
* @createDate 2023-10-08 11:06:53
*/
@Service
public class ApsInventoryLockServiceImpl extends ServiceImpl<ApsInventoryLockMapper, ApsInventoryLock>
    implements ApsInventoryLockService{

    @Autowired
    private K3CloudApi api;


    @Autowired
    private KingdeeToApsInventoryLock kingdeeToApsInventoryLock;

    @Override
    public Boolean updateDataVersions() throws Exception {
        List<KingdeeInventoryLock> result = getKingdeeInventoryLockList();
        // 物料映射表
        Map<String, String> materialIdToNameMap = getMaterialIdToNameMap();
        ArrayList<ApsInventoryLock> apsInventoryLockList = getApsInventoryLockList(result, materialIdToNameMap);
        return saveBatch(apsInventoryLockList);
    }

    private ArrayList<ApsInventoryLock> getApsInventoryLockList(List<KingdeeInventoryLock> result, Map<String, String> materialIdToNameMap) {
        ArrayList<ApsInventoryLock> apsInventoryLockList = new ArrayList<>();
//        Integer maxVersion = apsTableVersionService.getMaxVersion();
        for (KingdeeInventoryLock kingdeeInventoryLock : result) {
            // 获取 FDocumentStatus 的 id
            kingdeeInventoryLock.setFMaterialId(materialIdToNameMap.get(kingdeeInventoryLock.getFMaterialId()));
            ApsInventoryLock apsInventoryLock = kingdeeToApsInventoryLock.convert(kingdeeInventoryLock ,InterfaceDataServiceImpl.maxVersion);
            apsInventoryLockList.add(apsInventoryLock);
        }
        return apsInventoryLockList;
    }

    private Map<String, String> getMaterialIdToNameMap() throws Exception {
        QueryParam queryParam;
        queryParam = new QueryParam();
        queryParam.setFormId("BD_MATERIAL");
        queryParam.setFieldKeys("FMaterialId,FNumber");
        List<MaterialIdToName> midToNameList = api.executeBillQuery(queryParam, MaterialIdToName.class);
        Map<String, String> materialIdToNameMap = new HashMap<>();
        midToNameList.forEach(materialIdToName -> {
            materialIdToNameMap.put(materialIdToName.getFMaterialId(), materialIdToName.getFNumber());
        });
        return materialIdToNameMap;
    }

    private List<KingdeeInventoryLock> getKingdeeInventoryLockList() throws Exception {
        QueryParam queryParam = new QueryParam();
        queryParam.setFormId("STK_LockStock");
        queryParam.setFieldKeys("FMaterialId,FEXPIRYDATE,FLockQty");
        List<String> queryFilters = new ArrayList<>();
        queryFilters.add("FStockOrgId = 1");
        queryParam.setFilterString(String.join(" and ", queryFilters));
        List<KingdeeInventoryLock> result = api.executeBillQuery(queryParam, KingdeeInventoryLock.class);
        return result;
    }
}




