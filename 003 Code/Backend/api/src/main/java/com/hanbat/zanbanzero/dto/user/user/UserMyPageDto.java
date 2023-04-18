package com.hanbat.zanbanzero.dto.user.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbat.zanbanzero.entity.user.user.UserMypage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMypageDto {
    private Long userId;
    private Map<Integer, String> coupon;
    private int point;

    public static UserMypageDto createUserMyPageDto(UserMypage userMyPage) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return new UserMypageDto(
                userMyPage.getId(),
                objectMapper.readValue(userMyPage.getCoupon(), new TypeReference<>() {}),
                userMyPage.getPoint()
        );
    }
}
