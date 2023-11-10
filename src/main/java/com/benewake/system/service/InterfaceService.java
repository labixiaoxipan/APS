package com.benewake.system.service;

import com.benewake.system.entity.vo.PageListRestVo;

import java.util.List;

public interface InterfaceService {
    List<Object> getMultipleVersionsData(Integer page, Integer size, Integer type);

    PageListRestVo<Object> getAllPage(Integer page, Integer size, Integer type);

    Boolean add(String request, Integer type);

    Boolean update(String request, Integer type);

    Boolean delete(List<Integer> ids, Integer type);
}