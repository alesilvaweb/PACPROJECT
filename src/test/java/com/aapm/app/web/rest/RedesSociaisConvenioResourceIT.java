package com.aapm.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aapm.app.IntegrationTest;
import com.aapm.app.domain.Convenio;
import com.aapm.app.domain.IconsRedesSociais;
import com.aapm.app.domain.RedesSociaisConvenio;
import com.aapm.app.repository.RedesSociaisConvenioRepository;
import com.aapm.app.service.RedesSociaisConvenioService;
import com.aapm.app.service.criteria.RedesSociaisConvenioCriteria;
import com.aapm.app.service.dto.RedesSociaisConvenioDTO;
import com.aapm.app.service.mapper.RedesSociaisConvenioMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RedesSociaisConvenioResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RedesSociaisConvenioResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_ENDERECO = "AAAAAAAAAA";
    private static final String UPDATED_ENDERECO = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/redes-sociais-convenios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RedesSociaisConvenioRepository redesSociaisConvenioRepository;

    @Mock
    private RedesSociaisConvenioRepository redesSociaisConvenioRepositoryMock;

    @Autowired
    private RedesSociaisConvenioMapper redesSociaisConvenioMapper;

    @Mock
    private RedesSociaisConvenioService redesSociaisConvenioServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRedesSociaisConvenioMockMvc;

    private RedesSociaisConvenio redesSociaisConvenio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RedesSociaisConvenio createEntity(EntityManager em) {
        RedesSociaisConvenio redesSociaisConvenio = new RedesSociaisConvenio()
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO)
            .endereco(DEFAULT_ENDERECO)
            .created(DEFAULT_CREATED)
            .modified(DEFAULT_MODIFIED);
        return redesSociaisConvenio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RedesSociaisConvenio createUpdatedEntity(EntityManager em) {
        RedesSociaisConvenio redesSociaisConvenio = new RedesSociaisConvenio()
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .endereco(UPDATED_ENDERECO)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);
        return redesSociaisConvenio;
    }

    @BeforeEach
    public void initTest() {
        redesSociaisConvenio = createEntity(em);
    }

    @Test
    @Transactional
    void createRedesSociaisConvenio() throws Exception {
        int databaseSizeBeforeCreate = redesSociaisConvenioRepository.findAll().size();
        // Create the RedesSociaisConvenio
        RedesSociaisConvenioDTO redesSociaisConvenioDTO = redesSociaisConvenioMapper.toDto(redesSociaisConvenio);
        restRedesSociaisConvenioMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(redesSociaisConvenioDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RedesSociaisConvenio in the database
        List<RedesSociaisConvenio> redesSociaisConvenioList = redesSociaisConvenioRepository.findAll();
        assertThat(redesSociaisConvenioList).hasSize(databaseSizeBeforeCreate + 1);
        RedesSociaisConvenio testRedesSociaisConvenio = redesSociaisConvenioList.get(redesSociaisConvenioList.size() - 1);
        assertThat(testRedesSociaisConvenio.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testRedesSociaisConvenio.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testRedesSociaisConvenio.getEndereco()).isEqualTo(DEFAULT_ENDERECO);
        assertThat(testRedesSociaisConvenio.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testRedesSociaisConvenio.getModified()).isEqualTo(DEFAULT_MODIFIED);
    }

    @Test
    @Transactional
    void createRedesSociaisConvenioWithExistingId() throws Exception {
        // Create the RedesSociaisConvenio with an existing ID
        redesSociaisConvenio.setId(1L);
        RedesSociaisConvenioDTO redesSociaisConvenioDTO = redesSociaisConvenioMapper.toDto(redesSociaisConvenio);

        int databaseSizeBeforeCreate = redesSociaisConvenioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRedesSociaisConvenioMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(redesSociaisConvenioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RedesSociaisConvenio in the database
        List<RedesSociaisConvenio> redesSociaisConvenioList = redesSociaisConvenioRepository.findAll();
        assertThat(redesSociaisConvenioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = redesSociaisConvenioRepository.findAll().size();
        // set the field null
        redesSociaisConvenio.setNome(null);

        // Create the RedesSociaisConvenio, which fails.
        RedesSociaisConvenioDTO redesSociaisConvenioDTO = redesSociaisConvenioMapper.toDto(redesSociaisConvenio);

        restRedesSociaisConvenioMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(redesSociaisConvenioDTO))
            )
            .andExpect(status().isBadRequest());

        List<RedesSociaisConvenio> redesSociaisConvenioList = redesSociaisConvenioRepository.findAll();
        assertThat(redesSociaisConvenioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEnderecoIsRequired() throws Exception {
        int databaseSizeBeforeTest = redesSociaisConvenioRepository.findAll().size();
        // set the field null
        redesSociaisConvenio.setEndereco(null);

        // Create the RedesSociaisConvenio, which fails.
        RedesSociaisConvenioDTO redesSociaisConvenioDTO = redesSociaisConvenioMapper.toDto(redesSociaisConvenio);

        restRedesSociaisConvenioMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(redesSociaisConvenioDTO))
            )
            .andExpect(status().isBadRequest());

        List<RedesSociaisConvenio> redesSociaisConvenioList = redesSociaisConvenioRepository.findAll();
        assertThat(redesSociaisConvenioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRedesSociaisConvenios() throws Exception {
        // Initialize the database
        redesSociaisConvenioRepository.saveAndFlush(redesSociaisConvenio);

        // Get all the redesSociaisConvenioList
        restRedesSociaisConvenioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(redesSociaisConvenio.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRedesSociaisConveniosWithEagerRelationshipsIsEnabled() throws Exception {
        when(redesSociaisConvenioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRedesSociaisConvenioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(redesSociaisConvenioServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRedesSociaisConveniosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(redesSociaisConvenioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRedesSociaisConvenioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(redesSociaisConvenioRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getRedesSociaisConvenio() throws Exception {
        // Initialize the database
        redesSociaisConvenioRepository.saveAndFlush(redesSociaisConvenio);

        // Get the redesSociaisConvenio
        restRedesSociaisConvenioMockMvc
            .perform(get(ENTITY_API_URL_ID, redesSociaisConvenio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(redesSociaisConvenio.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.endereco").value(DEFAULT_ENDERECO))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.modified").value(DEFAULT_MODIFIED.toString()));
    }

    @Test
    @Transactional
    void getRedesSociaisConveniosByIdFiltering() throws Exception {
        // Initialize the database
        redesSociaisConvenioRepository.saveAndFlush(redesSociaisConvenio);

        Long id = redesSociaisConvenio.getId();

        defaultRedesSociaisConvenioShouldBeFound("id.equals=" + id);
        defaultRedesSociaisConvenioShouldNotBeFound("id.notEquals=" + id);

        defaultRedesSociaisConvenioShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRedesSociaisConvenioShouldNotBeFound("id.greaterThan=" + id);

        defaultRedesSociaisConvenioShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRedesSociaisConvenioShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRedesSociaisConveniosByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        redesSociaisConvenioRepository.saveAndFlush(redesSociaisConvenio);

        // Get all the redesSociaisConvenioList where nome equals to DEFAULT_NOME
        defaultRedesSociaisConvenioShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the redesSociaisConvenioList where nome equals to UPDATED_NOME
        defaultRedesSociaisConvenioShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllRedesSociaisConveniosByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        redesSociaisConvenioRepository.saveAndFlush(redesSociaisConvenio);

        // Get all the redesSociaisConvenioList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultRedesSociaisConvenioShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the redesSociaisConvenioList where nome equals to UPDATED_NOME
        defaultRedesSociaisConvenioShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllRedesSociaisConveniosByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        redesSociaisConvenioRepository.saveAndFlush(redesSociaisConvenio);

        // Get all the redesSociaisConvenioList where nome is not null
        defaultRedesSociaisConvenioShouldBeFound("nome.specified=true");

        // Get all the redesSociaisConvenioList where nome is null
        defaultRedesSociaisConvenioShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllRedesSociaisConveniosByNomeContainsSomething() throws Exception {
        // Initialize the database
        redesSociaisConvenioRepository.saveAndFlush(redesSociaisConvenio);

        // Get all the redesSociaisConvenioList where nome contains DEFAULT_NOME
        defaultRedesSociaisConvenioShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the redesSociaisConvenioList where nome contains UPDATED_NOME
        defaultRedesSociaisConvenioShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllRedesSociaisConveniosByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        redesSociaisConvenioRepository.saveAndFlush(redesSociaisConvenio);

        // Get all the redesSociaisConvenioList where nome does not contain DEFAULT_NOME
        defaultRedesSociaisConvenioShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the redesSociaisConvenioList where nome does not contain UPDATED_NOME
        defaultRedesSociaisConvenioShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllRedesSociaisConveniosByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        redesSociaisConvenioRepository.saveAndFlush(redesSociaisConvenio);

        // Get all the redesSociaisConvenioList where descricao equals to DEFAULT_DESCRICAO
        defaultRedesSociaisConvenioShouldBeFound("descricao.equals=" + DEFAULT_DESCRICAO);

        // Get all the redesSociaisConvenioList where descricao equals to UPDATED_DESCRICAO
        defaultRedesSociaisConvenioShouldNotBeFound("descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllRedesSociaisConveniosByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        redesSociaisConvenioRepository.saveAndFlush(redesSociaisConvenio);

        // Get all the redesSociaisConvenioList where descricao in DEFAULT_DESCRICAO or UPDATED_DESCRICAO
        defaultRedesSociaisConvenioShouldBeFound("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO);

        // Get all the redesSociaisConvenioList where descricao equals to UPDATED_DESCRICAO
        defaultRedesSociaisConvenioShouldNotBeFound("descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllRedesSociaisConveniosByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        redesSociaisConvenioRepository.saveAndFlush(redesSociaisConvenio);

        // Get all the redesSociaisConvenioList where descricao is not null
        defaultRedesSociaisConvenioShouldBeFound("descricao.specified=true");

        // Get all the redesSociaisConvenioList where descricao is null
        defaultRedesSociaisConvenioShouldNotBeFound("descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllRedesSociaisConveniosByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        redesSociaisConvenioRepository.saveAndFlush(redesSociaisConvenio);

        // Get all the redesSociaisConvenioList where descricao contains DEFAULT_DESCRICAO
        defaultRedesSociaisConvenioShouldBeFound("descricao.contains=" + DEFAULT_DESCRICAO);

        // Get all the redesSociaisConvenioList where descricao contains UPDATED_DESCRICAO
        defaultRedesSociaisConvenioShouldNotBeFound("descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllRedesSociaisConveniosByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        redesSociaisConvenioRepository.saveAndFlush(redesSociaisConvenio);

        // Get all the redesSociaisConvenioList where descricao does not contain DEFAULT_DESCRICAO
        defaultRedesSociaisConvenioShouldNotBeFound("descricao.doesNotContain=" + DEFAULT_DESCRICAO);

        // Get all the redesSociaisConvenioList where descricao does not contain UPDATED_DESCRICAO
        defaultRedesSociaisConvenioShouldBeFound("descricao.doesNotContain=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllRedesSociaisConveniosByEnderecoIsEqualToSomething() throws Exception {
        // Initialize the database
        redesSociaisConvenioRepository.saveAndFlush(redesSociaisConvenio);

        // Get all the redesSociaisConvenioList where endereco equals to DEFAULT_ENDERECO
        defaultRedesSociaisConvenioShouldBeFound("endereco.equals=" + DEFAULT_ENDERECO);

        // Get all the redesSociaisConvenioList where endereco equals to UPDATED_ENDERECO
        defaultRedesSociaisConvenioShouldNotBeFound("endereco.equals=" + UPDATED_ENDERECO);
    }

    @Test
    @Transactional
    void getAllRedesSociaisConveniosByEnderecoIsInShouldWork() throws Exception {
        // Initialize the database
        redesSociaisConvenioRepository.saveAndFlush(redesSociaisConvenio);

        // Get all the redesSociaisConvenioList where endereco in DEFAULT_ENDERECO or UPDATED_ENDERECO
        defaultRedesSociaisConvenioShouldBeFound("endereco.in=" + DEFAULT_ENDERECO + "," + UPDATED_ENDERECO);

        // Get all the redesSociaisConvenioList where endereco equals to UPDATED_ENDERECO
        defaultRedesSociaisConvenioShouldNotBeFound("endereco.in=" + UPDATED_ENDERECO);
    }

    @Test
    @Transactional
    void getAllRedesSociaisConveniosByEnderecoIsNullOrNotNull() throws Exception {
        // Initialize the database
        redesSociaisConvenioRepository.saveAndFlush(redesSociaisConvenio);

        // Get all the redesSociaisConvenioList where endereco is not null
        defaultRedesSociaisConvenioShouldBeFound("endereco.specified=true");

        // Get all the redesSociaisConvenioList where endereco is null
        defaultRedesSociaisConvenioShouldNotBeFound("endereco.specified=false");
    }

    @Test
    @Transactional
    void getAllRedesSociaisConveniosByEnderecoContainsSomething() throws Exception {
        // Initialize the database
        redesSociaisConvenioRepository.saveAndFlush(redesSociaisConvenio);

        // Get all the redesSociaisConvenioList where endereco contains DEFAULT_ENDERECO
        defaultRedesSociaisConvenioShouldBeFound("endereco.contains=" + DEFAULT_ENDERECO);

        // Get all the redesSociaisConvenioList where endereco contains UPDATED_ENDERECO
        defaultRedesSociaisConvenioShouldNotBeFound("endereco.contains=" + UPDATED_ENDERECO);
    }

    @Test
    @Transactional
    void getAllRedesSociaisConveniosByEnderecoNotContainsSomething() throws Exception {
        // Initialize the database
        redesSociaisConvenioRepository.saveAndFlush(redesSociaisConvenio);

        // Get all the redesSociaisConvenioList where endereco does not contain DEFAULT_ENDERECO
        defaultRedesSociaisConvenioShouldNotBeFound("endereco.doesNotContain=" + DEFAULT_ENDERECO);

        // Get all the redesSociaisConvenioList where endereco does not contain UPDATED_ENDERECO
        defaultRedesSociaisConvenioShouldBeFound("endereco.doesNotContain=" + UPDATED_ENDERECO);
    }

    @Test
    @Transactional
    void getAllRedesSociaisConveniosByCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        redesSociaisConvenioRepository.saveAndFlush(redesSociaisConvenio);

        // Get all the redesSociaisConvenioList where created equals to DEFAULT_CREATED
        defaultRedesSociaisConvenioShouldBeFound("created.equals=" + DEFAULT_CREATED);

        // Get all the redesSociaisConvenioList where created equals to UPDATED_CREATED
        defaultRedesSociaisConvenioShouldNotBeFound("created.equals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllRedesSociaisConveniosByCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        redesSociaisConvenioRepository.saveAndFlush(redesSociaisConvenio);

        // Get all the redesSociaisConvenioList where created in DEFAULT_CREATED or UPDATED_CREATED
        defaultRedesSociaisConvenioShouldBeFound("created.in=" + DEFAULT_CREATED + "," + UPDATED_CREATED);

        // Get all the redesSociaisConvenioList where created equals to UPDATED_CREATED
        defaultRedesSociaisConvenioShouldNotBeFound("created.in=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllRedesSociaisConveniosByCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        redesSociaisConvenioRepository.saveAndFlush(redesSociaisConvenio);

        // Get all the redesSociaisConvenioList where created is not null
        defaultRedesSociaisConvenioShouldBeFound("created.specified=true");

        // Get all the redesSociaisConvenioList where created is null
        defaultRedesSociaisConvenioShouldNotBeFound("created.specified=false");
    }

    @Test
    @Transactional
    void getAllRedesSociaisConveniosByModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        redesSociaisConvenioRepository.saveAndFlush(redesSociaisConvenio);

        // Get all the redesSociaisConvenioList where modified equals to DEFAULT_MODIFIED
        defaultRedesSociaisConvenioShouldBeFound("modified.equals=" + DEFAULT_MODIFIED);

        // Get all the redesSociaisConvenioList where modified equals to UPDATED_MODIFIED
        defaultRedesSociaisConvenioShouldNotBeFound("modified.equals=" + UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void getAllRedesSociaisConveniosByModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        redesSociaisConvenioRepository.saveAndFlush(redesSociaisConvenio);

        // Get all the redesSociaisConvenioList where modified in DEFAULT_MODIFIED or UPDATED_MODIFIED
        defaultRedesSociaisConvenioShouldBeFound("modified.in=" + DEFAULT_MODIFIED + "," + UPDATED_MODIFIED);

        // Get all the redesSociaisConvenioList where modified equals to UPDATED_MODIFIED
        defaultRedesSociaisConvenioShouldNotBeFound("modified.in=" + UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void getAllRedesSociaisConveniosByModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        redesSociaisConvenioRepository.saveAndFlush(redesSociaisConvenio);

        // Get all the redesSociaisConvenioList where modified is not null
        defaultRedesSociaisConvenioShouldBeFound("modified.specified=true");

        // Get all the redesSociaisConvenioList where modified is null
        defaultRedesSociaisConvenioShouldNotBeFound("modified.specified=false");
    }

    @Test
    @Transactional
    void getAllRedesSociaisConveniosByIconIsEqualToSomething() throws Exception {
        IconsRedesSociais icon;
        if (TestUtil.findAll(em, IconsRedesSociais.class).isEmpty()) {
            redesSociaisConvenioRepository.saveAndFlush(redesSociaisConvenio);
            icon = IconsRedesSociaisResourceIT.createEntity(em);
        } else {
            icon = TestUtil.findAll(em, IconsRedesSociais.class).get(0);
        }
        em.persist(icon);
        em.flush();
        redesSociaisConvenio.setIcon(icon);
        redesSociaisConvenioRepository.saveAndFlush(redesSociaisConvenio);
        Long iconId = icon.getId();

        // Get all the redesSociaisConvenioList where icon equals to iconId
        defaultRedesSociaisConvenioShouldBeFound("iconId.equals=" + iconId);

        // Get all the redesSociaisConvenioList where icon equals to (iconId + 1)
        defaultRedesSociaisConvenioShouldNotBeFound("iconId.equals=" + (iconId + 1));
    }

    @Test
    @Transactional
    void getAllRedesSociaisConveniosByConvenioIsEqualToSomething() throws Exception {
        Convenio convenio;
        if (TestUtil.findAll(em, Convenio.class).isEmpty()) {
            redesSociaisConvenioRepository.saveAndFlush(redesSociaisConvenio);
            convenio = ConvenioResourceIT.createEntity(em);
        } else {
            convenio = TestUtil.findAll(em, Convenio.class).get(0);
        }
        em.persist(convenio);
        em.flush();
        redesSociaisConvenio.setConvenio(convenio);
        redesSociaisConvenioRepository.saveAndFlush(redesSociaisConvenio);
        Long convenioId = convenio.getId();

        // Get all the redesSociaisConvenioList where convenio equals to convenioId
        defaultRedesSociaisConvenioShouldBeFound("convenioId.equals=" + convenioId);

        // Get all the redesSociaisConvenioList where convenio equals to (convenioId + 1)
        defaultRedesSociaisConvenioShouldNotBeFound("convenioId.equals=" + (convenioId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRedesSociaisConvenioShouldBeFound(String filter) throws Exception {
        restRedesSociaisConvenioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(redesSociaisConvenio.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())));

        // Check, that the count call also returns 1
        restRedesSociaisConvenioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRedesSociaisConvenioShouldNotBeFound(String filter) throws Exception {
        restRedesSociaisConvenioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRedesSociaisConvenioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRedesSociaisConvenio() throws Exception {
        // Get the redesSociaisConvenio
        restRedesSociaisConvenioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRedesSociaisConvenio() throws Exception {
        // Initialize the database
        redesSociaisConvenioRepository.saveAndFlush(redesSociaisConvenio);

        int databaseSizeBeforeUpdate = redesSociaisConvenioRepository.findAll().size();

        // Update the redesSociaisConvenio
        RedesSociaisConvenio updatedRedesSociaisConvenio = redesSociaisConvenioRepository.findById(redesSociaisConvenio.getId()).get();
        // Disconnect from session so that the updates on updatedRedesSociaisConvenio are not directly saved in db
        em.detach(updatedRedesSociaisConvenio);
        updatedRedesSociaisConvenio
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .endereco(UPDATED_ENDERECO)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);
        RedesSociaisConvenioDTO redesSociaisConvenioDTO = redesSociaisConvenioMapper.toDto(updatedRedesSociaisConvenio);

        restRedesSociaisConvenioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, redesSociaisConvenioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(redesSociaisConvenioDTO))
            )
            .andExpect(status().isOk());

        // Validate the RedesSociaisConvenio in the database
        List<RedesSociaisConvenio> redesSociaisConvenioList = redesSociaisConvenioRepository.findAll();
        assertThat(redesSociaisConvenioList).hasSize(databaseSizeBeforeUpdate);
        RedesSociaisConvenio testRedesSociaisConvenio = redesSociaisConvenioList.get(redesSociaisConvenioList.size() - 1);
        assertThat(testRedesSociaisConvenio.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testRedesSociaisConvenio.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testRedesSociaisConvenio.getEndereco()).isEqualTo(UPDATED_ENDERECO);
        assertThat(testRedesSociaisConvenio.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testRedesSociaisConvenio.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void putNonExistingRedesSociaisConvenio() throws Exception {
        int databaseSizeBeforeUpdate = redesSociaisConvenioRepository.findAll().size();
        redesSociaisConvenio.setId(count.incrementAndGet());

        // Create the RedesSociaisConvenio
        RedesSociaisConvenioDTO redesSociaisConvenioDTO = redesSociaisConvenioMapper.toDto(redesSociaisConvenio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRedesSociaisConvenioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, redesSociaisConvenioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(redesSociaisConvenioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RedesSociaisConvenio in the database
        List<RedesSociaisConvenio> redesSociaisConvenioList = redesSociaisConvenioRepository.findAll();
        assertThat(redesSociaisConvenioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRedesSociaisConvenio() throws Exception {
        int databaseSizeBeforeUpdate = redesSociaisConvenioRepository.findAll().size();
        redesSociaisConvenio.setId(count.incrementAndGet());

        // Create the RedesSociaisConvenio
        RedesSociaisConvenioDTO redesSociaisConvenioDTO = redesSociaisConvenioMapper.toDto(redesSociaisConvenio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRedesSociaisConvenioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(redesSociaisConvenioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RedesSociaisConvenio in the database
        List<RedesSociaisConvenio> redesSociaisConvenioList = redesSociaisConvenioRepository.findAll();
        assertThat(redesSociaisConvenioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRedesSociaisConvenio() throws Exception {
        int databaseSizeBeforeUpdate = redesSociaisConvenioRepository.findAll().size();
        redesSociaisConvenio.setId(count.incrementAndGet());

        // Create the RedesSociaisConvenio
        RedesSociaisConvenioDTO redesSociaisConvenioDTO = redesSociaisConvenioMapper.toDto(redesSociaisConvenio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRedesSociaisConvenioMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(redesSociaisConvenioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RedesSociaisConvenio in the database
        List<RedesSociaisConvenio> redesSociaisConvenioList = redesSociaisConvenioRepository.findAll();
        assertThat(redesSociaisConvenioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRedesSociaisConvenioWithPatch() throws Exception {
        // Initialize the database
        redesSociaisConvenioRepository.saveAndFlush(redesSociaisConvenio);

        int databaseSizeBeforeUpdate = redesSociaisConvenioRepository.findAll().size();

        // Update the redesSociaisConvenio using partial update
        RedesSociaisConvenio partialUpdatedRedesSociaisConvenio = new RedesSociaisConvenio();
        partialUpdatedRedesSociaisConvenio.setId(redesSociaisConvenio.getId());

        partialUpdatedRedesSociaisConvenio.endereco(UPDATED_ENDERECO).modified(UPDATED_MODIFIED);

        restRedesSociaisConvenioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRedesSociaisConvenio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRedesSociaisConvenio))
            )
            .andExpect(status().isOk());

        // Validate the RedesSociaisConvenio in the database
        List<RedesSociaisConvenio> redesSociaisConvenioList = redesSociaisConvenioRepository.findAll();
        assertThat(redesSociaisConvenioList).hasSize(databaseSizeBeforeUpdate);
        RedesSociaisConvenio testRedesSociaisConvenio = redesSociaisConvenioList.get(redesSociaisConvenioList.size() - 1);
        assertThat(testRedesSociaisConvenio.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testRedesSociaisConvenio.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testRedesSociaisConvenio.getEndereco()).isEqualTo(UPDATED_ENDERECO);
        assertThat(testRedesSociaisConvenio.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testRedesSociaisConvenio.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void fullUpdateRedesSociaisConvenioWithPatch() throws Exception {
        // Initialize the database
        redesSociaisConvenioRepository.saveAndFlush(redesSociaisConvenio);

        int databaseSizeBeforeUpdate = redesSociaisConvenioRepository.findAll().size();

        // Update the redesSociaisConvenio using partial update
        RedesSociaisConvenio partialUpdatedRedesSociaisConvenio = new RedesSociaisConvenio();
        partialUpdatedRedesSociaisConvenio.setId(redesSociaisConvenio.getId());

        partialUpdatedRedesSociaisConvenio
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .endereco(UPDATED_ENDERECO)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);

        restRedesSociaisConvenioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRedesSociaisConvenio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRedesSociaisConvenio))
            )
            .andExpect(status().isOk());

        // Validate the RedesSociaisConvenio in the database
        List<RedesSociaisConvenio> redesSociaisConvenioList = redesSociaisConvenioRepository.findAll();
        assertThat(redesSociaisConvenioList).hasSize(databaseSizeBeforeUpdate);
        RedesSociaisConvenio testRedesSociaisConvenio = redesSociaisConvenioList.get(redesSociaisConvenioList.size() - 1);
        assertThat(testRedesSociaisConvenio.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testRedesSociaisConvenio.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testRedesSociaisConvenio.getEndereco()).isEqualTo(UPDATED_ENDERECO);
        assertThat(testRedesSociaisConvenio.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testRedesSociaisConvenio.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingRedesSociaisConvenio() throws Exception {
        int databaseSizeBeforeUpdate = redesSociaisConvenioRepository.findAll().size();
        redesSociaisConvenio.setId(count.incrementAndGet());

        // Create the RedesSociaisConvenio
        RedesSociaisConvenioDTO redesSociaisConvenioDTO = redesSociaisConvenioMapper.toDto(redesSociaisConvenio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRedesSociaisConvenioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, redesSociaisConvenioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(redesSociaisConvenioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RedesSociaisConvenio in the database
        List<RedesSociaisConvenio> redesSociaisConvenioList = redesSociaisConvenioRepository.findAll();
        assertThat(redesSociaisConvenioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRedesSociaisConvenio() throws Exception {
        int databaseSizeBeforeUpdate = redesSociaisConvenioRepository.findAll().size();
        redesSociaisConvenio.setId(count.incrementAndGet());

        // Create the RedesSociaisConvenio
        RedesSociaisConvenioDTO redesSociaisConvenioDTO = redesSociaisConvenioMapper.toDto(redesSociaisConvenio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRedesSociaisConvenioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(redesSociaisConvenioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RedesSociaisConvenio in the database
        List<RedesSociaisConvenio> redesSociaisConvenioList = redesSociaisConvenioRepository.findAll();
        assertThat(redesSociaisConvenioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRedesSociaisConvenio() throws Exception {
        int databaseSizeBeforeUpdate = redesSociaisConvenioRepository.findAll().size();
        redesSociaisConvenio.setId(count.incrementAndGet());

        // Create the RedesSociaisConvenio
        RedesSociaisConvenioDTO redesSociaisConvenioDTO = redesSociaisConvenioMapper.toDto(redesSociaisConvenio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRedesSociaisConvenioMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(redesSociaisConvenioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RedesSociaisConvenio in the database
        List<RedesSociaisConvenio> redesSociaisConvenioList = redesSociaisConvenioRepository.findAll();
        assertThat(redesSociaisConvenioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRedesSociaisConvenio() throws Exception {
        // Initialize the database
        redesSociaisConvenioRepository.saveAndFlush(redesSociaisConvenio);

        int databaseSizeBeforeDelete = redesSociaisConvenioRepository.findAll().size();

        // Delete the redesSociaisConvenio
        restRedesSociaisConvenioMockMvc
            .perform(delete(ENTITY_API_URL_ID, redesSociaisConvenio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RedesSociaisConvenio> redesSociaisConvenioList = redesSociaisConvenioRepository.findAll();
        assertThat(redesSociaisConvenioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
