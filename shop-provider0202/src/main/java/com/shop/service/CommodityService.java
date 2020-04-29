package com.shop.service;

import com.shop.bean.Commodity;
import com.shop.bean.CommodityImage;
import com.shop.bean.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface CommodityService {
    ResponseBody<List<Commodity>> getCommodityList(Integer pageIndex, Integer pageSize, Integer order);
    ResponseBody<List<Commodity>> getCommodityListByTypeId(Integer typeId, Integer pageIndex, Integer pageSize, Integer order);
    ResponseBody<List<Commodity>> getCommodityListByName(String name, Integer pageIndex, Integer pageSize, Integer order);
    ResponseBody<Commodity> getCommodityById(Integer commodityId);
    ResponseBody<List<Commodity>> getCommoditiesByIdList(List<Integer> commodityIdList);
    ResponseBody<Integer> updateList(List<Commodity> commodityList);

    ResponseBody addCommodity(Commodity commodity);
    ResponseBody updateCommodity(Commodity commodity);
    ResponseBody delCommodity(Integer id);
    ResponseBody uploadImage(MultipartFile srcFile, Integer commodityId);
    ResponseBody addImage(CommodityImage image);
    ResponseBody delImage(CommodityImage image);
}
