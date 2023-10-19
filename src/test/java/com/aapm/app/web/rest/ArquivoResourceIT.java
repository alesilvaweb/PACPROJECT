package com.aapm.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aapm.app.IntegrationTest;
import com.aapm.app.domain.Arquivo;
import com.aapm.app.domain.enumeration.StatusArquivo;
import com.aapm.app.repository.ArquivoRepository;
import com.aapm.app.service.criteria.ArquivoCriteria;
import com.aapm.app.service.dto.ArquivoDTO;
import com.aapm.app.service.mapper.ArquivoMapper;
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
 * Integration tests for the {@link ArquivoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArquivoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final byte[] DEFAULT_ARQUIVO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ARQUIVO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ARQUIVO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ARQUIVO_CONTENT_TYPE = "image/png";

    private static final StatusArquivo DEFAULT_STATUS = StatusArquivo.Carregado;
    private static final StatusArquivo UPDATED_STATUS = StatusArquivo.Processado;

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/arquivos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ArquivoRepository arquivoRepository;

    @Autowired
    private ArquivoMapper arquivoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArquivoMockMvc;

    private Arquivo arquivo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Arquivo createEntity(EntityManager em) {
        Arquivo arquivo = new Arquivo()
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO)
            .arquivo(DEFAULT_ARQUIVO)
            .arquivoContentType(DEFAULT_ARQUIVO_CONTENT_TYPE)
            .status(DEFAULT_STATUS)
            .created(DEFAULT_CREATED)
            .modified(DEFAULT_MODIFIED);
        return arquivo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Arquivo createUpdatedEntity(EntityManager em) {
        Arquivo arquivo = new Arquivo()
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .arquivo(UPDATED_ARQUIVO)
            .arquivoContentType(UPDATED_ARQUIVO_CONTENT_TYPE)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);
        return arquivo;
    }

    @BeforeEach
    public void initTest() {
        arquivo = createEntity(em);
    }

    @Test
    @Transactional
    void createArquivo() throws Exception {
        int databaseSizeBeforeCreate = arquivoRepository.findAll().size();
        // Create the Arquivo
        ArquivoDTO arquivoDTO = arquivoMapper.toDto(arquivo);
        restArquivoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arquivoDTO)))
            .andExpect(status().isCreated());

        // Validate the Arquivo in the database
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeCreate + 1);
        Arquivo testArquivo = arquivoList.get(arquivoList.size() - 1);
        assertThat(testArquivo.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testArquivo.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testArquivo.getArquivo()).isEqualTo(DEFAULT_ARQUIVO);
        assertThat(testArquivo.getArquivoContentType()).isEqualTo(DEFAULT_ARQUIVO_CONTENT_TYPE);
        assertThat(testArquivo.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testArquivo.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testArquivo.getModified()).isEqualTo(DEFAULT_MODIFIED);
    }

    @Test
    @Transactional
    void createArquivoWithExistingId() throws Exception {
        // Create the Arquivo with an existing ID
        arquivo.setId(1L);
        ArquivoDTO arquivoDTO = arquivoMapper.toDto(arquivo);

        int databaseSizeBeforeCreate = arquivoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArquivoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arquivoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Arquivo in the database
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = arquivoRepository.findAll().size();
        // set the field null
        arquivo.setNome(null);

        // Create the Arquivo, which fails.
        ArquivoDTO arquivoDTO = arquivoMapper.toDto(arquivo);

        restArquivoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arquivoDTO)))
            .andExpect(status().isBadRequest());

        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllArquivos() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        // Get all the arquivoList
        restArquivoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(arquivo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].arquivoContentType").value(hasItem(DEFAULT_ARQUIVO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].arquivo").value(hasItem(Base64Utils.encodeToString(DEFAULT_ARQUIVO))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())));
    }

    @Test
    @Transactional
    void getArquivo() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        // Get the arquivo
        restArquivoMockMvc
            .perform(get(ENTITY_API_URL_ID, arquivo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(arquivo.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.arquivoContentType").value(DEFAULT_ARQUIVO_CONTENT_TYPE))
            .andExpect(jsonPath("$.arquivo").value(Base64Utils.encodeToString(DEFAULT_ARQUIVO)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.modified").value(DEFAULT_MODIFIED.toString()));
    }

    @Test
    @Transactional
    void getArquivosByIdFiltering() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        Long id = arquivo.getId();

        defaultArquivoShouldBeFound("id.equals=" + id);
        defaultArquivoShouldNotBeFound("id.notEquals=" + id);

        defaultArquivoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultArquivoShouldNotBeFound("id.greaterThan=" + id);

        defaultArquivoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultArquivoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllArquivosByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        // Get all the arquivoList where nome equals to DEFAULT_NOME
        defaultArquivoShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the arquivoList where nome equals to UPDATED_NOME
        defaultArquivoShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllArquivosByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        // Get all the arquivoList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultArquivoShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the arquivoList where nome equals to UPDATED_NOME
        defaultArquivoShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllArquivosByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        // Get all the arquivoList where nome is not null
        defaultArquivoShouldBeFound("nome.specified=true");

        // Get all the arquivoList where nome is null
        defaultArquivoShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllArquivosByNomeContainsSomething() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        // Get all the arquivoList where nome contains DEFAULT_NOME
        defaultArquivoShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the arquivoList where nome contains UPDATED_NOME
        defaultArquivoShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllArquivosByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        // Get all the arquivoList where nome does not contain DEFAULT_NOME
        defaultArquivoShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the arquivoList where nome does not contain UPDATED_NOME
        defaultArquivoShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllArquivosByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        // Get all the arquivoList where descricao equals to DEFAULT_DESCRICAO
        defaultArquivoShouldBeFound("descricao.equals=" + DEFAULT_DESCRICAO);

        // Get all the arquivoList where descricao equals to UPDATED_DESCRICAO
        defaultArquivoShouldNotBeFound("descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllArquivosByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        // Get all the arquivoList where descricao in DEFAULT_DESCRICAO or UPDATED_DESCRICAO
        defaultArquivoShouldBeFound("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO);

        // Get all the arquivoList where descricao equals to UPDATED_DESCRICAO
        defaultArquivoShouldNotBeFound("descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllArquivosByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        // Get all the arquivoList where descricao is not null
        defaultArquivoShouldBeFound("descricao.specified=true");

        // Get all the arquivoList where descricao is null
        defaultArquivoShouldNotBeFound("descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllArquivosByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        // Get all the arquivoList where descricao contains DEFAULT_DESCRICAO
        defaultArquivoShouldBeFound("descricao.contains=" + DEFAULT_DESCRICAO);

        // Get all the arquivoList where descricao contains UPDATED_DESCRICAO
        defaultArquivoShouldNotBeFound("descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllArquivosByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        // Get all the arquivoList where descricao does not contain DEFAULT_DESCRICAO
        defaultArquivoShouldNotBeFound("descricao.doesNotContain=" + DEFAULT_DESCRICAO);

        // Get all the arquivoList where descricao does not contain UPDATED_DESCRICAO
        defaultArquivoShouldBeFound("descricao.doesNotContain=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllArquivosByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        // Get all the arquivoList where status equals to DEFAULT_STATUS
        defaultArquivoShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the arquivoList where status equals to UPDATED_STATUS
        defaultArquivoShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllArquivosByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        // Get all the arquivoList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultArquivoShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the arquivoList where status equals to UPDATED_STATUS
        defaultArquivoShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllArquivosByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        // Get all the arquivoList where status is not null
        defaultArquivoShouldBeFound("status.specified=true");

        // Get all the arquivoList where status is null
        defaultArquivoShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllArquivosByCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        // Get all the arquivoList where created equals to DEFAULT_CREATED
        defaultArquivoShouldBeFound("created.equals=" + DEFAULT_CREATED);

        // Get all the arquivoList where created equals to UPDATED_CREATED
        defaultArquivoShouldNotBeFound("created.equals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllArquivosByCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        // Get all the arquivoList where created in DEFAULT_CREATED or UPDATED_CREATED
        defaultArquivoShouldBeFound("created.in=" + DEFAULT_CREATED + "," + UPDATED_CREATED);

        // Get all the arquivoList where created equals to UPDATED_CREATED
        defaultArquivoShouldNotBeFound("created.in=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllArquivosByCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        // Get all the arquivoList where created is not null
        defaultArquivoShouldBeFound("created.specified=true");

        // Get all the arquivoList where created is null
        defaultArquivoShouldNotBeFound("created.specified=false");
    }

    @Test
    @Transactional
    void getAllArquivosByModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        // Get all the arquivoList where modified equals to DEFAULT_MODIFIED
        defaultArquivoShouldBeFound("modified.equals=" + DEFAULT_MODIFIED);

        // Get all the arquivoList where modified equals to UPDATED_MODIFIED
        defaultArquivoShouldNotBeFound("modified.equals=" + UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void getAllArquivosByModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        // Get all the arquivoList where modified in DEFAULT_MODIFIED or UPDATED_MODIFIED
        defaultArquivoShouldBeFound("modified.in=" + DEFAULT_MODIFIED + "," + UPDATED_MODIFIED);

        // Get all the arquivoList where modified equals to UPDATED_MODIFIED
        defaultArquivoShouldNotBeFound("modified.in=" + UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void getAllArquivosByModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        // Get all the arquivoList where modified is not null
        defaultArquivoShouldBeFound("modified.specified=true");

        // Get all the arquivoList where modified is null
        defaultArquivoShouldNotBeFound("modified.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultArquivoShouldBeFound(String filter) throws Exception {
        restArquivoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(arquivo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].arquivoContentType").value(hasItem(DEFAULT_ARQUIVO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].arquivo").value(hasItem(Base64Utils.encodeToString(DEFAULT_ARQUIVO))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())));

        // Check, that the count call also returns 1
        restArquivoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultArquivoShouldNotBeFound(String filter) throws Exception {
        restArquivoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restArquivoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingArquivo() throws Exception {
        // Get the arquivo
        restArquivoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingArquivo() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        int databaseSizeBeforeUpdate = arquivoRepository.findAll().size();

        // Update the arquivo
        Arquivo updatedArquivo = arquivoRepository.findById(arquivo.getId()).get();
        // Disconnect from session so that the updates on updatedArquivo are not directly saved in db
        em.detach(updatedArquivo);
        updatedArquivo
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .arquivo(UPDATED_ARQUIVO)
            .arquivoContentType(UPDATED_ARQUIVO_CONTENT_TYPE)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);
        ArquivoDTO arquivoDTO = arquivoMapper.toDto(updatedArquivo);

        restArquivoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, arquivoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arquivoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Arquivo in the database
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeUpdate);
        Arquivo testArquivo = arquivoList.get(arquivoList.size() - 1);
        assertThat(testArquivo.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testArquivo.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testArquivo.getArquivo()).isEqualTo(UPDATED_ARQUIVO);
        assertThat(testArquivo.getArquivoContentType()).isEqualTo(UPDATED_ARQUIVO_CONTENT_TYPE);
        assertThat(testArquivo.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testArquivo.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testArquivo.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void putNonExistingArquivo() throws Exception {
        int databaseSizeBeforeUpdate = arquivoRepository.findAll().size();
        arquivo.setId(count.incrementAndGet());

        // Create the Arquivo
        ArquivoDTO arquivoDTO = arquivoMapper.toDto(arquivo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArquivoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, arquivoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arquivoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Arquivo in the database
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArquivo() throws Exception {
        int databaseSizeBeforeUpdate = arquivoRepository.findAll().size();
        arquivo.setId(count.incrementAndGet());

        // Create the Arquivo
        ArquivoDTO arquivoDTO = arquivoMapper.toDto(arquivo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArquivoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arquivoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Arquivo in the database
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArquivo() throws Exception {
        int databaseSizeBeforeUpdate = arquivoRepository.findAll().size();
        arquivo.setId(count.incrementAndGet());

        // Create the Arquivo
        ArquivoDTO arquivoDTO = arquivoMapper.toDto(arquivo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArquivoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arquivoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Arquivo in the database
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArquivoWithPatch() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        int databaseSizeBeforeUpdate = arquivoRepository.findAll().size();

        // Update the arquivo using partial update
        Arquivo partialUpdatedArquivo = new Arquivo();
        partialUpdatedArquivo.setId(arquivo.getId());

        partialUpdatedArquivo
            .nome(UPDATED_NOME)
            .arquivo(UPDATED_ARQUIVO)
            .arquivoContentType(UPDATED_ARQUIVO_CONTENT_TYPE)
            .status(UPDATED_STATUS);

        restArquivoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArquivo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArquivo))
            )
            .andExpect(status().isOk());

        // Validate the Arquivo in the database
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeUpdate);
        Arquivo testArquivo = arquivoList.get(arquivoList.size() - 1);
        assertThat(testArquivo.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testArquivo.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testArquivo.getArquivo()).isEqualTo(UPDATED_ARQUIVO);
        assertThat(testArquivo.getArquivoContentType()).isEqualTo(UPDATED_ARQUIVO_CONTENT_TYPE);
        assertThat(testArquivo.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testArquivo.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testArquivo.getModified()).isEqualTo(DEFAULT_MODIFIED);
    }

    @Test
    @Transactional
    void fullUpdateArquivoWithPatch() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        int databaseSizeBeforeUpdate = arquivoRepository.findAll().size();

        // Update the arquivo using partial update
        Arquivo partialUpdatedArquivo = new Arquivo();
        partialUpdatedArquivo.setId(arquivo.getId());

        partialUpdatedArquivo
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .arquivo(UPDATED_ARQUIVO)
            .arquivoContentType(UPDATED_ARQUIVO_CONTENT_TYPE)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);

        restArquivoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArquivo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArquivo))
            )
            .andExpect(status().isOk());

        // Validate the Arquivo in the database
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeUpdate);
        Arquivo testArquivo = arquivoList.get(arquivoList.size() - 1);
        assertThat(testArquivo.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testArquivo.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testArquivo.getArquivo()).isEqualTo(UPDATED_ARQUIVO);
        assertThat(testArquivo.getArquivoContentType()).isEqualTo(UPDATED_ARQUIVO_CONTENT_TYPE);
        assertThat(testArquivo.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testArquivo.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testArquivo.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingArquivo() throws Exception {
        int databaseSizeBeforeUpdate = arquivoRepository.findAll().size();
        arquivo.setId(count.incrementAndGet());

        // Create the Arquivo
        ArquivoDTO arquivoDTO = arquivoMapper.toDto(arquivo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArquivoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, arquivoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(arquivoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Arquivo in the database
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArquivo() throws Exception {
        int databaseSizeBeforeUpdate = arquivoRepository.findAll().size();
        arquivo.setId(count.incrementAndGet());

        // Create the Arquivo
        ArquivoDTO arquivoDTO = arquivoMapper.toDto(arquivo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArquivoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(arquivoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Arquivo in the database
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArquivo() throws Exception {
        int databaseSizeBeforeUpdate = arquivoRepository.findAll().size();
        arquivo.setId(count.incrementAndGet());

        // Create the Arquivo
        ArquivoDTO arquivoDTO = arquivoMapper.toDto(arquivo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArquivoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(arquivoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Arquivo in the database
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArquivo() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        int databaseSizeBeforeDelete = arquivoRepository.findAll().size();

        // Delete the arquivo
        restArquivoMockMvc
            .perform(delete(ENTITY_API_URL_ID, arquivo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
