package com.benewake.system.service.mes.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.benewake.system.entity.ApsPcbaVersion;
import com.benewake.system.entity.mes.MesPcbaVersion;
import com.benewake.system.service.mes.ApsPcbaVersionService;
import com.benewake.system.mapper.ApsPcbaVersionMapper;
import com.benewake.system.transfer.MesToApsPcbaVersion;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author ASUS
* @description 针对表【aps_pcba_version】的数据库操作Service实现
* @createDate 2023-10-19 10:16:37
*/
@Service
public class ApsPcbaVersionServiceImpl extends ServiceImpl<ApsPcbaVersionMapper, ApsPcbaVersion>
    implements ApsPcbaVersionService{

    @Autowired
    private MesToApsPcbaVersion mesToApsPcbaVersion;
    @Override
    public Boolean updateDataVersions() throws Exception {
        Integer maxVersionIncr = this.getMaxVersionIncr();
        String baseUrl = "http://ql.benewake.com//openApi"; // 替换为实际的API URL
        String accessToken = "2633eb1b-1348-4a8e-bda2-e66540e1d8b7"; // 替换为实际的accessToken
        String app = "e51262a3";
        // 创建请求体 JSON 字符串
        String requestBody = "{\"pageSize\": 100, \"pageNum\": 1}";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 创建POST请求
            HttpPost httpPost = new HttpPost(baseUrl + "/app/" + app + "/apply/filter");
            // 设置请求头
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("accessToken", accessToken);
            // 设置请求体
            httpPost.setEntity(new StringEntity(requestBody, ContentType.APPLICATION_JSON));
            // 发送请求并获取响应
            CloseableHttpResponse response = httpClient.execute(httpPost);
            // 解析响应内容
            ArrayList<ApsPcbaVersion> apsPcbaVersions = null;
            if (response.getEntity() != null) {
                String responseString = EntityUtils.toString(response.getEntity());
                // 声明并初始化 queTitle 和 @ExcelProperty 之间的映射
                Map<String, String> fieldMapping = new HashMap<>();
                fieldMapping.put("生产订单编号", "productionOrderNumber");
                fieldMapping.put("本次分板完成数", "burnInCompletionQuantity");
                fieldMapping.put("分板合格数", "BurnQualifiedCount");
                fieldMapping.put("物料编码", "materialCode");
                fieldMapping.put("物料名称", "materialName");
                fieldMapping.put("分板治具编号", "BurnFixtureNumber");
                // 使用Gson解析JSON
                // 使用Gson解析JSON
                JsonObject jsonObject = JsonParser.parseString(responseString).getAsJsonObject();
                JsonArray answersArray = jsonObject.getAsJsonObject("result").getAsJsonArray("result");
                // 创建数据列表
                List<MesPcbaVersion> dataList = new ArrayList<>();
                // 遍历answersArray
                for (JsonElement element : answersArray) {
                    JsonObject answer = element.getAsJsonObject();
                    JsonArray answerArray = answer.getAsJsonArray("answers"); // Replace "your_array_key" with the actual key
                    // 创建新的PCshaolu对象
                    MesPcbaVersion MesPcbaVersion = new MesPcbaVersion();
                    for (int i = 0; i < answerArray.size(); i++) {
                        JsonObject item = answerArray.get(i).getAsJsonObject();
                        // 获取queTitle
                        String queTitle = item.get("queTitle").getAsString();
                        // 检查queTitle是否在映射表中
                        if (fieldMapping.containsKey(queTitle)) {
                            String excelFieldName = fieldMapping.get(queTitle);
                            // 获取valuesArray
                            JsonArray valuesArray = item.getAsJsonArray("values");
                            // 遍历valuesArray以获取所有值
                            for (int j = 0; j < valuesArray.size(); j++) {
                                JsonObject value = valuesArray.get(j).getAsJsonObject();
                                String dataValue = value.get("dataValue").getAsString();
                                int id = Integer.parseInt(value.get("queId").getAsString());
                                // 通过反射设置字段值，假设pcbashaolu是具有对应字段的JavaBean
                                try {
                                    Field field = MesPcbaVersion.getClass().getDeclaredField(excelFieldName);
                                    field.setAccessible(true);
                                    // 如果字段是BurnQualifiedCount并且还没有设置过，设置字段值
                                    if (excelFieldName.equals("BurnQualifiedCount")) {
                                        if (id == 84935179) {
                                            field.set(MesPcbaVersion, dataValue);
                                        }
                                    } else {
                                        field.set(MesPcbaVersion, dataValue);
                                    }
                                } catch (NoSuchFieldException | IllegalAccessException e) {
                                    // 处理反射异常，如字段不存在或访问权限问题
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    // 将pcshaolu对象添加到数据列表
                    apsPcbaVersions = new ArrayList<>();
                    dataList.add(MesPcbaVersion);
                    for (MesPcbaVersion mesPcbaVersion : dataList) {
                        ApsPcbaVersion apsPcbaVersion = mesToApsPcbaVersion.convert(mesPcbaVersion, maxVersionIncr);
                        apsPcbaVersions.add(apsPcbaVersion);
                    }
                }
            }
            return saveBatch(apsPcbaVersions);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Object> selectVersionPageList(Integer pass, Integer size, List versionToChVersionArrayList) {
        return null;
    }
}




