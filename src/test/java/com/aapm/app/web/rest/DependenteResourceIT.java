package com.aapm.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aapm.app.IntegrationTest;
import com.aapm.app.domain.Associado;
import com.aapm.app.domain.Dependente;
import com.aapm.app.domain.enumeration.Status;
import com.aapm.app.repository.DependenteRepository;
import com.aapm.app.service.DependenteService;
import com.aapm.app.service.criteria.DependenteCriteria;
import com.aapm.app.service.dto.DependenteDTO;
import com.aapm.app.service.mapper.DependenteMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link DependenteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DependenteResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA_NASCIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_NASCIMENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_NASCIMENTO = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_PARENTESCO = "AAAAAAAAAA";
    private static final String UPDATED_PARENTESCO = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.Ativo;
    private static final Status UPDATED_STATUS = Status.Inativo;

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/dependentes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DependenteRepository dependenteRepository;

    @Mock
    private DependenteRepository dependenteRepositoryMock;

    @Autowired
    private DependenteMapper dependenteMapper;

    @Mock
    private DependenteService dependenteServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDependenteMockMvc;

    private Dependente dependente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dependente createEntity(EntityManager em) {
        Dependente dependente = new Dependente()
            .nome(DEFAULT_NOME)
            .dataNascimento(DEFAULT_DATA_NASCIMENTO)
            .parentesco(DEFAULT_PARENTESCO)
            .status(DEFAULT_STATUS)
            .created(DEFAULT_CREATED)
            .modified(DEFAULT_MODIFIED);
        return dependente;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dependente createUpdatedEntity(EntityManager em) {
        Dependente dependente = new Dependente()
            .nome(UPDATED_NOME)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .parentesco(UPDATED_PARENTESCO)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);
        return dependente;
    }

    @BeforeEach
    public void initTest() {
        dependente = createEntity(em);
    }

    @Test
    @Transactional
    void createDependente() throws Exception {
        int databaseSizeBeforeCreate = dependenteRepository.findAll().size();
        // Create the Dependente
        DependenteDTO dependenteDTO = dependenteMapper.toDto(dependente);
        restDependenteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dependenteDTO)))
            .andExpect(status().isCreated());

        // Validate the Dependente in the database
        List<Dependente> dependenteList = dependenteRepository.findAll();
        assertThat(dependenteList).hasSize(databaseSizeBeforeCreate + 1);
        Dependente testDependente = dependenteList.get(dependenteList.size() - 1);
        assertThat(testDependente.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testDependente.getDataNascimento()).isEqualTo(DEFAULT_DATA_NASCIMENTO);
        assertThat(testDependente.getParentesco()).isEqualTo(DEFAULT_PARENTESCO);
        assertThat(testDependente.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDependente.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testDependente.getModified()).isEqualTo(DEFAULT_MODIFIED);
    }

    @Test
    @Transactional
    void createDependenteWithExistingId() throws Exception {
        // Create the Dependente with an existing ID
        dependente.setId(1L);
        DependenteDTO dependenteDTO = dependenteMapper.toDto(dependente);

        int databaseSizeBeforeCreate = dependenteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDependenteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dependenteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dependente in the database
        List<Dependente> dependenteList = dependenteRepository.findAll();
        assertThat(dependenteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = dependenteRepository.findAll().size();
        // set the field null
        dependente.setNome(null);

        // Create the Dependente, which fails.
        DependenteDTO dependenteDTO = dependenteMapper.toDto(dependente);

        restDependenteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dependenteDTO)))
            .andExpect(status().isBadRequest());

        List<Dependente> dependenteList = dependenteRepository.findAll();
        assertThat(dependenteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataNascimentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = dependenteRepository.findAll().size();
        // set the field null
        dependente.setDataNascimento(null);

        // Create the Dependente, which fails.
        DependenteDTO dependenteDTO = dependenteMapper.toDto(dependente);

        restDependenteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dependenteDTO)))
            .andExpect(status().isBadRequest());

        List<Dependente> dependenteList = dependenteRepository.findAll();
        assertThat(dependenteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkParentescoIsRequired() throws Exception {
        int databaseSizeBeforeTest = dependenteRepository.findAll().size();
        // set the field null
        dependente.setParentesco(null);

        // Create the Dependente, which fails.
        DependenteDTO dependenteDTO = dependenteMapper.toDto(dependente);

        restDependenteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dependenteDTO)))
            .andExpect(status().isBadRequest());

        List<Dependente> dependenteList = dependenteRepository.findAll();
        assertThat(dependenteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDependentes() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        // Get all the dependenteList
        restDependenteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dependente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].parentesco").value(hasItem(DEFAULT_PARENTESCO)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDependentesWithEagerRelationshipsIsEnabled() throws Exception {
        when(dependenteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDependenteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(dependenteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDependentesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(dependenteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDependenteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(dependenteRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDependente() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        // Get the dependente
        restDependenteMockMvc
            .perform(get(ENTITY_API_URL_ID, dependente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dependente.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.dataNascimento").value(DEFAULT_DATA_NASCIMENTO.toString()))
            .andExpect(jsonPath("$.parentesco").value(DEFAULT_PARENTESCO))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.modified").value(DEFAULT_MODIFIED.toString()));
    }

    @Test
    @Transactional
    void getDependentesByIdFiltering() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        Long id = dependente.getId();

        defaultDependenteShouldBeFound("id.equals=" + id);
        defaultDependenteShouldNotBeFound("id.notEquals=" + id);

        defaultDependenteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDependenteShouldNotBeFound("id.greaterThan=" + id);

        defaultDependenteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDependenteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDependentesByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        // Get all the dependenteList where nome equals to DEFAULT_NOME
        defaultDependenteShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the dependenteList where nome equals to UPDATED_NOME
        defaultDependenteShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllDependentesByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        // Get all the dependenteList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultDependenteShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the dependenteList where nome equals to UPDATED_NOME
        defaultDependenteShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllDependentesByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        // Get all the dependenteList where nome is not null
        defaultDependenteShouldBeFound("nome.specified=true");

        // Get all the dependenteList where nome is null
        defaultDependenteShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllDependentesByNomeContainsSomething() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        // Get all the dependenteList where nome contains DEFAULT_NOME
        defaultDependenteShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the dependenteList where nome contains UPDATED_NOME
        defaultDependenteShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllDependentesByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        // Get all the dependenteList where nome does not contain DEFAULT_NOME
        defaultDependenteShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the dependenteList where nome does not contain UPDATED_NOME
        defaultDependenteShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllDependentesByDataNascimentoIsEqualToSomething() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        // Get all the dependenteList where dataNascimento equals to DEFAULT_DATA_NASCIMENTO
        defaultDependenteShouldBeFound("dataNascimento.equals=" + DEFAULT_DATA_NASCIMENTO);

        // Get all the dependenteList where dataNascimento equals to UPDATED_DATA_NASCIMENTO
        defaultDependenteShouldNotBeFound("dataNascimento.equals=" + UPDATED_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllDependentesByDataNascimentoIsInShouldWork() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        // Get all the dependenteList where dataNascimento in DEFAULT_DATA_NASCIMENTO or UPDATED_DATA_NASCIMENTO
        defaultDependenteShouldBeFound("dataNascimento.in=" + DEFAULT_DATA_NASCIMENTO + "," + UPDATED_DATA_NASCIMENTO);

        // Get all the dependenteList where dataNascimento equals to UPDATED_DATA_NASCIMENTO
        defaultDependenteShouldNotBeFound("dataNascimento.in=" + UPDATED_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllDependentesByDataNascimentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        // Get all the dependenteList where dataNascimento is not null
        defaultDependenteShouldBeFound("dataNascimento.specified=true");

        // Get all the dependenteList where dataNascimento is null
        defaultDependenteShouldNotBeFound("dataNascimento.specified=false");
    }

    @Test
    @Transactional
    void getAllDependentesByDataNascimentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        // Get all the dependenteList where dataNascimento is greater than or equal to DEFAULT_DATA_NASCIMENTO
        defaultDependenteShouldBeFound("dataNascimento.greaterThanOrEqual=" + DEFAULT_DATA_NASCIMENTO);

        // Get all the dependenteList where dataNascimento is greater than or equal to UPDATED_DATA_NASCIMENTO
        defaultDependenteShouldNotBeFound("dataNascimento.greaterThanOrEqual=" + UPDATED_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllDependentesByDataNascimentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        // Get all the dependenteList where dataNascimento is less than or equal to DEFAULT_DATA_NASCIMENTO
        defaultDependenteShouldBeFound("dataNascimento.lessThanOrEqual=" + DEFAULT_DATA_NASCIMENTO);

        // Get all the dependenteList where dataNascimento is less than or equal to SMALLER_DATA_NASCIMENTO
        defaultDependenteShouldNotBeFound("dataNascimento.lessThanOrEqual=" + SMALLER_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllDependentesByDataNascimentoIsLessThanSomething() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        // Get all the dependenteList where dataNascimento is less than DEFAULT_DATA_NASCIMENTO
        defaultDependenteShouldNotBeFound("dataNascimento.lessThan=" + DEFAULT_DATA_NASCIMENTO);

        // Get all the dependenteList where dataNascimento is less than UPDATED_DATA_NASCIMENTO
        defaultDependenteShouldBeFound("dataNascimento.lessThan=" + UPDATED_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllDependentesByDataNascimentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        // Get all the dependenteList where dataNascimento is greater than DEFAULT_DATA_NASCIMENTO
        defaultDependenteShouldNotBeFound("dataNascimento.greaterThan=" + DEFAULT_DATA_NASCIMENTO);

        // Get all the dependenteList where dataNascimento is greater than SMALLER_DATA_NASCIMENTO
        defaultDependenteShouldBeFound("dataNascimento.greaterThan=" + SMALLER_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllDependentesByParentescoIsEqualToSomething() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        // Get all the dependenteList where parentesco equals to DEFAULT_PARENTESCO
        defaultDependenteShouldBeFound("parentesco.equals=" + DEFAULT_PARENTESCO);

        // Get all the dependenteList where parentesco equals to UPDATED_PARENTESCO
        defaultDependenteShouldNotBeFound("parentesco.equals=" + UPDATED_PARENTESCO);
    }

    @Test
    @Transactional
    void getAllDependentesByParentescoIsInShouldWork() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        // Get all the dependenteList where parentesco in DEFAULT_PARENTESCO or UPDATED_PARENTESCO
        defaultDependenteShouldBeFound("parentesco.in=" + DEFAULT_PARENTESCO + "," + UPDATED_PARENTESCO);

        // Get all the dependenteList where parentesco equals to UPDATED_PARENTESCO
        defaultDependenteShouldNotBeFound("parentesco.in=" + UPDATED_PARENTESCO);
    }

    @Test
    @Transactional
    void getAllDependentesByParentescoIsNullOrNotNull() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        // Get all the dependenteList where parentesco is not null
        defaultDependenteShouldBeFound("parentesco.specified=true");

        // Get all the dependenteList where parentesco is null
        defaultDependenteShouldNotBeFound("parentesco.specified=false");
    }

    @Test
    @Transactional
    void getAllDependentesByParentescoContainsSomething() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        // Get all the dependenteList where parentesco contains DEFAULT_PARENTESCO
        defaultDependenteShouldBeFound("parentesco.contains=" + DEFAULT_PARENTESCO);

        // Get all the dependenteList where parentesco contains UPDATED_PARENTESCO
        defaultDependenteShouldNotBeFound("parentesco.contains=" + UPDATED_PARENTESCO);
    }

    @Test
    @Transactional
    void getAllDependentesByParentescoNotContainsSomething() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        // Get all the dependenteList where parentesco does not contain DEFAULT_PARENTESCO
        defaultDependenteShouldNotBeFound("parentesco.doesNotContain=" + DEFAULT_PARENTESCO);

        // Get all the dependenteList where parentesco does not contain UPDATED_PARENTESCO
        defaultDependenteShouldBeFound("parentesco.doesNotContain=" + UPDATED_PARENTESCO);
    }

    @Test
    @Transactional
    void getAllDependentesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        // Get all the dependenteList where status equals to DEFAULT_STATUS
        defaultDependenteShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the dependenteList where status equals to UPDATED_STATUS
        defaultDependenteShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllDependentesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        // Get all the dependenteList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultDependenteShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the dependenteList where status equals to UPDATED_STATUS
        defaultDependenteShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllDependentesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        // Get all the dependenteList where status is not null
        defaultDependenteShouldBeFound("status.specified=true");

        // Get all the dependenteList where status is null
        defaultDependenteShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllDependentesByCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        // Get all the dependenteList where created equals to DEFAULT_CREATED
        defaultDependenteShouldBeFound("created.equals=" + DEFAULT_CREATED);

        // Get all the dependenteList where created equals to UPDATED_CREATED
        defaultDependenteShouldNotBeFound("created.equals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllDependentesByCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        // Get all the dependenteList where created in DEFAULT_CREATED or UPDATED_CREATED
        defaultDependenteShouldBeFound("created.in=" + DEFAULT_CREATED + "," + UPDATED_CREATED);

        // Get all the dependenteList where created equals to UPDATED_CREATED
        defaultDependenteShouldNotBeFound("created.in=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllDependentesByCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        // Get all the dependenteList where created is not null
        defaultDependenteShouldBeFound("created.specified=true");

        // Get all the dependenteList where created is null
        defaultDependenteShouldNotBeFound("created.specified=false");
    }

    @Test
    @Transactional
    void getAllDependentesByModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        // Get all the dependenteList where modified equals to DEFAULT_MODIFIED
        defaultDependenteShouldBeFound("modified.equals=" + DEFAULT_MODIFIED);

        // Get all the dependenteList where modified equals to UPDATED_MODIFIED
        defaultDependenteShouldNotBeFound("modified.equals=" + UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void getAllDependentesByModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        // Get all the dependenteList where modified in DEFAULT_MODIFIED or UPDATED_MODIFIED
        defaultDependenteShouldBeFound("modified.in=" + DEFAULT_MODIFIED + "," + UPDATED_MODIFIED);

        // Get all the dependenteList where modified equals to UPDATED_MODIFIED
        defaultDependenteShouldNotBeFound("modified.in=" + UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void getAllDependentesByModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        // Get all the dependenteList where modified is not null
        defaultDependenteShouldBeFound("modified.specified=true");

        // Get all the dependenteList where modified is null
        defaultDependenteShouldNotBeFound("modified.specified=false");
    }

    @Test
    @Transactional
    void getAllDependentesByAssociadoIsEqualToSomething() throws Exception {
        Associado associado;
        if (TestUtil.findAll(em, Associado.class).isEmpty()) {
            dependenteRepository.saveAndFlush(dependente);
            associado = AssociadoResourceIT.createEntity(em);
        } else {
            associado = TestUtil.findAll(em, Associado.class).get(0);
        }
        em.persist(associado);
        em.flush();
        dependente.setAssociado(associado);
        dependenteRepository.saveAndFlush(dependente);
        Long associadoId = associado.getId();

        // Get all the dependenteList where associado equals to associadoId
        defaultDependenteShouldBeFound("associadoId.equals=" + associadoId);

        // Get all the dependenteList where associado equals to (associadoId + 1)
        defaultDependenteShouldNotBeFound("associadoId.equals=" + (associadoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDependenteShouldBeFound(String filter) throws Exception {
        restDependenteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dependente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].parentesco").value(hasItem(DEFAULT_PARENTESCO)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())));

        // Check, that the count call also returns 1
        restDependenteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDependenteShouldNotBeFound(String filter) throws Exception {
        restDependenteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDependenteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDependente() throws Exception {
        // Get the dependente
        restDependenteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDependente() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        int databaseSizeBeforeUpdate = dependenteRepository.findAll().size();

        // Update the dependente
        Dependente updatedDependente = dependenteRepository.findById(dependente.getId()).get();
        // Disconnect from session so that the updates on updatedDependente are not directly saved in db
        em.detach(updatedDependente);
        updatedDependente
            .nome(UPDATED_NOME)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .parentesco(UPDATED_PARENTESCO)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);
        DependenteDTO dependenteDTO = dependenteMapper.toDto(updatedDependente);

        restDependenteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dependenteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dependenteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Dependente in the database
        List<Dependente> dependenteList = dependenteRepository.findAll();
        assertThat(dependenteList).hasSize(databaseSizeBeforeUpdate);
        Dependente testDependente = dependenteList.get(dependenteList.size() - 1);
        assertThat(testDependente.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testDependente.getDataNascimento()).isEqualTo(UPDATED_DATA_NASCIMENTO);
        assertThat(testDependente.getParentesco()).isEqualTo(UPDATED_PARENTESCO);
        assertThat(testDependente.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDependente.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testDependente.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void putNonExistingDependente() throws Exception {
        int databaseSizeBeforeUpdate = dependenteRepository.findAll().size();
        dependente.setId(count.incrementAndGet());

        // Create the Dependente
        DependenteDTO dependenteDTO = dependenteMapper.toDto(dependente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDependenteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dependenteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dependenteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dependente in the database
        List<Dependente> dependenteList = dependenteRepository.findAll();
        assertThat(dependenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDependente() throws Exception {
        int databaseSizeBeforeUpdate = dependenteRepository.findAll().size();
        dependente.setId(count.incrementAndGet());

        // Create the Dependente
        DependenteDTO dependenteDTO = dependenteMapper.toDto(dependente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDependenteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dependenteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dependente in the database
        List<Dependente> dependenteList = dependenteRepository.findAll();
        assertThat(dependenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDependente() throws Exception {
        int databaseSizeBeforeUpdate = dependenteRepository.findAll().size();
        dependente.setId(count.incrementAndGet());

        // Create the Dependente
        DependenteDTO dependenteDTO = dependenteMapper.toDto(dependente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDependenteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dependenteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dependente in the database
        List<Dependente> dependenteList = dependenteRepository.findAll();
        assertThat(dependenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDependenteWithPatch() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        int databaseSizeBeforeUpdate = dependenteRepository.findAll().size();

        // Update the dependente using partial update
        Dependente partialUpdatedDependente = new Dependente();
        partialUpdatedDependente.setId(dependente.getId());

        partialUpdatedDependente.status(UPDATED_STATUS);

        restDependenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDependente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDependente))
            )
            .andExpect(status().isOk());

        // Validate the Dependente in the database
        List<Dependente> dependenteList = dependenteRepository.findAll();
        assertThat(dependenteList).hasSize(databaseSizeBeforeUpdate);
        Dependente testDependente = dependenteList.get(dependenteList.size() - 1);
        assertThat(testDependente.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testDependente.getDataNascimento()).isEqualTo(DEFAULT_DATA_NASCIMENTO);
        assertThat(testDependente.getParentesco()).isEqualTo(DEFAULT_PARENTESCO);
        assertThat(testDependente.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDependente.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testDependente.getModified()).isEqualTo(DEFAULT_MODIFIED);
    }

    @Test
    @Transactional
    void fullUpdateDependenteWithPatch() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        int databaseSizeBeforeUpdate = dependenteRepository.findAll().size();

        // Update the dependente using partial update
        Dependente partialUpdatedDependente = new Dependente();
        partialUpdatedDependente.setId(dependente.getId());

        partialUpdatedDependente
            .nome(UPDATED_NOME)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .parentesco(UPDATED_PARENTESCO)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);

        restDependenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDependente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDependente))
            )
            .andExpect(status().isOk());

        // Validate the Dependente in the database
        List<Dependente> dependenteList = dependenteRepository.findAll();
        assertThat(dependenteList).hasSize(databaseSizeBeforeUpdate);
        Dependente testDependente = dependenteList.get(dependenteList.size() - 1);
        assertThat(testDependente.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testDependente.getDataNascimento()).isEqualTo(UPDATED_DATA_NASCIMENTO);
        assertThat(testDependente.getParentesco()).isEqualTo(UPDATED_PARENTESCO);
        assertThat(testDependente.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDependente.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testDependente.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingDependente() throws Exception {
        int databaseSizeBeforeUpdate = dependenteRepository.findAll().size();
        dependente.setId(count.incrementAndGet());

        // Create the Dependente
        DependenteDTO dependenteDTO = dependenteMapper.toDto(dependente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDependenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dependenteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dependenteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dependente in the database
        List<Dependente> dependenteList = dependenteRepository.findAll();
        assertThat(dependenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDependente() throws Exception {
        int databaseSizeBeforeUpdate = dependenteRepository.findAll().size();
        dependente.setId(count.incrementAndGet());

        // Create the Dependente
        DependenteDTO dependenteDTO = dependenteMapper.toDto(dependente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDependenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dependenteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dependente in the database
        List<Dependente> dependenteList = dependenteRepository.findAll();
        assertThat(dependenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDependente() throws Exception {
        int databaseSizeBeforeUpdate = dependenteRepository.findAll().size();
        dependente.setId(count.incrementAndGet());

        // Create the Dependente
        DependenteDTO dependenteDTO = dependenteMapper.toDto(dependente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDependenteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dependenteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dependente in the database
        List<Dependente> dependenteList = dependenteRepository.findAll();
        assertThat(dependenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDependente() throws Exception {
        // Initialize the database
        dependenteRepository.saveAndFlush(dependente);

        int databaseSizeBeforeDelete = dependenteRepository.findAll().size();

        // Delete the dependente
        restDependenteMockMvc
            .perform(delete(ENTITY_API_URL_ID, dependente.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dependente> dependenteList = dependenteRepository.findAll();
        assertThat(dependenteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
