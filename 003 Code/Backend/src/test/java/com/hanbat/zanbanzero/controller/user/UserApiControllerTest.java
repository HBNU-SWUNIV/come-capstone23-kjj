package com.hanbat.zanbanzero.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbat.zanbanzero.dto.user.info.UserInfoDto;
import com.hanbat.zanbanzero.dto.user.user.UserDto;
import com.hanbat.zanbanzero.dto.user.user.UserMyPageDto;
import com.hanbat.zanbanzero.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class) // application context 전체를 로딩하지 않고 필요한 bean만 주입받음
@WebMvcTest(UserApiController.class) //클래스 지정하여 스캔
@AutoConfigureMockMvc(addFilters = false) // Security 설정 무시
class UserApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private final UserDto userDto = new UserDto(1L, "test username", "1234", null);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void join() throws Exception {
        Mockito.doNothing().when(userService).join(userDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/join")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.content().string("회원가입에 성공했습니다."));

        Mockito.verify(userService, Mockito.times(1)).join(userDto);
    }

    @Test
    void check() throws Exception {
        // 1. 아이디 중복 x
        {
            Mockito.when(userService.check(userDto)).thenReturn(false);

            mockMvc.perform(MockMvcRequestBuilders.post("/join/check")
                            .content(objectMapper.writeValueAsString(userDto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string("사용 가능한 아이디입니다."));
        }

        // 2. 아이디 중복
        {
            Mockito.when(userService.check(userDto)).thenReturn(true);

            mockMvc.perform(MockMvcRequestBuilders.post("/join/check")
                            .content(objectMapper.writeValueAsString(userDto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(409))
                    .andExpect(MockMvcResultMatchers.content().string("중복된 아이디입니다."));
        }
        Mockito.verify(userService, Mockito.times(2)).check(userDto);
    }

    @Test
    void getInfo() throws Exception {
        UserInfoDto userInfoDto = new UserInfoDto();
        Mockito.when(userService.getInfo(userDto)).thenReturn(userInfoDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/info")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(userInfoDto)));

        Mockito.verify(userService, Mockito.times(1)).getInfo(userDto);
    }

    @Test
    void getMyPage() throws Exception {
        UserMyPageDto userMyPageDto = new UserMyPageDto();
        Mockito.when(userService.getMyPage(userDto.getId())).thenReturn(userMyPageDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/1/page")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(userMyPageDto)));

        Mockito.verify(userService, Mockito.times(1)).getMyPage(userDto.getId());
    }
}