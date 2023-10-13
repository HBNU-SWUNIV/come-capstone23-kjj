package com.hanbat.zanbanzero.auto_init;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbat.zanbanzero.auto_init.dto.Recipe;
import com.hanbat.zanbanzero.entity.menu.MenuFood;
import com.hanbat.zanbanzero.repository.menu.MenuFoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@RestController
@RequiredArgsConstructor
public class main {

    private final RestTemplate restTemplate;
    private final MenuFoodRepository menuFoodRepository;
    private final int size = 200;

    @GetMapping("/update-db")
    public void run() throws JsonProcessingException {
        // 레시피 기본정보
        Map<Integer, Recipe> result = new HashMap<>();
        defaultInfo(result);
        foodInfo(result);

        for (Recipe recipe : result.values()) {
            menuFoodRepository.save(MenuFood.of(recipe));
        }
    }

    private void defaultInfo(Map<Integer, Recipe> result) {
        String url = "http://211.237.50.150:7080/openapi/010d741bad9f0ca5cbc3791e8ea0f7001281d0c8a64a6d6e3653f0b6f427e539/json/Grid_20150827000000000226_1/1/" + size;

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        Map<String, Object> grid = (Map<String, Object>) response.get("Grid_20150827000000000226_1");
        if (grid != null) {
            List<Map<String, Object>> rows = (List<Map<String, Object>>) grid.get("row");
            for (Map<String, Object> recipe : rows) {
                int recipeId = (int) recipe.get("RECIPE_ID");
                String recipeName = (String) recipe.get("RECIPE_NM_KO");
                result.put(recipeId, new Recipe(recipeId, recipeName, null));
            }
        }
    }

    private void foodInfo(Map<Integer, Recipe> result) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        for (int i = 1; i <= size; i++) {
            String url = "http://211.237.50.150:7080/openapi/010d741bad9f0ca5cbc3791e8ea0f7001281d0c8a64a6d6e3653f0b6f427e539/json/Grid_20150827000000000227_1/1/99?RECIPE_ID=" + i;
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            Map<String, Object> grid = (Map<String, Object>) response.get("Grid_20150827000000000227_1");

            Map<String, Integer> foodResult = new HashMap<>();
            if (grid != null) {
                List<Map<String, Object>> rows = (List<Map<String, Object>>) grid.get("row");
                for (Map<String, Object> recipe : rows) {
                    String foodName = (String) recipe.get("IRDNT_NM");
                    String foodWeight = (String) recipe.get("IRDNT_CPCTY");

                    // 문자열 분석
                    Pattern pattern = Pattern.compile("(\\d+)([a-zA-Z가-힣]*)");
                    Matcher matcher = pattern.matcher(foodWeight);

                    int extractedNumber = 0;

                    while (matcher.find()) {
                        int number = Integer.parseInt(matcher.group(1));
                        String unit = matcher.group(2);

                        if (!unit.isEmpty()) {
                            switch (unit) {
                                case "컵":
                                    extractedNumber = number * 100;
                                    break;
                                case "개":
                                    extractedNumber = number;
                                    break;
                                case "g":
                                    extractedNumber = number;
                                    break;
                                case "모":
                                    extractedNumber = 50;
                                    break;
                                case "ml":
                                    extractedNumber = number;
                                    break;
                                case "톨":
                                    extractedNumber = number;
                                    break;
                                case "뿌리":
                                    extractedNumber = number;
                                    break;
                                case "장":
                                    extractedNumber = number;
                                    break;
                                case "알":
                                    extractedNumber = number;
                                    break;
                                case "포기":
                                    extractedNumber = number * 1000;
                                    break;
                                case "공기":
                                    extractedNumber = number * 200;
                                    break;
                                case "큰술":
                                    extractedNumber = number * 15;
                                    break;
                                case "작은술":
                                    extractedNumber = number * 5;
                                    break;
                                case "마리":
                                    extractedNumber = number;
                                    break;
                                case "kg":
                                    extractedNumber = number * 1000;
                                    break;
                                case "쪽":
                                    extractedNumber = number * 5;
                                    break;
                                case "통":
                                    extractedNumber = number * 10;
                                    break;
                                default:
//                                    System.out.println("Unsupported unit: " + foodName + unit);
                                    break;
                            }
                        }
                    }
                    // 숫자가 없는 경우 continue
                    if (extractedNumber == 0) continue;

                    foodResult.put(foodName, extractedNumber);
                    result.get(i).setFood(objectMapper.writeValueAsString(foodResult));
                }
            }
        }
    }
}
