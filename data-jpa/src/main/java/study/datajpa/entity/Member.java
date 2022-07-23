package study.datajpa.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String username;

    //엔티티는 프록시 기술을 활용하기 때문에 파라미터가 없는 생성자를 protect로 선언해주어야 한다.
    protected Member() {
    }

    public Member(String username) {
        this.username = username;
    }
}
