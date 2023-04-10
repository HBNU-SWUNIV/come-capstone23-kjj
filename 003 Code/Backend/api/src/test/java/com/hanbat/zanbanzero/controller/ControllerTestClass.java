package com.hanbat.zanbanzero.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class) // application context 전체를 로딩하지 않고 필요한 bean만 주입받음
@WebMvcTest
public class ControllerTestClass {

    @Autowired protected MockMvc mockMvc;

    protected final ObjectMapper objectMapper = new ObjectMapper();
}
