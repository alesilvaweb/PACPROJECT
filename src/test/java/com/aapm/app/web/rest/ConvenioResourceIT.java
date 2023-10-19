package com.aapm.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aapm.app.IntegrationTest;
import com.aapm.app.domain.Categoria;
import com.aapm.app.domain.Convenio;
import com.aapm.app.domain.ImagensConvenio;
import com.aapm.app.domain.RedesSociaisConvenio;
import com.aapm.app.domain.enumeration.Status;
import com.aapm.app.repository.ConvenioRepository;
import com.aapm.app.service.ConvenioService;
import com.aapm.app.service.criteria.ConvenioCriteria;
import com.aapm.app.service.dto.ConvenioDTO;
import com.aapm.app.service.mapper.ConvenioMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ConvenioResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ConvenioResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_ENDERECO = "AAAAAAAAAA";
    private static final String UPDATED_ENDERECO = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_LOCALIZACAO = "AAAAAAAAAA";
    private static final String UPDATED_LOCALIZACAO = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.Ativo;
    private static final Status UPDATED_STATUS = Status.Inativo;

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/convenios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConvenioRepository convenioRepository;

    @Mock
    private ConvenioRepository convenioRepositoryMock;

    @Autowired
    private ConvenioMapper convenioMapper;

    @Mock
    private ConvenioService convenioServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConvenioMockMvc;

    private Convenio convenio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Convenio createEntity(EntityManager em) {
        Convenio convenio = new Convenio()
            .nome(DEFAULT_NOME)
            .titulo(DEFAULT_TITULO)
            .descricao(DEFAULT_DESCRICAO)
            .endereco(DEFAULT_ENDERECO)
            .telefone(DEFAULT_TELEFONE)
            .email(DEFAULT_EMAIL)
            .localizacao(DEFAULT_LOCALIZACAO)
            .status(DEFAULT_STATUS)
            .created(DEFAULT_CREATED)
            .modified(DEFAULT_MODIFIED);
        return convenio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Convenio createUpdatedEntity(EntityManager em) {
        Convenio convenio = new Convenio()
            .nome(UPDATED_NOME)
            .titulo(UPDATED_TITULO)
            .descricao(UPDATED_DESCRICAO)
            .endereco(UPDATED_ENDERECO)
            .telefone(UPDATED_TELEFONE)
            .email(UPDATED_EMAIL)
            .localizacao(UPDATED_LOCALIZACAO)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);
        return convenio;
    }

    @BeforeEach
    public void initTest() {
        convenio = createEntity(em);
    }

    @Test
    @Transactional
    void createConvenio() throws Exception {
        int databaseSizeBeforeCreate = convenioRepository.findAll().size();
        // Create the Convenio
        ConvenioDTO convenioDTO = convenioMapper.toDto(convenio);
        restConvenioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(convenioDTO)))
            .andExpect(status().isCreated());

        // Validate the Convenio in the database
        List<Convenio> convenioList = convenioRepository.findAll();
        assertThat(convenioList).hasSize(databaseSizeBeforeCreate + 1);
        Convenio testConvenio = convenioList.get(convenioList.size() - 1);
        assertThat(testConvenio.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testConvenio.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testConvenio.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testConvenio.getEndereco()).isEqualTo(DEFAULT_ENDERECO);
        assertThat(testConvenio.getTelefone()).isEqualTo(DEFAULT_TELEFONE);
        assertThat(testConvenio.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testConvenio.getLocalizacao()).isEqualTo(DEFAULT_LOCALIZACAO);
        assertThat(testConvenio.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testConvenio.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testConvenio.getModified()).isEqualTo(DEFAULT_MODIFIED);
    }

    @Test
    @Transactional
    void createConvenioWithExistingId() throws Exception {
        // Create the Convenio with an existing ID
        convenio.setId(1L);
        ConvenioDTO convenioDTO = convenioMapper.toDto(convenio);

        int databaseSizeBeforeCreate = convenioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConvenioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(convenioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Convenio in the database
        List<Convenio> convenioList = convenioRepository.findAll();
        assertThat(convenioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = convenioRepository.findAll().size();
        // set the field null
        convenio.setNome(null);

        // Create the Convenio, which fails.
        ConvenioDTO convenioDTO = convenioMapper.toDto(convenio);

        restConvenioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(convenioDTO)))
            .andExpect(status().isBadRequest());

        List<Convenio> convenioList = convenioRepository.findAll();
        assertThat(convenioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllConvenios() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList
        restConvenioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(convenio.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO)))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].localizacao").value(hasItem(DEFAULT_LOCALIZACAO)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllConveniosWithEagerRelationshipsIsEnabled() throws Exception {
        when(convenioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restConvenioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(convenioServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllConveniosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(convenioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restConvenioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(convenioRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getConvenio() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get the convenio
        restConvenioMockMvc
            .perform(get(ENTITY_API_URL_ID, convenio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(convenio.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()))
            .andExpect(jsonPath("$.endereco").value(DEFAULT_ENDERECO))
            .andExpect(jsonPath("$.telefone").value(DEFAULT_TELEFONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.localizacao").value(DEFAULT_LOCALIZACAO))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.modified").value(DEFAULT_MODIFIED.toString()));
    }

    @Test
    @Transactional
    void getConveniosByIdFiltering() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        Long id = convenio.getId();

        defaultConvenioShouldBeFound("id.equals=" + id);
        defaultConvenioShouldNotBeFound("id.notEquals=" + id);

        defaultConvenioShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultConvenioShouldNotBeFound("id.greaterThan=" + id);

        defaultConvenioShouldBeFound("id.lessThanOrEqual=" + id);
        defaultConvenioShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllConveniosByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where nome equals to DEFAULT_NOME
        defaultConvenioShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the convenioList where nome equals to UPDATED_NOME
        defaultConvenioShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllConveniosByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultConvenioShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the convenioList where nome equals to UPDATED_NOME
        defaultConvenioShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllConveniosByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where nome is not null
        defaultConvenioShouldBeFound("nome.specified=true");

        // Get all the convenioList where nome is null
        defaultConvenioShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllConveniosByNomeContainsSomething() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where nome contains DEFAULT_NOME
        defaultConvenioShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the convenioList where nome contains UPDATED_NOME
        defaultConvenioShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllConveniosByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where nome does not contain DEFAULT_NOME
        defaultConvenioShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the convenioList where nome does not contain UPDATED_NOME
        defaultConvenioShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllConveniosByTituloIsEqualToSomething() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where titulo equals to DEFAULT_TITULO
        defaultConvenioShouldBeFound("titulo.equals=" + DEFAULT_TITULO);

        // Get all the convenioList where titulo equals to UPDATED_TITULO
        defaultConvenioShouldNotBeFound("titulo.equals=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllConveniosByTituloIsInShouldWork() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where titulo in DEFAULT_TITULO or UPDATED_TITULO
        defaultConvenioShouldBeFound("titulo.in=" + DEFAULT_TITULO + "," + UPDATED_TITULO);

        // Get all the convenioList where titulo equals to UPDATED_TITULO
        defaultConvenioShouldNotBeFound("titulo.in=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllConveniosByTituloIsNullOrNotNull() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where titulo is not null
        defaultConvenioShouldBeFound("titulo.specified=true");

        // Get all the convenioList where titulo is null
        defaultConvenioShouldNotBeFound("titulo.specified=false");
    }

    @Test
    @Transactional
    void getAllConveniosByTituloContainsSomething() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where titulo contains DEFAULT_TITULO
        defaultConvenioShouldBeFound("titulo.contains=" + DEFAULT_TITULO);

        // Get all the convenioList where titulo contains UPDATED_TITULO
        defaultConvenioShouldNotBeFound("titulo.contains=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllConveniosByTituloNotContainsSomething() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where titulo does not contain DEFAULT_TITULO
        defaultConvenioShouldNotBeFound("titulo.doesNotContain=" + DEFAULT_TITULO);

        // Get all the convenioList where titulo does not contain UPDATED_TITULO
        defaultConvenioShouldBeFound("titulo.doesNotContain=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllConveniosByEnderecoIsEqualToSomething() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where endereco equals to DEFAULT_ENDERECO
        defaultConvenioShouldBeFound("endereco.equals=" + DEFAULT_ENDERECO);

        // Get all the convenioList where endereco equals to UPDATED_ENDERECO
        defaultConvenioShouldNotBeFound("endereco.equals=" + UPDATED_ENDERECO);
    }

    @Test
    @Transactional
    void getAllConveniosByEnderecoIsInShouldWork() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where endereco in DEFAULT_ENDERECO or UPDATED_ENDERECO
        defaultConvenioShouldBeFound("endereco.in=" + DEFAULT_ENDERECO + "," + UPDATED_ENDERECO);

        // Get all the convenioList where endereco equals to UPDATED_ENDERECO
        defaultConvenioShouldNotBeFound("endereco.in=" + UPDATED_ENDERECO);
    }

    @Test
    @Transactional
    void getAllConveniosByEnderecoIsNullOrNotNull() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where endereco is not null
        defaultConvenioShouldBeFound("endereco.specified=true");

        // Get all the convenioList where endereco is null
        defaultConvenioShouldNotBeFound("endereco.specified=false");
    }

    @Test
    @Transactional
    void getAllConveniosByEnderecoContainsSomething() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where endereco contains DEFAULT_ENDERECO
        defaultConvenioShouldBeFound("endereco.contains=" + DEFAULT_ENDERECO);

        // Get all the convenioList where endereco contains UPDATED_ENDERECO
        defaultConvenioShouldNotBeFound("endereco.contains=" + UPDATED_ENDERECO);
    }

    @Test
    @Transactional
    void getAllConveniosByEnderecoNotContainsSomething() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where endereco does not contain DEFAULT_ENDERECO
        defaultConvenioShouldNotBeFound("endereco.doesNotContain=" + DEFAULT_ENDERECO);

        // Get all the convenioList where endereco does not contain UPDATED_ENDERECO
        defaultConvenioShouldBeFound("endereco.doesNotContain=" + UPDATED_ENDERECO);
    }

    @Test
    @Transactional
    void getAllConveniosByTelefoneIsEqualToSomething() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where telefone equals to DEFAULT_TELEFONE
        defaultConvenioShouldBeFound("telefone.equals=" + DEFAULT_TELEFONE);

        // Get all the convenioList where telefone equals to UPDATED_TELEFONE
        defaultConvenioShouldNotBeFound("telefone.equals=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllConveniosByTelefoneIsInShouldWork() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where telefone in DEFAULT_TELEFONE or UPDATED_TELEFONE
        defaultConvenioShouldBeFound("telefone.in=" + DEFAULT_TELEFONE + "," + UPDATED_TELEFONE);

        // Get all the convenioList where telefone equals to UPDATED_TELEFONE
        defaultConvenioShouldNotBeFound("telefone.in=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllConveniosByTelefoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where telefone is not null
        defaultConvenioShouldBeFound("telefone.specified=true");

        // Get all the convenioList where telefone is null
        defaultConvenioShouldNotBeFound("telefone.specified=false");
    }

    @Test
    @Transactional
    void getAllConveniosByTelefoneContainsSomething() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where telefone contains DEFAULT_TELEFONE
        defaultConvenioShouldBeFound("telefone.contains=" + DEFAULT_TELEFONE);

        // Get all the convenioList where telefone contains UPDATED_TELEFONE
        defaultConvenioShouldNotBeFound("telefone.contains=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllConveniosByTelefoneNotContainsSomething() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where telefone does not contain DEFAULT_TELEFONE
        defaultConvenioShouldNotBeFound("telefone.doesNotContain=" + DEFAULT_TELEFONE);

        // Get all the convenioList where telefone does not contain UPDATED_TELEFONE
        defaultConvenioShouldBeFound("telefone.doesNotContain=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllConveniosByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where email equals to DEFAULT_EMAIL
        defaultConvenioShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the convenioList where email equals to UPDATED_EMAIL
        defaultConvenioShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllConveniosByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultConvenioShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the convenioList where email equals to UPDATED_EMAIL
        defaultConvenioShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllConveniosByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where email is not null
        defaultConvenioShouldBeFound("email.specified=true");

        // Get all the convenioList where email is null
        defaultConvenioShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllConveniosByEmailContainsSomething() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where email contains DEFAULT_EMAIL
        defaultConvenioShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the convenioList where email contains UPDATED_EMAIL
        defaultConvenioShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllConveniosByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where email does not contain DEFAULT_EMAIL
        defaultConvenioShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the convenioList where email does not contain UPDATED_EMAIL
        defaultConvenioShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllConveniosByLocalizacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where localizacao equals to DEFAULT_LOCALIZACAO
        defaultConvenioShouldBeFound("localizacao.equals=" + DEFAULT_LOCALIZACAO);

        // Get all the convenioList where localizacao equals to UPDATED_LOCALIZACAO
        defaultConvenioShouldNotBeFound("localizacao.equals=" + UPDATED_LOCALIZACAO);
    }

    @Test
    @Transactional
    void getAllConveniosByLocalizacaoIsInShouldWork() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where localizacao in DEFAULT_LOCALIZACAO or UPDATED_LOCALIZACAO
        defaultConvenioShouldBeFound("localizacao.in=" + DEFAULT_LOCALIZACAO + "," + UPDATED_LOCALIZACAO);

        // Get all the convenioList where localizacao equals to UPDATED_LOCALIZACAO
        defaultConvenioShouldNotBeFound("localizacao.in=" + UPDATED_LOCALIZACAO);
    }

    @Test
    @Transactional
    void getAllConveniosByLocalizacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where localizacao is not null
        defaultConvenioShouldBeFound("localizacao.specified=true");

        // Get all the convenioList where localizacao is null
        defaultConvenioShouldNotBeFound("localizacao.specified=false");
    }

    @Test
    @Transactional
    void getAllConveniosByLocalizacaoContainsSomething() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where localizacao contains DEFAULT_LOCALIZACAO
        defaultConvenioShouldBeFound("localizacao.contains=" + DEFAULT_LOCALIZACAO);

        // Get all the convenioList where localizacao contains UPDATED_LOCALIZACAO
        defaultConvenioShouldNotBeFound("localizacao.contains=" + UPDATED_LOCALIZACAO);
    }

    @Test
    @Transactional
    void getAllConveniosByLocalizacaoNotContainsSomething() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where localizacao does not contain DEFAULT_LOCALIZACAO
        defaultConvenioShouldNotBeFound("localizacao.doesNotContain=" + DEFAULT_LOCALIZACAO);

        // Get all the convenioList where localizacao does not contain UPDATED_LOCALIZACAO
        defaultConvenioShouldBeFound("localizacao.doesNotContain=" + UPDATED_LOCALIZACAO);
    }

    @Test
    @Transactional
    void getAllConveniosByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where status equals to DEFAULT_STATUS
        defaultConvenioShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the convenioList where status equals to UPDATED_STATUS
        defaultConvenioShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllConveniosByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultConvenioShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the convenioList where status equals to UPDATED_STATUS
        defaultConvenioShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllConveniosByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where status is not null
        defaultConvenioShouldBeFound("status.specified=true");

        // Get all the convenioList where status is null
        defaultConvenioShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllConveniosByCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where created equals to DEFAULT_CREATED
        defaultConvenioShouldBeFound("created.equals=" + DEFAULT_CREATED);

        // Get all the convenioList where created equals to UPDATED_CREATED
        defaultConvenioShouldNotBeFound("created.equals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllConveniosByCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where created in DEFAULT_CREATED or UPDATED_CREATED
        defaultConvenioShouldBeFound("created.in=" + DEFAULT_CREATED + "," + UPDATED_CREATED);

        // Get all the convenioList where created equals to UPDATED_CREATED
        defaultConvenioShouldNotBeFound("created.in=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllConveniosByCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where created is not null
        defaultConvenioShouldBeFound("created.specified=true");

        // Get all the convenioList where created is null
        defaultConvenioShouldNotBeFound("created.specified=false");
    }

    @Test
    @Transactional
    void getAllConveniosByModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where modified equals to DEFAULT_MODIFIED
        defaultConvenioShouldBeFound("modified.equals=" + DEFAULT_MODIFIED);

        // Get all the convenioList where modified equals to UPDATED_MODIFIED
        defaultConvenioShouldNotBeFound("modified.equals=" + UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void getAllConveniosByModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where modified in DEFAULT_MODIFIED or UPDATED_MODIFIED
        defaultConvenioShouldBeFound("modified.in=" + DEFAULT_MODIFIED + "," + UPDATED_MODIFIED);

        // Get all the convenioList where modified equals to UPDATED_MODIFIED
        defaultConvenioShouldNotBeFound("modified.in=" + UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void getAllConveniosByModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        // Get all the convenioList where modified is not null
        defaultConvenioShouldBeFound("modified.specified=true");

        // Get all the convenioList where modified is null
        defaultConvenioShouldNotBeFound("modified.specified=false");
    }

    @Test
    @Transactional
    void getAllConveniosByImagensIsEqualToSomething() throws Exception {
        ImagensConvenio imagens;
        if (TestUtil.findAll(em, ImagensConvenio.class).isEmpty()) {
            convenioRepository.saveAndFlush(convenio);
            imagens = ImagensConvenioResourceIT.createEntity(em);
        } else {
            imagens = TestUtil.findAll(em, ImagensConvenio.class).get(0);
        }
        em.persist(imagens);
        em.flush();
        convenio.addImagens(imagens);
        convenioRepository.saveAndFlush(convenio);
        Long imagensId = imagens.getId();

        // Get all the convenioList where imagens equals to imagensId
        defaultConvenioShouldBeFound("imagensId.equals=" + imagensId);

        // Get all the convenioList where imagens equals to (imagensId + 1)
        defaultConvenioShouldNotBeFound("imagensId.equals=" + (imagensId + 1));
    }

    @Test
    @Transactional
    void getAllConveniosByRedesSociaisIsEqualToSomething() throws Exception {
        RedesSociaisConvenio redesSociais;
        if (TestUtil.findAll(em, RedesSociaisConvenio.class).isEmpty()) {
            convenioRepository.saveAndFlush(convenio);
            redesSociais = RedesSociaisConvenioResourceIT.createEntity(em);
        } else {
            redesSociais = TestUtil.findAll(em, RedesSociaisConvenio.class).get(0);
        }
        em.persist(redesSociais);
        em.flush();
        convenio.addRedesSociais(redesSociais);
        convenioRepository.saveAndFlush(convenio);
        Long redesSociaisId = redesSociais.getId();

        // Get all the convenioList where redesSociais equals to redesSociaisId
        defaultConvenioShouldBeFound("redesSociaisId.equals=" + redesSociaisId);

        // Get all the convenioList where redesSociais equals to (redesSociaisId + 1)
        defaultConvenioShouldNotBeFound("redesSociaisId.equals=" + (redesSociaisId + 1));
    }

    @Test
    @Transactional
    void getAllConveniosByCategoriaIsEqualToSomething() throws Exception {
        Categoria categoria;
        if (TestUtil.findAll(em, Categoria.class).isEmpty()) {
            convenioRepository.saveAndFlush(convenio);
            categoria = CategoriaResourceIT.createEntity(em);
        } else {
            categoria = TestUtil.findAll(em, Categoria.class).get(0);
        }
        em.persist(categoria);
        em.flush();
        convenio.setCategoria(categoria);
        convenioRepository.saveAndFlush(convenio);
        Long categoriaId = categoria.getId();

        // Get all the convenioList where categoria equals to categoriaId
        defaultConvenioShouldBeFound("categoriaId.equals=" + categoriaId);

        // Get all the convenioList where categoria equals to (categoriaId + 1)
        defaultConvenioShouldNotBeFound("categoriaId.equals=" + (categoriaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultConvenioShouldBeFound(String filter) throws Exception {
        restConvenioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(convenio.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO)))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].localizacao").value(hasItem(DEFAULT_LOCALIZACAO)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())));

        // Check, that the count call also returns 1
        restConvenioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultConvenioShouldNotBeFound(String filter) throws Exception {
        restConvenioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restConvenioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingConvenio() throws Exception {
        // Get the convenio
        restConvenioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConvenio() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        int databaseSizeBeforeUpdate = convenioRepository.findAll().size();

        // Update the convenio
        Convenio updatedConvenio = convenioRepository.findById(convenio.getId()).get();
        // Disconnect from session so that the updates on updatedConvenio are not directly saved in db
        em.detach(updatedConvenio);
        updatedConvenio
            .nome(UPDATED_NOME)
            .titulo(UPDATED_TITULO)
            .descricao(UPDATED_DESCRICAO)
            .endereco(UPDATED_ENDERECO)
            .telefone(UPDATED_TELEFONE)
            .email(UPDATED_EMAIL)
            .localizacao(UPDATED_LOCALIZACAO)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);
        ConvenioDTO convenioDTO = convenioMapper.toDto(updatedConvenio);

        restConvenioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, convenioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(convenioDTO))
            )
            .andExpect(status().isOk());

        // Validate the Convenio in the database
        List<Convenio> convenioList = convenioRepository.findAll();
        assertThat(convenioList).hasSize(databaseSizeBeforeUpdate);
        Convenio testConvenio = convenioList.get(convenioList.size() - 1);
        assertThat(testConvenio.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testConvenio.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testConvenio.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testConvenio.getEndereco()).isEqualTo(UPDATED_ENDERECO);
        assertThat(testConvenio.getTelefone()).isEqualTo(UPDATED_TELEFONE);
        assertThat(testConvenio.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testConvenio.getLocalizacao()).isEqualTo(UPDATED_LOCALIZACAO);
        assertThat(testConvenio.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testConvenio.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testConvenio.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void putNonExistingConvenio() throws Exception {
        int databaseSizeBeforeUpdate = convenioRepository.findAll().size();
        convenio.setId(count.incrementAndGet());

        // Create the Convenio
        ConvenioDTO convenioDTO = convenioMapper.toDto(convenio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConvenioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, convenioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(convenioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Convenio in the database
        List<Convenio> convenioList = convenioRepository.findAll();
        assertThat(convenioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConvenio() throws Exception {
        int databaseSizeBeforeUpdate = convenioRepository.findAll().size();
        convenio.setId(count.incrementAndGet());

        // Create the Convenio
        ConvenioDTO convenioDTO = convenioMapper.toDto(convenio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConvenioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(convenioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Convenio in the database
        List<Convenio> convenioList = convenioRepository.findAll();
        assertThat(convenioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConvenio() throws Exception {
        int databaseSizeBeforeUpdate = convenioRepository.findAll().size();
        convenio.setId(count.incrementAndGet());

        // Create the Convenio
        ConvenioDTO convenioDTO = convenioMapper.toDto(convenio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConvenioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(convenioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Convenio in the database
        List<Convenio> convenioList = convenioRepository.findAll();
        assertThat(convenioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConvenioWithPatch() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        int databaseSizeBeforeUpdate = convenioRepository.findAll().size();

        // Update the convenio using partial update
        Convenio partialUpdatedConvenio = new Convenio();
        partialUpdatedConvenio.setId(convenio.getId());

        partialUpdatedConvenio.nome(UPDATED_NOME).localizacao(UPDATED_LOCALIZACAO).status(UPDATED_STATUS);

        restConvenioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConvenio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConvenio))
            )
            .andExpect(status().isOk());

        // Validate the Convenio in the database
        List<Convenio> convenioList = convenioRepository.findAll();
        assertThat(convenioList).hasSize(databaseSizeBeforeUpdate);
        Convenio testConvenio = convenioList.get(convenioList.size() - 1);
        assertThat(testConvenio.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testConvenio.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testConvenio.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testConvenio.getEndereco()).isEqualTo(DEFAULT_ENDERECO);
        assertThat(testConvenio.getTelefone()).isEqualTo(DEFAULT_TELEFONE);
        assertThat(testConvenio.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testConvenio.getLocalizacao()).isEqualTo(UPDATED_LOCALIZACAO);
        assertThat(testConvenio.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testConvenio.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testConvenio.getModified()).isEqualTo(DEFAULT_MODIFIED);
    }

    @Test
    @Transactional
    void fullUpdateConvenioWithPatch() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        int databaseSizeBeforeUpdate = convenioRepository.findAll().size();

        // Update the convenio using partial update
        Convenio partialUpdatedConvenio = new Convenio();
        partialUpdatedConvenio.setId(convenio.getId());

        partialUpdatedConvenio
            .nome(UPDATED_NOME)
            .titulo(UPDATED_TITULO)
            .descricao(UPDATED_DESCRICAO)
            .endereco(UPDATED_ENDERECO)
            .telefone(UPDATED_TELEFONE)
            .email(UPDATED_EMAIL)
            .localizacao(UPDATED_LOCALIZACAO)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);

        restConvenioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConvenio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConvenio))
            )
            .andExpect(status().isOk());

        // Validate the Convenio in the database
        List<Convenio> convenioList = convenioRepository.findAll();
        assertThat(convenioList).hasSize(databaseSizeBeforeUpdate);
        Convenio testConvenio = convenioList.get(convenioList.size() - 1);
        assertThat(testConvenio.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testConvenio.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testConvenio.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testConvenio.getEndereco()).isEqualTo(UPDATED_ENDERECO);
        assertThat(testConvenio.getTelefone()).isEqualTo(UPDATED_TELEFONE);
        assertThat(testConvenio.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testConvenio.getLocalizacao()).isEqualTo(UPDATED_LOCALIZACAO);
        assertThat(testConvenio.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testConvenio.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testConvenio.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingConvenio() throws Exception {
        int databaseSizeBeforeUpdate = convenioRepository.findAll().size();
        convenio.setId(count.incrementAndGet());

        // Create the Convenio
        ConvenioDTO convenioDTO = convenioMapper.toDto(convenio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConvenioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, convenioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(convenioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Convenio in the database
        List<Convenio> convenioList = convenioRepository.findAll();
        assertThat(convenioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConvenio() throws Exception {
        int databaseSizeBeforeUpdate = convenioRepository.findAll().size();
        convenio.setId(count.incrementAndGet());

        // Create the Convenio
        ConvenioDTO convenioDTO = convenioMapper.toDto(convenio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConvenioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(convenioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Convenio in the database
        List<Convenio> convenioList = convenioRepository.findAll();
        assertThat(convenioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConvenio() throws Exception {
        int databaseSizeBeforeUpdate = convenioRepository.findAll().size();
        convenio.setId(count.incrementAndGet());

        // Create the Convenio
        ConvenioDTO convenioDTO = convenioMapper.toDto(convenio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConvenioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(convenioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Convenio in the database
        List<Convenio> convenioList = convenioRepository.findAll();
        assertThat(convenioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConvenio() throws Exception {
        // Initialize the database
        convenioRepository.saveAndFlush(convenio);

        int databaseSizeBeforeDelete = convenioRepository.findAll().size();

        // Delete the convenio
        restConvenioMockMvc
            .perform(delete(ENTITY_API_URL_ID, convenio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Convenio> convenioList = convenioRepository.findAll();
        assertThat(convenioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
