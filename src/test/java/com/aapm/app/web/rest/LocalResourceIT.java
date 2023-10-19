package com.aapm.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aapm.app.IntegrationTest;
import com.aapm.app.domain.Local;
import com.aapm.app.domain.Reserva;
import com.aapm.app.domain.enumeration.Status;
import com.aapm.app.repository.LocalRepository;
import com.aapm.app.service.criteria.LocalCriteria;
import com.aapm.app.service.dto.LocalDTO;
import com.aapm.app.service.mapper.LocalMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link LocalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LocalResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final Integer DEFAULT_CAPACIDADE = 1;
    private static final Integer UPDATED_CAPACIDADE = 2;
    private static final Integer SMALLER_CAPACIDADE = 1 - 1;

    private static final byte[] DEFAULT_IMAGEN = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGEN = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGEN_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGEN_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_OBSERVACOES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACOES = "BBBBBBBBBB";

    private static final String DEFAULT_LOCALIZACAO = "AAAAAAAAAA";
    private static final String UPDATED_LOCALIZACAO = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.Ativo;
    private static final Status UPDATED_STATUS = Status.Inativo;

    private static final Float DEFAULT_VALOR = 1F;
    private static final Float UPDATED_VALOR = 2F;
    private static final Float SMALLER_VALOR = 1F - 1F;

    private static final String DEFAULT_COR = "AAAAAAAAAA";
    private static final String UPDATED_COR = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/locals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private LocalMapper localMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocalMockMvc;

    private Local local;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Local createEntity(EntityManager em) {
        Local local = new Local()
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO)
            .capacidade(DEFAULT_CAPACIDADE)
            .imagen(DEFAULT_IMAGEN)
            .imagenContentType(DEFAULT_IMAGEN_CONTENT_TYPE)
            .observacoes(DEFAULT_OBSERVACOES)
            .localizacao(DEFAULT_LOCALIZACAO)
            .status(DEFAULT_STATUS)
            .valor(DEFAULT_VALOR)
            .cor(DEFAULT_COR)
            .created(DEFAULT_CREATED)
            .modified(DEFAULT_MODIFIED);
        return local;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Local createUpdatedEntity(EntityManager em) {
        Local local = new Local()
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .capacidade(UPDATED_CAPACIDADE)
            .imagen(UPDATED_IMAGEN)
            .imagenContentType(UPDATED_IMAGEN_CONTENT_TYPE)
            .observacoes(UPDATED_OBSERVACOES)
            .localizacao(UPDATED_LOCALIZACAO)
            .status(UPDATED_STATUS)
            .valor(UPDATED_VALOR)
            .cor(UPDATED_COR)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);
        return local;
    }

    @BeforeEach
    public void initTest() {
        local = createEntity(em);
    }

    @Test
    @Transactional
    void createLocal() throws Exception {
        int databaseSizeBeforeCreate = localRepository.findAll().size();
        // Create the Local
        LocalDTO localDTO = localMapper.toDto(local);
        restLocalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(localDTO)))
            .andExpect(status().isCreated());

        // Validate the Local in the database
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeCreate + 1);
        Local testLocal = localList.get(localList.size() - 1);
        assertThat(testLocal.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testLocal.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testLocal.getCapacidade()).isEqualTo(DEFAULT_CAPACIDADE);
        assertThat(testLocal.getImagen()).isEqualTo(DEFAULT_IMAGEN);
        assertThat(testLocal.getImagenContentType()).isEqualTo(DEFAULT_IMAGEN_CONTENT_TYPE);
        assertThat(testLocal.getObservacoes()).isEqualTo(DEFAULT_OBSERVACOES);
        assertThat(testLocal.getLocalizacao()).isEqualTo(DEFAULT_LOCALIZACAO);
        assertThat(testLocal.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testLocal.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testLocal.getCor()).isEqualTo(DEFAULT_COR);
        assertThat(testLocal.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testLocal.getModified()).isEqualTo(DEFAULT_MODIFIED);
    }

    @Test
    @Transactional
    void createLocalWithExistingId() throws Exception {
        // Create the Local with an existing ID
        local.setId(1L);
        LocalDTO localDTO = localMapper.toDto(local);

        int databaseSizeBeforeCreate = localRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(localDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Local in the database
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = localRepository.findAll().size();
        // set the field null
        local.setNome(null);

        // Create the Local, which fails.
        LocalDTO localDTO = localMapper.toDto(local);

        restLocalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(localDTO)))
            .andExpect(status().isBadRequest());

        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCapacidadeIsRequired() throws Exception {
        int databaseSizeBeforeTest = localRepository.findAll().size();
        // set the field null
        local.setCapacidade(null);

        // Create the Local, which fails.
        LocalDTO localDTO = localMapper.toDto(local);

        restLocalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(localDTO)))
            .andExpect(status().isBadRequest());

        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValorIsRequired() throws Exception {
        int databaseSizeBeforeTest = localRepository.findAll().size();
        // set the field null
        local.setValor(null);

        // Create the Local, which fails.
        LocalDTO localDTO = localMapper.toDto(local);

        restLocalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(localDTO)))
            .andExpect(status().isBadRequest());

        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLocals() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList
        restLocalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(local.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].capacidade").value(hasItem(DEFAULT_CAPACIDADE)))
            .andExpect(jsonPath("$.[*].imagenContentType").value(hasItem(DEFAULT_IMAGEN_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imagen").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGEN))))
            .andExpect(jsonPath("$.[*].observacoes").value(hasItem(DEFAULT_OBSERVACOES.toString())))
            .andExpect(jsonPath("$.[*].localizacao").value(hasItem(DEFAULT_LOCALIZACAO)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.doubleValue())))
            .andExpect(jsonPath("$.[*].cor").value(hasItem(DEFAULT_COR)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())));
    }

    @Test
    @Transactional
    void getLocal() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get the local
        restLocalMockMvc
            .perform(get(ENTITY_API_URL_ID, local.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(local.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()))
            .andExpect(jsonPath("$.capacidade").value(DEFAULT_CAPACIDADE))
            .andExpect(jsonPath("$.imagenContentType").value(DEFAULT_IMAGEN_CONTENT_TYPE))
            .andExpect(jsonPath("$.imagen").value(Base64Utils.encodeToString(DEFAULT_IMAGEN)))
            .andExpect(jsonPath("$.observacoes").value(DEFAULT_OBSERVACOES.toString()))
            .andExpect(jsonPath("$.localizacao").value(DEFAULT_LOCALIZACAO))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR.doubleValue()))
            .andExpect(jsonPath("$.cor").value(DEFAULT_COR))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.modified").value(DEFAULT_MODIFIED.toString()));
    }

    @Test
    @Transactional
    void getLocalsByIdFiltering() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        Long id = local.getId();

        defaultLocalShouldBeFound("id.equals=" + id);
        defaultLocalShouldNotBeFound("id.notEquals=" + id);

        defaultLocalShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLocalShouldNotBeFound("id.greaterThan=" + id);

        defaultLocalShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLocalShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLocalsByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where nome equals to DEFAULT_NOME
        defaultLocalShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the localList where nome equals to UPDATED_NOME
        defaultLocalShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllLocalsByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultLocalShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the localList where nome equals to UPDATED_NOME
        defaultLocalShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllLocalsByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where nome is not null
        defaultLocalShouldBeFound("nome.specified=true");

        // Get all the localList where nome is null
        defaultLocalShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllLocalsByNomeContainsSomething() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where nome contains DEFAULT_NOME
        defaultLocalShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the localList where nome contains UPDATED_NOME
        defaultLocalShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllLocalsByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where nome does not contain DEFAULT_NOME
        defaultLocalShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the localList where nome does not contain UPDATED_NOME
        defaultLocalShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllLocalsByCapacidadeIsEqualToSomething() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where capacidade equals to DEFAULT_CAPACIDADE
        defaultLocalShouldBeFound("capacidade.equals=" + DEFAULT_CAPACIDADE);

        // Get all the localList where capacidade equals to UPDATED_CAPACIDADE
        defaultLocalShouldNotBeFound("capacidade.equals=" + UPDATED_CAPACIDADE);
    }

    @Test
    @Transactional
    void getAllLocalsByCapacidadeIsInShouldWork() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where capacidade in DEFAULT_CAPACIDADE or UPDATED_CAPACIDADE
        defaultLocalShouldBeFound("capacidade.in=" + DEFAULT_CAPACIDADE + "," + UPDATED_CAPACIDADE);

        // Get all the localList where capacidade equals to UPDATED_CAPACIDADE
        defaultLocalShouldNotBeFound("capacidade.in=" + UPDATED_CAPACIDADE);
    }

    @Test
    @Transactional
    void getAllLocalsByCapacidadeIsNullOrNotNull() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where capacidade is not null
        defaultLocalShouldBeFound("capacidade.specified=true");

        // Get all the localList where capacidade is null
        defaultLocalShouldNotBeFound("capacidade.specified=false");
    }

    @Test
    @Transactional
    void getAllLocalsByCapacidadeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where capacidade is greater than or equal to DEFAULT_CAPACIDADE
        defaultLocalShouldBeFound("capacidade.greaterThanOrEqual=" + DEFAULT_CAPACIDADE);

        // Get all the localList where capacidade is greater than or equal to UPDATED_CAPACIDADE
        defaultLocalShouldNotBeFound("capacidade.greaterThanOrEqual=" + UPDATED_CAPACIDADE);
    }

    @Test
    @Transactional
    void getAllLocalsByCapacidadeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where capacidade is less than or equal to DEFAULT_CAPACIDADE
        defaultLocalShouldBeFound("capacidade.lessThanOrEqual=" + DEFAULT_CAPACIDADE);

        // Get all the localList where capacidade is less than or equal to SMALLER_CAPACIDADE
        defaultLocalShouldNotBeFound("capacidade.lessThanOrEqual=" + SMALLER_CAPACIDADE);
    }

    @Test
    @Transactional
    void getAllLocalsByCapacidadeIsLessThanSomething() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where capacidade is less than DEFAULT_CAPACIDADE
        defaultLocalShouldNotBeFound("capacidade.lessThan=" + DEFAULT_CAPACIDADE);

        // Get all the localList where capacidade is less than UPDATED_CAPACIDADE
        defaultLocalShouldBeFound("capacidade.lessThan=" + UPDATED_CAPACIDADE);
    }

    @Test
    @Transactional
    void getAllLocalsByCapacidadeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where capacidade is greater than DEFAULT_CAPACIDADE
        defaultLocalShouldNotBeFound("capacidade.greaterThan=" + DEFAULT_CAPACIDADE);

        // Get all the localList where capacidade is greater than SMALLER_CAPACIDADE
        defaultLocalShouldBeFound("capacidade.greaterThan=" + SMALLER_CAPACIDADE);
    }

    @Test
    @Transactional
    void getAllLocalsByLocalizacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where localizacao equals to DEFAULT_LOCALIZACAO
        defaultLocalShouldBeFound("localizacao.equals=" + DEFAULT_LOCALIZACAO);

        // Get all the localList where localizacao equals to UPDATED_LOCALIZACAO
        defaultLocalShouldNotBeFound("localizacao.equals=" + UPDATED_LOCALIZACAO);
    }

    @Test
    @Transactional
    void getAllLocalsByLocalizacaoIsInShouldWork() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where localizacao in DEFAULT_LOCALIZACAO or UPDATED_LOCALIZACAO
        defaultLocalShouldBeFound("localizacao.in=" + DEFAULT_LOCALIZACAO + "," + UPDATED_LOCALIZACAO);

        // Get all the localList where localizacao equals to UPDATED_LOCALIZACAO
        defaultLocalShouldNotBeFound("localizacao.in=" + UPDATED_LOCALIZACAO);
    }

    @Test
    @Transactional
    void getAllLocalsByLocalizacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where localizacao is not null
        defaultLocalShouldBeFound("localizacao.specified=true");

        // Get all the localList where localizacao is null
        defaultLocalShouldNotBeFound("localizacao.specified=false");
    }

    @Test
    @Transactional
    void getAllLocalsByLocalizacaoContainsSomething() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where localizacao contains DEFAULT_LOCALIZACAO
        defaultLocalShouldBeFound("localizacao.contains=" + DEFAULT_LOCALIZACAO);

        // Get all the localList where localizacao contains UPDATED_LOCALIZACAO
        defaultLocalShouldNotBeFound("localizacao.contains=" + UPDATED_LOCALIZACAO);
    }

    @Test
    @Transactional
    void getAllLocalsByLocalizacaoNotContainsSomething() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where localizacao does not contain DEFAULT_LOCALIZACAO
        defaultLocalShouldNotBeFound("localizacao.doesNotContain=" + DEFAULT_LOCALIZACAO);

        // Get all the localList where localizacao does not contain UPDATED_LOCALIZACAO
        defaultLocalShouldBeFound("localizacao.doesNotContain=" + UPDATED_LOCALIZACAO);
    }

    @Test
    @Transactional
    void getAllLocalsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where status equals to DEFAULT_STATUS
        defaultLocalShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the localList where status equals to UPDATED_STATUS
        defaultLocalShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllLocalsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultLocalShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the localList where status equals to UPDATED_STATUS
        defaultLocalShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllLocalsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where status is not null
        defaultLocalShouldBeFound("status.specified=true");

        // Get all the localList where status is null
        defaultLocalShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllLocalsByValorIsEqualToSomething() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where valor equals to DEFAULT_VALOR
        defaultLocalShouldBeFound("valor.equals=" + DEFAULT_VALOR);

        // Get all the localList where valor equals to UPDATED_VALOR
        defaultLocalShouldNotBeFound("valor.equals=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllLocalsByValorIsInShouldWork() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where valor in DEFAULT_VALOR or UPDATED_VALOR
        defaultLocalShouldBeFound("valor.in=" + DEFAULT_VALOR + "," + UPDATED_VALOR);

        // Get all the localList where valor equals to UPDATED_VALOR
        defaultLocalShouldNotBeFound("valor.in=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllLocalsByValorIsNullOrNotNull() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where valor is not null
        defaultLocalShouldBeFound("valor.specified=true");

        // Get all the localList where valor is null
        defaultLocalShouldNotBeFound("valor.specified=false");
    }

    @Test
    @Transactional
    void getAllLocalsByValorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where valor is greater than or equal to DEFAULT_VALOR
        defaultLocalShouldBeFound("valor.greaterThanOrEqual=" + DEFAULT_VALOR);

        // Get all the localList where valor is greater than or equal to UPDATED_VALOR
        defaultLocalShouldNotBeFound("valor.greaterThanOrEqual=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllLocalsByValorIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where valor is less than or equal to DEFAULT_VALOR
        defaultLocalShouldBeFound("valor.lessThanOrEqual=" + DEFAULT_VALOR);

        // Get all the localList where valor is less than or equal to SMALLER_VALOR
        defaultLocalShouldNotBeFound("valor.lessThanOrEqual=" + SMALLER_VALOR);
    }

    @Test
    @Transactional
    void getAllLocalsByValorIsLessThanSomething() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where valor is less than DEFAULT_VALOR
        defaultLocalShouldNotBeFound("valor.lessThan=" + DEFAULT_VALOR);

        // Get all the localList where valor is less than UPDATED_VALOR
        defaultLocalShouldBeFound("valor.lessThan=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllLocalsByValorIsGreaterThanSomething() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where valor is greater than DEFAULT_VALOR
        defaultLocalShouldNotBeFound("valor.greaterThan=" + DEFAULT_VALOR);

        // Get all the localList where valor is greater than SMALLER_VALOR
        defaultLocalShouldBeFound("valor.greaterThan=" + SMALLER_VALOR);
    }

    @Test
    @Transactional
    void getAllLocalsByCorIsEqualToSomething() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where cor equals to DEFAULT_COR
        defaultLocalShouldBeFound("cor.equals=" + DEFAULT_COR);

        // Get all the localList where cor equals to UPDATED_COR
        defaultLocalShouldNotBeFound("cor.equals=" + UPDATED_COR);
    }

    @Test
    @Transactional
    void getAllLocalsByCorIsInShouldWork() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where cor in DEFAULT_COR or UPDATED_COR
        defaultLocalShouldBeFound("cor.in=" + DEFAULT_COR + "," + UPDATED_COR);

        // Get all the localList where cor equals to UPDATED_COR
        defaultLocalShouldNotBeFound("cor.in=" + UPDATED_COR);
    }

    @Test
    @Transactional
    void getAllLocalsByCorIsNullOrNotNull() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where cor is not null
        defaultLocalShouldBeFound("cor.specified=true");

        // Get all the localList where cor is null
        defaultLocalShouldNotBeFound("cor.specified=false");
    }

    @Test
    @Transactional
    void getAllLocalsByCorContainsSomething() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where cor contains DEFAULT_COR
        defaultLocalShouldBeFound("cor.contains=" + DEFAULT_COR);

        // Get all the localList where cor contains UPDATED_COR
        defaultLocalShouldNotBeFound("cor.contains=" + UPDATED_COR);
    }

    @Test
    @Transactional
    void getAllLocalsByCorNotContainsSomething() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where cor does not contain DEFAULT_COR
        defaultLocalShouldNotBeFound("cor.doesNotContain=" + DEFAULT_COR);

        // Get all the localList where cor does not contain UPDATED_COR
        defaultLocalShouldBeFound("cor.doesNotContain=" + UPDATED_COR);
    }

    @Test
    @Transactional
    void getAllLocalsByCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where created equals to DEFAULT_CREATED
        defaultLocalShouldBeFound("created.equals=" + DEFAULT_CREATED);

        // Get all the localList where created equals to UPDATED_CREATED
        defaultLocalShouldNotBeFound("created.equals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllLocalsByCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where created in DEFAULT_CREATED or UPDATED_CREATED
        defaultLocalShouldBeFound("created.in=" + DEFAULT_CREATED + "," + UPDATED_CREATED);

        // Get all the localList where created equals to UPDATED_CREATED
        defaultLocalShouldNotBeFound("created.in=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllLocalsByCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where created is not null
        defaultLocalShouldBeFound("created.specified=true");

        // Get all the localList where created is null
        defaultLocalShouldNotBeFound("created.specified=false");
    }

    @Test
    @Transactional
    void getAllLocalsByModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where modified equals to DEFAULT_MODIFIED
        defaultLocalShouldBeFound("modified.equals=" + DEFAULT_MODIFIED);

        // Get all the localList where modified equals to UPDATED_MODIFIED
        defaultLocalShouldNotBeFound("modified.equals=" + UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void getAllLocalsByModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where modified in DEFAULT_MODIFIED or UPDATED_MODIFIED
        defaultLocalShouldBeFound("modified.in=" + DEFAULT_MODIFIED + "," + UPDATED_MODIFIED);

        // Get all the localList where modified equals to UPDATED_MODIFIED
        defaultLocalShouldNotBeFound("modified.in=" + UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void getAllLocalsByModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where modified is not null
        defaultLocalShouldBeFound("modified.specified=true");

        // Get all the localList where modified is null
        defaultLocalShouldNotBeFound("modified.specified=false");
    }

    @Test
    @Transactional
    void getAllLocalsByReservaIsEqualToSomething() throws Exception {
        Reserva reserva;
        if (TestUtil.findAll(em, Reserva.class).isEmpty()) {
            localRepository.saveAndFlush(local);
            reserva = ReservaResourceIT.createEntity(em);
        } else {
            reserva = TestUtil.findAll(em, Reserva.class).get(0);
        }
        em.persist(reserva);
        em.flush();
        local.addReserva(reserva);
        localRepository.saveAndFlush(local);
        Long reservaId = reserva.getId();

        // Get all the localList where reserva equals to reservaId
        defaultLocalShouldBeFound("reservaId.equals=" + reservaId);

        // Get all the localList where reserva equals to (reservaId + 1)
        defaultLocalShouldNotBeFound("reservaId.equals=" + (reservaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLocalShouldBeFound(String filter) throws Exception {
        restLocalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(local.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].capacidade").value(hasItem(DEFAULT_CAPACIDADE)))
            .andExpect(jsonPath("$.[*].imagenContentType").value(hasItem(DEFAULT_IMAGEN_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imagen").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGEN))))
            .andExpect(jsonPath("$.[*].observacoes").value(hasItem(DEFAULT_OBSERVACOES.toString())))
            .andExpect(jsonPath("$.[*].localizacao").value(hasItem(DEFAULT_LOCALIZACAO)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.doubleValue())))
            .andExpect(jsonPath("$.[*].cor").value(hasItem(DEFAULT_COR)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())));

        // Check, that the count call also returns 1
        restLocalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLocalShouldNotBeFound(String filter) throws Exception {
        restLocalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLocalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLocal() throws Exception {
        // Get the local
        restLocalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLocal() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        int databaseSizeBeforeUpdate = localRepository.findAll().size();

        // Update the local
        Local updatedLocal = localRepository.findById(local.getId()).get();
        // Disconnect from session so that the updates on updatedLocal are not directly saved in db
        em.detach(updatedLocal);
        updatedLocal
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .capacidade(UPDATED_CAPACIDADE)
            .imagen(UPDATED_IMAGEN)
            .imagenContentType(UPDATED_IMAGEN_CONTENT_TYPE)
            .observacoes(UPDATED_OBSERVACOES)
            .localizacao(UPDATED_LOCALIZACAO)
            .status(UPDATED_STATUS)
            .valor(UPDATED_VALOR)
            .cor(UPDATED_COR)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);
        LocalDTO localDTO = localMapper.toDto(updatedLocal);

        restLocalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, localDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(localDTO))
            )
            .andExpect(status().isOk());

        // Validate the Local in the database
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeUpdate);
        Local testLocal = localList.get(localList.size() - 1);
        assertThat(testLocal.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testLocal.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testLocal.getCapacidade()).isEqualTo(UPDATED_CAPACIDADE);
        assertThat(testLocal.getImagen()).isEqualTo(UPDATED_IMAGEN);
        assertThat(testLocal.getImagenContentType()).isEqualTo(UPDATED_IMAGEN_CONTENT_TYPE);
        assertThat(testLocal.getObservacoes()).isEqualTo(UPDATED_OBSERVACOES);
        assertThat(testLocal.getLocalizacao()).isEqualTo(UPDATED_LOCALIZACAO);
        assertThat(testLocal.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testLocal.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testLocal.getCor()).isEqualTo(UPDATED_COR);
        assertThat(testLocal.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testLocal.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void putNonExistingLocal() throws Exception {
        int databaseSizeBeforeUpdate = localRepository.findAll().size();
        local.setId(count.incrementAndGet());

        // Create the Local
        LocalDTO localDTO = localMapper.toDto(local);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, localDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(localDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Local in the database
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocal() throws Exception {
        int databaseSizeBeforeUpdate = localRepository.findAll().size();
        local.setId(count.incrementAndGet());

        // Create the Local
        LocalDTO localDTO = localMapper.toDto(local);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(localDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Local in the database
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocal() throws Exception {
        int databaseSizeBeforeUpdate = localRepository.findAll().size();
        local.setId(count.incrementAndGet());

        // Create the Local
        LocalDTO localDTO = localMapper.toDto(local);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(localDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Local in the database
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocalWithPatch() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        int databaseSizeBeforeUpdate = localRepository.findAll().size();

        // Update the local using partial update
        Local partialUpdatedLocal = new Local();
        partialUpdatedLocal.setId(local.getId());

        partialUpdatedLocal
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .imagen(UPDATED_IMAGEN)
            .imagenContentType(UPDATED_IMAGEN_CONTENT_TYPE)
            .localizacao(UPDATED_LOCALIZACAO)
            .status(UPDATED_STATUS)
            .cor(UPDATED_COR)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);

        restLocalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocal))
            )
            .andExpect(status().isOk());

        // Validate the Local in the database
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeUpdate);
        Local testLocal = localList.get(localList.size() - 1);
        assertThat(testLocal.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testLocal.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testLocal.getCapacidade()).isEqualTo(DEFAULT_CAPACIDADE);
        assertThat(testLocal.getImagen()).isEqualTo(UPDATED_IMAGEN);
        assertThat(testLocal.getImagenContentType()).isEqualTo(UPDATED_IMAGEN_CONTENT_TYPE);
        assertThat(testLocal.getObservacoes()).isEqualTo(DEFAULT_OBSERVACOES);
        assertThat(testLocal.getLocalizacao()).isEqualTo(UPDATED_LOCALIZACAO);
        assertThat(testLocal.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testLocal.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testLocal.getCor()).isEqualTo(UPDATED_COR);
        assertThat(testLocal.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testLocal.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void fullUpdateLocalWithPatch() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        int databaseSizeBeforeUpdate = localRepository.findAll().size();

        // Update the local using partial update
        Local partialUpdatedLocal = new Local();
        partialUpdatedLocal.setId(local.getId());

        partialUpdatedLocal
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .capacidade(UPDATED_CAPACIDADE)
            .imagen(UPDATED_IMAGEN)
            .imagenContentType(UPDATED_IMAGEN_CONTENT_TYPE)
            .observacoes(UPDATED_OBSERVACOES)
            .localizacao(UPDATED_LOCALIZACAO)
            .status(UPDATED_STATUS)
            .valor(UPDATED_VALOR)
            .cor(UPDATED_COR)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);

        restLocalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocal))
            )
            .andExpect(status().isOk());

        // Validate the Local in the database
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeUpdate);
        Local testLocal = localList.get(localList.size() - 1);
        assertThat(testLocal.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testLocal.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testLocal.getCapacidade()).isEqualTo(UPDATED_CAPACIDADE);
        assertThat(testLocal.getImagen()).isEqualTo(UPDATED_IMAGEN);
        assertThat(testLocal.getImagenContentType()).isEqualTo(UPDATED_IMAGEN_CONTENT_TYPE);
        assertThat(testLocal.getObservacoes()).isEqualTo(UPDATED_OBSERVACOES);
        assertThat(testLocal.getLocalizacao()).isEqualTo(UPDATED_LOCALIZACAO);
        assertThat(testLocal.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testLocal.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testLocal.getCor()).isEqualTo(UPDATED_COR);
        assertThat(testLocal.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testLocal.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingLocal() throws Exception {
        int databaseSizeBeforeUpdate = localRepository.findAll().size();
        local.setId(count.incrementAndGet());

        // Create the Local
        LocalDTO localDTO = localMapper.toDto(local);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, localDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(localDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Local in the database
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocal() throws Exception {
        int databaseSizeBeforeUpdate = localRepository.findAll().size();
        local.setId(count.incrementAndGet());

        // Create the Local
        LocalDTO localDTO = localMapper.toDto(local);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(localDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Local in the database
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocal() throws Exception {
        int databaseSizeBeforeUpdate = localRepository.findAll().size();
        local.setId(count.incrementAndGet());

        // Create the Local
        LocalDTO localDTO = localMapper.toDto(local);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(localDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Local in the database
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocal() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        int databaseSizeBeforeDelete = localRepository.findAll().size();

        // Delete the local
        restLocalMockMvc
            .perform(delete(ENTITY_API_URL_ID, local.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
