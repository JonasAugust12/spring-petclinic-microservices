package org.springframework.samples.petclinic.visits;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class VisitsServiceApplicationTest {

    @Test
    void contextLoads() {
        assertDoesNotThrow(() -> VisitsServiceApplication.main(new String[] {}));
    }
}
