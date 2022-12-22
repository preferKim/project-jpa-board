package devcourse.board.domain.member;

import devcourse.board.domain.member.model.Member;
import devcourse.board.domain.member.model.MemberRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원 저장")
    void save() {
        // given
        MemberRequest.JoinDto joinDto = new MemberRequest.JoinDto("member1");
        MemberRequest.SpecificJoinDto specificJoinDto = new MemberRequest.SpecificJoinDto("member2", 27, "hobby2");

        // when
        memberService.save(joinDto);
        memberService.save(specificJoinDto);

        // then
        List<Member> findMembers = memberRepository.findAll();
        int actualSize = findMembers.size();
        int expectedSize = 2;

        assertThat(actualSize).isEqualTo(expectedSize);
    }
}