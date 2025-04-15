package com.hyodore.hyodorebackend;

import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

  @Test
  void 사진_삭제_요청_문서화() throws Exception {
    String body = """
          {
            "userId": "user123",
            "photoIds": ["uuid-1", "uuid-2"]
          }
        """;

    mockMvc.perform(post("/api/gallery/delete")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andDo(document("gallery-delete",
            requestFields(
                fieldWithPath("userId").description("삭제 요청 사용자 ID"),
                fieldWithPath("photoIds").description("삭제할 사진 UUID 리스트")
            ),
            responseFields(
                fieldWithPath("syncedAt").description("동기화 완료 시각 (서버 기준)"),
                fieldWithPath("newPhoto").description("새로 업로드된 사진 목록"),
                fieldWithPath("newPhoto[].photoId").description("사진 ID"),
                fieldWithPath("newPhoto[].familyId").description("가족 ID"),
                fieldWithPath("newPhoto[].photoUrl").description("S3 URL"),
                fieldWithPath("newPhoto[].uploadedBy").description("업로드한 사용자 ID"),
                fieldWithPath("newPhoto[].uploadedAt").description("업로드 시각"),
                fieldWithPath("newPhoto[].deleted").description("삭제 여부"),
                fieldWithPath("newPhoto[].deletedAt").description("삭제된 시각").optional(),

                fieldWithPath("deletedPhoto").description("삭제된 사진 목록"),
                fieldWithPath("deletedPhoto[].photoId").description("사진 ID"),
                fieldWithPath("deletedPhoto[].familyId").description("가족 ID"),
                fieldWithPath("deletedPhoto[].photoUrl").description("S3 URL"),
                fieldWithPath("deletedPhoto[].uploadedBy").description("업로드한 사용자 ID"),
                fieldWithPath("deletedPhoto[].uploadedAt").description("업로드 시각"),
                fieldWithPath("deletedPhoto[].deleted").description("삭제 여부"),
                fieldWithPath("deletedPhoto[].deletedAt").description("삭제된 시각").optional()
            )
        ));
  }

  @Test
  void 최근_동기화_기반_사진_변화_조회_문서화() throws Exception {
    mockMvc.perform(get("/api/gallery/sync")
            .param("userId", "user123")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(document("gallery-sync",queryParameters(
            parameterWithName("userId").description("유저 ID")
            ),
            responseFields(
                fieldWithPath("syncedAt").description("이번 동기화 시각 (서버 기준)"),
                fieldWithPath("newPhoto").description("동기화 이후 추가된 사진 목록"),
                fieldWithPath("newPhoto[].photoId").description("사진 ID"),
                fieldWithPath("newPhoto[].familyId").description("가족 ID"),
                fieldWithPath("newPhoto[].photoUrl").description("사진 S3 URL"),
                fieldWithPath("newPhoto[].uploadedBy").description("업로드한 사용자 ID"),
                fieldWithPath("newPhoto[].uploadedAt").description("업로드 시각"),
                fieldWithPath("newPhoto[].deleted").description("삭제 여부"),
                fieldWithPath("newPhoto[].deletedAt").description("삭제된 시각").optional(),

                fieldWithPath("deletedPhoto").description("동기화 이후 삭제된 사진 목록"),
                fieldWithPath("deletedPhoto[].photoId").description("사진 ID"),
                fieldWithPath("deletedPhoto[].familyId").description("가족 ID"),
                fieldWithPath("deletedPhoto[].photoUrl").description("사진 S3 URL"),
                fieldWithPath("deletedPhoto[].uploadedBy").description("업로드한 사용자 ID"),
                fieldWithPath("deletedPhoto[].uploadedAt").description("업로드 시각"),
                fieldWithPath("deletedPhoto[].deleted").description("삭제 여부"),
                fieldWithPath("deletedPhoto[].deletedAt").description("삭제된 시각").optional()
            )
        ));
  }

  @Test
  void 전체_사진_조회_문서화() throws Exception {
    mockMvc.perform(get("/api/gallery/all")
            .param("userId", "user123")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(document("gallery-all",
            queryParameters(
                parameterWithName("userId").description("사용자 ID (가족 ID 기준으로 조회)")
            ),
            responseFields(
                fieldWithPath("photos").description("현재 삭제되지 않은 전체 사진 목록"),
                fieldWithPath("photos[].photoId").description("사진 ID"),
                fieldWithPath("photos[].familyId").description("가족 ID"),
                fieldWithPath("photos[].photoUrl").description("사진 S3 URL"),
                fieldWithPath("photos[].uploadedBy").description("업로드한 사용자 ID"),
                fieldWithPath("photos[].uploadedAt").description("업로드 시각"),
                fieldWithPath("photos[].deleted").description("삭제 여부"),
                fieldWithPath("photos[].deletedAt").description("삭제된 시각").optional()
            )
        ));
  }



}
