package com.hanbat.zanbanzero.dto.user.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.modelmapper.ModelMapper;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserInfoDto {
    private Long id;
    private String username;

    public static UserInfoDto createUserInfoDto(UserDto dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, UserInfoDto.class);
    }
}
