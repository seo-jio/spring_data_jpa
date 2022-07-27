package study.datajpa.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item implements Persistable<String> {
    //@GeneratedValue : persist를 할 때 pk 값을 생성하여 넣어준다. 그 전까지는 null
    @Id
    private String id;

    @CreatedDate
    private LocalDateTime createdDate;


    //Persistable 인터페이스 사용시 밑에 두 메소드를 필수로 override 해야한다.
    @Override
    public String getId() {
        return id;
    }

    //createdDate 값이 null이라면 새로운 엔티티라는 걸 알려주는 메소드 재정의
    @Override
    public boolean isNew() {
        return createdDate == null;
    }

    public Item(String id) {
        this.id = id;
    }
}
