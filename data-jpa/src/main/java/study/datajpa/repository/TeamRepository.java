package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Team;

//<타입(엔티티), Id(엔티티의 pk 타입)>
//스프링이 구현체를 proxy로 생성하여 제공함.
public interface TeamRepository extends JpaRepository<Team, Long> {
}
