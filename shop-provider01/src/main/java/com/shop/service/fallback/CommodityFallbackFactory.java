package com.shop.service.fallback;

import com.shop.bean.Commodity;
import com.shop.bean.ResponseBody;
import com.shop.exception.BasicException;
import com.shop.service.CommodityClientService;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.List;

@Component
public class CommodityFallbackFactory implements FallbackFactory<CommodityClientService> {
    @Override
    public CommodityClientService create(Throwable cause) {
        return new CommodityClientService() {
            @Override
            public ResponseBody<Commodity> getCommodityById(Integer commodityId) {
                return ResponseBody.error("商品服务故障");
            }

            @Override
            public ResponseBody<List<Commodity>> getCommoditiesByIdList(List<Integer> commodityIdList) {
                return ResponseBody.error("商品服务故障");
            }

            @Override
            public ResponseBody<Integer> updateList(List<Commodity> commodityList) {
                return ResponseBody.error("商品服务故障");
            }
        };
    }
}
