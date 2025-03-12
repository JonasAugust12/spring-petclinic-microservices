package org.springframework.samples.petclinic.vets.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpecialtyTest {

    @Test
    void testSpecialtyGettersAndSetters() {
        Specialty specialty = new Specialty();
        specialty.setId(1);
        specialty.setName("Dentistry");

        assertEquals(1, specialty.getId());
        assertEquals("Dentistry", specialty.getName());
    }

    @Test
    void testSpecialtyDefaultConstructor() {
        Specialty specialty = new Specialty();
        assertNull(specialty.getId());
        assertNull(specialty.getName());
    }
}
