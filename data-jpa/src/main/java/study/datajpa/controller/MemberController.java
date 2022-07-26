package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id){
        Member findMember = memberRepository.findById(id).get();
        return findMember.getUsername();
    }

    //클래스 컨버터가 pk값을 보고 엔티티로 바로 받아와 준다.(사용을 권장하진 않는다)
    //오직 조회용으로만 사용해야한다.
    @GetMapping("/member2/{id}")
    public String findMember2(@PathVariable("id") Member member){
        return member.getUsername();
    }

    //@PageableDefault를 사용하여 특정 메소드에서의 페이지 전략을 지정
    //전체 페이지 전략 지정은 application.yml에 존재
    //api에로 return할 때는 무조건 dto를 return 해야한다.
    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 5) Pageable pageable){
        Page<Member> members = memberRepository.findAll(pageable);
        Page<MemberDto> map = members.map(m -> new MemberDto(m));  //엔티티 -> dto 변환
        return map;
    }

    @PostConstruct
    public void init(){
        for(int i = 0; i < 100; i++){
            memberRepository.save(new Member("user" + i, i));
        }
    }
}
