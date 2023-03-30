package com.hanbat.zanbanzero.controller.user;

import com.hanbat.zanbanzero.controller.ControllerTestClass;
import com.hanbat.zanbanzero.dto.user.info.UserInfoDto;
import com.hanbat.zanbanzero.dto.user.user.UserDto;
import com.hanbat.zanbanzero.dto.user.user.UserMyPageDto;
import com.hanbat.zanbanzero.exception.controller.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongRequestDetails;
import com.hanbat.zanbanzero.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserApiController.class) //클래스 지정하여 스캔
@AutoConfigureMockMvc(addFilters = false) // Security 설정 무시
class UserApiControllerTest extends ControllerTestClass {

    @MockBean
    private UserService userService;

    private final UserDto dto = new UserDto(1L, "test username", "1234", null);

    @Test
    void join() throws Exception {
        // 1. 정상 가입
        {
            // Given
            Mockito.doNothing().when(userService).join(dto);

            // When
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/join")
                            .content(objectMapper.writeValueAsString(dto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            // Then
            String expected = "회원가입에 성공했습니다.";

            assertEquals(expected, result.getResponse().getContentAsString());
            assertEquals(200, result.getResponse().getStatus());

            Mockito.verify(userService, Mockito.times(1)).join(dto);
        }

        // 2. null 데이터
        {
            // Given
            UserDto nullDto = new UserDto(1L, null, "1234", null);
            Mockito.doThrow(WrongRequestDetails.class).when(userService).join(nullDto);

            // When
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/join")
                            .content(objectMapper.writeValueAsString(nullDto))
                            .contentType(MediaType.APPLICATION_JSON))
                            .andReturn();

            // Then
            assertEquals(412, result.getResponse().getStatus());

            Mockito.verify(userService, Mockito.times(1)).join(nullDto);
        }

        // 3. empty 데이터
        {
            // Given
            UserDto emptyDto = new UserDto(1L, "", "1234", null);
            Mockito.doThrow(WrongRequestDetails.class).when(userService).join(emptyDto);

            // When
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/join")
                            .content(objectMapper.writeValueAsString(emptyDto))
                            .contentType(MediaType.APPLICATION_JSON))
                            .andReturn();

            // Then
            assertEquals(412, result.getResponse().getStatus());

            Mockito.verify(userService, Mockito.times(1)).join(emptyDto);
        }
    }

    @Test
    void check() throws Exception {
        // 1. 아이디 중복 x
        {
            // Given
            Mockito.when(userService.check(dto)).thenReturn(false);

            // When
            mockMvc.perform(MockMvcRequestBuilders.post("/join/check")
                            .content(objectMapper.writeValueAsString(dto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string("사용 가능한 아이디입니다."));
        }

        // 2. 아이디 중복
        {
            // Given
            Mockito.when(userService.check(dto)).thenReturn(true);

            // When
            mockMvc.perform(MockMvcRequestBuilders.post("/join/check")
                            .content(objectMapper.writeValueAsString(dto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(409))
                    .andExpect(MockMvcResultMatchers.content().string("중복된 아이디입니다."));
        }
        Mockito.verify(userService, Mockito.times(2)).check(dto);
    }

    @Test
    void getInfo() throws Exception {
        // 1. 정상 요청
        {
            // Given
            UserInfoDto userInfoDto = new UserInfoDto();
            Mockito.when(userService.getInfo(dto)).thenReturn(userInfoDto);

            // When
            mockMvc.perform(MockMvcRequestBuilders.get("/api/user/info")
                            .content(objectMapper.writeValueAsString(dto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(userInfoDto)));

            // Then
            Mockito.verify(userService, Mockito.times(1)).getInfo(dto);
        }
    }

    @Test
    void getMyPage() throws Exception {
        // 1. 정상 요청
        {
            // Given
            UserMyPageDto userMyPageDto = new UserMyPageDto();
            Mockito.when(userService.getMyPage(dto.getId())).thenReturn(userMyPageDto);

            // When
            mockMvc.perform(MockMvcRequestBuilders.get("/api/user/1/page")
                            .content(objectMapper.writeValueAsString(dto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(userMyPageDto)));

            // Then
            Mockito.verify(userService, Mockito.times(1)).getMyPage(dto.getId());
        }

        // 2. 잘못된 id
        {
            // Given
            UserDto wrongId = new UserDto(99L, "wrong id", "1234", null);
            Mockito.doThrow(CantFindByIdException.class).when(userService).getMyPage(wrongId.getId());

            // When
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/" + wrongId.getId() + "/page")
                            .content(objectMapper.writeValueAsString(wrongId))
                            .contentType(MediaType.APPLICATION_JSON))
                            .andReturn();

            // Then
            assertEquals(500, result.getResponse().getStatus());

            Mockito.verify(userService, Mockito.times(1)).getMyPage(wrongId.getId());
        }
    }
}