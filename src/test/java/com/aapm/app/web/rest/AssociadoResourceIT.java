package com.aapm.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aapm.app.IntegrationTest;
import com.aapm.app.domain.Associado;
import com.aapm.app.domain.Contato;
import com.aapm.app.domain.Dependente;
import com.aapm.app.domain.Reserva;
import com.aapm.app.domain.enumeration.Status;
import com.aapm.app.repository.AssociadoRepository;
import com.aapm.app.service.criteria.AssociadoCriteria;
import com.aapm.app.service.dto.AssociadoDTO;
import com.aapm.app.service.mapper.AssociadoMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link AssociadoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AssociadoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_MATRICULA = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULA = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.Ativo;
    private static final Status UPDATED_STATUS = Status.Inativo;

    private static final String DEFAULT_TELEFONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA_NASCIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_NASCIMENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_NASCIMENTO = LocalDate.ofEpochDay(-1L);

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/associados";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssociadoRepository associadoRepository;

    @Autowired
    private AssociadoMapper associadoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssociadoMockMvc;

    private Associado associado;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Associado createEntity(EntityManager em) {
        Associado associado = new Associado()
            .nome(DEFAULT_NOME)
            .matricula(DEFAULT_MATRICULA)
            .status(DEFAULT_STATUS)
            .telefone(DEFAULT_TELEFONE)
            .email(DEFAULT_EMAIL)
            .dataNascimento(DEFAULT_DATA_NASCIMENTO)
            .created(DEFAULT_CREATED)
            .modified(DEFAULT_MODIFIED);
        return associado;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Associado createUpdatedEntity(EntityManager em) {
        Associado associado = new Associado()
            .nome(UPDATED_NOME)
            .matricula(UPDATED_MATRICULA)
            .status(UPDATED_STATUS)
            .telefone(UPDATED_TELEFONE)
            .email(UPDATED_EMAIL)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);
        return associado;
    }

    @BeforeEach
    public void initTest() {
        associado = createEntity(em);
    }

    @Test
    @Transactional
    void createAssociado() throws Exception {
        int databaseSizeBeforeCreate = associadoRepository.findAll().size();
        // Create the Associado
        AssociadoDTO associadoDTO = associadoMapper.toDto(associado);
        restAssociadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(associadoDTO)))
            .andExpect(status().isCreated());

        // Validate the Associado in the database
        List<Associado> associadoList = associadoRepository.findAll();
        assertThat(associadoList).hasSize(databaseSizeBeforeCreate + 1);
        Associado testAssociado = associadoList.get(associadoList.size() - 1);
        assertThat(testAssociado.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testAssociado.getMatricula()).isEqualTo(DEFAULT_MATRICULA);
        assertThat(testAssociado.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testAssociado.getTelefone()).isEqualTo(DEFAULT_TELEFONE);
        assertThat(testAssociado.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAssociado.getDataNascimento()).isEqualTo(DEFAULT_DATA_NASCIMENTO);
        assertThat(testAssociado.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testAssociado.getModified()).isEqualTo(DEFAULT_MODIFIED);
    }

    @Test
    @Transactional
    void createAssociadoWithExistingId() throws Exception {
        // Create the Associado with an existing ID
        associado.setId(1L);
        AssociadoDTO associadoDTO = associadoMapper.toDto(associado);

        int databaseSizeBeforeCreate = associadoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssociadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(associadoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Associado in the database
        List<Associado> associadoList = associadoRepository.findAll();
        assertThat(associadoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = associadoRepository.findAll().size();
        // set the field null
        associado.setNome(null);

        // Create the Associado, which fails.
        AssociadoDTO associadoDTO = associadoMapper.toDto(associado);

        restAssociadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(associadoDTO)))
            .andExpect(status().isBadRequest());

        List<Associado> associadoList = associadoRepository.findAll();
        assertThat(associadoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMatriculaIsRequired() throws Exception {
        int databaseSizeBeforeTest = associadoRepository.findAll().size();
        // set the field null
        associado.setMatricula(null);

        // Create the Associado, which fails.
        AssociadoDTO associadoDTO = associadoMapper.toDto(associado);

        restAssociadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(associadoDTO)))
            .andExpect(status().isBadRequest());

        List<Associado> associadoList = associadoRepository.findAll();
        assertThat(associadoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = associadoRepository.findAll().size();
        // set the field null
        associado.setEmail(null);

        // Create the Associado, which fails.
        AssociadoDTO associadoDTO = associadoMapper.toDto(associado);

        restAssociadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(associadoDTO)))
            .andExpect(status().isBadRequest());

        List<Associado> associadoList = associadoRepository.findAll();
        assertThat(associadoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAssociados() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList
        restAssociadoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(associado.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].matricula").value(hasItem(DEFAULT_MATRICULA)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())));
    }

    @Test
    @Transactional
    void getAssociado() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get the associado
        restAssociadoMockMvc
            .perform(get(ENTITY_API_URL_ID, associado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(associado.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.matricula").value(DEFAULT_MATRICULA))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.telefone").value(DEFAULT_TELEFONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.dataNascimento").value(DEFAULT_DATA_NASCIMENTO.toString()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.modified").value(DEFAULT_MODIFIED.toString()));
    }

    @Test
    @Transactional
    void getAssociadosByIdFiltering() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        Long id = associado.getId();

        defaultAssociadoShouldBeFound("id.equals=" + id);
        defaultAssociadoShouldNotBeFound("id.notEquals=" + id);

        defaultAssociadoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAssociadoShouldNotBeFound("id.greaterThan=" + id);

        defaultAssociadoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAssociadoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAssociadosByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where nome equals to DEFAULT_NOME
        defaultAssociadoShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the associadoList where nome equals to UPDATED_NOME
        defaultAssociadoShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllAssociadosByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultAssociadoShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the associadoList where nome equals to UPDATED_NOME
        defaultAssociadoShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllAssociadosByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where nome is not null
        defaultAssociadoShouldBeFound("nome.specified=true");

        // Get all the associadoList where nome is null
        defaultAssociadoShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllAssociadosByNomeContainsSomething() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where nome contains DEFAULT_NOME
        defaultAssociadoShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the associadoList where nome contains UPDATED_NOME
        defaultAssociadoShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllAssociadosByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where nome does not contain DEFAULT_NOME
        defaultAssociadoShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the associadoList where nome does not contain UPDATED_NOME
        defaultAssociadoShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllAssociadosByMatriculaIsEqualToSomething() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where matricula equals to DEFAULT_MATRICULA
        defaultAssociadoShouldBeFound("matricula.equals=" + DEFAULT_MATRICULA);

        // Get all the associadoList where matricula equals to UPDATED_MATRICULA
        defaultAssociadoShouldNotBeFound("matricula.equals=" + UPDATED_MATRICULA);
    }

    @Test
    @Transactional
    void getAllAssociadosByMatriculaIsInShouldWork() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where matricula in DEFAULT_MATRICULA or UPDATED_MATRICULA
        defaultAssociadoShouldBeFound("matricula.in=" + DEFAULT_MATRICULA + "," + UPDATED_MATRICULA);

        // Get all the associadoList where matricula equals to UPDATED_MATRICULA
        defaultAssociadoShouldNotBeFound("matricula.in=" + UPDATED_MATRICULA);
    }

    @Test
    @Transactional
    void getAllAssociadosByMatriculaIsNullOrNotNull() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where matricula is not null
        defaultAssociadoShouldBeFound("matricula.specified=true");

        // Get all the associadoList where matricula is null
        defaultAssociadoShouldNotBeFound("matricula.specified=false");
    }

    @Test
    @Transactional
    void getAllAssociadosByMatriculaContainsSomething() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where matricula contains DEFAULT_MATRICULA
        defaultAssociadoShouldBeFound("matricula.contains=" + DEFAULT_MATRICULA);

        // Get all the associadoList where matricula contains UPDATED_MATRICULA
        defaultAssociadoShouldNotBeFound("matricula.contains=" + UPDATED_MATRICULA);
    }

    @Test
    @Transactional
    void getAllAssociadosByMatriculaNotContainsSomething() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where matricula does not contain DEFAULT_MATRICULA
        defaultAssociadoShouldNotBeFound("matricula.doesNotContain=" + DEFAULT_MATRICULA);

        // Get all the associadoList where matricula does not contain UPDATED_MATRICULA
        defaultAssociadoShouldBeFound("matricula.doesNotContain=" + UPDATED_MATRICULA);
    }

    @Test
    @Transactional
    void getAllAssociadosByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where status equals to DEFAULT_STATUS
        defaultAssociadoShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the associadoList where status equals to UPDATED_STATUS
        defaultAssociadoShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAssociadosByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultAssociadoShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the associadoList where status equals to UPDATED_STATUS
        defaultAssociadoShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAssociadosByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where status is not null
        defaultAssociadoShouldBeFound("status.specified=true");

        // Get all the associadoList where status is null
        defaultAssociadoShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllAssociadosByTelefoneIsEqualToSomething() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where telefone equals to DEFAULT_TELEFONE
        defaultAssociadoShouldBeFound("telefone.equals=" + DEFAULT_TELEFONE);

        // Get all the associadoList where telefone equals to UPDATED_TELEFONE
        defaultAssociadoShouldNotBeFound("telefone.equals=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllAssociadosByTelefoneIsInShouldWork() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where telefone in DEFAULT_TELEFONE or UPDATED_TELEFONE
        defaultAssociadoShouldBeFound("telefone.in=" + DEFAULT_TELEFONE + "," + UPDATED_TELEFONE);

        // Get all the associadoList where telefone equals to UPDATED_TELEFONE
        defaultAssociadoShouldNotBeFound("telefone.in=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllAssociadosByTelefoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where telefone is not null
        defaultAssociadoShouldBeFound("telefone.specified=true");

        // Get all the associadoList where telefone is null
        defaultAssociadoShouldNotBeFound("telefone.specified=false");
    }

    @Test
    @Transactional
    void getAllAssociadosByTelefoneContainsSomething() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where telefone contains DEFAULT_TELEFONE
        defaultAssociadoShouldBeFound("telefone.contains=" + DEFAULT_TELEFONE);

        // Get all the associadoList where telefone contains UPDATED_TELEFONE
        defaultAssociadoShouldNotBeFound("telefone.contains=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllAssociadosByTelefoneNotContainsSomething() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where telefone does not contain DEFAULT_TELEFONE
        defaultAssociadoShouldNotBeFound("telefone.doesNotContain=" + DEFAULT_TELEFONE);

        // Get all the associadoList where telefone does not contain UPDATED_TELEFONE
        defaultAssociadoShouldBeFound("telefone.doesNotContain=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllAssociadosByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where email equals to DEFAULT_EMAIL
        defaultAssociadoShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the associadoList where email equals to UPDATED_EMAIL
        defaultAssociadoShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAssociadosByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultAssociadoShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the associadoList where email equals to UPDATED_EMAIL
        defaultAssociadoShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAssociadosByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where email is not null
        defaultAssociadoShouldBeFound("email.specified=true");

        // Get all the associadoList where email is null
        defaultAssociadoShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllAssociadosByEmailContainsSomething() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where email contains DEFAULT_EMAIL
        defaultAssociadoShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the associadoList where email contains UPDATED_EMAIL
        defaultAssociadoShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAssociadosByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where email does not contain DEFAULT_EMAIL
        defaultAssociadoShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the associadoList where email does not contain UPDATED_EMAIL
        defaultAssociadoShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAssociadosByDataNascimentoIsEqualToSomething() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where dataNascimento equals to DEFAULT_DATA_NASCIMENTO
        defaultAssociadoShouldBeFound("dataNascimento.equals=" + DEFAULT_DATA_NASCIMENTO);

        // Get all the associadoList where dataNascimento equals to UPDATED_DATA_NASCIMENTO
        defaultAssociadoShouldNotBeFound("dataNascimento.equals=" + UPDATED_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllAssociadosByDataNascimentoIsInShouldWork() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where dataNascimento in DEFAULT_DATA_NASCIMENTO or UPDATED_DATA_NASCIMENTO
        defaultAssociadoShouldBeFound("dataNascimento.in=" + DEFAULT_DATA_NASCIMENTO + "," + UPDATED_DATA_NASCIMENTO);

        // Get all the associadoList where dataNascimento equals to UPDATED_DATA_NASCIMENTO
        defaultAssociadoShouldNotBeFound("dataNascimento.in=" + UPDATED_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllAssociadosByDataNascimentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where dataNascimento is not null
        defaultAssociadoShouldBeFound("dataNascimento.specified=true");

        // Get all the associadoList where dataNascimento is null
        defaultAssociadoShouldNotBeFound("dataNascimento.specified=false");
    }

    @Test
    @Transactional
    void getAllAssociadosByDataNascimentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where dataNascimento is greater than or equal to DEFAULT_DATA_NASCIMENTO
        defaultAssociadoShouldBeFound("dataNascimento.greaterThanOrEqual=" + DEFAULT_DATA_NASCIMENTO);

        // Get all the associadoList where dataNascimento is greater than or equal to UPDATED_DATA_NASCIMENTO
        defaultAssociadoShouldNotBeFound("dataNascimento.greaterThanOrEqual=" + UPDATED_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllAssociadosByDataNascimentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where dataNascimento is less than or equal to DEFAULT_DATA_NASCIMENTO
        defaultAssociadoShouldBeFound("dataNascimento.lessThanOrEqual=" + DEFAULT_DATA_NASCIMENTO);

        // Get all the associadoList where dataNascimento is less than or equal to SMALLER_DATA_NASCIMENTO
        defaultAssociadoShouldNotBeFound("dataNascimento.lessThanOrEqual=" + SMALLER_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllAssociadosByDataNascimentoIsLessThanSomething() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where dataNascimento is less than DEFAULT_DATA_NASCIMENTO
        defaultAssociadoShouldNotBeFound("dataNascimento.lessThan=" + DEFAULT_DATA_NASCIMENTO);

        // Get all the associadoList where dataNascimento is less than UPDATED_DATA_NASCIMENTO
        defaultAssociadoShouldBeFound("dataNascimento.lessThan=" + UPDATED_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllAssociadosByDataNascimentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where dataNascimento is greater than DEFAULT_DATA_NASCIMENTO
        defaultAssociadoShouldNotBeFound("dataNascimento.greaterThan=" + DEFAULT_DATA_NASCIMENTO);

        // Get all the associadoList where dataNascimento is greater than SMALLER_DATA_NASCIMENTO
        defaultAssociadoShouldBeFound("dataNascimento.greaterThan=" + SMALLER_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllAssociadosByCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where created equals to DEFAULT_CREATED
        defaultAssociadoShouldBeFound("created.equals=" + DEFAULT_CREATED);

        // Get all the associadoList where created equals to UPDATED_CREATED
        defaultAssociadoShouldNotBeFound("created.equals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllAssociadosByCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where created in DEFAULT_CREATED or UPDATED_CREATED
        defaultAssociadoShouldBeFound("created.in=" + DEFAULT_CREATED + "," + UPDATED_CREATED);

        // Get all the associadoList where created equals to UPDATED_CREATED
        defaultAssociadoShouldNotBeFound("created.in=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllAssociadosByCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where created is not null
        defaultAssociadoShouldBeFound("created.specified=true");

        // Get all the associadoList where created is null
        defaultAssociadoShouldNotBeFound("created.specified=false");
    }

    @Test
    @Transactional
    void getAllAssociadosByModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where modified equals to DEFAULT_MODIFIED
        defaultAssociadoShouldBeFound("modified.equals=" + DEFAULT_MODIFIED);

        // Get all the associadoList where modified equals to UPDATED_MODIFIED
        defaultAssociadoShouldNotBeFound("modified.equals=" + UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void getAllAssociadosByModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where modified in DEFAULT_MODIFIED or UPDATED_MODIFIED
        defaultAssociadoShouldBeFound("modified.in=" + DEFAULT_MODIFIED + "," + UPDATED_MODIFIED);

        // Get all the associadoList where modified equals to UPDATED_MODIFIED
        defaultAssociadoShouldNotBeFound("modified.in=" + UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void getAllAssociadosByModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        // Get all the associadoList where modified is not null
        defaultAssociadoShouldBeFound("modified.specified=true");

        // Get all the associadoList where modified is null
        defaultAssociadoShouldNotBeFound("modified.specified=false");
    }

    @Test
    @Transactional
    void getAllAssociadosByReservaIsEqualToSomething() throws Exception {
        Reserva reserva;
        if (TestUtil.findAll(em, Reserva.class).isEmpty()) {
            associadoRepository.saveAndFlush(associado);
            reserva = ReservaResourceIT.createEntity(em);
        } else {
            reserva = TestUtil.findAll(em, Reserva.class).get(0);
        }
        em.persist(reserva);
        em.flush();
        associado.addReserva(reserva);
        associadoRepository.saveAndFlush(associado);
        Long reservaId = reserva.getId();

        // Get all the associadoList where reserva equals to reservaId
        defaultAssociadoShouldBeFound("reservaId.equals=" + reservaId);

        // Get all the associadoList where reserva equals to (reservaId + 1)
        defaultAssociadoShouldNotBeFound("reservaId.equals=" + (reservaId + 1));
    }

    @Test
    @Transactional
    void getAllAssociadosByContatosIsEqualToSomething() throws Exception {
        Contato contatos;
        if (TestUtil.findAll(em, Contato.class).isEmpty()) {
            associadoRepository.saveAndFlush(associado);
            contatos = ContatoResourceIT.createEntity(em);
        } else {
            contatos = TestUtil.findAll(em, Contato.class).get(0);
        }
        em.persist(contatos);
        em.flush();
        associado.addContatos(contatos);
        associadoRepository.saveAndFlush(associado);
        Long contatosId = contatos.getId();

        // Get all the associadoList where contatos equals to contatosId
        defaultAssociadoShouldBeFound("contatosId.equals=" + contatosId);

        // Get all the associadoList where contatos equals to (contatosId + 1)
        defaultAssociadoShouldNotBeFound("contatosId.equals=" + (contatosId + 1));
    }

    @Test
    @Transactional
    void getAllAssociadosByDependentesIsEqualToSomething() throws Exception {
        Dependente dependentes;
        if (TestUtil.findAll(em, Dependente.class).isEmpty()) {
            associadoRepository.saveAndFlush(associado);
            dependentes = DependenteResourceIT.createEntity(em);
        } else {
            dependentes = TestUtil.findAll(em, Dependente.class).get(0);
        }
        em.persist(dependentes);
        em.flush();
        associado.addDependentes(dependentes);
        associadoRepository.saveAndFlush(associado);
        Long dependentesId = dependentes.getId();

        // Get all the associadoList where dependentes equals to dependentesId
        defaultAssociadoShouldBeFound("dependentesId.equals=" + dependentesId);

        // Get all the associadoList where dependentes equals to (dependentesId + 1)
        defaultAssociadoShouldNotBeFound("dependentesId.equals=" + (dependentesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssociadoShouldBeFound(String filter) throws Exception {
        restAssociadoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(associado.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].matricula").value(hasItem(DEFAULT_MATRICULA)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())));

        // Check, that the count call also returns 1
        restAssociadoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssociadoShouldNotBeFound(String filter) throws Exception {
        restAssociadoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssociadoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAssociado() throws Exception {
        // Get the associado
        restAssociadoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAssociado() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        int databaseSizeBeforeUpdate = associadoRepository.findAll().size();

        // Update the associado
        Associado updatedAssociado = associadoRepository.findById(associado.getId()).get();
        // Disconnect from session so that the updates on updatedAssociado are not directly saved in db
        em.detach(updatedAssociado);
        updatedAssociado
            .nome(UPDATED_NOME)
            .matricula(UPDATED_MATRICULA)
            .status(UPDATED_STATUS)
            .telefone(UPDATED_TELEFONE)
            .email(UPDATED_EMAIL)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);
        AssociadoDTO associadoDTO = associadoMapper.toDto(updatedAssociado);

        restAssociadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, associadoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(associadoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Associado in the database
        List<Associado> associadoList = associadoRepository.findAll();
        assertThat(associadoList).hasSize(databaseSizeBeforeUpdate);
        Associado testAssociado = associadoList.get(associadoList.size() - 1);
        assertThat(testAssociado.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testAssociado.getMatricula()).isEqualTo(UPDATED_MATRICULA);
        assertThat(testAssociado.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAssociado.getTelefone()).isEqualTo(UPDATED_TELEFONE);
        assertThat(testAssociado.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAssociado.getDataNascimento()).isEqualTo(UPDATED_DATA_NASCIMENTO);
        assertThat(testAssociado.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testAssociado.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void putNonExistingAssociado() throws Exception {
        int databaseSizeBeforeUpdate = associadoRepository.findAll().size();
        associado.setId(count.incrementAndGet());

        // Create the Associado
        AssociadoDTO associadoDTO = associadoMapper.toDto(associado);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssociadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, associadoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(associadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Associado in the database
        List<Associado> associadoList = associadoRepository.findAll();
        assertThat(associadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssociado() throws Exception {
        int databaseSizeBeforeUpdate = associadoRepository.findAll().size();
        associado.setId(count.incrementAndGet());

        // Create the Associado
        AssociadoDTO associadoDTO = associadoMapper.toDto(associado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssociadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(associadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Associado in the database
        List<Associado> associadoList = associadoRepository.findAll();
        assertThat(associadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssociado() throws Exception {
        int databaseSizeBeforeUpdate = associadoRepository.findAll().size();
        associado.setId(count.incrementAndGet());

        // Create the Associado
        AssociadoDTO associadoDTO = associadoMapper.toDto(associado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssociadoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(associadoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Associado in the database
        List<Associado> associadoList = associadoRepository.findAll();
        assertThat(associadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAssociadoWithPatch() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        int databaseSizeBeforeUpdate = associadoRepository.findAll().size();

        // Update the associado using partial update
        Associado partialUpdatedAssociado = new Associado();
        partialUpdatedAssociado.setId(associado.getId());

        partialUpdatedAssociado.email(UPDATED_EMAIL).dataNascimento(UPDATED_DATA_NASCIMENTO).modified(UPDATED_MODIFIED);

        restAssociadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssociado.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssociado))
            )
            .andExpect(status().isOk());

        // Validate the Associado in the database
        List<Associado> associadoList = associadoRepository.findAll();
        assertThat(associadoList).hasSize(databaseSizeBeforeUpdate);
        Associado testAssociado = associadoList.get(associadoList.size() - 1);
        assertThat(testAssociado.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testAssociado.getMatricula()).isEqualTo(DEFAULT_MATRICULA);
        assertThat(testAssociado.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testAssociado.getTelefone()).isEqualTo(DEFAULT_TELEFONE);
        assertThat(testAssociado.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAssociado.getDataNascimento()).isEqualTo(UPDATED_DATA_NASCIMENTO);
        assertThat(testAssociado.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testAssociado.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void fullUpdateAssociadoWithPatch() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        int databaseSizeBeforeUpdate = associadoRepository.findAll().size();

        // Update the associado using partial update
        Associado partialUpdatedAssociado = new Associado();
        partialUpdatedAssociado.setId(associado.getId());

        partialUpdatedAssociado
            .nome(UPDATED_NOME)
            .matricula(UPDATED_MATRICULA)
            .status(UPDATED_STATUS)
            .telefone(UPDATED_TELEFONE)
            .email(UPDATED_EMAIL)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);

        restAssociadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssociado.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssociado))
            )
            .andExpect(status().isOk());

        // Validate the Associado in the database
        List<Associado> associadoList = associadoRepository.findAll();
        assertThat(associadoList).hasSize(databaseSizeBeforeUpdate);
        Associado testAssociado = associadoList.get(associadoList.size() - 1);
        assertThat(testAssociado.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testAssociado.getMatricula()).isEqualTo(UPDATED_MATRICULA);
        assertThat(testAssociado.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAssociado.getTelefone()).isEqualTo(UPDATED_TELEFONE);
        assertThat(testAssociado.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAssociado.getDataNascimento()).isEqualTo(UPDATED_DATA_NASCIMENTO);
        assertThat(testAssociado.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testAssociado.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingAssociado() throws Exception {
        int databaseSizeBeforeUpdate = associadoRepository.findAll().size();
        associado.setId(count.incrementAndGet());

        // Create the Associado
        AssociadoDTO associadoDTO = associadoMapper.toDto(associado);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssociadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, associadoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(associadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Associado in the database
        List<Associado> associadoList = associadoRepository.findAll();
        assertThat(associadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssociado() throws Exception {
        int databaseSizeBeforeUpdate = associadoRepository.findAll().size();
        associado.setId(count.incrementAndGet());

        // Create the Associado
        AssociadoDTO associadoDTO = associadoMapper.toDto(associado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssociadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(associadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Associado in the database
        List<Associado> associadoList = associadoRepository.findAll();
        assertThat(associadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssociado() throws Exception {
        int databaseSizeBeforeUpdate = associadoRepository.findAll().size();
        associado.setId(count.incrementAndGet());

        // Create the Associado
        AssociadoDTO associadoDTO = associadoMapper.toDto(associado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssociadoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(associadoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Associado in the database
        List<Associado> associadoList = associadoRepository.findAll();
        assertThat(associadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAssociado() throws Exception {
        // Initialize the database
        associadoRepository.saveAndFlush(associado);

        int databaseSizeBeforeDelete = associadoRepository.findAll().size();

        // Delete the associado
        restAssociadoMockMvc
            .perform(delete(ENTITY_API_URL_ID, associado.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Associado> associadoList = associadoRepository.findAll();
        assertThat(associadoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
