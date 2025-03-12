package org.springframework.samples.petclinic.visits.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.visits.model.Visit;
import org.springframework.samples.petclinic.visits.model.VisitRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(VisitResource.class)
@ActiveProfiles("test")
class VisitResourceTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    VisitRepository visitRepository;

    @Test
    void shouldFetchVisits() throws Exception {
        given(visitRepository.findByPetIdIn(Arrays.asList(111, 222)))
            .willReturn(Arrays.asList(
                Visit.VisitBuilder.aVisit().id(1).petId(111).build(),
                Visit.VisitBuilder.aVisit().id(2).petId(222).build(),
                Visit.VisitBuilder.aVisit().id(3).petId(222).build()
            ));

        mvc.perform(get("/pets/visits?petId=111,222"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.items[0].id").value(1))
            .andExpect(jsonPath("$.items[1].id").value(2))
            .andExpect(jsonPath("$.items[2].id").value(3))
            .andExpect(jsonPath("$.items[0].petId").value(111))
            .andExpect(jsonPath("$.items[1].petId").value(222))
            .andExpect(jsonPath("$.items[2].petId").value(222));
    }

    @Test
    void shouldCreateVisit() throws Exception {
        Visit visit = Visit.VisitBuilder.aVisit()
            .id(1)
            .petId(123)
            .description("Test visit")
            .date(new Date())
            .build();

        given(visitRepository.save(any(Visit.class))).willReturn(visit);

        mvc.perform(post("/owners/1/pets/123/visits")
                .contentType(APPLICATION_JSON)
                .content("{\"description\": \"Test visit\", \"date\": \"2023-10-01\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.petId").value(123))
            .andExpect(jsonPath("$.description").value("Test visit"));
    }

    @Test
    void shouldReadVisitsByPetId() throws Exception {
        List<Visit> visits = Arrays.asList(
            Visit.VisitBuilder.aVisit().id(1).petId(123).description("Visit 1").build(),
            Visit.VisitBuilder.aVisit().id(2).petId(123).description("Visit 2").build()
        );

        given(visitRepository.findByPetId(123)).willReturn(visits);

        mvc.perform(get("/owners/1/pets/123/visits"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[0].petId").value(123))
            .andExpect(jsonPath("$[1].petId").value(123))
            .andExpect(jsonPath("$[0].description").value("Visit 1"))
            .andExpect(jsonPath("$[1].description").value("Visit 2"));
    }
}
