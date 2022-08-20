package com.liulin.product;

import com.liulin.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.UUID;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallProductApplicationTests {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void testFindPath() {
        Long[] catelogPath = categoryService.findCatelogPath(225L);
        log.info(Arrays.toString(catelogPath));
    }



    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Test
    public void testRedis() {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set("llll","word_"+ UUID.randomUUID());
        System.out.println(ops.get("llll"));

    }
}
