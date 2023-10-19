package com.aapm.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aapm.app.IntegrationTest;
import com.aapm.app.domain.Associado;
import com.aapm.app.domain.Contato;
import com.aapm.app.domain.enumeration.TipoContato;
import com.aapm.app.repository.ContatoRepository;
import com.aapm.app.service.ContatoService;
import com.aapm.app.service.criteria.ContatoCriteria;
import com.aapm.app.service.dto.ContatoDTO;
import com.aapm.app.service.mapper.ContatoMapper;
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
 * Integration tests for the {@link ContatoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ContatoResourceIT {

    private static final TipoContato DEFAULT_TIPO = TipoContato.Telefone;
    private static final TipoContato UPDATED_TIPO = TipoContato.Email;

    private static final String DEFAULT_CONTATO = "AAAAAAAAAA";
    private static final String UPDATED_CONTATO = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/contatoes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContatoRepository contatoRepository;

    @Mock
    private ContatoRepository contatoRepositoryMock;

    @Autowired
    private ContatoMapper contatoMapper;

    @Mock
    private ContatoService contatoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContatoMockMvc;

    private Contato contato;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contato createEntity(EntityManager em) {
        Contato contato = new Contato().tipo(DEFAULT_TIPO).contato(DEFAULT_CONTATO).created(DEFAULT_CREATED).modified(DEFAULT_MODIFIED);
        return contato;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contato createUpdatedEntity(EntityManager em) {
        Contato contato = new Contato().tipo(UPDATED_TIPO).contato(UPDATED_CONTATO).created(UPDATED_CREATED).modified(UPDATED_MODIFIED);
        return contato;
    }

    @BeforeEach
    public void initTest() {
        contato = createEntity(em);
    }

    @Test
    @Transactional
    void createContato() throws Exception {
        int databaseSizeBeforeCreate = contatoRepository.findAll().size();
        // Create the Contato
        ContatoDTO contatoDTO = contatoMapper.toDto(contato);
        restContatoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contatoDTO)))
            .andExpect(status().isCreated());

        // Validate the Contato in the database
        List<Contato> contatoList = contatoRepository.findAll();
        assertThat(contatoList).hasSize(databaseSizeBeforeCreate + 1);
        Contato testContato = contatoList.get(contatoList.size() - 1);
        assertThat(testContato.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testContato.getContato()).isEqualTo(DEFAULT_CONTATO);
        assertThat(testContato.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testContato.getModified()).isEqualTo(DEFAULT_MODIFIED);
    }

    @Test
    @Transactional
    void createContatoWithExistingId() throws Exception {
        // Create the Contato with an existing ID
        contato.setId(1L);
        ContatoDTO contatoDTO = contatoMapper.toDto(contato);

        int databaseSizeBeforeCreate = contatoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContatoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contatoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contato in the database
        List<Contato> contatoList = contatoRepository.findAll();
        assertThat(contatoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = contatoRepository.findAll().size();
        // set the field null
        contato.setTipo(null);

        // Create the Contato, which fails.
        ContatoDTO contatoDTO = contatoMapper.toDto(contato);

        restContatoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contatoDTO)))
            .andExpect(status().isBadRequest());

        List<Contato> contatoList = contatoRepository.findAll();
        assertThat(contatoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContatoIsRequired() throws Exception {
        int databaseSizeBeforeTest = contatoRepository.findAll().size();
        // set the field null
        contato.setContato(null);

        // Create the Contato, which fails.
        ContatoDTO contatoDTO = contatoMapper.toDto(contato);

        restContatoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contatoDTO)))
            .andExpect(status().isBadRequest());

        List<Contato> contatoList = contatoRepository.findAll();
        assertThat(contatoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContatoes() throws Exception {
        // Initialize the database
        contatoRepository.saveAndFlush(contato);

        // Get all the contatoList
        restContatoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contato.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].contato").value(hasItem(DEFAULT_CONTATO)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContatoesWithEagerRelationshipsIsEnabled() throws Exception {
        when(contatoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContatoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(contatoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContatoesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(contatoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContatoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(contatoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getContato() throws Exception {
        // Initialize the database
        contatoRepository.saveAndFlush(contato);

        // Get the contato
        restContatoMockMvc
            .perform(get(ENTITY_API_URL_ID, contato.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contato.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.contato").value(DEFAULT_CONTATO))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.modified").value(DEFAULT_MODIFIED.toString()));
    }

    @Test
    @Transactional
    void getContatoesByIdFiltering() throws Exception {
        // Initialize the database
        contatoRepository.saveAndFlush(contato);

        Long id = contato.getId();

        defaultContatoShouldBeFound("id.equals=" + id);
        defaultContatoShouldNotBeFound("id.notEquals=" + id);

        defaultContatoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultContatoShouldNotBeFound("id.greaterThan=" + id);

        defaultContatoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultContatoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllContatoesByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        contatoRepository.saveAndFlush(contato);

        // Get all the contatoList where tipo equals to DEFAULT_TIPO
        defaultContatoShouldBeFound("tipo.equals=" + DEFAULT_TIPO);

        // Get all the contatoList where tipo equals to UPDATED_TIPO
        defaultContatoShouldNotBeFound("tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllContatoesByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        contatoRepository.saveAndFlush(contato);

        // Get all the contatoList where tipo in DEFAULT_TIPO or UPDATED_TIPO
        defaultContatoShouldBeFound("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO);

        // Get all the contatoList where tipo equals to UPDATED_TIPO
        defaultContatoShouldNotBeFound("tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllContatoesByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        contatoRepository.saveAndFlush(contato);

        // Get all the contatoList where tipo is not null
        defaultContatoShouldBeFound("tipo.specified=true");

        // Get all the contatoList where tipo is null
        defaultContatoShouldNotBeFound("tipo.specified=false");
    }

    @Test
    @Transactional
    void getAllContatoesByContatoIsEqualToSomething() throws Exception {
        // Initialize the database
        contatoRepository.saveAndFlush(contato);

        // Get all the contatoList where contato equals to DEFAULT_CONTATO
        defaultContatoShouldBeFound("contato.equals=" + DEFAULT_CONTATO);

        // Get all the contatoList where contato equals to UPDATED_CONTATO
        defaultContatoShouldNotBeFound("contato.equals=" + UPDATED_CONTATO);
    }

    @Test
    @Transactional
    void getAllContatoesByContatoIsInShouldWork() throws Exception {
        // Initialize the database
        contatoRepository.saveAndFlush(contato);

        // Get all the contatoList where contato in DEFAULT_CONTATO or UPDATED_CONTATO
        defaultContatoShouldBeFound("contato.in=" + DEFAULT_CONTATO + "," + UPDATED_CONTATO);

        // Get all the contatoList where contato equals to UPDATED_CONTATO
        defaultContatoShouldNotBeFound("contato.in=" + UPDATED_CONTATO);
    }

    @Test
    @Transactional
    void getAllContatoesByContatoIsNullOrNotNull() throws Exception {
        // Initialize the database
        contatoRepository.saveAndFlush(contato);

        // Get all the contatoList where contato is not null
        defaultContatoShouldBeFound("contato.specified=true");

        // Get all the contatoList where contato is null
        defaultContatoShouldNotBeFound("contato.specified=false");
    }

    @Test
    @Transactional
    void getAllContatoesByContatoContainsSomething() throws Exception {
        // Initialize the database
        contatoRepository.saveAndFlush(contato);

        // Get all the contatoList where contato contains DEFAULT_CONTATO
        defaultContatoShouldBeFound("contato.contains=" + DEFAULT_CONTATO);

        // Get all the contatoList where contato contains UPDATED_CONTATO
        defaultContatoShouldNotBeFound("contato.contains=" + UPDATED_CONTATO);
    }

    @Test
    @Transactional
    void getAllContatoesByContatoNotContainsSomething() throws Exception {
        // Initialize the database
        contatoRepository.saveAndFlush(contato);

        // Get all the contatoList where contato does not contain DEFAULT_CONTATO
        defaultContatoShouldNotBeFound("contato.doesNotContain=" + DEFAULT_CONTATO);

        // Get all the contatoList where contato does not contain UPDATED_CONTATO
        defaultContatoShouldBeFound("contato.doesNotContain=" + UPDATED_CONTATO);
    }

    @Test
    @Transactional
    void getAllContatoesByCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        contatoRepository.saveAndFlush(contato);

        // Get all the contatoList where created equals to DEFAULT_CREATED
        defaultContatoShouldBeFound("created.equals=" + DEFAULT_CREATED);

        // Get all the contatoList where created equals to UPDATED_CREATED
        defaultContatoShouldNotBeFound("created.equals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllContatoesByCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        contatoRepository.saveAndFlush(contato);

        // Get all the contatoList where created in DEFAULT_CREATED or UPDATED_CREATED
        defaultContatoShouldBeFound("created.in=" + DEFAULT_CREATED + "," + UPDATED_CREATED);

        // Get all the contatoList where created equals to UPDATED_CREATED
        defaultContatoShouldNotBeFound("created.in=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllContatoesByCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        contatoRepository.saveAndFlush(contato);

        // Get all the contatoList where created is not null
        defaultContatoShouldBeFound("created.specified=true");

        // Get all the contatoList where created is null
        defaultContatoShouldNotBeFound("created.specified=false");
    }

    @Test
    @Transactional
    void getAllContatoesByModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        contatoRepository.saveAndFlush(contato);

        // Get all the contatoList where modified equals to DEFAULT_MODIFIED
        defaultContatoShouldBeFound("modified.equals=" + DEFAULT_MODIFIED);

        // Get all the contatoList where modified equals to UPDATED_MODIFIED
        defaultContatoShouldNotBeFound("modified.equals=" + UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void getAllContatoesByModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        contatoRepository.saveAndFlush(contato);

        // Get all the contatoList where modified in DEFAULT_MODIFIED or UPDATED_MODIFIED
        defaultContatoShouldBeFound("modified.in=" + DEFAULT_MODIFIED + "," + UPDATED_MODIFIED);

        // Get all the contatoList where modified equals to UPDATED_MODIFIED
        defaultContatoShouldNotBeFound("modified.in=" + UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void getAllContatoesByModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        contatoRepository.saveAndFlush(contato);

        // Get all the contatoList where modified is not null
        defaultContatoShouldBeFound("modified.specified=true");

        // Get all the contatoList where modified is null
        defaultContatoShouldNotBeFound("modified.specified=false");
    }

    @Test
    @Transactional
    void getAllContatoesByAssociadoIsEqualToSomething() throws Exception {
        Associado associado;
        if (TestUtil.findAll(em, Associado.class).isEmpty()) {
            contatoRepository.saveAndFlush(contato);
            associado = AssociadoResourceIT.createEntity(em);
        } else {
            associado = TestUtil.findAll(em, Associado.class).get(0);
        }
        em.persist(associado);
        em.flush();
        contato.setAssociado(associado);
        contatoRepository.saveAndFlush(contato);
        Long associadoId = associado.getId();

        // Get all the contatoList where associado equals to associadoId
        defaultContatoShouldBeFound("associadoId.equals=" + associadoId);

        // Get all the contatoList where associado equals to (associadoId + 1)
        defaultContatoShouldNotBeFound("associadoId.equals=" + (associadoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContatoShouldBeFound(String filter) throws Exception {
        restContatoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contato.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].contato").value(hasItem(DEFAULT_CONTATO)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())));

        // Check, that the count call also returns 1
        restContatoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContatoShouldNotBeFound(String filter) throws Exception {
        restContatoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContatoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingContato() throws Exception {
        // Get the contato
        restContatoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContato() throws Exception {
        // Initialize the database
        contatoRepository.saveAndFlush(contato);

        int databaseSizeBeforeUpdate = contatoRepository.findAll().size();

        // Update the contato
        Contato updatedContato = contatoRepository.findById(contato.getId()).get();
        // Disconnect from session so that the updates on updatedContato are not directly saved in db
        em.detach(updatedContato);
        updatedContato.tipo(UPDATED_TIPO).contato(UPDATED_CONTATO).created(UPDATED_CREATED).modified(UPDATED_MODIFIED);
        ContatoDTO contatoDTO = contatoMapper.toDto(updatedContato);

        restContatoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contatoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contatoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Contato in the database
        List<Contato> contatoList = contatoRepository.findAll();
        assertThat(contatoList).hasSize(databaseSizeBeforeUpdate);
        Contato testContato = contatoList.get(contatoList.size() - 1);
        assertThat(testContato.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testContato.getContato()).isEqualTo(UPDATED_CONTATO);
        assertThat(testContato.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testContato.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void putNonExistingContato() throws Exception {
        int databaseSizeBeforeUpdate = contatoRepository.findAll().size();
        contato.setId(count.incrementAndGet());

        // Create the Contato
        ContatoDTO contatoDTO = contatoMapper.toDto(contato);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContatoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contatoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contatoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contato in the database
        List<Contato> contatoList = contatoRepository.findAll();
        assertThat(contatoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContato() throws Exception {
        int databaseSizeBeforeUpdate = contatoRepository.findAll().size();
        contato.setId(count.incrementAndGet());

        // Create the Contato
        ContatoDTO contatoDTO = contatoMapper.toDto(contato);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContatoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contatoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contato in the database
        List<Contato> contatoList = contatoRepository.findAll();
        assertThat(contatoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContato() throws Exception {
        int databaseSizeBeforeUpdate = contatoRepository.findAll().size();
        contato.setId(count.incrementAndGet());

        // Create the Contato
        ContatoDTO contatoDTO = contatoMapper.toDto(contato);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContatoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contatoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contato in the database
        List<Contato> contatoList = contatoRepository.findAll();
        assertThat(contatoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContatoWithPatch() throws Exception {
        // Initialize the database
        contatoRepository.saveAndFlush(contato);

        int databaseSizeBeforeUpdate = contatoRepository.findAll().size();

        // Update the contato using partial update
        Contato partialUpdatedContato = new Contato();
        partialUpdatedContato.setId(contato.getId());

        partialUpdatedContato.tipo(UPDATED_TIPO).created(UPDATED_CREATED).modified(UPDATED_MODIFIED);

        restContatoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContato.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContato))
            )
            .andExpect(status().isOk());

        // Validate the Contato in the database
        List<Contato> contatoList = contatoRepository.findAll();
        assertThat(contatoList).hasSize(databaseSizeBeforeUpdate);
        Contato testContato = contatoList.get(contatoList.size() - 1);
        assertThat(testContato.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testContato.getContato()).isEqualTo(DEFAULT_CONTATO);
        assertThat(testContato.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testContato.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void fullUpdateContatoWithPatch() throws Exception {
        // Initialize the database
        contatoRepository.saveAndFlush(contato);

        int databaseSizeBeforeUpdate = contatoRepository.findAll().size();

        // Update the contato using partial update
        Contato partialUpdatedContato = new Contato();
        partialUpdatedContato.setId(contato.getId());

        partialUpdatedContato.tipo(UPDATED_TIPO).contato(UPDATED_CONTATO).created(UPDATED_CREATED).modified(UPDATED_MODIFIED);

        restContatoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContato.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContato))
            )
            .andExpect(status().isOk());

        // Validate the Contato in the database
        List<Contato> contatoList = contatoRepository.findAll();
        assertThat(contatoList).hasSize(databaseSizeBeforeUpdate);
        Contato testContato = contatoList.get(contatoList.size() - 1);
        assertThat(testContato.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testContato.getContato()).isEqualTo(UPDATED_CONTATO);
        assertThat(testContato.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testContato.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingContato() throws Exception {
        int databaseSizeBeforeUpdate = contatoRepository.findAll().size();
        contato.setId(count.incrementAndGet());

        // Create the Contato
        ContatoDTO contatoDTO = contatoMapper.toDto(contato);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContatoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contatoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contatoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contato in the database
        List<Contato> contatoList = contatoRepository.findAll();
        assertThat(contatoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContato() throws Exception {
        int databaseSizeBeforeUpdate = contatoRepository.findAll().size();
        contato.setId(count.incrementAndGet());

        // Create the Contato
        ContatoDTO contatoDTO = contatoMapper.toDto(contato);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContatoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contatoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contato in the database
        List<Contato> contatoList = contatoRepository.findAll();
        assertThat(contatoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContato() throws Exception {
        int databaseSizeBeforeUpdate = contatoRepository.findAll().size();
        contato.setId(count.incrementAndGet());

        // Create the Contato
        ContatoDTO contatoDTO = contatoMapper.toDto(contato);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContatoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(contatoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contato in the database
        List<Contato> contatoList = contatoRepository.findAll();
        assertThat(contatoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContato() throws Exception {
        // Initialize the database
        contatoRepository.saveAndFlush(contato);

        int databaseSizeBeforeDelete = contatoRepository.findAll().size();

        // Delete the contato
        restContatoMockMvc
            .perform(delete(ENTITY_API_URL_ID, contato.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Contato> contatoList = contatoRepository.findAll();
        assertThat(contatoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
