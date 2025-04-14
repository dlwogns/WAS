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
                    [
                      {
                        "fileName": "family1.jpg",
                        "contentType": "image/jpeg"
                      },
                      {
                        "fileName": "pic2.png",
                        "contentType": "image/png"
                      }
                    ]
                """))
        .andExpect(status().isOk())
        .andDo(document("gallery-upload-init",
            requestFields(
                fieldWithPath("[].fileName").description("파일 이름"),
                fieldWithPath("[].contentType").description("MIME 타입")
            ),
            responseFields(
                fieldWithPath("[].photoId").description("사진 UUID"),
                fieldWithPath("[].uploadUrl").description("S3 Presigned PUT URL"),
                fieldWithPath("[].photoUrl").description("정적 URL")
            )
        ));
  }
  @Test
  void 업로드_완료_요청_문서화() throws Exception {
    String body = """
      {
        "userId": "user123",
        "photos": [
          {
            "photoId": "uuid-1",
            "photoUrl": "https://your-bucket.s3.amazonaws.com/photos/uuid-1.jpg",
            "uploadAt": "2025-04-14T18:00:00"
          },
          {
            "photoId": "uuid-2",
            "photoUrl": "https://your-bucket.s3.amazonaws.com/photos/uuid-2.png",
            "uploadAt": "2025-04-14T18:01:00"
          }
        ]
      }
    """;

    mockMvc.perform(post("/api/gallery/upload/complete")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andDo(document("gallery-upload-complete",
            requestFields(
                fieldWithPath("userId").description("업로드를 수행한 사용자 ID"),
                fieldWithPath("photos[].photoId").description("Presigned URL 요청에서 받은 UUID"),
                fieldWithPath("photos[].photoUrl").description("S3에 업로드된 사진 URL"),
                fieldWithPath("photos[].uploadAt").description("클라이언트에서 업로드 완료된 시간 (ISO 8601)")
            ),
            responseFields(
                fieldWithPath("syncedAt").description("동기화 완료 시각 (서버 기준)"),
                fieldWithPath("newPhoto").description("새로 업로드된 사진 목록"),
                fieldWithPath("newPhoto[].photoId").description("사진 고유 ID"),
                fieldWithPath("newPhoto[].familyId").description("가족 ID"),
                fieldWithPath("newPhoto[].photoUrl").description("S3 URL"),
                fieldWithPath("newPhoto[].uploadedBy").description("업로드한 사용자 ID"),
                fieldWithPath("newPhoto[].uploadedAt").description("업로드 시각"),
                fieldWithPath("newPhoto[].deleted").description("삭제 여부"),
                fieldWithPath("newPhoto[].deletedAt").description("삭제된 시각").optional(),

                fieldWithPath("deletedPhoto").description("삭제된 사진 목록"),
                fieldWithPath("deletedPhoto[].photoId").description("삭제된 사진 ID"),
                fieldWithPath("deletedPhoto[].familyId").description("가족 ID"),
                fieldWithPath("deletedPhoto[].photoUrl").description("삭제된 사진 URL"),
                fieldWithPath("deletedPhoto[].uploadedBy").description("업로드한 사용자 ID"),
                fieldWithPath("deletedPhoto[].uploadedAt").description("업로드 시각"),
                fieldWithPath("deletedPhoto[].deleted").description("삭제 여부"),
                fieldWithPath("deletedPhoto[].deletedAt").description("삭제된 시각").optional()
            )
        ));

  }
}
