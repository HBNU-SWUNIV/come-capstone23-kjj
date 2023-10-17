package com.hanbat.zanbanzero.controller.user;

import com.hanbat.zanbanzero.controller.ControllerTestClass;
import com.hanbat.zanbanzero.dto.user.info.UserInfoDto;
import com.hanbat.zanbanzero.dto.user.user.UserDto;
import com.hanbat.zanbanzero.dto.user.user.UserJoinDto;
import com.hanbat.zanbanzero.dto.user.user.UserMypageDto;
import com.hanbat.zanbanzero.dto.user.user.UsernameDto;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.WrongRequestDetails;
import com.hanbat.zanbanzero.service.user.UserServiceImplV1;
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
    private UserServiceImplV1 userService;

    private final UserDto dto = new UserDto(1L, "test username", "1234", null);
    private final UserJoinDto dto2 = new UserJoinDto("test username", "1234");
    private final UsernameDto dto3 = new UsernameDto("test username");

    @Test
    void join() throws Exception {
        // 1. 정상 가입
        {
            // Given
            Mockito.doNothing().when(userService).join(dto2);

            // When
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/join")
                            .content(objectMapper.writeValueAsString(dto2))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            // Then
            String expected = "회원가입에 성공했습니다.";

            assertEquals(expected, result.getResponse().getContentAsString());
            assertEquals(200, result.getResponse().getStatus());

            Mockito.verify(userService, Mockito.times(1)).join(dto2);
        }

        // 2. null 데이터
        {
            // Given
            UserJoinDto nullDto = new UserJoinDto(null, "1234");
            Mockito.doThrow(WrongRequestDetails.class).when(userService).join(nullDto);

            // When
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/join")
                            .content(objectMapper.writeValueAsString(nullDto))
                            .contentType(MediaType.APPLICATION_JSON))
                            .andReturn();

            // Then
            assertEquals(400, result.getResponse().getStatus());

            Mockito.verify(userService, Mockito.times(1)).join(nullDto);
        }

        // 3. empty 데이터
        {
            // Given
            UserJoinDto emptyDto = new UserJoinDto("", "1234");
            Mockito.doThrow(WrongRequestDetails.class).when(userService).join(emptyDto);

            // When
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/join")
                            .content(objectMapper.writeValueAsString(emptyDto))
                            .contentType(MediaType.APPLICATION_JSON))
                            .andReturn();

            // Then
            assertEquals(400, result.getResponse().getStatus());

            Mockito.verify(userService, Mockito.times(1)).join(emptyDto);
        }
    }

    @Test
    void check() throws Exception {
        // 1. 아이디 중복 x
        {
            // Given
            Mockito.when(userService.check(dto3.getUsername())).thenReturn(false);

            // When
            mockMvc.perform(MockMvcRequestBuilders.post("/join/check")
                            .content(objectMapper.writeValueAsString(dto3.getUsername()))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string("사용 가능한 아이디입니다."));
        }

        // 2. 아이디 중복
        {
            // Given
            Mockito.when(userService.check(dto3.getUsername())).thenReturn(true);

            // When
            mockMvc.perform(MockMvcRequestBuilders.post("/join/check")
                            .content(objectMapper.writeValueAsString(dto3.getUsername()))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(409))
                    .andExpect(MockMvcResultMatchers.content().string("중복된 아이디입니다."));
        }
        Mockito.verify(userService, Mockito.times(2)).check(dto3.getUsername());
    }

    @Test
    void getInfo() throws Exception {
        // 1. 정상 요청
        {
            // Given
            UserInfoDto userInfoDto = new UserInfoDto();
            Mockito.when(userService.getInfo(null)).thenReturn(userInfoDto);

            // When
            mockMvc.perform(MockMvcRequestBuilders.get("/api/user/info")
                            .content(objectMapper.writeValueAsString(dto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(userInfoDto)));

            // Then
            Mockito.verify(userService, Mockito.times(1)).getInfo(null);
        }
    }

    @Test
    void getMyPage() throws Exception {
        // 1. 정상 요청
        {
            // Given
            UserMypageDto userMyPageDto = new UserMypageDto();
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