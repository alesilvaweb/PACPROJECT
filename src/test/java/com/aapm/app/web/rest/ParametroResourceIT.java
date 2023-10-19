package com.aapm.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aapm.app.IntegrationTest;
import com.aapm.app.domain.Parametro;
import com.aapm.app.domain.enumeration.Status;
import com.aapm.app.repository.ParametroRepository;
import com.aapm.app.service.criteria.ParametroCriteria;
import com.aapm.app.service.dto.ParametroDTO;
import com.aapm.app.service.mapper.ParametroMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ParametroResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ParametroResourceIT {

    private static final String DEFAULT_PARAMETRO = "AAAAAAAAAA";
    private static final String UPDATED_PARAMETRO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_CHAVE = "AAAAAAAAAA";
    private static final String UPDATED_CHAVE = "BBBBBBBBBB";

    private static final String DEFAULT_VALOR = "AAAAAAAAAA";
    private static final String UPDATED_VALOR = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.Ativo;
    private static final Status UPDATED_STATUS = Status.Inativo;

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/parametros";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ParametroRepository parametroRepository;

    @Autowired
    private ParametroMapper parametroMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParametroMockMvc;

    private Parametro parametro;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parametro createEntity(EntityManager em) {
        Parametro parametro = new Parametro()
            .parametro(DEFAULT_PARAMETRO)
            .descricao(DEFAULT_DESCRICAO)
            .chave(DEFAULT_CHAVE)
            .valor(DEFAULT_VALOR)
            .status(DEFAULT_STATUS)
            .created(DEFAULT_CREATED)
            .modified(DEFAULT_MODIFIED);
        return parametro;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parametro createUpdatedEntity(EntityManager em) {
        Parametro parametro = new Parametro()
            .parametro(UPDATED_PARAMETRO)
            .descricao(UPDATED_DESCRICAO)
            .chave(UPDATED_CHAVE)
            .valor(UPDATED_VALOR)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);
        return parametro;
    }

    @BeforeEach
    public void initTest() {
        parametro = createEntity(em);
    }

    @Test
    @Transactional
    void createParametro() throws Exception {
        int databaseSizeBeforeCreate = parametroRepository.findAll().size();
        // Create the Parametro
        ParametroDTO parametroDTO = parametroMapper.toDto(parametro);
        restParametroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parametroDTO)))
            .andExpect(status().isCreated());

        // Validate the Parametro in the database
        List<Parametro> parametroList = parametroRepository.findAll();
        assertThat(parametroList).hasSize(databaseSizeBeforeCreate + 1);
        Parametro testParametro = parametroList.get(parametroList.size() - 1);
        assertThat(testParametro.getParametro()).isEqualTo(DEFAULT_PARAMETRO);
        assertThat(testParametro.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testParametro.getChave()).isEqualTo(DEFAULT_CHAVE);
        assertThat(testParametro.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testParametro.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testParametro.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testParametro.getModified()).isEqualTo(DEFAULT_MODIFIED);
    }

    @Test
    @Transactional
    void createParametroWithExistingId() throws Exception {
        // Create the Parametro with an existing ID
        parametro.setId(1L);
        ParametroDTO parametroDTO = parametroMapper.toDto(parametro);

        int databaseSizeBeforeCreate = parametroRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParametroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parametroDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Parametro in the database
        List<Parametro> parametroList = parametroRepository.findAll();
        assertThat(parametroList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkParametroIsRequired() throws Exception {
        int databaseSizeBeforeTest = parametroRepository.findAll().size();
        // set the field null
        parametro.setParametro(null);

        // Create the Parametro, which fails.
        ParametroDTO parametroDTO = parametroMapper.toDto(parametro);

        restParametroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parametroDTO)))
            .andExpect(status().isBadRequest());

        List<Parametro> parametroList = parametroRepository.findAll();
        assertThat(parametroList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValorIsRequired() throws Exception {
        int databaseSizeBeforeTest = parametroRepository.findAll().size();
        // set the field null
        parametro.setValor(null);

        // Create the Parametro, which fails.
        ParametroDTO parametroDTO = parametroMapper.toDto(parametro);

        restParametroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parametroDTO)))
            .andExpect(status().isBadRequest());

        List<Parametro> parametroList = parametroRepository.findAll();
        assertThat(parametroList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllParametros() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get all the parametroList
        restParametroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parametro.getId().intValue())))
            .andExpect(jsonPath("$.[*].parametro").value(hasItem(DEFAULT_PARAMETRO)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].chave").value(hasItem(DEFAULT_CHAVE)))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())));
    }

    @Test
    @Transactional
    void getParametro() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get the parametro
        restParametroMockMvc
            .perform(get(ENTITY_API_URL_ID, parametro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(parametro.getId().intValue()))
            .andExpect(jsonPath("$.parametro").value(DEFAULT_PARAMETRO))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.chave").value(DEFAULT_CHAVE))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.modified").value(DEFAULT_MODIFIED.toString()));
    }

    @Test
    @Transactional
    void getParametrosByIdFiltering() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        Long id = parametro.getId();

        defaultParametroShouldBeFound("id.equals=" + id);
        defaultParametroShouldNotBeFound("id.notEquals=" + id);

        defaultParametroShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultParametroShouldNotBeFound("id.greaterThan=" + id);

        defaultParametroShouldBeFound("id.lessThanOrEqual=" + id);
        defaultParametroShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllParametrosByParametroIsEqualToSomething() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get all the parametroList where parametro equals to DEFAULT_PARAMETRO
        defaultParametroShouldBeFound("parametro.equals=" + DEFAULT_PARAMETRO);

        // Get all the parametroList where parametro equals to UPDATED_PARAMETRO
        defaultParametroShouldNotBeFound("parametro.equals=" + UPDATED_PARAMETRO);
    }

    @Test
    @Transactional
    void getAllParametrosByParametroIsInShouldWork() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get all the parametroList where parametro in DEFAULT_PARAMETRO or UPDATED_PARAMETRO
        defaultParametroShouldBeFound("parametro.in=" + DEFAULT_PARAMETRO + "," + UPDATED_PARAMETRO);

        // Get all the parametroList where parametro equals to UPDATED_PARAMETRO
        defaultParametroShouldNotBeFound("parametro.in=" + UPDATED_PARAMETRO);
    }

    @Test
    @Transactional
    void getAllParametrosByParametroIsNullOrNotNull() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get all the parametroList where parametro is not null
        defaultParametroShouldBeFound("parametro.specified=true");

        // Get all the parametroList where parametro is null
        defaultParametroShouldNotBeFound("parametro.specified=false");
    }

    @Test
    @Transactional
    void getAllParametrosByParametroContainsSomething() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get all the parametroList where parametro contains DEFAULT_PARAMETRO
        defaultParametroShouldBeFound("parametro.contains=" + DEFAULT_PARAMETRO);

        // Get all the parametroList where parametro contains UPDATED_PARAMETRO
        defaultParametroShouldNotBeFound("parametro.contains=" + UPDATED_PARAMETRO);
    }

    @Test
    @Transactional
    void getAllParametrosByParametroNotContainsSomething() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get all the parametroList where parametro does not contain DEFAULT_PARAMETRO
        defaultParametroShouldNotBeFound("parametro.doesNotContain=" + DEFAULT_PARAMETRO);

        // Get all the parametroList where parametro does not contain UPDATED_PARAMETRO
        defaultParametroShouldBeFound("parametro.doesNotContain=" + UPDATED_PARAMETRO);
    }

    @Test
    @Transactional
    void getAllParametrosByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get all the parametroList where descricao equals to DEFAULT_DESCRICAO
        defaultParametroShouldBeFound("descricao.equals=" + DEFAULT_DESCRICAO);

        // Get all the parametroList where descricao equals to UPDATED_DESCRICAO
        defaultParametroShouldNotBeFound("descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllParametrosByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get all the parametroList where descricao in DEFAULT_DESCRICAO or UPDATED_DESCRICAO
        defaultParametroShouldBeFound("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO);

        // Get all the parametroList where descricao equals to UPDATED_DESCRICAO
        defaultParametroShouldNotBeFound("descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllParametrosByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get all the parametroList where descricao is not null
        defaultParametroShouldBeFound("descricao.specified=true");

        // Get all the parametroList where descricao is null
        defaultParametroShouldNotBeFound("descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllParametrosByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get all the parametroList where descricao contains DEFAULT_DESCRICAO
        defaultParametroShouldBeFound("descricao.contains=" + DEFAULT_DESCRICAO);

        // Get all the parametroList where descricao contains UPDATED_DESCRICAO
        defaultParametroShouldNotBeFound("descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllParametrosByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get all the parametroList where descricao does not contain DEFAULT_DESCRICAO
        defaultParametroShouldNotBeFound("descricao.doesNotContain=" + DEFAULT_DESCRICAO);

        // Get all the parametroList where descricao does not contain UPDATED_DESCRICAO
        defaultParametroShouldBeFound("descricao.doesNotContain=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllParametrosByChaveIsEqualToSomething() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get all the parametroList where chave equals to DEFAULT_CHAVE
        defaultParametroShouldBeFound("chave.equals=" + DEFAULT_CHAVE);

        // Get all the parametroList where chave equals to UPDATED_CHAVE
        defaultParametroShouldNotBeFound("chave.equals=" + UPDATED_CHAVE);
    }

    @Test
    @Transactional
    void getAllParametrosByChaveIsInShouldWork() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get all the parametroList where chave in DEFAULT_CHAVE or UPDATED_CHAVE
        defaultParametroShouldBeFound("chave.in=" + DEFAULT_CHAVE + "," + UPDATED_CHAVE);

        // Get all the parametroList where chave equals to UPDATED_CHAVE
        defaultParametroShouldNotBeFound("chave.in=" + UPDATED_CHAVE);
    }

    @Test
    @Transactional
    void getAllParametrosByChaveIsNullOrNotNull() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get all the parametroList where chave is not null
        defaultParametroShouldBeFound("chave.specified=true");

        // Get all the parametroList where chave is null
        defaultParametroShouldNotBeFound("chave.specified=false");
    }

    @Test
    @Transactional
    void getAllParametrosByChaveContainsSomething() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get all the parametroList where chave contains DEFAULT_CHAVE
        defaultParametroShouldBeFound("chave.contains=" + DEFAULT_CHAVE);

        // Get all the parametroList where chave contains UPDATED_CHAVE
        defaultParametroShouldNotBeFound("chave.contains=" + UPDATED_CHAVE);
    }

    @Test
    @Transactional
    void getAllParametrosByChaveNotContainsSomething() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get all the parametroList where chave does not contain DEFAULT_CHAVE
        defaultParametroShouldNotBeFound("chave.doesNotContain=" + DEFAULT_CHAVE);

        // Get all the parametroList where chave does not contain UPDATED_CHAVE
        defaultParametroShouldBeFound("chave.doesNotContain=" + UPDATED_CHAVE);
    }

    @Test
    @Transactional
    void getAllParametrosByValorIsEqualToSomething() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get all the parametroList where valor equals to DEFAULT_VALOR
        defaultParametroShouldBeFound("valor.equals=" + DEFAULT_VALOR);

        // Get all the parametroList where valor equals to UPDATED_VALOR
        defaultParametroShouldNotBeFound("valor.equals=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllParametrosByValorIsInShouldWork() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get all the parametroList where valor in DEFAULT_VALOR or UPDATED_VALOR
        defaultParametroShouldBeFound("valor.in=" + DEFAULT_VALOR + "," + UPDATED_VALOR);

        // Get all the parametroList where valor equals to UPDATED_VALOR
        defaultParametroShouldNotBeFound("valor.in=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllParametrosByValorIsNullOrNotNull() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get all the parametroList where valor is not null
        defaultParametroShouldBeFound("valor.specified=true");

        // Get all the parametroList where valor is null
        defaultParametroShouldNotBeFound("valor.specified=false");
    }

    @Test
    @Transactional
    void getAllParametrosByValorContainsSomething() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get all the parametroList where valor contains DEFAULT_VALOR
        defaultParametroShouldBeFound("valor.contains=" + DEFAULT_VALOR);

        // Get all the parametroList where valor contains UPDATED_VALOR
        defaultParametroShouldNotBeFound("valor.contains=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllParametrosByValorNotContainsSomething() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get all the parametroList where valor does not contain DEFAULT_VALOR
        defaultParametroShouldNotBeFound("valor.doesNotContain=" + DEFAULT_VALOR);

        // Get all the parametroList where valor does not contain UPDATED_VALOR
        defaultParametroShouldBeFound("valor.doesNotContain=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllParametrosByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get all the parametroList where status equals to DEFAULT_STATUS
        defaultParametroShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the parametroList where status equals to UPDATED_STATUS
        defaultParametroShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllParametrosByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get all the parametroList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultParametroShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the parametroList where status equals to UPDATED_STATUS
        defaultParametroShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllParametrosByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get all the parametroList where status is not null
        defaultParametroShouldBeFound("status.specified=true");

        // Get all the parametroList where status is null
        defaultParametroShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllParametrosByCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get all the parametroList where created equals to DEFAULT_CREATED
        defaultParametroShouldBeFound("created.equals=" + DEFAULT_CREATED);

        // Get all the parametroList where created equals to UPDATED_CREATED
        defaultParametroShouldNotBeFound("created.equals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllParametrosByCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get all the parametroList where created in DEFAULT_CREATED or UPDATED_CREATED
        defaultParametroShouldBeFound("created.in=" + DEFAULT_CREATED + "," + UPDATED_CREATED);

        // Get all the parametroList where created equals to UPDATED_CREATED
        defaultParametroShouldNotBeFound("created.in=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllParametrosByCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get all the parametroList where created is not null
        defaultParametroShouldBeFound("created.specified=true");

        // Get all the parametroList where created is null
        defaultParametroShouldNotBeFound("created.specified=false");
    }

    @Test
    @Transactional
    void getAllParametrosByModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get all the parametroList where modified equals to DEFAULT_MODIFIED
        defaultParametroShouldBeFound("modified.equals=" + DEFAULT_MODIFIED);

        // Get all the parametroList where modified equals to UPDATED_MODIFIED
        defaultParametroShouldNotBeFound("modified.equals=" + UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void getAllParametrosByModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get all the parametroList where modified in DEFAULT_MODIFIED or UPDATED_MODIFIED
        defaultParametroShouldBeFound("modified.in=" + DEFAULT_MODIFIED + "," + UPDATED_MODIFIED);

        // Get all the parametroList where modified equals to UPDATED_MODIFIED
        defaultParametroShouldNotBeFound("modified.in=" + UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void getAllParametrosByModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get all the parametroList where modified is not null
        defaultParametroShouldBeFound("modified.specified=true");

        // Get all the parametroList where modified is null
        defaultParametroShouldNotBeFound("modified.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultParametroShouldBeFound(String filter) throws Exception {
        restParametroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parametro.getId().intValue())))
            .andExpect(jsonPath("$.[*].parametro").value(hasItem(DEFAULT_PARAMETRO)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].chave").value(hasItem(DEFAULT_CHAVE)))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())));

        // Check, that the count call also returns 1
        restParametroMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultParametroShouldNotBeFound(String filter) throws Exception {
        restParametroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restParametroMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingParametro() throws Exception {
        // Get the parametro
        restParametroMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingParametro() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        int databaseSizeBeforeUpdate = parametroRepository.findAll().size();

        // Update the parametro
        Parametro updatedParametro = parametroRepository.findById(parametro.getId()).get();
        // Disconnect from session so that the updates on updatedParametro are not directly saved in db
        em.detach(updatedParametro);
        updatedParametro
            .parametro(UPDATED_PARAMETRO)
            .descricao(UPDATED_DESCRICAO)
            .chave(UPDATED_CHAVE)
            .valor(UPDATED_VALOR)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);
        ParametroDTO parametroDTO = parametroMapper.toDto(updatedParametro);

        restParametroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parametroDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parametroDTO))
            )
            .andExpect(status().isOk());

        // Validate the Parametro in the database
        List<Parametro> parametroList = parametroRepository.findAll();
        assertThat(parametroList).hasSize(databaseSizeBeforeUpdate);
        Parametro testParametro = parametroList.get(parametroList.size() - 1);
        assertThat(testParametro.getParametro()).isEqualTo(UPDATED_PARAMETRO);
        assertThat(testParametro.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testParametro.getChave()).isEqualTo(UPDATED_CHAVE);
        assertThat(testParametro.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testParametro.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testParametro.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testParametro.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void putNonExistingParametro() throws Exception {
        int databaseSizeBeforeUpdate = parametroRepository.findAll().size();
        parametro.setId(count.incrementAndGet());

        // Create the Parametro
        ParametroDTO parametroDTO = parametroMapper.toDto(parametro);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParametroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parametroDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parametroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parametro in the database
        List<Parametro> parametroList = parametroRepository.findAll();
        assertThat(parametroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParametro() throws Exception {
        int databaseSizeBeforeUpdate = parametroRepository.findAll().size();
        parametro.setId(count.incrementAndGet());

        // Create the Parametro
        ParametroDTO parametroDTO = parametroMapper.toDto(parametro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParametroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parametroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parametro in the database
        List<Parametro> parametroList = parametroRepository.findAll();
        assertThat(parametroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParametro() throws Exception {
        int databaseSizeBeforeUpdate = parametroRepository.findAll().size();
        parametro.setId(count.incrementAndGet());

        // Create the Parametro
        ParametroDTO parametroDTO = parametroMapper.toDto(parametro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParametroMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parametroDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parametro in the database
        List<Parametro> parametroList = parametroRepository.findAll();
        assertThat(parametroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateParametroWithPatch() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        int databaseSizeBeforeUpdate = parametroRepository.findAll().size();

        // Update the parametro using partial update
        Parametro partialUpdatedParametro = new Parametro();
        partialUpdatedParametro.setId(parametro.getId());

        partialUpdatedParametro
            .parametro(UPDATED_PARAMETRO)
            .descricao(UPDATED_DESCRICAO)
            .chave(UPDATED_CHAVE)
            .valor(UPDATED_VALOR)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED);

        restParametroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParametro.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParametro))
            )
            .andExpect(status().isOk());

        // Validate the Parametro in the database
        List<Parametro> parametroList = parametroRepository.findAll();
        assertThat(parametroList).hasSize(databaseSizeBeforeUpdate);
        Parametro testParametro = parametroList.get(parametroList.size() - 1);
        assertThat(testParametro.getParametro()).isEqualTo(UPDATED_PARAMETRO);
        assertThat(testParametro.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testParametro.getChave()).isEqualTo(UPDATED_CHAVE);
        assertThat(testParametro.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testParametro.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testParametro.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testParametro.getModified()).isEqualTo(DEFAULT_MODIFIED);
    }

    @Test
    @Transactional
    void fullUpdateParametroWithPatch() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        int databaseSizeBeforeUpdate = parametroRepository.findAll().size();

        // Update the parametro using partial update
        Parametro partialUpdatedParametro = new Parametro();
        partialUpdatedParametro.setId(parametro.getId());

        partialUpdatedParametro
            .parametro(UPDATED_PARAMETRO)
            .descricao(UPDATED_DESCRICAO)
            .chave(UPDATED_CHAVE)
            .valor(UPDATED_VALOR)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);

        restParametroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParametro.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParametro))
            )
            .andExpect(status().isOk());

        // Validate the Parametro in the database
        List<Parametro> parametroList = parametroRepository.findAll();
        assertThat(parametroList).hasSize(databaseSizeBeforeUpdate);
        Parametro testParametro = parametroList.get(parametroList.size() - 1);
        assertThat(testParametro.getParametro()).isEqualTo(UPDATED_PARAMETRO);
        assertThat(testParametro.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testParametro.getChave()).isEqualTo(UPDATED_CHAVE);
        assertThat(testParametro.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testParametro.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testParametro.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testParametro.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingParametro() throws Exception {
        int databaseSizeBeforeUpdate = parametroRepository.findAll().size();
        parametro.setId(count.incrementAndGet());

        // Create the Parametro
        ParametroDTO parametroDTO = parametroMapper.toDto(parametro);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParametroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, parametroDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parametroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parametro in the database
        List<Parametro> parametroList = parametroRepository.findAll();
        assertThat(parametroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParametro() throws Exception {
        int databaseSizeBeforeUpdate = parametroRepository.findAll().size();
        parametro.setId(count.incrementAndGet());

        // Create the Parametro
        ParametroDTO parametroDTO = parametroMapper.toDto(parametro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParametroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parametroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parametro in the database
        List<Parametro> parametroList = parametroRepository.findAll();
        assertThat(parametroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParametro() throws Exception {
        int databaseSizeBeforeUpdate = parametroRepository.findAll().size();
        parametro.setId(count.incrementAndGet());

        // Create the Parametro
        ParametroDTO parametroDTO = parametroMapper.toDto(parametro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParametroMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(parametroDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parametro in the database
        List<Parametro> parametroList = parametroRepository.findAll();
        assertThat(parametroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParametro() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        int databaseSizeBeforeDelete = parametroRepository.findAll().size();

        // Delete the parametro
        restParametroMockMvc
            .perform(delete(ENTITY_API_URL_ID, parametro.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Parametro> parametroList = parametroRepository.findAll();
        assertThat(parametroList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
