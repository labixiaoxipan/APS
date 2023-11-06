package com.benewake.system.service.scheduling.result.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.benewake.system.entity.ApsMaterialShortageAnalysis;
import com.benewake.system.entity.ApsSemiFinishedGoodsProductionPlan;
import com.benewake.system.entity.enums.SchedulingResultType;
import com.benewake.system.entity.vo.PageListRestVo;
import com.benewake.system.service.ApsTableVersionService;
import com.benewake.system.service.scheduling.result.ApsMaterialShortageAnalysisService;
import com.benewake.system.mapper.ApsMaterialShortageAnalysisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author ASUS
* @description 针对表【aps_material_shortage_analysis】的数据库操作Service实现
* @createDate 2023-11-02 11:39:08
*/
@Service
public class ApsMaterialShortageAnalysisServiceImpl extends ServiceImpl<ApsMaterialShortageAnalysisMapper, ApsMaterialShortageAnalysis>
    implements ApsMaterialShortageAnalysisService{

    @Autowired
    private ApsTableVersionService apsTableVersionService;

    @Override
    public PageListRestVo<ApsMaterialShortageAnalysis> getAllPage(Integer page, Integer size) {
        Integer apsTableVersion = getApsTableVersion(SchedulingResultType.APS_MATERIAL_SHORTAGE_ANALYSIS.getCode(), apsTableVersionService);
        Page<ApsMaterialShortageAnalysis> pageTemp = new Page<>();
        pageTemp.setSize(size);
        pageTemp.setCurrent(page);
        LambdaQueryWrapper<ApsMaterialShortageAnalysis> productionPlanLambdaQueryWrapper = new LambdaQueryWrapper<>();
        productionPlanLambdaQueryWrapper.eq(ApsMaterialShortageAnalysis::getVersion ,apsTableVersion);
        Page<ApsMaterialShortageAnalysis> productionPlanPage = page(pageTemp, productionPlanLambdaQueryWrapper);
        PageListRestVo<ApsMaterialShortageAnalysis> restVo = new PageListRestVo<>();
        restVo.setList(productionPlanPage.getRecords());
        restVo.setPages(productionPlanPage.getPages());
        restVo.setTotal(productionPlanPage.getTotal());
        restVo.setPage(page);
        restVo.setSize(size);
        return restVo;
    }
}




