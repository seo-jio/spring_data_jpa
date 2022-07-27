package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired private MemberRepository memberRepository;
    @Autowired private TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

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

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 =
                memberRepository.findById(member1.getId()).get();
        Member findMember2 =
                memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() throws Exception {
        //쿼리로만 확인
        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("hello", 15);
    }

    @Test
    public void findMemberDto() throws Exception{
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member member = new Member("seojio", 10, team);
        memberRepository.save(member);

        List<MemberDto> result = memberRepository.findMemberDto();
        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    public void findByNames(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("member1", "member2"));
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void paging() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        //when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        //Dto로 page의 map 메소드를 통해 한번에 변화할 수 있다.
        Page<MemberDto> map = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));
        for (MemberDto memberDto : map) {
            System.out.println("memberDto = " + memberDto);
        }

        //then
        List<Member> content = page.getContent();
        Long totalCount = page.getTotalElements();

        assertThat(content.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);  //현재 페이지
        assertThat(page.getTotalPages()).isEqualTo(2); //총 페이지 수
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();

    }

    @Test
    public void bulkAgePlus() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 30));
        memberRepository.save(new Member("member5", 40));
        //when
        memberRepository.bulkAgePlus(20);
//        em.clear(); //벌크 연산 이후 clear 필수
        List<Member> result = memberRepository.findByUsername("member5");
        Member member = result.get(0);
        System.out.println("member = " + member);
    }

    @Test
    public void entityGraph() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        //when
        List<Member> result = memberRepository.findAll();
    }

    //사용자 정의 레포지토리의 findMemberCustom()메소드를 불러온다.
    @Test
    public void findMemberCustom() throws Exception {
        List<Member> result = memberRepository.findMemberCustom();
    }

    @Test
    public void basic() throws Exception {
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);
        em.persist(new Member("m1", 0, teamA));
        em.persist(new Member("m2", 0, teamA));
        em.flush();
        //when
        //Probe 생성
        Member member = new Member("m1");
        Team team = new Team("teamA"); //내부조인으로 teamA 가능
        member.setTeam(team);
        //ExampleMatcher 생성, age 프로퍼티는 무시
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("age");
        Example<Member> example = Example.of(member, matcher);
        List<Member> result = memberRepository.findAll(example);
        //then
        assertThat(result.size()).isEqualTo(1);
    }

    //==Projection==//
    //projection 사용 시 select에 엔티티의 모든 필드를 넣는 것이 아니라 특정 필드만 넣어서 sql을 보낼 날려 성능을 최적화 하거나
    //dto 타입으로 return 받을 수 있다.
    @Test
    public void projectionTest() throws Exception {
        Team teamA = new Team("teamA");
        em.persist(teamA);
        em.persist(new Member("m1", 0, teamA));
        em.persist(new Member("m2", 0, teamA));
        em.flush();

        List<UsernameOnlyDto> result = memberRepository.findProjectionsByUsername("m1");
        for (UsernameOnlyDto usernameOnlyDto : result) {
            System.out.println("usernameOnlyDto.getUsername() = " + usernameOnlyDto.getUsername());
        }
    }

    //==NativeQuery==//
    @Test
    public void nativeQuery() throws Exception {
        Team teamA = new Team("teamA");
        em.persist(teamA);
        em.persist(new Member("m1", 0, teamA));
        em.persist(new Member("m2", 0, teamA));
        em.flush();

        Member member = memberRepository.findByNativeQuery("m1");
        System.out.println("member = " + member);
    }


}