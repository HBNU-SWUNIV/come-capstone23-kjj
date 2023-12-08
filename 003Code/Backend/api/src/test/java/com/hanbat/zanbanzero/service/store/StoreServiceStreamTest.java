package com.hanbat.zanbanzero.service.store;

import com.hanbat.zanbanzero.entity.calculate.CalculateMenu;
import com.hanbat.zanbanzero.repository.calculate.CalculateMenuRepository;
import com.hanbat.zanbanzero.repository.calculate.CalculateRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest
class StoreServiceStreamTest {

    @Autowired
    private CalculateMenuRepository calculateMenuRepository;
    @Autowired
    private CalculateRepository calculateRepository;

    private final int COUNT = 10;
    @Test
    void runSingleStreamTest() {
        List<Long> resultList = new ArrayList<>();
        for (int i = 1; i <= COUNT; i++){
            resultList.add(testSingleStream());
        }
        for (Long result : resultList) log.info("Single Stream : {}", result);
    }

    @Test
    void runParallelStreamTest() {
        List<Long> resultList = new ArrayList<>();
        for (int i = 1; i <= COUNT; i++){
            resultList.add(testParallelStream());
        }
        for (Long result : resultList) log.info("Parallel Stream : {}", result);
    }

    private long testSingleStream() {
        List<Long> idList = calculateRepository.findTop5IdOrderByIdDesc();
        List<CalculateMenu> calculateMenus = calculateMenuRepository.getPopularMenus(idList);
        Map<String, Integer> result = new HashMap<>();

        // Single Stream
        long start = System.nanoTime();
        calculateMenus.forEach(calculateMenu -> {
            String name = calculateMenu.getMenu();
            Integer count = calculateMenu.getCount();
            result.put(name, result.getOrDefault(name, 0) + count);
        });
        long end = System.nanoTime();
        return end - start;
    }

    private long testParallelStream() {
        List<Long> idList = calculateRepository.findTop5IdOrderByIdDesc();
        List<CalculateMenu> calculateMenus = calculateMenuRepository.getPopularMenus(idList);
        Map<String, Integer> result = new HashMap<>();

        // Parallel Stream
        long start = System.nanoTime();
        calculateMenus.parallelStream().forEach(calculateMenu -> {
            String name = calculateMenu.getMenu();
            Integer count = calculateMenu.getCount();
            result.put(name, result.getOrDefault(name, 0) + count);
        });
        long end = System.nanoTime();
        return end - start;
    }
}