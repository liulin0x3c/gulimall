package com.liulin.product.feign;


import com.liulin.common.to.SkuHasStockTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("gulimall-ware")
public interface WareFeignService {
    @PostMapping("/ware/waresku/list")
    List<SkuHasStockTo> getStockStatementBySKuIds(@RequestBody List<Long> ids);
}
