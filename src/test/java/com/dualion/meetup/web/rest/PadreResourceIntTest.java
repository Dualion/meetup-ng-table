package com.dualion.meetup.web.rest;

import com.dualion.meetup.MeetupApp;

import com.dualion.meetup.domain.Padre;
import com.dualion.meetup.repository.PadreRepository;
import com.dualion.meetup.service.PadreService;
import com.dualion.meetup.service.dto.PadreDTO;
import com.dualion.meetup.service.mapper.PadreMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PadreResource REST controller.
 *
 * @see PadreResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MeetupApp.class)
public class PadreResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAA";
    private static final String UPDATED_NOMBRE = "BBBBB";

    private static final String DEFAULT_APELLIDOS = "AAAAA";
    private static final String UPDATED_APELLIDOS = "BBBBB";

    @Inject
    private PadreRepository padreRepository;

    @Inject
    private PadreMapper padreMapper;

    @Inject
    private PadreService padreService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPadreMockMvc;

    private Padre padre;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PadreResource padreResource = new PadreResource();
        ReflectionTestUtils.setField(padreResource, "padreService", padreService);
        this.restPadreMockMvc = MockMvcBuilders.standaloneSetup(padreResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Padre createEntity(EntityManager em) {
        Padre padre = new Padre()
                .nombre(DEFAULT_NOMBRE)
                .apellidos(DEFAULT_APELLIDOS);
        return padre;
    }

    @Before
    public void initTest() {
        padre = createEntity(em);
    }

    @Test
    @Transactional
    public void createPadre() throws Exception {
        int databaseSizeBeforeCreate = padreRepository.findAll().size();

        // Create the Padre
        PadreDTO padreDTO = padreMapper.padreToPadreDTO(padre);

        restPadreMockMvc.perform(post("/api/padres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(padreDTO)))
                .andExpect(status().isCreated());

        // Validate the Padre in the database
        List<Padre> padres = padreRepository.findAll();
        assertThat(padres).hasSize(databaseSizeBeforeCreate + 1);
        Padre testPadre = padres.get(padres.size() - 1);
        assertThat(testPadre.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPadre.getApellidos()).isEqualTo(DEFAULT_APELLIDOS);
    }

    @Test
    @Transactional
    public void getAllPadres() throws Exception {
        // Initialize the database
        padreRepository.saveAndFlush(padre);

        // Get all the padres
        restPadreMockMvc.perform(get("/api/padres?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(padre.getId().intValue())))
                .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
                .andExpect(jsonPath("$.[*].apellidos").value(hasItem(DEFAULT_APELLIDOS.toString())));
    }

    @Test
    @Transactional
    public void getPadre() throws Exception {
        // Initialize the database
        padreRepository.saveAndFlush(padre);

        // Get the padre
        restPadreMockMvc.perform(get("/api/padres/{id}", padre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(padre.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.apellidos").value(DEFAULT_APELLIDOS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPadre() throws Exception {
        // Get the padre
        restPadreMockMvc.perform(get("/api/padres/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePadre() throws Exception {
        // Initialize the database
        padreRepository.saveAndFlush(padre);
        int databaseSizeBeforeUpdate = padreRepository.findAll().size();

        // Update the padre
        Padre updatedPadre = padreRepository.findOne(padre.getId());
        updatedPadre
                .nombre(UPDATED_NOMBRE)
                .apellidos(UPDATED_APELLIDOS);
        PadreDTO padreDTO = padreMapper.padreToPadreDTO(updatedPadre);

        restPadreMockMvc.perform(put("/api/padres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(padreDTO)))
                .andExpect(status().isOk());

        // Validate the Padre in the database
        List<Padre> padres = padreRepository.findAll();
        assertThat(padres).hasSize(databaseSizeBeforeUpdate);
        Padre testPadre = padres.get(padres.size() - 1);
        assertThat(testPadre.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPadre.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    public void deletePadre() throws Exception {
        // Initialize the database
        padreRepository.saveAndFlush(padre);
        int databaseSizeBeforeDelete = padreRepository.findAll().size();

        // Get the padre
        restPadreMockMvc.perform(delete("/api/padres/{id}", padre.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Padre> padres = padreRepository.findAll();
        assertThat(padres).hasSize(databaseSizeBeforeDelete - 1);
    }
}
