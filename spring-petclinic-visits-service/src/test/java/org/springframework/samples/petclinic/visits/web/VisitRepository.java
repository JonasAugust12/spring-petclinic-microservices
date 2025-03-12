package org.springframework.samples.petclinic.visits.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class VisitRepositoryTest {

    @Autowired
    VisitRepository visitRepository;

    @Test
    void testFindByPetId() {
        Visit visit = new Visit();
        visit.setPetId(1);
        visit.setDescription("Checkup");
        visit.setDate(new Date());
        visitRepository.save(visit);

        List<Visit> visits = visitRepository.findByPetId(1);
        assertFalse(visits.isEmpty());
        assertEquals(1, visits.get(0).getPetId());
    }

    @Test
    void testFindByPetIdIn() {
        Visit visit1 = new Visit();
        visit1.setPetId(1);
        visit1.setDescription("Vaccine");
        visit1.setDate(new Date());

        Visit visit2 = new Visit();
        visit2.setPetId(2);
        visit2.setDescription("Surgery");
        visit2.setDate(new Date());

        visitRepository.saveAll(Arrays.asList(visit1, visit2));

        List<Visit> visits = visitRepository.findByPetIdIn(Arrays.asList(1, 2));
        assertEquals(2, visits.size());
    }
}
