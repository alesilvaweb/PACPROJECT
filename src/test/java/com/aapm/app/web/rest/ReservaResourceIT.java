package com.aapm.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aapm.app.IntegrationTest;
import com.aapm.app.domain.Associado;
import com.aapm.app.domain.Departamento;
import com.aapm.app.domain.Local;
import com.aapm.app.domain.Reserva;
import com.aapm.app.domain.enumeration.StatusReserva;
import com.aapm.app.repository.ReservaRepository;
import com.aapm.app.service.ReservaService;
import com.aapm.app.service.criteria.ReservaCriteria;
import com.aapm.app.service.dto.ReservaDTO;
import com.aapm.app.service.mapper.ReservaMapper;
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
 * Integration tests for the {@link ReservaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReservaResourceIT {

    private static final String DEFAULT_MOTIVO_RESERVA = "AAAAAAAAAA";
    private static final String UPDATED_MOTIVO_RESERVA = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUM_PESSOAS = 1;
    private static final Integer UPDATED_NUM_PESSOAS = 2;
    private static final Integer SMALLER_NUM_PESSOAS = 1 - 1;

    private static final StatusReserva DEFAULT_STATUS = StatusReserva.Agendado;
    private static final StatusReserva UPDATED_STATUS = StatusReserva.Bloqueado;

    private static final LocalDate DEFAULT_DATA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_SOMENTE_FUNCIONARIOS = false;
    private static final Boolean UPDATED_SOMENTE_FUNCIONARIOS = true;

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/reservas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReservaRepository reservaRepository;

    @Mock
    private ReservaRepository reservaRepositoryMock;

    @Autowired
    private ReservaMapper reservaMapper;

    @Mock
    private ReservaService reservaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReservaMockMvc;

    private Reserva reserva;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reserva createEntity(EntityManager em) {
        Reserva reserva = new Reserva()
            .motivoReserva(DEFAULT_MOTIVO_RESERVA)
            .descricao(DEFAULT_DESCRICAO)
            .numPessoas(DEFAULT_NUM_PESSOAS)
            .status(DEFAULT_STATUS)
            .data(DEFAULT_DATA)
            .somenteFuncionarios(DEFAULT_SOMENTE_FUNCIONARIOS)
            .created(DEFAULT_CREATED)
            .modified(DEFAULT_MODIFIED);
        return reserva;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reserva createUpdatedEntity(EntityManager em) {
        Reserva reserva = new Reserva()
            .motivoReserva(UPDATED_MOTIVO_RESERVA)
            .descricao(UPDATED_DESCRICAO)
            .numPessoas(UPDATED_NUM_PESSOAS)
            .status(UPDATED_STATUS)
            .data(UPDATED_DATA)
            .somenteFuncionarios(UPDATED_SOMENTE_FUNCIONARIOS)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);
        return reserva;
    }

    @BeforeEach
    public void initTest() {
        reserva = createEntity(em);
    }

    @Test
    @Transactional
    void createReserva() throws Exception {
        int databaseSizeBeforeCreate = reservaRepository.findAll().size();
        // Create the Reserva
        ReservaDTO reservaDTO = reservaMapper.toDto(reserva);
        restReservaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reservaDTO)))
            .andExpect(status().isCreated());

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeCreate + 1);
        Reserva testReserva = reservaList.get(reservaList.size() - 1);
        assertThat(testReserva.getMotivoReserva()).isEqualTo(DEFAULT_MOTIVO_RESERVA);
        assertThat(testReserva.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testReserva.getNumPessoas()).isEqualTo(DEFAULT_NUM_PESSOAS);
        assertThat(testReserva.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testReserva.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testReserva.getSomenteFuncionarios()).isEqualTo(DEFAULT_SOMENTE_FUNCIONARIOS);
        assertThat(testReserva.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testReserva.getModified()).isEqualTo(DEFAULT_MODIFIED);
    }

    @Test
    @Transactional
    void createReservaWithExistingId() throws Exception {
        // Create the Reserva with an existing ID
        reserva.setId(1L);
        ReservaDTO reservaDTO = reservaMapper.toDto(reserva);

        int databaseSizeBeforeCreate = reservaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReservaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reservaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMotivoReservaIsRequired() throws Exception {
        int databaseSizeBeforeTest = reservaRepository.findAll().size();
        // set the field null
        reserva.setMotivoReserva(null);

        // Create the Reserva, which fails.
        ReservaDTO reservaDTO = reservaMapper.toDto(reserva);

        restReservaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reservaDTO)))
            .andExpect(status().isBadRequest());

        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumPessoasIsRequired() throws Exception {
        int databaseSizeBeforeTest = reservaRepository.findAll().size();
        // set the field null
        reserva.setNumPessoas(null);

        // Create the Reserva, which fails.
        ReservaDTO reservaDTO = reservaMapper.toDto(reserva);

        restReservaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reservaDTO)))
            .andExpect(status().isBadRequest());

        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataIsRequired() throws Exception {
        int databaseSizeBeforeTest = reservaRepository.findAll().size();
        // set the field null
        reserva.setData(null);

        // Create the Reserva, which fails.
        ReservaDTO reservaDTO = reservaMapper.toDto(reserva);

        restReservaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reservaDTO)))
            .andExpect(status().isBadRequest());

        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReservas() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList
        restReservaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reserva.getId().intValue())))
            .andExpect(jsonPath("$.[*].motivoReserva").value(hasItem(DEFAULT_MOTIVO_RESERVA)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].numPessoas").value(hasItem(DEFAULT_NUM_PESSOAS)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())))
            .andExpect(jsonPath("$.[*].somenteFuncionarios").value(hasItem(DEFAULT_SOMENTE_FUNCIONARIOS.booleanValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReservasWithEagerRelationshipsIsEnabled() throws Exception {
        when(reservaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReservaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(reservaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReservasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(reservaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReservaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(reservaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getReserva() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get the reserva
        restReservaMockMvc
            .perform(get(ENTITY_API_URL_ID, reserva.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reserva.getId().intValue()))
            .andExpect(jsonPath("$.motivoReserva").value(DEFAULT_MOTIVO_RESERVA))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.numPessoas").value(DEFAULT_NUM_PESSOAS))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()))
            .andExpect(jsonPath("$.somenteFuncionarios").value(DEFAULT_SOMENTE_FUNCIONARIOS.booleanValue()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.modified").value(DEFAULT_MODIFIED.toString()));
    }

    @Test
    @Transactional
    void getReservasByIdFiltering() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        Long id = reserva.getId();

        defaultReservaShouldBeFound("id.equals=" + id);
        defaultReservaShouldNotBeFound("id.notEquals=" + id);

        defaultReservaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultReservaShouldNotBeFound("id.greaterThan=" + id);

        defaultReservaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultReservaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReservasByMotivoReservaIsEqualToSomething() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where motivoReserva equals to DEFAULT_MOTIVO_RESERVA
        defaultReservaShouldBeFound("motivoReserva.equals=" + DEFAULT_MOTIVO_RESERVA);

        // Get all the reservaList where motivoReserva equals to UPDATED_MOTIVO_RESERVA
        defaultReservaShouldNotBeFound("motivoReserva.equals=" + UPDATED_MOTIVO_RESERVA);
    }

    @Test
    @Transactional
    void getAllReservasByMotivoReservaIsInShouldWork() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where motivoReserva in DEFAULT_MOTIVO_RESERVA or UPDATED_MOTIVO_RESERVA
        defaultReservaShouldBeFound("motivoReserva.in=" + DEFAULT_MOTIVO_RESERVA + "," + UPDATED_MOTIVO_RESERVA);

        // Get all the reservaList where motivoReserva equals to UPDATED_MOTIVO_RESERVA
        defaultReservaShouldNotBeFound("motivoReserva.in=" + UPDATED_MOTIVO_RESERVA);
    }

    @Test
    @Transactional
    void getAllReservasByMotivoReservaIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where motivoReserva is not null
        defaultReservaShouldBeFound("motivoReserva.specified=true");

        // Get all the reservaList where motivoReserva is null
        defaultReservaShouldNotBeFound("motivoReserva.specified=false");
    }

    @Test
    @Transactional
    void getAllReservasByMotivoReservaContainsSomething() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where motivoReserva contains DEFAULT_MOTIVO_RESERVA
        defaultReservaShouldBeFound("motivoReserva.contains=" + DEFAULT_MOTIVO_RESERVA);

        // Get all the reservaList where motivoReserva contains UPDATED_MOTIVO_RESERVA
        defaultReservaShouldNotBeFound("motivoReserva.contains=" + UPDATED_MOTIVO_RESERVA);
    }

    @Test
    @Transactional
    void getAllReservasByMotivoReservaNotContainsSomething() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where motivoReserva does not contain DEFAULT_MOTIVO_RESERVA
        defaultReservaShouldNotBeFound("motivoReserva.doesNotContain=" + DEFAULT_MOTIVO_RESERVA);

        // Get all the reservaList where motivoReserva does not contain UPDATED_MOTIVO_RESERVA
        defaultReservaShouldBeFound("motivoReserva.doesNotContain=" + UPDATED_MOTIVO_RESERVA);
    }

    @Test
    @Transactional
    void getAllReservasByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where descricao equals to DEFAULT_DESCRICAO
        defaultReservaShouldBeFound("descricao.equals=" + DEFAULT_DESCRICAO);

        // Get all the reservaList where descricao equals to UPDATED_DESCRICAO
        defaultReservaShouldNotBeFound("descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllReservasByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where descricao in DEFAULT_DESCRICAO or UPDATED_DESCRICAO
        defaultReservaShouldBeFound("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO);

        // Get all the reservaList where descricao equals to UPDATED_DESCRICAO
        defaultReservaShouldNotBeFound("descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllReservasByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where descricao is not null
        defaultReservaShouldBeFound("descricao.specified=true");

        // Get all the reservaList where descricao is null
        defaultReservaShouldNotBeFound("descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllReservasByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where descricao contains DEFAULT_DESCRICAO
        defaultReservaShouldBeFound("descricao.contains=" + DEFAULT_DESCRICAO);

        // Get all the reservaList where descricao contains UPDATED_DESCRICAO
        defaultReservaShouldNotBeFound("descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllReservasByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where descricao does not contain DEFAULT_DESCRICAO
        defaultReservaShouldNotBeFound("descricao.doesNotContain=" + DEFAULT_DESCRICAO);

        // Get all the reservaList where descricao does not contain UPDATED_DESCRICAO
        defaultReservaShouldBeFound("descricao.doesNotContain=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllReservasByNumPessoasIsEqualToSomething() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where numPessoas equals to DEFAULT_NUM_PESSOAS
        defaultReservaShouldBeFound("numPessoas.equals=" + DEFAULT_NUM_PESSOAS);

        // Get all the reservaList where numPessoas equals to UPDATED_NUM_PESSOAS
        defaultReservaShouldNotBeFound("numPessoas.equals=" + UPDATED_NUM_PESSOAS);
    }

    @Test
    @Transactional
    void getAllReservasByNumPessoasIsInShouldWork() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where numPessoas in DEFAULT_NUM_PESSOAS or UPDATED_NUM_PESSOAS
        defaultReservaShouldBeFound("numPessoas.in=" + DEFAULT_NUM_PESSOAS + "," + UPDATED_NUM_PESSOAS);

        // Get all the reservaList where numPessoas equals to UPDATED_NUM_PESSOAS
        defaultReservaShouldNotBeFound("numPessoas.in=" + UPDATED_NUM_PESSOAS);
    }

    @Test
    @Transactional
    void getAllReservasByNumPessoasIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where numPessoas is not null
        defaultReservaShouldBeFound("numPessoas.specified=true");

        // Get all the reservaList where numPessoas is null
        defaultReservaShouldNotBeFound("numPessoas.specified=false");
    }

    @Test
    @Transactional
    void getAllReservasByNumPessoasIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where numPessoas is greater than or equal to DEFAULT_NUM_PESSOAS
        defaultReservaShouldBeFound("numPessoas.greaterThanOrEqual=" + DEFAULT_NUM_PESSOAS);

        // Get all the reservaList where numPessoas is greater than or equal to UPDATED_NUM_PESSOAS
        defaultReservaShouldNotBeFound("numPessoas.greaterThanOrEqual=" + UPDATED_NUM_PESSOAS);
    }

    @Test
    @Transactional
    void getAllReservasByNumPessoasIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where numPessoas is less than or equal to DEFAULT_NUM_PESSOAS
        defaultReservaShouldBeFound("numPessoas.lessThanOrEqual=" + DEFAULT_NUM_PESSOAS);

        // Get all the reservaList where numPessoas is less than or equal to SMALLER_NUM_PESSOAS
        defaultReservaShouldNotBeFound("numPessoas.lessThanOrEqual=" + SMALLER_NUM_PESSOAS);
    }

    @Test
    @Transactional
    void getAllReservasByNumPessoasIsLessThanSomething() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where numPessoas is less than DEFAULT_NUM_PESSOAS
        defaultReservaShouldNotBeFound("numPessoas.lessThan=" + DEFAULT_NUM_PESSOAS);

        // Get all the reservaList where numPessoas is less than UPDATED_NUM_PESSOAS
        defaultReservaShouldBeFound("numPessoas.lessThan=" + UPDATED_NUM_PESSOAS);
    }

    @Test
    @Transactional
    void getAllReservasByNumPessoasIsGreaterThanSomething() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where numPessoas is greater than DEFAULT_NUM_PESSOAS
        defaultReservaShouldNotBeFound("numPessoas.greaterThan=" + DEFAULT_NUM_PESSOAS);

        // Get all the reservaList where numPessoas is greater than SMALLER_NUM_PESSOAS
        defaultReservaShouldBeFound("numPessoas.greaterThan=" + SMALLER_NUM_PESSOAS);
    }

    @Test
    @Transactional
    void getAllReservasByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where status equals to DEFAULT_STATUS
        defaultReservaShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the reservaList where status equals to UPDATED_STATUS
        defaultReservaShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllReservasByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultReservaShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the reservaList where status equals to UPDATED_STATUS
        defaultReservaShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllReservasByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where status is not null
        defaultReservaShouldBeFound("status.specified=true");

        // Get all the reservaList where status is null
        defaultReservaShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllReservasByDataIsEqualToSomething() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where data equals to DEFAULT_DATA
        defaultReservaShouldBeFound("data.equals=" + DEFAULT_DATA);

        // Get all the reservaList where data equals to UPDATED_DATA
        defaultReservaShouldNotBeFound("data.equals=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllReservasByDataIsInShouldWork() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where data in DEFAULT_DATA or UPDATED_DATA
        defaultReservaShouldBeFound("data.in=" + DEFAULT_DATA + "," + UPDATED_DATA);

        // Get all the reservaList where data equals to UPDATED_DATA
        defaultReservaShouldNotBeFound("data.in=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllReservasByDataIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where data is not null
        defaultReservaShouldBeFound("data.specified=true");

        // Get all the reservaList where data is null
        defaultReservaShouldNotBeFound("data.specified=false");
    }

    @Test
    @Transactional
    void getAllReservasByDataIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where data is greater than or equal to DEFAULT_DATA
        defaultReservaShouldBeFound("data.greaterThanOrEqual=" + DEFAULT_DATA);

        // Get all the reservaList where data is greater than or equal to UPDATED_DATA
        defaultReservaShouldNotBeFound("data.greaterThanOrEqual=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllReservasByDataIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where data is less than or equal to DEFAULT_DATA
        defaultReservaShouldBeFound("data.lessThanOrEqual=" + DEFAULT_DATA);

        // Get all the reservaList where data is less than or equal to SMALLER_DATA
        defaultReservaShouldNotBeFound("data.lessThanOrEqual=" + SMALLER_DATA);
    }

    @Test
    @Transactional
    void getAllReservasByDataIsLessThanSomething() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where data is less than DEFAULT_DATA
        defaultReservaShouldNotBeFound("data.lessThan=" + DEFAULT_DATA);

        // Get all the reservaList where data is less than UPDATED_DATA
        defaultReservaShouldBeFound("data.lessThan=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllReservasByDataIsGreaterThanSomething() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where data is greater than DEFAULT_DATA
        defaultReservaShouldNotBeFound("data.greaterThan=" + DEFAULT_DATA);

        // Get all the reservaList where data is greater than SMALLER_DATA
        defaultReservaShouldBeFound("data.greaterThan=" + SMALLER_DATA);
    }

    @Test
    @Transactional
    void getAllReservasBySomenteFuncionariosIsEqualToSomething() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where somenteFuncionarios equals to DEFAULT_SOMENTE_FUNCIONARIOS
        defaultReservaShouldBeFound("somenteFuncionarios.equals=" + DEFAULT_SOMENTE_FUNCIONARIOS);

        // Get all the reservaList where somenteFuncionarios equals to UPDATED_SOMENTE_FUNCIONARIOS
        defaultReservaShouldNotBeFound("somenteFuncionarios.equals=" + UPDATED_SOMENTE_FUNCIONARIOS);
    }

    @Test
    @Transactional
    void getAllReservasBySomenteFuncionariosIsInShouldWork() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where somenteFuncionarios in DEFAULT_SOMENTE_FUNCIONARIOS or UPDATED_SOMENTE_FUNCIONARIOS
        defaultReservaShouldBeFound("somenteFuncionarios.in=" + DEFAULT_SOMENTE_FUNCIONARIOS + "," + UPDATED_SOMENTE_FUNCIONARIOS);

        // Get all the reservaList where somenteFuncionarios equals to UPDATED_SOMENTE_FUNCIONARIOS
        defaultReservaShouldNotBeFound("somenteFuncionarios.in=" + UPDATED_SOMENTE_FUNCIONARIOS);
    }

    @Test
    @Transactional
    void getAllReservasBySomenteFuncionariosIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where somenteFuncionarios is not null
        defaultReservaShouldBeFound("somenteFuncionarios.specified=true");

        // Get all the reservaList where somenteFuncionarios is null
        defaultReservaShouldNotBeFound("somenteFuncionarios.specified=false");
    }

    @Test
    @Transactional
    void getAllReservasByCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where created equals to DEFAULT_CREATED
        defaultReservaShouldBeFound("created.equals=" + DEFAULT_CREATED);

        // Get all the reservaList where created equals to UPDATED_CREATED
        defaultReservaShouldNotBeFound("created.equals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllReservasByCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where created in DEFAULT_CREATED or UPDATED_CREATED
        defaultReservaShouldBeFound("created.in=" + DEFAULT_CREATED + "," + UPDATED_CREATED);

        // Get all the reservaList where created equals to UPDATED_CREATED
        defaultReservaShouldNotBeFound("created.in=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllReservasByCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where created is not null
        defaultReservaShouldBeFound("created.specified=true");

        // Get all the reservaList where created is null
        defaultReservaShouldNotBeFound("created.specified=false");
    }

    @Test
    @Transactional
    void getAllReservasByModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where modified equals to DEFAULT_MODIFIED
        defaultReservaShouldBeFound("modified.equals=" + DEFAULT_MODIFIED);

        // Get all the reservaList where modified equals to UPDATED_MODIFIED
        defaultReservaShouldNotBeFound("modified.equals=" + UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void getAllReservasByModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where modified in DEFAULT_MODIFIED or UPDATED_MODIFIED
        defaultReservaShouldBeFound("modified.in=" + DEFAULT_MODIFIED + "," + UPDATED_MODIFIED);

        // Get all the reservaList where modified equals to UPDATED_MODIFIED
        defaultReservaShouldNotBeFound("modified.in=" + UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void getAllReservasByModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList where modified is not null
        defaultReservaShouldBeFound("modified.specified=true");

        // Get all the reservaList where modified is null
        defaultReservaShouldNotBeFound("modified.specified=false");
    }

    @Test
    @Transactional
    void getAllReservasByLocalIsEqualToSomething() throws Exception {
        Local local;
        if (TestUtil.findAll(em, Local.class).isEmpty()) {
            reservaRepository.saveAndFlush(reserva);
            local = LocalResourceIT.createEntity(em);
        } else {
            local = TestUtil.findAll(em, Local.class).get(0);
        }
        em.persist(local);
        em.flush();
        reserva.setLocal(local);
        reservaRepository.saveAndFlush(reserva);
        Long localId = local.getId();

        // Get all the reservaList where local equals to localId
        defaultReservaShouldBeFound("localId.equals=" + localId);

        // Get all the reservaList where local equals to (localId + 1)
        defaultReservaShouldNotBeFound("localId.equals=" + (localId + 1));
    }

    @Test
    @Transactional
    void getAllReservasByAssociadoIsEqualToSomething() throws Exception {
        Associado associado;
        if (TestUtil.findAll(em, Associado.class).isEmpty()) {
            reservaRepository.saveAndFlush(reserva);
            associado = AssociadoResourceIT.createEntity(em);
        } else {
            associado = TestUtil.findAll(em, Associado.class).get(0);
        }
        em.persist(associado);
        em.flush();
        reserva.setAssociado(associado);
        reservaRepository.saveAndFlush(reserva);
        Long associadoId = associado.getId();

        // Get all the reservaList where associado equals to associadoId
        defaultReservaShouldBeFound("associadoId.equals=" + associadoId);

        // Get all the reservaList where associado equals to (associadoId + 1)
        defaultReservaShouldNotBeFound("associadoId.equals=" + (associadoId + 1));
    }

    @Test
    @Transactional
    void getAllReservasByDepartamentoIsEqualToSomething() throws Exception {
        Departamento departamento;
        if (TestUtil.findAll(em, Departamento.class).isEmpty()) {
            reservaRepository.saveAndFlush(reserva);
            departamento = DepartamentoResourceIT.createEntity(em);
        } else {
            departamento = TestUtil.findAll(em, Departamento.class).get(0);
        }
        em.persist(departamento);
        em.flush();
        reserva.setDepartamento(departamento);
        reservaRepository.saveAndFlush(reserva);
        Long departamentoId = departamento.getId();

        // Get all the reservaList where departamento equals to departamentoId
        defaultReservaShouldBeFound("departamentoId.equals=" + departamentoId);

        // Get all the reservaList where departamento equals to (departamentoId + 1)
        defaultReservaShouldNotBeFound("departamentoId.equals=" + (departamentoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReservaShouldBeFound(String filter) throws Exception {
        restReservaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reserva.getId().intValue())))
            .andExpect(jsonPath("$.[*].motivoReserva").value(hasItem(DEFAULT_MOTIVO_RESERVA)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].numPessoas").value(hasItem(DEFAULT_NUM_PESSOAS)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())))
            .andExpect(jsonPath("$.[*].somenteFuncionarios").value(hasItem(DEFAULT_SOMENTE_FUNCIONARIOS.booleanValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())));

        // Check, that the count call also returns 1
        restReservaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReservaShouldNotBeFound(String filter) throws Exception {
        restReservaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReservaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReserva() throws Exception {
        // Get the reserva
        restReservaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReserva() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        int databaseSizeBeforeUpdate = reservaRepository.findAll().size();

        // Update the reserva
        Reserva updatedReserva = reservaRepository.findById(reserva.getId()).get();
        // Disconnect from session so that the updates on updatedReserva are not directly saved in db
        em.detach(updatedReserva);
        updatedReserva
            .motivoReserva(UPDATED_MOTIVO_RESERVA)
            .descricao(UPDATED_DESCRICAO)
            .numPessoas(UPDATED_NUM_PESSOAS)
            .status(UPDATED_STATUS)
            .data(UPDATED_DATA)
            .somenteFuncionarios(UPDATED_SOMENTE_FUNCIONARIOS)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);
        ReservaDTO reservaDTO = reservaMapper.toDto(updatedReserva);

        restReservaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reservaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reservaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeUpdate);
        Reserva testReserva = reservaList.get(reservaList.size() - 1);
        assertThat(testReserva.getMotivoReserva()).isEqualTo(UPDATED_MOTIVO_RESERVA);
        assertThat(testReserva.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testReserva.getNumPessoas()).isEqualTo(UPDATED_NUM_PESSOAS);
        assertThat(testReserva.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testReserva.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testReserva.getSomenteFuncionarios()).isEqualTo(UPDATED_SOMENTE_FUNCIONARIOS);
        assertThat(testReserva.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testReserva.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void putNonExistingReserva() throws Exception {
        int databaseSizeBeforeUpdate = reservaRepository.findAll().size();
        reserva.setId(count.incrementAndGet());

        // Create the Reserva
        ReservaDTO reservaDTO = reservaMapper.toDto(reserva);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reservaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reservaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReserva() throws Exception {
        int databaseSizeBeforeUpdate = reservaRepository.findAll().size();
        reserva.setId(count.incrementAndGet());

        // Create the Reserva
        ReservaDTO reservaDTO = reservaMapper.toDto(reserva);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reservaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReserva() throws Exception {
        int databaseSizeBeforeUpdate = reservaRepository.findAll().size();
        reserva.setId(count.incrementAndGet());

        // Create the Reserva
        ReservaDTO reservaDTO = reservaMapper.toDto(reserva);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reservaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReservaWithPatch() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        int databaseSizeBeforeUpdate = reservaRepository.findAll().size();

        // Update the reserva using partial update
        Reserva partialUpdatedReserva = new Reserva();
        partialUpdatedReserva.setId(reserva.getId());

        partialUpdatedReserva
            .motivoReserva(UPDATED_MOTIVO_RESERVA)
            .descricao(UPDATED_DESCRICAO)
            .numPessoas(UPDATED_NUM_PESSOAS)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED);

        restReservaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReserva.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReserva))
            )
            .andExpect(status().isOk());

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeUpdate);
        Reserva testReserva = reservaList.get(reservaList.size() - 1);
        assertThat(testReserva.getMotivoReserva()).isEqualTo(UPDATED_MOTIVO_RESERVA);
        assertThat(testReserva.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testReserva.getNumPessoas()).isEqualTo(UPDATED_NUM_PESSOAS);
        assertThat(testReserva.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testReserva.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testReserva.getSomenteFuncionarios()).isEqualTo(DEFAULT_SOMENTE_FUNCIONARIOS);
        assertThat(testReserva.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testReserva.getModified()).isEqualTo(DEFAULT_MODIFIED);
    }

    @Test
    @Transactional
    void fullUpdateReservaWithPatch() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        int databaseSizeBeforeUpdate = reservaRepository.findAll().size();

        // Update the reserva using partial update
        Reserva partialUpdatedReserva = new Reserva();
        partialUpdatedReserva.setId(reserva.getId());

        partialUpdatedReserva
            .motivoReserva(UPDATED_MOTIVO_RESERVA)
            .descricao(UPDATED_DESCRICAO)
            .numPessoas(UPDATED_NUM_PESSOAS)
            .status(UPDATED_STATUS)
            .data(UPDATED_DATA)
            .somenteFuncionarios(UPDATED_SOMENTE_FUNCIONARIOS)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);

        restReservaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReserva.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReserva))
            )
            .andExpect(status().isOk());

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeUpdate);
        Reserva testReserva = reservaList.get(reservaList.size() - 1);
        assertThat(testReserva.getMotivoReserva()).isEqualTo(UPDATED_MOTIVO_RESERVA);
        assertThat(testReserva.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testReserva.getNumPessoas()).isEqualTo(UPDATED_NUM_PESSOAS);
        assertThat(testReserva.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testReserva.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testReserva.getSomenteFuncionarios()).isEqualTo(UPDATED_SOMENTE_FUNCIONARIOS);
        assertThat(testReserva.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testReserva.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingReserva() throws Exception {
        int databaseSizeBeforeUpdate = reservaRepository.findAll().size();
        reserva.setId(count.incrementAndGet());

        // Create the Reserva
        ReservaDTO reservaDTO = reservaMapper.toDto(reserva);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reservaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reservaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReserva() throws Exception {
        int databaseSizeBeforeUpdate = reservaRepository.findAll().size();
        reserva.setId(count.incrementAndGet());

        // Create the Reserva
        ReservaDTO reservaDTO = reservaMapper.toDto(reserva);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reservaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReserva() throws Exception {
        int databaseSizeBeforeUpdate = reservaRepository.findAll().size();
        reserva.setId(count.incrementAndGet());

        // Create the Reserva
        ReservaDTO reservaDTO = reservaMapper.toDto(reserva);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(reservaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reserva in the database
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReserva() throws Exception {
        // Initialize the database
        reservaRepository.saveAndFlush(reserva);

        int databaseSizeBeforeDelete = reservaRepository.findAll().size();

        // Delete the reserva
        restReservaMockMvc
            .perform(delete(ENTITY_API_URL_ID, reserva.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Reserva> reservaList = reservaRepository.findAll();
        assertThat(reservaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
