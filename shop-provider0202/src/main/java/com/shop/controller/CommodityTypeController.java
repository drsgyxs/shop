package com.shop.controller;

import com.shop.bean.CommodityType;
import com.shop.bean.ResponseBody;
import com.shop.service.CommodityTypeService;
import com.shop.validate.Insert;
import com.shop.validate.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Validated
public class CommodityTypeController {
    private final CommodityTypeService service;

    @Autowired
    public CommodityTypeController(CommodityTypeService service) {
        this.service = service;
    }

    @GetMapping("/commodity_type")
    public ResponseBody<List<CommodityType>> getCommodityTypeList() {
        return service.getCommodityTypeLevelList();
    }

    @PostMapping("/commodity_type")
    public ResponseBody addType(@Validated(Insert.class) @RequestBody CommodityType type) {
        return service.addType(type);
    }

    @PutMapping("/commodity_type")
    public ResponseBody updateType(@Validated(Update.class) @RequestBody CommodityType type) {
        return service.updateType(type);
    }

    @DeleteMapping("/commodity_type/{id}")
    public ResponseBody delType(@PathVariable("id") Integer id) {
        return service.delType(id);
    }

    @GetMapping("/commodity_type/{level}")
    public ResponseBody getTypeList(@PathVariable("level") Integer level) {
        return service.getTypeList(level);
    }

}
