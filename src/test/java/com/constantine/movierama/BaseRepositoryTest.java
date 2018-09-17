package com.constantine.movierama;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@EnableJpaRepositories(basePackages = "com.constantine.movierama.repositories")
public class BaseRepositoryTest {

    @Test
    void init() {
        // just an empty test just not to let intellij nagging
    }
}
