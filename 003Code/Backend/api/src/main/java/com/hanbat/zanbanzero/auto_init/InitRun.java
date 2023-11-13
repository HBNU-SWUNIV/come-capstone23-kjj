package com.hanbat.zanbanzero.auto_init;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbat.zanbanzero.auto_init.dto.Recipe;
import com.hanbat.zanbanzero.auto_init.dto.ReqBody;
import com.hanbat.zanbanzero.auto_init.dto.ReqMessage;
import com.hanbat.zanbanzero.entity.menu.MenuFood;
import com.hanbat.zanbanzero.repository.menu.MenuFoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequiredArgsConstructor
public class InitRun {

    private final RestTemplate restTemplate;
    private final MenuFoodRepository menuFoodRepository;
    @Value("${chatgpt.api-key}") private String apiKey;
    private final int startSize = 201;
    private final int endSize = 210;

    @GetMapping("/update-db")
    public void run() throws JsonProcessingException {
        // 레시피 기본정보
        Map<Integer, Recipe> result = new HashMap<>();
        defaultInfo(result);
        foodInfoV2(result);
        log.info("레시피 세팅 완료");

        for (Recipe recipe : result.values()) {
            menuFoodRepository.save(MenuFood.from(recipe));
        }
        result.clear();
    }

    private void defaultInfo(Map<Integer, Recipe> result) {
        String url = "http://211.237.50.150:7080/openapi/010d741bad9f0ca5cbc3791e8ea0f7001281d0c8a64a6d6e3653f0b6f427e539/json/Grid_20150827000000000226_1/" + startSize + "/" + endSize;

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
        for (int i = startSize; i <= endSize; i++) {
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

    private void foodInfoV2(Map<Integer, Recipe> result) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        for (int i = startSize; i <= endSize; i++) {
            String url = "http://211.237.50.150:7080/openapi/010d741bad9f0ca5cbc3791e8ea0f7001281d0c8a64a6d6e3653f0b6f427e539/json/Grid_20150827000000000227_1/1/99?RECIPE_ID=" + i;
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            Map<String, Object> grid = (Map<String, Object>) response.get("Grid_20150827000000000227_1");

            Map<String, Integer> foodResult = new HashMap<>();
            if (grid != null) {
                List<Map<String, Object>> rows = (List<Map<String, Object>>) grid.get("row");
                for (Map<String, Object> recipe : rows) {
                    String foodName = (String) recipe.get("IRDNT_NM");
                    String foodWeight = (String) recipe.get("IRDNT_CPCTY");

                    Pattern pattern = Pattern.compile("(\\d+)([a-zA-Z가-힣]*)");
                    Matcher matcher = pattern.matcher(foodWeight);

                    int extractedNumber = 0;

                    while (matcher.find()) {
                        int number = Integer.parseInt(matcher.group(1));
                        String unit = matcher.group(2);

                        if (unit.equals("g")) {
                            extractedNumber = number;
                            break;
                        }

                        String q = createQuestion(foodName, foodWeight);
                        ResponseEntity<Map> res = restTemplate.postForEntity(
                                "https://api.openai.com/v1/chat/completions",
                                createData(q),
                                Map.class
                        );
                        Map<String, Object> body = (Map<String, Object>) res.getBody();
                        List<Map<String, Object>> choices = (List<Map<String, Object>>) body.get("choices");
                        Map<String, String> message = (Map<String, String>) choices.get(0).get("message");
                        String answer = message.get("content");
                        System.out.println(q + " : " + answer);

                        // gpt 대답 문자열 파싱
                        Pattern pattern1 = Pattern.compile("\"([^\"]*)\"");
                        Matcher matcher1 = pattern1.matcher(answer);

                        while (matcher1.find()) {
                            String matchedText = matcher1.group(1);
                            try {
                                extractedNumber = Integer.parseInt(matchedText.replaceAll("g", ""));
                            } catch (NumberFormatException e) {
                                log.warn("변환 실패 : matchedText = {}", matchedText);
                            }
                        }
                    }

                    // 숫자가 없는 경우 continue
                    if (extractedNumber == 0) {
                        System.out.println("continue - foodName = " + foodName + " foodWeight = " + foodWeight);
                        continue;
                    }
                    foodResult.put(foodName, extractedNumber);
                    result.get(i).setFood(objectMapper.writeValueAsString(foodResult));
                }
            }
        }
    }

    private String createQuestion(String foodName, String foodWeight) {
//        return foodName + " " + foodWeight + "의 g을 \"100g\"과 같은 형태로 알려줘. 쌍따옴표는 반드시 사용해야 해";
        return foodName + " " + foodWeight + "의 g은?\"100\" 형식으로 알려줘";
    }

    private HttpEntity<ReqBody> createData(String message) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", apiKey);

        List<ReqMessage> messages = new ArrayList<>();
        messages.add(new ReqMessage("user", message));
        ReqBody body = new ReqBody("gpt-3.5-turbo", false, messages);
        return new HttpEntity<>(body, httpHeaders);
    }
}