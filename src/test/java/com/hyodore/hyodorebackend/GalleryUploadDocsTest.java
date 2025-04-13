package com.hyodore.hyodorebackend;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
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
class GalleryUploadDocsTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void presignedUrl생성문서() throws Exception {
    mockMvc.perform(post("/api/gallery/upload/init")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                    {
                      "file_name": "cat.jpg",
                      "content_type": "image/jpeg"
                    }
                """))
        .andExpect(status().isOk())
        .andDo(document("gallery-upload-init",
            requestFields(
                fieldWithPath("file_name").description("업로드할 파일 이름"),
                fieldWithPath("content_type").description("파일 MIME 타입")
            ),
            responseFields(
                fieldWithPath("photoId").description("파일 UUID"),
                fieldWithPath("uploadUrl").description("S3 업로드용 Presigned URL"),
                fieldWithPath("photoUrl").description("S3 정적 URL")
            )
        ));
  }
}
