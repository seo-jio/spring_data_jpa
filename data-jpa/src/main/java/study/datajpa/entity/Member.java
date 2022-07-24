package study.datajpa.entity;

import lombok.*;
import org.springframework.data.web.config.QuerydslWebConfiguration;

import javax.persistence.*;

@Entity
@Getter @Setter
//엔티티는 프록시 기술을 활용하기 때문에 파라미터가 없는 생성자를 protect로 선언해주어야 한다.
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
@NamedQuery(
        name="Member.findByUsername",
        query="select m from Member m where m.username = :username")
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    private String username;

    private int age;

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        this.team = team;
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }
}
