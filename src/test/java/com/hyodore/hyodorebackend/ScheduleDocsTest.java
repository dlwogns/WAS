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
public class ScheduleDocsTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void 일정_업로드() throws Exception {
    mockMvc.perform(post("/api/schedule/upload")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "scheduleId": "550e8400-e29b-41d4-a716-446655440000",
              "userId": "user123",
              "scheduleDesc": "회의 참석",
              "scheduleDate": "2025-04-30T15:00:00Z"
            }
      """))
        .andExpect(status().isOk())
        .andDo(document("schedule-upload",
            requestFields(
                fieldWithPath("scheduleId").description("일정 ID (UUID)"),
                fieldWithPath("userId").description("사용자 ID"),
                fieldWithPath("scheduleDesc").description("일정 설명"),
                fieldWithPath("scheduleDate").description("일정 날짜 및 시간 (ISO 8601)")
            ),
            responseBody()));
  }

  @Test
  void 일정_삭제() throws Exception {
    mockMvc.perform(post("/api/schedule/delete")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
            {
              "scheduleId": "550e8400-e29b-41d4-a716-446655440000"
            }
            """))
        .andExpect(status().isOk())
        .andDo(document("schedule-delete",
            requestFields(
                fieldWithPath("scheduleId").description("삭제할 일정 ID (UUID)")
            ),
            responseBody()
        ));
  }

}
