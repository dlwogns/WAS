package com.hyodore.hyodorebackend;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseBody;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ActiveProfiles("test")
public class EventDocsTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void 이벤트_처리() throws Exception {
    mockMvc.perform(post("/api/events")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
            {
               "eventCode":"E1",
               "deviceId":"device123"
            }
      """))
        .andExpect(status().isOk())
        .andDo(document("event",
            requestFields(
                fieldWithPath("eventCode").description("이벤트 타입"),
                fieldWithPath("deviceId").description("디바이스 ID")
            ),
            responseBody()));
  }

}
