package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired private MemberRepository memberRepository;

    @Test
    public void MemberRepositoryTest() throws Exception {
        //given
        Member member = new Member("memberA");

        //when
        memberRepository.save(member);
        Member findMember = memberRepository.findById(member.getId()).get();
        //then

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember).isEqualTo(member);  //jpa는 트랜잭션 안에서 엔티티의 동일성을 보장

    }
}