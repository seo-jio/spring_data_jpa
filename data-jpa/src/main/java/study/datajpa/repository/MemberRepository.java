package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    //쿼리 메소드 사용 시 엔티티의 필드명이 변경되면 애플리케이션 로딩 시점에 오류를 인지할 수 있다는 장점을 지닌다.
    //그러나 필요로 하는 파라미터(필드)가 너무 많을 경우 메소드 이름이 너무 길어지는 단점이 존재
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    //named query 사용, 그러나 실무에선 사용하지 않는다.
    List<Member> findByUsername(@Param("username") String username);

    //application 로딩 시점에 jpql을 파싱하기 때문에 문법오류(철자오류)가 있을 경우 오류를 발생시킨다는 막대한 장점을 지님
    //파라미터가 많아도 메소드 이름을 짧게 지을 수 있다.
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUserNameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    //파라미터에 List가 아닌 Collection을 주면 set, list 등 모두 가능하기 때문에 사용
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> name);

    //==반환 타입==//
    //컬렉션 : 결과 없을 경우 빈 컬렉션을 반환
    //엔티티 : 결과 없을 겨우 null을 반환
    //조회 시 결과가 있을 수도 있고 없을 수도 있다면 Optional<>을 사용하자!


    //==페이징==//
    //Slice를 상속받은게 Page, Slice의 기능들은 강의자료 참고
    //여러 테이블이 조인되는 경우라면 total 카운트를 구할 때 이를 paging 쿼리와 totalCount 쿼리를 분리하여 따로 지정할 수 있다.
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m.username) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);
}
