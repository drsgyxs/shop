package com.shop.service;

import com.shop.bean.Commodity;
import com.shop.bean.ResponseBody;
import com.shop.service.fallback.CommodityFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(value = "commodity", fallbackFactory = CommodityFallbackFactory.class)
public interface CommodityClientService {
    @GetMapping("/commodity/{commodityId}")
    ResponseBody<Commodity> getCommodityById(@PathVariable("commodityId") Integer commodityId);

    @GetMapping("/commodity/idList")
    ResponseBody<List<Commodity>> getCommoditiesByIdList(@RequestParam("list") List<Integer> commodityIdList);

    @PutMapping("/commodity/list")
    ResponseBody<Integer> updateList(@RequestBody List<Commodity> commodityList);

}

