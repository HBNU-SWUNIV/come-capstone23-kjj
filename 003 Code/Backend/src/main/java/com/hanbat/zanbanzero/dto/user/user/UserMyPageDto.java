package com.hanbat.zanbanzero.dto.user.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbat.zanbanzero.entity.user.user.UserMyPage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMyPageDto {
    private Long userId;
    private Map<Integer, String> coupon;
    private int point;

    public static UserMyPageDto createUserMyPageDto(UserMyPage userMyPage) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return new UserMyPageDto(
                userMyPage.getId(),
                objectMapper.readValue(userMyPage.getCoupon(), new TypeReference<Map<Integer, String>>() {}),
                userMyPage.getPoint()
        );
    }
}
