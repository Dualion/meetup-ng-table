package com.dualion.meetup.web.rest;

import com.dualion.meetup.MeetupApp;

import com.dualion.meetup.domain.Hijo;
import com.dualion.meetup.repository.HijoRepository;
import com.dualion.meetup.service.HijoService;
import com.dualion.meetup.service.dto.HijoDTO;
import com.dualion.meetup.service.mapper.HijoMapper;

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
 * Test class for the HijoResource REST controller.
 *
 * @see HijoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MeetupApp.class)
public class HijoResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAA";
    private static final String UPDATED_NOMBRE = "BBBBB";

    private static final String DEFAULT_APELLIDOS = "AAAAA";
    private static final String UPDATED_APELLIDOS = "BBBBB";

    @Inject
    private HijoRepository hijoRepository;

    @Inject
    private HijoMapper hijoMapper;

    @Inject
    private HijoService hijoService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restHijoMockMvc;

    private Hijo hijo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HijoResource hijoResource = new HijoResource();
        ReflectionTestUtils.setField(hijoResource, "hijoService", hijoService);
        this.restHijoMockMvc = MockMvcBuilders.standaloneSetup(hijoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hijo createEntity(EntityManager em) {
        Hijo hijo = new Hijo()
                .nombre(DEFAULT_NOMBRE)
                .apellidos(DEFAULT_APELLIDOS);
        return hijo;
    }

    @Before
    public void initTest() {
        hijo = createEntity(em);
    }

    @Test
    @Transactional
    public void createHijo() throws Exception {
        int databaseSizeBeforeCreate = hijoRepository.findAll().size();

        // Create the Hijo
        HijoDTO hijoDTO = hijoMapper.hijoToHijoDTO(hijo);

        restHijoMockMvc.perform(post("/api/hijos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hijoDTO)))
                .andExpect(status().isCreated());

        // Validate the Hijo in the database
        List<Hijo> hijos = hijoRepository.findAll();
        assertThat(hijos).hasSize(databaseSizeBeforeCreate + 1);
        Hijo testHijo = hijos.get(hijos.size() - 1);
        assertThat(testHijo.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testHijo.getApellidos()).isEqualTo(DEFAULT_APELLIDOS);
    }

    @Test
    @Transactional
    public void getAllHijos() throws Exception {
        // Initialize the database
        hijoRepository.saveAndFlush(hijo);

        // Get all the hijos
        restHijoMockMvc.perform(get("/api/hijos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(hijo.getId().intValue())))
                .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
                .andExpect(jsonPath("$.[*].apellidos").value(hasItem(DEFAULT_APELLIDOS.toString())));
    }

    @Test
    @Transactional
    public void getHijo() throws Exception {
        // Initialize the database
        hijoRepository.saveAndFlush(hijo);

        // Get the hijo
        restHijoMockMvc.perform(get("/api/hijos/{id}", hijo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(hijo.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.apellidos").value(DEFAULT_APELLIDOS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHijo() throws Exception {
        // Get the hijo
        restHijoMockMvc.perform(get("/api/hijos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHijo() throws Exception {
        // Initialize the database
        hijoRepository.saveAndFlush(hijo);
        int databaseSizeBeforeUpdate = hijoRepository.findAll().size();

        // Update the hijo
        Hijo updatedHijo = hijoRepository.findOne(hijo.getId());
        updatedHijo
                .nombre(UPDATED_NOMBRE)
                .apellidos(UPDATED_APELLIDOS);
        HijoDTO hijoDTO = hijoMapper.hijoToHijoDTO(updatedHijo);

        restHijoMockMvc.perform(put("/api/hijos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hijoDTO)))
                .andExpect(status().isOk());

        // Validate the Hijo in the database
        List<Hijo> hijos = hijoRepository.findAll();
        assertThat(hijos).hasSize(databaseSizeBeforeUpdate);
        Hijo testHijo = hijos.get(hijos.size() - 1);
        assertThat(testHijo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testHijo.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    public void deleteHijo() throws Exception {
        // Initialize the database
        hijoRepository.saveAndFlush(hijo);
        int databaseSizeBeforeDelete = hijoRepository.findAll().size();

        // Get the hijo
        restHijoMockMvc.perform(delete("/api/hijos/{id}", hijo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Hijo> hijos = hijoRepository.findAll();
        assertThat(hijos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
