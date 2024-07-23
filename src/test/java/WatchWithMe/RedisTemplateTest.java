package WatchWithMe;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class RedisTemplateTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void testStrings() {
        // given: 테스트 준비 단계, Redis에서 값을 설정하고 가져오기 위한 ValueOperations 객체를 생성합니다.
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        // 사용할 Redis 키를 정의합니다.
        String key = "stringKey";

        // when: 테스트 실행 단계, 정의한 키에 값을 설정합니다.
        valueOperations.set(key, "hello");

        // then: 테스트 검증 단계, Redis에서 값을 가져와서 예상한 값과 일치하는지 확인합니다.
        String value = valueOperations.get(key);
        // 가져온 값이 "hello"와 같은지 검증합니다.
        assertThat(value).isEqualTo("hello");
    }


    @Test
    void testSet() {
        // given
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        String key = "setKey";

        // when
        setOperations.add(key, "h", "e", "l", "l", "o");

        // then
        Set<String> members = setOperations.members(key);
        Long size = setOperations.size(key);

        assertThat(members).containsOnly("h", "e", "l", "o");
        assertThat(size).isEqualTo(4);
    }

    @Test
    void testHash() {
        // given
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        String key = "hashKey";

        // when
        hashOperations.put(key, "hello", "world");

        // then
        Object value = hashOperations.get(key, "hello");
        assertThat(value).isEqualTo("world");

        Map<Object, Object> entries = hashOperations.entries(key);
        assertThat(entries.keySet()).containsExactly("hello");
        assertThat(entries.values()).containsExactly("world");

        Long size = hashOperations.size(key);
        assertThat(size).isEqualTo(entries.size());
    }
}

