package com.shop.controller;

import com.shop.bean.Commodity;
import com.shop.bean.CommodityImage;
import com.shop.bean.ResponseBody;
import com.shop.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Validated
public class CommodityController {
    private final CommodityService commodityService;

    @Autowired
    public CommodityController(CommodityService commodityService) {
        this.commodityService = commodityService;
    }

    @GetMapping("/commodity")
    public ResponseBody<List<Commodity>> getCommodityList(@RequestParam(required = false, defaultValue = "false") Boolean isSale,
                                                          @NotNull(message = "当前页码不能为空") Integer pageIndex,
                                                          @RequestParam(required = false, defaultValue = "8") Integer pageSize,
                                                          @RequestParam(required = false, defaultValue = "0") Integer order) {
        return commodityService.getCommodityList(isSale, pageIndex, pageSize, order);
    }

    @GetMapping("/commodity/type/{typeId}")
    public ResponseBody<List<Commodity>> getCommodityListByTypeId(@PathVariable("typeId") Integer typeId,
                                                                  @NotNull(message = "当前页码不能为空") Integer pageIndex,
                                                                  @RequestParam(required = false, defaultValue = "8") Integer pageSize,
                                                                  @RequestParam(required = false, defaultValue = "0") Integer order) {
        return commodityService.getCommodityListByTypeId(typeId, pageIndex, pageSize, order);
    }

    @GetMapping("/commodity/name")
    public ResponseBody<List<Commodity>> getCommodityListByName(@NotNull(message = "搜索关键字不能为空") String name,
                                                                @NotNull(message = "当前页码不能为空") Integer pageIndex,
                                                                @RequestParam(required = false, defaultValue = "8") Integer pageSize,
                                                                @RequestParam(required = false, defaultValue = "0") Integer order) {
        return commodityService.getCommodityListByName(name, pageIndex, pageSize, order);
    }

    @GetMapping("/commodity/{id}")
    public ResponseBody<Commodity> getCommodityById(@PathVariable("id") Integer id) {
        return commodityService.getCommodityById(id);
    }

    @GetMapping("/commodity/idList")
    public ResponseBody<List<Commodity>> getCommoditiesByIdList(@NotEmpty(message = "商品id列表不能为空")
                                                                @RequestParam(value = "list") List<Integer> commodityIdList) {
        return commodityService.getCommoditiesByIdList(commodityIdList);
    }

    @PutMapping("/commodity/list")
    public ResponseBody<Integer> updateList(@NotEmpty(message = "商品列表不能为空")
                                            @RequestBody List<Commodity> commodityList) {
        return commodityService.updateList(commodityList);
    }

    @PostMapping("/commodity")
    public ResponseBody addCommodity(@RequestBody Commodity commodity) {
        return commodityService.addCommodity(commodity);
    }

    @PutMapping("/commodity")
    public ResponseBody updateCommodity(@RequestBody Commodity commodity) {
        return commodityService.updateCommodity(commodity);
    }

    @DeleteMapping("/commodity/{id}")
    public ResponseBody delCommodity(@PathVariable("id") Integer id) {
        return commodityService.delCommodity(id);
    }

    @PostMapping("/upload")
    public ResponseBody uploadImage(@RequestParam("file") MultipartFile srcFile, Integer commodityId) {
        return commodityService.uploadImage(srcFile, commodityId);
    }

    @PostMapping("/image")
    public ResponseBody addImage(@RequestBody CommodityImage image) {
        return commodityService.addImage(image);
    }

    @DeleteMapping("/image")
    public ResponseBody delImage(CommodityImage image) {
        return commodityService.delImage(image);
    }
}
