package devcourse.board.web.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import devcourse.board.domain.member.MemberRepository;
import devcourse.board.domain.member.model.Member;
import devcourse.board.domain.post.PostRepository;
import devcourse.board.domain.post.model.Post;
import devcourse.board.domain.post.model.PostCreationRequest;
import devcourse.board.domain.post.model.PostUpdateRequest;
import devcourse.board.web.authentication.AuthenticationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class PostApiV1Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    private Member dummyMember;

    @BeforeEach
    void setUp() {
        this.dummyMember = Member.create("example@email.com", "0000", "member");
    }

    @Test
    @DisplayName("????????? ??????")
    void call_createPost() throws Exception {
        // given
        Member member = this.dummyMember;
        memberRepository.save(member);

        PostCreationRequest creationRequest =
                new PostCreationRequest("post-title", "post-content");

        Cookie memberIdCookie = new Cookie(AuthenticationUtil.COOKIE_NAME, String.valueOf(member.getId()));

        // when & then
        mockMvc.perform(post("/api/v1/posts")
                        .cookie(memberIdCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationRequest)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("post-create-v1",
                        requestFields(
                                fieldWithPath("title").description("????????? ??????"),
                                fieldWithPath("content").description("????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("identifier").description("????????? ?????????")
                        )));
    }

    @Test
    @DisplayName("????????? ?????? ??????")
    void call_getPost() throws Exception {
        // given
        Member member = this.dummyMember;
        memberRepository.save(member);

        Post post = Post.create(member, "post-title", "post-content");
        postRepository.save(post);

        // when & then
        mockMvc.perform(get("/api/v1/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-get-one-v1",
                        pathParameters(
                                parameterWithName("postId").description("????????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("postId").description("????????? ?????????"),
                                fieldWithPath("title").description("????????? ??????"),
                                fieldWithPath("content").description("????????? ??????"),
                                fieldWithPath("createdAt").description("????????? ?????? ??????"),
                                fieldWithPath("createdBy").description("????????? ????????? ??????")
                        )));
    }

    @Test
    @DisplayName("????????? ????????? ??????")
    void call_getPosts() throws Exception {
        // given
        Member member = Member.create(
                "example@email.com",
                "0000",
                "member",
                null,
                null);
        memberRepository.save(member);

        Post post = Post.create(member, "post-title", "post-content");
        postRepository.save(post);

        // when & then
        mockMvc.perform(get("/api/v1/posts")
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-get-all-v1",
                        requestParameters(
                                parameterWithName("page").description("?????? ?????????"),
                                parameterWithName("size").description("????????? ????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("simplePostResponses").description("????????? ????????? ?????? ??????"),

                                fieldWithPath("simplePostResponses[].postId").description("????????? ?????????"),
                                fieldWithPath("simplePostResponses[].title").description("????????? ??????")
                        )));
    }

    @Test
    @DisplayName("????????? ????????? ????????? ???????????? ???????????? ????????? ??? ??????.")
    void call_updatePost() throws Exception {
        // given
        Member member = this.dummyMember;
        memberRepository.save(member);

        Post post = Post.create(member, "old-title", "old-content");
        postRepository.save(post);

        PostUpdateRequest updateRequest = new PostUpdateRequest("new-title", "new-content");

        Cookie idCookie = new Cookie(AuthenticationUtil.COOKIE_NAME, String.valueOf(member.getId()));

        // when & then
        mockMvc.perform(patch("/api/v1/posts/{postId}", post.getId())
                        .cookie(idCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-update-v1",
                        pathParameters(
                                parameterWithName("postId").description("????????? ?????????")
                        ),
                        requestFields(
                                fieldWithPath("title").description("????????? ??????"),
                                fieldWithPath("content").description("????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("postId").description("????????? ?????????"),
                                fieldWithPath("title").description("????????? ??????"),
                                fieldWithPath("content").description("????????? ??????"),
                                fieldWithPath("createdAt").description("????????? ?????????"),
                                fieldWithPath("createdBy").description("????????? ????????? ??????")
                        )));
    }
}