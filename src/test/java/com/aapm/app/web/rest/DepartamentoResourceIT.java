package com.aapm.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aapm.app.IntegrationTest;
import com.aapm.app.domain.Departamento;
import com.aapm.app.domain.Reserva;
import com.aapm.app.domain.enumeration.Status;
import com.aapm.app.repository.DepartamentoRepository;
import com.aapm.app.service.criteria.DepartamentoCriteria;
import com.aapm.app.service.dto.DepartamentoDTO;
import com.aapm.app.service.mapper.DepartamentoMapper;
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
 * Integration tests for the {@link DepartamentoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DepartamentoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.Ativo;
    private static final Status UPDATED_STATUS = Status.Inativo;

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/departamentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private DepartamentoMapper departamentoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepartamentoMockMvc;

    private Departamento departamento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Departamento createEntity(EntityManager em) {
        Departamento departamento = new Departamento()
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO)
            .status(DEFAULT_STATUS)
            .created(DEFAULT_CREATED)
            .modified(DEFAULT_MODIFIED);
        return departamento;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Departamento createUpdatedEntity(EntityManager em) {
        Departamento departamento = new Departamento()
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);
        return departamento;
    }

    @BeforeEach
    public void initTest() {
        departamento = createEntity(em);
    }

    @Test
    @Transactional
    void createDepartamento() throws Exception {
        int databaseSizeBeforeCreate = departamentoRepository.findAll().size();
        // Create the Departamento
        DepartamentoDTO departamentoDTO = departamentoMapper.toDto(departamento);
        restDepartamentoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departamentoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Departamento in the database
        List<Departamento> departamentoList = departamentoRepository.findAll();
        assertThat(departamentoList).hasSize(databaseSizeBeforeCreate + 1);
        Departamento testDepartamento = departamentoList.get(departamentoList.size() - 1);
        assertThat(testDepartamento.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testDepartamento.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testDepartamento.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDepartamento.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testDepartamento.getModified()).isEqualTo(DEFAULT_MODIFIED);
    }

    @Test
    @Transactional
    void createDepartamentoWithExistingId() throws Exception {
        // Create the Departamento with an existing ID
        departamento.setId(1L);
        DepartamentoDTO departamentoDTO = departamentoMapper.toDto(departamento);

        int databaseSizeBeforeCreate = departamentoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepartamentoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departamentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departamento in the database
        List<Departamento> departamentoList = departamentoRepository.findAll();
        assertThat(departamentoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDepartamentos() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        // Get all the departamentoList
        restDepartamentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())));
    }

    @Test
    @Transactional
    void getDepartamento() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        // Get the departamento
        restDepartamentoMockMvc
            .perform(get(ENTITY_API_URL_ID, departamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(departamento.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.modified").value(DEFAULT_MODIFIED.toString()));
    }

    @Test
    @Transactional
    void getDepartamentosByIdFiltering() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        Long id = departamento.getId();

        defaultDepartamentoShouldBeFound("id.equals=" + id);
        defaultDepartamentoShouldNotBeFound("id.notEquals=" + id);

        defaultDepartamentoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDepartamentoShouldNotBeFound("id.greaterThan=" + id);

        defaultDepartamentoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDepartamentoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDepartamentosByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        // Get all the departamentoList where nome equals to DEFAULT_NOME
        defaultDepartamentoShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the departamentoList where nome equals to UPDATED_NOME
        defaultDepartamentoShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllDepartamentosByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        // Get all the departamentoList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultDepartamentoShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the departamentoList where nome equals to UPDATED_NOME
        defaultDepartamentoShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllDepartamentosByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        // Get all the departamentoList where nome is not null
        defaultDepartamentoShouldBeFound("nome.specified=true");

        // Get all the departamentoList where nome is null
        defaultDepartamentoShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartamentosByNomeContainsSomething() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        // Get all the departamentoList where nome contains DEFAULT_NOME
        defaultDepartamentoShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the departamentoList where nome contains UPDATED_NOME
        defaultDepartamentoShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllDepartamentosByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        // Get all the departamentoList where nome does not contain DEFAULT_NOME
        defaultDepartamentoShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the departamentoList where nome does not contain UPDATED_NOME
        defaultDepartamentoShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllDepartamentosByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        // Get all the departamentoList where descricao equals to DEFAULT_DESCRICAO
        defaultDepartamentoShouldBeFound("descricao.equals=" + DEFAULT_DESCRICAO);

        // Get all the departamentoList where descricao equals to UPDATED_DESCRICAO
        defaultDepartamentoShouldNotBeFound("descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllDepartamentosByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        // Get all the departamentoList where descricao in DEFAULT_DESCRICAO or UPDATED_DESCRICAO
        defaultDepartamentoShouldBeFound("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO);

        // Get all the departamentoList where descricao equals to UPDATED_DESCRICAO
        defaultDepartamentoShouldNotBeFound("descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllDepartamentosByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        // Get all the departamentoList where descricao is not null
        defaultDepartamentoShouldBeFound("descricao.specified=true");

        // Get all the departamentoList where descricao is null
        defaultDepartamentoShouldNotBeFound("descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartamentosByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        // Get all the departamentoList where descricao contains DEFAULT_DESCRICAO
        defaultDepartamentoShouldBeFound("descricao.contains=" + DEFAULT_DESCRICAO);

        // Get all the departamentoList where descricao contains UPDATED_DESCRICAO
        defaultDepartamentoShouldNotBeFound("descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllDepartamentosByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        // Get all the departamentoList where descricao does not contain DEFAULT_DESCRICAO
        defaultDepartamentoShouldNotBeFound("descricao.doesNotContain=" + DEFAULT_DESCRICAO);

        // Get all the departamentoList where descricao does not contain UPDATED_DESCRICAO
        defaultDepartamentoShouldBeFound("descricao.doesNotContain=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllDepartamentosByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        // Get all the departamentoList where status equals to DEFAULT_STATUS
        defaultDepartamentoShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the departamentoList where status equals to UPDATED_STATUS
        defaultDepartamentoShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllDepartamentosByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        // Get all the departamentoList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultDepartamentoShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the departamentoList where status equals to UPDATED_STATUS
        defaultDepartamentoShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllDepartamentosByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        // Get all the departamentoList where status is not null
        defaultDepartamentoShouldBeFound("status.specified=true");

        // Get all the departamentoList where status is null
        defaultDepartamentoShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartamentosByCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        // Get all the departamentoList where created equals to DEFAULT_CREATED
        defaultDepartamentoShouldBeFound("created.equals=" + DEFAULT_CREATED);

        // Get all the departamentoList where created equals to UPDATED_CREATED
        defaultDepartamentoShouldNotBeFound("created.equals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllDepartamentosByCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        // Get all the departamentoList where created in DEFAULT_CREATED or UPDATED_CREATED
        defaultDepartamentoShouldBeFound("created.in=" + DEFAULT_CREATED + "," + UPDATED_CREATED);

        // Get all the departamentoList where created equals to UPDATED_CREATED
        defaultDepartamentoShouldNotBeFound("created.in=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllDepartamentosByCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        // Get all the departamentoList where created is not null
        defaultDepartamentoShouldBeFound("created.specified=true");

        // Get all the departamentoList where created is null
        defaultDepartamentoShouldNotBeFound("created.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartamentosByModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        // Get all the departamentoList where modified equals to DEFAULT_MODIFIED
        defaultDepartamentoShouldBeFound("modified.equals=" + DEFAULT_MODIFIED);

        // Get all the departamentoList where modified equals to UPDATED_MODIFIED
        defaultDepartamentoShouldNotBeFound("modified.equals=" + UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void getAllDepartamentosByModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        // Get all the departamentoList where modified in DEFAULT_MODIFIED or UPDATED_MODIFIED
        defaultDepartamentoShouldBeFound("modified.in=" + DEFAULT_MODIFIED + "," + UPDATED_MODIFIED);

        // Get all the departamentoList where modified equals to UPDATED_MODIFIED
        defaultDepartamentoShouldNotBeFound("modified.in=" + UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void getAllDepartamentosByModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        // Get all the departamentoList where modified is not null
        defaultDepartamentoShouldBeFound("modified.specified=true");

        // Get all the departamentoList where modified is null
        defaultDepartamentoShouldNotBeFound("modified.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartamentosByReservaIsEqualToSomething() throws Exception {
        Reserva reserva;
        if (TestUtil.findAll(em, Reserva.class).isEmpty()) {
            departamentoRepository.saveAndFlush(departamento);
            reserva = ReservaResourceIT.createEntity(em);
        } else {
            reserva = TestUtil.findAll(em, Reserva.class).get(0);
        }
        em.persist(reserva);
        em.flush();
        departamento.addReserva(reserva);
        departamentoRepository.saveAndFlush(departamento);
        Long reservaId = reserva.getId();

        // Get all the departamentoList where reserva equals to reservaId
        defaultDepartamentoShouldBeFound("reservaId.equals=" + reservaId);

        // Get all the departamentoList where reserva equals to (reservaId + 1)
        defaultDepartamentoShouldNotBeFound("reservaId.equals=" + (reservaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDepartamentoShouldBeFound(String filter) throws Exception {
        restDepartamentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())));

        // Check, that the count call also returns 1
        restDepartamentoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDepartamentoShouldNotBeFound(String filter) throws Exception {
        restDepartamentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDepartamentoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDepartamento() throws Exception {
        // Get the departamento
        restDepartamentoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDepartamento() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        int databaseSizeBeforeUpdate = departamentoRepository.findAll().size();

        // Update the departamento
        Departamento updatedDepartamento = departamentoRepository.findById(departamento.getId()).get();
        // Disconnect from session so that the updates on updatedDepartamento are not directly saved in db
        em.detach(updatedDepartamento);
        updatedDepartamento
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);
        DepartamentoDTO departamentoDTO = departamentoMapper.toDto(updatedDepartamento);

        restDepartamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, departamentoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departamentoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Departamento in the database
        List<Departamento> departamentoList = departamentoRepository.findAll();
        assertThat(departamentoList).hasSize(databaseSizeBeforeUpdate);
        Departamento testDepartamento = departamentoList.get(departamentoList.size() - 1);
        assertThat(testDepartamento.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testDepartamento.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testDepartamento.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDepartamento.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testDepartamento.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void putNonExistingDepartamento() throws Exception {
        int databaseSizeBeforeUpdate = departamentoRepository.findAll().size();
        departamento.setId(count.incrementAndGet());

        // Create the Departamento
        DepartamentoDTO departamentoDTO = departamentoMapper.toDto(departamento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, departamentoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departamentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departamento in the database
        List<Departamento> departamentoList = departamentoRepository.findAll();
        assertThat(departamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepartamento() throws Exception {
        int databaseSizeBeforeUpdate = departamentoRepository.findAll().size();
        departamento.setId(count.incrementAndGet());

        // Create the Departamento
        DepartamentoDTO departamentoDTO = departamentoMapper.toDto(departamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departamentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departamento in the database
        List<Departamento> departamentoList = departamentoRepository.findAll();
        assertThat(departamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepartamento() throws Exception {
        int databaseSizeBeforeUpdate = departamentoRepository.findAll().size();
        departamento.setId(count.incrementAndGet());

        // Create the Departamento
        DepartamentoDTO departamentoDTO = departamentoMapper.toDto(departamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartamentoMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departamentoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Departamento in the database
        List<Departamento> departamentoList = departamentoRepository.findAll();
        assertThat(departamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDepartamentoWithPatch() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        int databaseSizeBeforeUpdate = departamentoRepository.findAll().size();

        // Update the departamento using partial update
        Departamento partialUpdatedDepartamento = new Departamento();
        partialUpdatedDepartamento.setId(departamento.getId());

        partialUpdatedDepartamento.nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO).status(UPDATED_STATUS).modified(UPDATED_MODIFIED);

        restDepartamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartamento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepartamento))
            )
            .andExpect(status().isOk());

        // Validate the Departamento in the database
        List<Departamento> departamentoList = departamentoRepository.findAll();
        assertThat(departamentoList).hasSize(databaseSizeBeforeUpdate);
        Departamento testDepartamento = departamentoList.get(departamentoList.size() - 1);
        assertThat(testDepartamento.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testDepartamento.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testDepartamento.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDepartamento.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testDepartamento.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void fullUpdateDepartamentoWithPatch() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        int databaseSizeBeforeUpdate = departamentoRepository.findAll().size();

        // Update the departamento using partial update
        Departamento partialUpdatedDepartamento = new Departamento();
        partialUpdatedDepartamento.setId(departamento.getId());

        partialUpdatedDepartamento
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);

        restDepartamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartamento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepartamento))
            )
            .andExpect(status().isOk());

        // Validate the Departamento in the database
        List<Departamento> departamentoList = departamentoRepository.findAll();
        assertThat(departamentoList).hasSize(databaseSizeBeforeUpdate);
        Departamento testDepartamento = departamentoList.get(departamentoList.size() - 1);
        assertThat(testDepartamento.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testDepartamento.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testDepartamento.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDepartamento.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testDepartamento.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingDepartamento() throws Exception {
        int databaseSizeBeforeUpdate = departamentoRepository.findAll().size();
        departamento.setId(count.incrementAndGet());

        // Create the Departamento
        DepartamentoDTO departamentoDTO = departamentoMapper.toDto(departamento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, departamentoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(departamentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departamento in the database
        List<Departamento> departamentoList = departamentoRepository.findAll();
        assertThat(departamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepartamento() throws Exception {
        int databaseSizeBeforeUpdate = departamentoRepository.findAll().size();
        departamento.setId(count.incrementAndGet());

        // Create the Departamento
        DepartamentoDTO departamentoDTO = departamentoMapper.toDto(departamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(departamentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departamento in the database
        List<Departamento> departamentoList = departamentoRepository.findAll();
        assertThat(departamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepartamento() throws Exception {
        int databaseSizeBeforeUpdate = departamentoRepository.findAll().size();
        departamento.setId(count.incrementAndGet());

        // Create the Departamento
        DepartamentoDTO departamentoDTO = departamentoMapper.toDto(departamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartamentoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(departamentoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Departamento in the database
        List<Departamento> departamentoList = departamentoRepository.findAll();
        assertThat(departamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDepartamento() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        int databaseSizeBeforeDelete = departamentoRepository.findAll().size();

        // Delete the departamento
        restDepartamentoMockMvc
            .perform(delete(ENTITY_API_URL_ID, departamento.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Departamento> departamentoList = departamentoRepository.findAll();
        assertThat(departamentoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
