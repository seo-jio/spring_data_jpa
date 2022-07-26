package study.datajpa.dto;

import lombok.Data;
import study.datajpa.entity.Member;

@Data
public class MemberDto {
    private Long id;
    private String username;
    private String teamName;

    public MemberDto(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }

    //엔티티를 dto로 변활할 때 편리성을 위하여 엔티티를 파라미터로 받는 생성자 구현
    public MemberDto(Member member){
        this.id = member.getId();
        this.username = member.getUsername();
    }
}
