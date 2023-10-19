package com.aapm.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aapm.app.IntegrationTest;
import com.aapm.app.domain.Mensagem;
import com.aapm.app.domain.Tipo;
import com.aapm.app.domain.enumeration.Status;
import com.aapm.app.repository.MensagemRepository;
import com.aapm.app.service.MensagemService;
import com.aapm.app.service.criteria.MensagemCriteria;
import com.aapm.app.service.dto.MensagemDTO;
import com.aapm.app.service.mapper.MensagemMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link MensagemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MensagemResourceIT {

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_CONTEUDO = "AAAAAAAAAA";
    private static final String UPDATED_CONTEUDO = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGEN = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGEN = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGEN_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGEN_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LINK = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final Status DEFAULT_STATUS = Status.Ativo;
    private static final Status UPDATED_STATUS = Status.Inativo;

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/mensagems";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MensagemRepository mensagemRepository;

    @Mock
    private MensagemRepository mensagemRepositoryMock;

    @Autowired
    private MensagemMapper mensagemMapper;

    @Mock
    private MensagemService mensagemServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMensagemMockMvc;

    private Mensagem mensagem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mensagem createEntity(EntityManager em) {
        Mensagem mensagem = new Mensagem()
            .titulo(DEFAULT_TITULO)
            .descricao(DEFAULT_DESCRICAO)
            .conteudo(DEFAULT_CONTEUDO)
            .imagen(DEFAULT_IMAGEN)
            .imagenContentType(DEFAULT_IMAGEN_CONTENT_TYPE)
            .link(DEFAULT_LINK)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .status(DEFAULT_STATUS)
            .created(DEFAULT_CREATED)
            .modified(DEFAULT_MODIFIED);
        return mensagem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mensagem createUpdatedEntity(EntityManager em) {
        Mensagem mensagem = new Mensagem()
            .titulo(UPDATED_TITULO)
            .descricao(UPDATED_DESCRICAO)
            .conteudo(UPDATED_CONTEUDO)
            .imagen(UPDATED_IMAGEN)
            .imagenContentType(UPDATED_IMAGEN_CONTENT_TYPE)
            .link(UPDATED_LINK)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);
        return mensagem;
    }

    @BeforeEach
    public void initTest() {
        mensagem = createEntity(em);
    }

    @Test
    @Transactional
    void createMensagem() throws Exception {
        int databaseSizeBeforeCreate = mensagemRepository.findAll().size();
        // Create the Mensagem
        MensagemDTO mensagemDTO = mensagemMapper.toDto(mensagem);
        restMensagemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mensagemDTO)))
            .andExpect(status().isCreated());

        // Validate the Mensagem in the database
        List<Mensagem> mensagemList = mensagemRepository.findAll();
        assertThat(mensagemList).hasSize(databaseSizeBeforeCreate + 1);
        Mensagem testMensagem = mensagemList.get(mensagemList.size() - 1);
        assertThat(testMensagem.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testMensagem.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testMensagem.getConteudo()).isEqualTo(DEFAULT_CONTEUDO);
        assertThat(testMensagem.getImagen()).isEqualTo(DEFAULT_IMAGEN);
        assertThat(testMensagem.getImagenContentType()).isEqualTo(DEFAULT_IMAGEN_CONTENT_TYPE);
        assertThat(testMensagem.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testMensagem.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testMensagem.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testMensagem.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testMensagem.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testMensagem.getModified()).isEqualTo(DEFAULT_MODIFIED);
    }

    @Test
    @Transactional
    void createMensagemWithExistingId() throws Exception {
        // Create the Mensagem with an existing ID
        mensagem.setId(1L);
        MensagemDTO mensagemDTO = mensagemMapper.toDto(mensagem);

        int databaseSizeBeforeCreate = mensagemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMensagemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mensagemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Mensagem in the database
        List<Mensagem> mensagemList = mensagemRepository.findAll();
        assertThat(mensagemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTituloIsRequired() throws Exception {
        int databaseSizeBeforeTest = mensagemRepository.findAll().size();
        // set the field null
        mensagem.setTitulo(null);

        // Create the Mensagem, which fails.
        MensagemDTO mensagemDTO = mensagemMapper.toDto(mensagem);

        restMensagemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mensagemDTO)))
            .andExpect(status().isBadRequest());

        List<Mensagem> mensagemList = mensagemRepository.findAll();
        assertThat(mensagemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = mensagemRepository.findAll().size();
        // set the field null
        mensagem.setDescricao(null);

        // Create the Mensagem, which fails.
        MensagemDTO mensagemDTO = mensagemMapper.toDto(mensagem);

        restMensagemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mensagemDTO)))
            .andExpect(status().isBadRequest());

        List<Mensagem> mensagemList = mensagemRepository.findAll();
        assertThat(mensagemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMensagems() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList
        restMensagemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mensagem.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].conteudo").value(hasItem(DEFAULT_CONTEUDO.toString())))
            .andExpect(jsonPath("$.[*].imagenContentType").value(hasItem(DEFAULT_IMAGEN_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imagen").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGEN))))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMensagemsWithEagerRelationshipsIsEnabled() throws Exception {
        when(mensagemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMensagemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(mensagemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMensagemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(mensagemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMensagemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(mensagemRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMensagem() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get the mensagem
        restMensagemMockMvc
            .perform(get(ENTITY_API_URL_ID, mensagem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mensagem.getId().intValue()))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.conteudo").value(DEFAULT_CONTEUDO.toString()))
            .andExpect(jsonPath("$.imagenContentType").value(DEFAULT_IMAGEN_CONTENT_TYPE))
            .andExpect(jsonPath("$.imagen").value(Base64Utils.encodeToString(DEFAULT_IMAGEN)))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.modified").value(DEFAULT_MODIFIED.toString()));
    }

    @Test
    @Transactional
    void getMensagemsByIdFiltering() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        Long id = mensagem.getId();

        defaultMensagemShouldBeFound("id.equals=" + id);
        defaultMensagemShouldNotBeFound("id.notEquals=" + id);

        defaultMensagemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMensagemShouldNotBeFound("id.greaterThan=" + id);

        defaultMensagemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMensagemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMensagemsByTituloIsEqualToSomething() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where titulo equals to DEFAULT_TITULO
        defaultMensagemShouldBeFound("titulo.equals=" + DEFAULT_TITULO);

        // Get all the mensagemList where titulo equals to UPDATED_TITULO
        defaultMensagemShouldNotBeFound("titulo.equals=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllMensagemsByTituloIsInShouldWork() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where titulo in DEFAULT_TITULO or UPDATED_TITULO
        defaultMensagemShouldBeFound("titulo.in=" + DEFAULT_TITULO + "," + UPDATED_TITULO);

        // Get all the mensagemList where titulo equals to UPDATED_TITULO
        defaultMensagemShouldNotBeFound("titulo.in=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllMensagemsByTituloIsNullOrNotNull() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where titulo is not null
        defaultMensagemShouldBeFound("titulo.specified=true");

        // Get all the mensagemList where titulo is null
        defaultMensagemShouldNotBeFound("titulo.specified=false");
    }

    @Test
    @Transactional
    void getAllMensagemsByTituloContainsSomething() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where titulo contains DEFAULT_TITULO
        defaultMensagemShouldBeFound("titulo.contains=" + DEFAULT_TITULO);

        // Get all the mensagemList where titulo contains UPDATED_TITULO
        defaultMensagemShouldNotBeFound("titulo.contains=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllMensagemsByTituloNotContainsSomething() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where titulo does not contain DEFAULT_TITULO
        defaultMensagemShouldNotBeFound("titulo.doesNotContain=" + DEFAULT_TITULO);

        // Get all the mensagemList where titulo does not contain UPDATED_TITULO
        defaultMensagemShouldBeFound("titulo.doesNotContain=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllMensagemsByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where descricao equals to DEFAULT_DESCRICAO
        defaultMensagemShouldBeFound("descricao.equals=" + DEFAULT_DESCRICAO);

        // Get all the mensagemList where descricao equals to UPDATED_DESCRICAO
        defaultMensagemShouldNotBeFound("descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllMensagemsByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where descricao in DEFAULT_DESCRICAO or UPDATED_DESCRICAO
        defaultMensagemShouldBeFound("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO);

        // Get all the mensagemList where descricao equals to UPDATED_DESCRICAO
        defaultMensagemShouldNotBeFound("descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllMensagemsByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where descricao is not null
        defaultMensagemShouldBeFound("descricao.specified=true");

        // Get all the mensagemList where descricao is null
        defaultMensagemShouldNotBeFound("descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllMensagemsByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where descricao contains DEFAULT_DESCRICAO
        defaultMensagemShouldBeFound("descricao.contains=" + DEFAULT_DESCRICAO);

        // Get all the mensagemList where descricao contains UPDATED_DESCRICAO
        defaultMensagemShouldNotBeFound("descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllMensagemsByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where descricao does not contain DEFAULT_DESCRICAO
        defaultMensagemShouldNotBeFound("descricao.doesNotContain=" + DEFAULT_DESCRICAO);

        // Get all the mensagemList where descricao does not contain UPDATED_DESCRICAO
        defaultMensagemShouldBeFound("descricao.doesNotContain=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllMensagemsByLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where link equals to DEFAULT_LINK
        defaultMensagemShouldBeFound("link.equals=" + DEFAULT_LINK);

        // Get all the mensagemList where link equals to UPDATED_LINK
        defaultMensagemShouldNotBeFound("link.equals=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllMensagemsByLinkIsInShouldWork() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where link in DEFAULT_LINK or UPDATED_LINK
        defaultMensagemShouldBeFound("link.in=" + DEFAULT_LINK + "," + UPDATED_LINK);

        // Get all the mensagemList where link equals to UPDATED_LINK
        defaultMensagemShouldNotBeFound("link.in=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllMensagemsByLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where link is not null
        defaultMensagemShouldBeFound("link.specified=true");

        // Get all the mensagemList where link is null
        defaultMensagemShouldNotBeFound("link.specified=false");
    }

    @Test
    @Transactional
    void getAllMensagemsByLinkContainsSomething() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where link contains DEFAULT_LINK
        defaultMensagemShouldBeFound("link.contains=" + DEFAULT_LINK);

        // Get all the mensagemList where link contains UPDATED_LINK
        defaultMensagemShouldNotBeFound("link.contains=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllMensagemsByLinkNotContainsSomething() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where link does not contain DEFAULT_LINK
        defaultMensagemShouldNotBeFound("link.doesNotContain=" + DEFAULT_LINK);

        // Get all the mensagemList where link does not contain UPDATED_LINK
        defaultMensagemShouldBeFound("link.doesNotContain=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllMensagemsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where startDate equals to DEFAULT_START_DATE
        defaultMensagemShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the mensagemList where startDate equals to UPDATED_START_DATE
        defaultMensagemShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllMensagemsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultMensagemShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the mensagemList where startDate equals to UPDATED_START_DATE
        defaultMensagemShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllMensagemsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where startDate is not null
        defaultMensagemShouldBeFound("startDate.specified=true");

        // Get all the mensagemList where startDate is null
        defaultMensagemShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMensagemsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultMensagemShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the mensagemList where startDate is greater than or equal to UPDATED_START_DATE
        defaultMensagemShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllMensagemsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where startDate is less than or equal to DEFAULT_START_DATE
        defaultMensagemShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the mensagemList where startDate is less than or equal to SMALLER_START_DATE
        defaultMensagemShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllMensagemsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where startDate is less than DEFAULT_START_DATE
        defaultMensagemShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the mensagemList where startDate is less than UPDATED_START_DATE
        defaultMensagemShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllMensagemsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where startDate is greater than DEFAULT_START_DATE
        defaultMensagemShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the mensagemList where startDate is greater than SMALLER_START_DATE
        defaultMensagemShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllMensagemsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where endDate equals to DEFAULT_END_DATE
        defaultMensagemShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the mensagemList where endDate equals to UPDATED_END_DATE
        defaultMensagemShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllMensagemsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultMensagemShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the mensagemList where endDate equals to UPDATED_END_DATE
        defaultMensagemShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllMensagemsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where endDate is not null
        defaultMensagemShouldBeFound("endDate.specified=true");

        // Get all the mensagemList where endDate is null
        defaultMensagemShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMensagemsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultMensagemShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the mensagemList where endDate is greater than or equal to UPDATED_END_DATE
        defaultMensagemShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllMensagemsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where endDate is less than or equal to DEFAULT_END_DATE
        defaultMensagemShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the mensagemList where endDate is less than or equal to SMALLER_END_DATE
        defaultMensagemShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllMensagemsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where endDate is less than DEFAULT_END_DATE
        defaultMensagemShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the mensagemList where endDate is less than UPDATED_END_DATE
        defaultMensagemShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllMensagemsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where endDate is greater than DEFAULT_END_DATE
        defaultMensagemShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the mensagemList where endDate is greater than SMALLER_END_DATE
        defaultMensagemShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllMensagemsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where status equals to DEFAULT_STATUS
        defaultMensagemShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the mensagemList where status equals to UPDATED_STATUS
        defaultMensagemShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMensagemsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultMensagemShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the mensagemList where status equals to UPDATED_STATUS
        defaultMensagemShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMensagemsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where status is not null
        defaultMensagemShouldBeFound("status.specified=true");

        // Get all the mensagemList where status is null
        defaultMensagemShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllMensagemsByCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where created equals to DEFAULT_CREATED
        defaultMensagemShouldBeFound("created.equals=" + DEFAULT_CREATED);

        // Get all the mensagemList where created equals to UPDATED_CREATED
        defaultMensagemShouldNotBeFound("created.equals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllMensagemsByCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where created in DEFAULT_CREATED or UPDATED_CREATED
        defaultMensagemShouldBeFound("created.in=" + DEFAULT_CREATED + "," + UPDATED_CREATED);

        // Get all the mensagemList where created equals to UPDATED_CREATED
        defaultMensagemShouldNotBeFound("created.in=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllMensagemsByCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where created is not null
        defaultMensagemShouldBeFound("created.specified=true");

        // Get all the mensagemList where created is null
        defaultMensagemShouldNotBeFound("created.specified=false");
    }

    @Test
    @Transactional
    void getAllMensagemsByModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where modified equals to DEFAULT_MODIFIED
        defaultMensagemShouldBeFound("modified.equals=" + DEFAULT_MODIFIED);

        // Get all the mensagemList where modified equals to UPDATED_MODIFIED
        defaultMensagemShouldNotBeFound("modified.equals=" + UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void getAllMensagemsByModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where modified in DEFAULT_MODIFIED or UPDATED_MODIFIED
        defaultMensagemShouldBeFound("modified.in=" + DEFAULT_MODIFIED + "," + UPDATED_MODIFIED);

        // Get all the mensagemList where modified equals to UPDATED_MODIFIED
        defaultMensagemShouldNotBeFound("modified.in=" + UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void getAllMensagemsByModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        // Get all the mensagemList where modified is not null
        defaultMensagemShouldBeFound("modified.specified=true");

        // Get all the mensagemList where modified is null
        defaultMensagemShouldNotBeFound("modified.specified=false");
    }

    @Test
    @Transactional
    void getAllMensagemsByTipoIsEqualToSomething() throws Exception {
        Tipo tipo;
        if (TestUtil.findAll(em, Tipo.class).isEmpty()) {
            mensagemRepository.saveAndFlush(mensagem);
            tipo = TipoResourceIT.createEntity(em);
        } else {
            tipo = TestUtil.findAll(em, Tipo.class).get(0);
        }
        em.persist(tipo);
        em.flush();
        mensagem.setTipo(tipo);
        mensagemRepository.saveAndFlush(mensagem);
        Long tipoId = tipo.getId();

        // Get all the mensagemList where tipo equals to tipoId
        defaultMensagemShouldBeFound("tipoId.equals=" + tipoId);

        // Get all the mensagemList where tipo equals to (tipoId + 1)
        defaultMensagemShouldNotBeFound("tipoId.equals=" + (tipoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMensagemShouldBeFound(String filter) throws Exception {
        restMensagemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mensagem.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].conteudo").value(hasItem(DEFAULT_CONTEUDO.toString())))
            .andExpect(jsonPath("$.[*].imagenContentType").value(hasItem(DEFAULT_IMAGEN_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imagen").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGEN))))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())));

        // Check, that the count call also returns 1
        restMensagemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMensagemShouldNotBeFound(String filter) throws Exception {
        restMensagemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMensagemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMensagem() throws Exception {
        // Get the mensagem
        restMensagemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMensagem() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        int databaseSizeBeforeUpdate = mensagemRepository.findAll().size();

        // Update the mensagem
        Mensagem updatedMensagem = mensagemRepository.findById(mensagem.getId()).get();
        // Disconnect from session so that the updates on updatedMensagem are not directly saved in db
        em.detach(updatedMensagem);
        updatedMensagem
            .titulo(UPDATED_TITULO)
            .descricao(UPDATED_DESCRICAO)
            .conteudo(UPDATED_CONTEUDO)
            .imagen(UPDATED_IMAGEN)
            .imagenContentType(UPDATED_IMAGEN_CONTENT_TYPE)
            .link(UPDATED_LINK)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);
        MensagemDTO mensagemDTO = mensagemMapper.toDto(updatedMensagem);

        restMensagemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mensagemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mensagemDTO))
            )
            .andExpect(status().isOk());

        // Validate the Mensagem in the database
        List<Mensagem> mensagemList = mensagemRepository.findAll();
        assertThat(mensagemList).hasSize(databaseSizeBeforeUpdate);
        Mensagem testMensagem = mensagemList.get(mensagemList.size() - 1);
        assertThat(testMensagem.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testMensagem.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testMensagem.getConteudo()).isEqualTo(UPDATED_CONTEUDO);
        assertThat(testMensagem.getImagen()).isEqualTo(UPDATED_IMAGEN);
        assertThat(testMensagem.getImagenContentType()).isEqualTo(UPDATED_IMAGEN_CONTENT_TYPE);
        assertThat(testMensagem.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testMensagem.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testMensagem.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testMensagem.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testMensagem.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testMensagem.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void putNonExistingMensagem() throws Exception {
        int databaseSizeBeforeUpdate = mensagemRepository.findAll().size();
        mensagem.setId(count.incrementAndGet());

        // Create the Mensagem
        MensagemDTO mensagemDTO = mensagemMapper.toDto(mensagem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMensagemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mensagemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mensagemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mensagem in the database
        List<Mensagem> mensagemList = mensagemRepository.findAll();
        assertThat(mensagemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMensagem() throws Exception {
        int databaseSizeBeforeUpdate = mensagemRepository.findAll().size();
        mensagem.setId(count.incrementAndGet());

        // Create the Mensagem
        MensagemDTO mensagemDTO = mensagemMapper.toDto(mensagem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMensagemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mensagemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mensagem in the database
        List<Mensagem> mensagemList = mensagemRepository.findAll();
        assertThat(mensagemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMensagem() throws Exception {
        int databaseSizeBeforeUpdate = mensagemRepository.findAll().size();
        mensagem.setId(count.incrementAndGet());

        // Create the Mensagem
        MensagemDTO mensagemDTO = mensagemMapper.toDto(mensagem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMensagemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mensagemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mensagem in the database
        List<Mensagem> mensagemList = mensagemRepository.findAll();
        assertThat(mensagemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMensagemWithPatch() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        int databaseSizeBeforeUpdate = mensagemRepository.findAll().size();

        // Update the mensagem using partial update
        Mensagem partialUpdatedMensagem = new Mensagem();
        partialUpdatedMensagem.setId(mensagem.getId());

        partialUpdatedMensagem
            .titulo(UPDATED_TITULO)
            .descricao(UPDATED_DESCRICAO)
            .conteudo(UPDATED_CONTEUDO)
            .imagen(UPDATED_IMAGEN)
            .imagenContentType(UPDATED_IMAGEN_CONTENT_TYPE)
            .link(UPDATED_LINK)
            .startDate(UPDATED_START_DATE)
            .modified(UPDATED_MODIFIED);

        restMensagemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMensagem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMensagem))
            )
            .andExpect(status().isOk());

        // Validate the Mensagem in the database
        List<Mensagem> mensagemList = mensagemRepository.findAll();
        assertThat(mensagemList).hasSize(databaseSizeBeforeUpdate);
        Mensagem testMensagem = mensagemList.get(mensagemList.size() - 1);
        assertThat(testMensagem.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testMensagem.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testMensagem.getConteudo()).isEqualTo(UPDATED_CONTEUDO);
        assertThat(testMensagem.getImagen()).isEqualTo(UPDATED_IMAGEN);
        assertThat(testMensagem.getImagenContentType()).isEqualTo(UPDATED_IMAGEN_CONTENT_TYPE);
        assertThat(testMensagem.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testMensagem.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testMensagem.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testMensagem.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testMensagem.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testMensagem.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void fullUpdateMensagemWithPatch() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        int databaseSizeBeforeUpdate = mensagemRepository.findAll().size();

        // Update the mensagem using partial update
        Mensagem partialUpdatedMensagem = new Mensagem();
        partialUpdatedMensagem.setId(mensagem.getId());

        partialUpdatedMensagem
            .titulo(UPDATED_TITULO)
            .descricao(UPDATED_DESCRICAO)
            .conteudo(UPDATED_CONTEUDO)
            .imagen(UPDATED_IMAGEN)
            .imagenContentType(UPDATED_IMAGEN_CONTENT_TYPE)
            .link(UPDATED_LINK)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);

        restMensagemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMensagem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMensagem))
            )
            .andExpect(status().isOk());

        // Validate the Mensagem in the database
        List<Mensagem> mensagemList = mensagemRepository.findAll();
        assertThat(mensagemList).hasSize(databaseSizeBeforeUpdate);
        Mensagem testMensagem = mensagemList.get(mensagemList.size() - 1);
        assertThat(testMensagem.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testMensagem.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testMensagem.getConteudo()).isEqualTo(UPDATED_CONTEUDO);
        assertThat(testMensagem.getImagen()).isEqualTo(UPDATED_IMAGEN);
        assertThat(testMensagem.getImagenContentType()).isEqualTo(UPDATED_IMAGEN_CONTENT_TYPE);
        assertThat(testMensagem.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testMensagem.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testMensagem.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testMensagem.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testMensagem.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testMensagem.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingMensagem() throws Exception {
        int databaseSizeBeforeUpdate = mensagemRepository.findAll().size();
        mensagem.setId(count.incrementAndGet());

        // Create the Mensagem
        MensagemDTO mensagemDTO = mensagemMapper.toDto(mensagem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMensagemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mensagemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mensagemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mensagem in the database
        List<Mensagem> mensagemList = mensagemRepository.findAll();
        assertThat(mensagemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMensagem() throws Exception {
        int databaseSizeBeforeUpdate = mensagemRepository.findAll().size();
        mensagem.setId(count.incrementAndGet());

        // Create the Mensagem
        MensagemDTO mensagemDTO = mensagemMapper.toDto(mensagem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMensagemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mensagemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mensagem in the database
        List<Mensagem> mensagemList = mensagemRepository.findAll();
        assertThat(mensagemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMensagem() throws Exception {
        int databaseSizeBeforeUpdate = mensagemRepository.findAll().size();
        mensagem.setId(count.incrementAndGet());

        // Create the Mensagem
        MensagemDTO mensagemDTO = mensagemMapper.toDto(mensagem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMensagemMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(mensagemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mensagem in the database
        List<Mensagem> mensagemList = mensagemRepository.findAll();
        assertThat(mensagemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMensagem() throws Exception {
        // Initialize the database
        mensagemRepository.saveAndFlush(mensagem);

        int databaseSizeBeforeDelete = mensagemRepository.findAll().size();

        // Delete the mensagem
        restMensagemMockMvc
            .perform(delete(ENTITY_API_URL_ID, mensagem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Mensagem> mensagemList = mensagemRepository.findAll();
        assertThat(mensagemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
