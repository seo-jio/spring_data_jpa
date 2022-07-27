package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Item;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemRepositoryTest {

    @Autowired ItemRepository itemRepository;

    //spring data repository 사용 시 기본적으로 주로 조회 기능이 많아 @Transactional(readOnly)가 걸려있고 수정 메소드에만 따로 @Transactional이 붙어있다.
    //spring data repository의 save 메소드는 isNew()메소드를 통해 새로운 엔티티면 em.persist, 원래 있던 엔티티면 em.merge를 호출 하는데
    //merge는 인자로 들어온 객체가 db에 있는지를 확인하는 select 쿼리가 나가서 좋지 않다.
    //엔티티의 pk를 @GeneratedValue를 사용하지 않는다면 엔티티 생성 시 id 값을 넣어주기 때문에 save할 때 이미 있던 엔티티라고 분류되어
    //이 경우 엔티티의 isNew()함수를 재정의 해주어야 한다.
    @Test
    public void ItemRepositoryTest() throws Exception {
        Item item = new Item("A");
        itemRepository.save(item);
    }
}