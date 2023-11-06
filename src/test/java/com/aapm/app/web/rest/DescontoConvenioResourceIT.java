package com.aapm.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aapm.app.IntegrationTest;
import com.aapm.app.domain.Convenio;
import com.aapm.app.domain.DescontoConvenio;
import com.aapm.app.repository.DescontoConvenioRepository;
import com.aapm.app.service.DescontoConvenioService;
import com.aapm.app.service.criteria.DescontoConvenioCriteria;
import com.aapm.app.service.dto.DescontoConvenioDTO;
import com.aapm.app.service.mapper.DescontoConvenioMapper;
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
 * Integration tests for the {@link DescontoConvenioResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DescontoConvenioResourceIT {

    private static final String DEFAULT_DESCONTO = "AAAAAAAAAA";
    private static final String UPDATED_DESCONTO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/desconto-convenios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DescontoConvenioRepository descontoConvenioRepository;

    @Mock
    private DescontoConvenioRepository descontoConvenioRepositoryMock;

    @Autowired
    private DescontoConvenioMapper descontoConvenioMapper;

    @Mock
    private DescontoConvenioService descontoConvenioServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDescontoConvenioMockMvc;

    private DescontoConvenio descontoConvenio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DescontoConvenio createEntity(EntityManager em) {
        DescontoConvenio descontoConvenio = new DescontoConvenio().desconto(DEFAULT_DESCONTO).descricao(DEFAULT_DESCRICAO);
        return descontoConvenio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DescontoConvenio createUpdatedEntity(EntityManager em) {
        DescontoConvenio descontoConvenio = new DescontoConvenio().desconto(UPDATED_DESCONTO).descricao(UPDATED_DESCRICAO);
        return descontoConvenio;
    }

    @BeforeEach
    public void initTest() {
        descontoConvenio = createEntity(em);
    }

    @Test
    @Transactional
    void createDescontoConvenio() throws Exception {
        int databaseSizeBeforeCreate = descontoConvenioRepository.findAll().size();
        // Create the DescontoConvenio
        DescontoConvenioDTO descontoConvenioDTO = descontoConvenioMapper.toDto(descontoConvenio);
        restDescontoConvenioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(descontoConvenioDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DescontoConvenio in the database
        List<DescontoConvenio> descontoConvenioList = descontoConvenioRepository.findAll();
        assertThat(descontoConvenioList).hasSize(databaseSizeBeforeCreate + 1);
        DescontoConvenio testDescontoConvenio = descontoConvenioList.get(descontoConvenioList.size() - 1);
        assertThat(testDescontoConvenio.getDesconto()).isEqualTo(DEFAULT_DESCONTO);
        assertThat(testDescontoConvenio.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void createDescontoConvenioWithExistingId() throws Exception {
        // Create the DescontoConvenio with an existing ID
        descontoConvenio.setId(1L);
        DescontoConvenioDTO descontoConvenioDTO = descontoConvenioMapper.toDto(descontoConvenio);

        int databaseSizeBeforeCreate = descontoConvenioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDescontoConvenioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(descontoConvenioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DescontoConvenio in the database
        List<DescontoConvenio> descontoConvenioList = descontoConvenioRepository.findAll();
        assertThat(descontoConvenioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDescontoConvenios() throws Exception {
        // Initialize the database
        descontoConvenioRepository.saveAndFlush(descontoConvenio);

        // Get all the descontoConvenioList
        restDescontoConvenioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(descontoConvenio.getId().intValue())))
            .andExpect(jsonPath("$.[*].desconto").value(hasItem(DEFAULT_DESCONTO)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDescontoConveniosWithEagerRelationshipsIsEnabled() throws Exception {
        when(descontoConvenioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDescontoConvenioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(descontoConvenioServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDescontoConveniosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(descontoConvenioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDescontoConvenioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(descontoConvenioRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDescontoConvenio() throws Exception {
        // Initialize the database
        descontoConvenioRepository.saveAndFlush(descontoConvenio);

        // Get the descontoConvenio
        restDescontoConvenioMockMvc
            .perform(get(ENTITY_API_URL_ID, descontoConvenio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(descontoConvenio.getId().intValue()))
            .andExpect(jsonPath("$.desconto").value(DEFAULT_DESCONTO))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    @Transactional
    void getDescontoConveniosByIdFiltering() throws Exception {
        // Initialize the database
        descontoConvenioRepository.saveAndFlush(descontoConvenio);

        Long id = descontoConvenio.getId();

        defaultDescontoConvenioShouldBeFound("id.equals=" + id);
        defaultDescontoConvenioShouldNotBeFound("id.notEquals=" + id);

        defaultDescontoConvenioShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDescontoConvenioShouldNotBeFound("id.greaterThan=" + id);

        defaultDescontoConvenioShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDescontoConvenioShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDescontoConveniosByDescontoIsEqualToSomething() throws Exception {
        // Initialize the database
        descontoConvenioRepository.saveAndFlush(descontoConvenio);

        // Get all the descontoConvenioList where desconto equals to DEFAULT_DESCONTO
        defaultDescontoConvenioShouldBeFound("desconto.equals=" + DEFAULT_DESCONTO);

        // Get all the descontoConvenioList where desconto equals to UPDATED_DESCONTO
        defaultDescontoConvenioShouldNotBeFound("desconto.equals=" + UPDATED_DESCONTO);
    }

    @Test
    @Transactional
    void getAllDescontoConveniosByDescontoIsInShouldWork() throws Exception {
        // Initialize the database
        descontoConvenioRepository.saveAndFlush(descontoConvenio);

        // Get all the descontoConvenioList where desconto in DEFAULT_DESCONTO or UPDATED_DESCONTO
        defaultDescontoConvenioShouldBeFound("desconto.in=" + DEFAULT_DESCONTO + "," + UPDATED_DESCONTO);

        // Get all the descontoConvenioList where desconto equals to UPDATED_DESCONTO
        defaultDescontoConvenioShouldNotBeFound("desconto.in=" + UPDATED_DESCONTO);
    }

    @Test
    @Transactional
    void getAllDescontoConveniosByDescontoIsNullOrNotNull() throws Exception {
        // Initialize the database
        descontoConvenioRepository.saveAndFlush(descontoConvenio);

        // Get all the descontoConvenioList where desconto is not null
        defaultDescontoConvenioShouldBeFound("desconto.specified=true");

        // Get all the descontoConvenioList where desconto is null
        defaultDescontoConvenioShouldNotBeFound("desconto.specified=false");
    }

    @Test
    @Transactional
    void getAllDescontoConveniosByDescontoContainsSomething() throws Exception {
        // Initialize the database
        descontoConvenioRepository.saveAndFlush(descontoConvenio);

        // Get all the descontoConvenioList where desconto contains DEFAULT_DESCONTO
        defaultDescontoConvenioShouldBeFound("desconto.contains=" + DEFAULT_DESCONTO);

        // Get all the descontoConvenioList where desconto contains UPDATED_DESCONTO
        defaultDescontoConvenioShouldNotBeFound("desconto.contains=" + UPDATED_DESCONTO);
    }

    @Test
    @Transactional
    void getAllDescontoConveniosByDescontoNotContainsSomething() throws Exception {
        // Initialize the database
        descontoConvenioRepository.saveAndFlush(descontoConvenio);

        // Get all the descontoConvenioList where desconto does not contain DEFAULT_DESCONTO
        defaultDescontoConvenioShouldNotBeFound("desconto.doesNotContain=" + DEFAULT_DESCONTO);

        // Get all the descontoConvenioList where desconto does not contain UPDATED_DESCONTO
        defaultDescontoConvenioShouldBeFound("desconto.doesNotContain=" + UPDATED_DESCONTO);
    }

    @Test
    @Transactional
    void getAllDescontoConveniosByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        descontoConvenioRepository.saveAndFlush(descontoConvenio);

        // Get all the descontoConvenioList where descricao equals to DEFAULT_DESCRICAO
        defaultDescontoConvenioShouldBeFound("descricao.equals=" + DEFAULT_DESCRICAO);

        // Get all the descontoConvenioList where descricao equals to UPDATED_DESCRICAO
        defaultDescontoConvenioShouldNotBeFound("descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllDescontoConveniosByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        descontoConvenioRepository.saveAndFlush(descontoConvenio);

        // Get all the descontoConvenioList where descricao in DEFAULT_DESCRICAO or UPDATED_DESCRICAO
        defaultDescontoConvenioShouldBeFound("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO);

        // Get all the descontoConvenioList where descricao equals to UPDATED_DESCRICAO
        defaultDescontoConvenioShouldNotBeFound("descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllDescontoConveniosByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        descontoConvenioRepository.saveAndFlush(descontoConvenio);

        // Get all the descontoConvenioList where descricao is not null
        defaultDescontoConvenioShouldBeFound("descricao.specified=true");

        // Get all the descontoConvenioList where descricao is null
        defaultDescontoConvenioShouldNotBeFound("descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllDescontoConveniosByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        descontoConvenioRepository.saveAndFlush(descontoConvenio);

        // Get all the descontoConvenioList where descricao contains DEFAULT_DESCRICAO
        defaultDescontoConvenioShouldBeFound("descricao.contains=" + DEFAULT_DESCRICAO);

        // Get all the descontoConvenioList where descricao contains UPDATED_DESCRICAO
        defaultDescontoConvenioShouldNotBeFound("descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllDescontoConveniosByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        descontoConvenioRepository.saveAndFlush(descontoConvenio);

        // Get all the descontoConvenioList where descricao does not contain DEFAULT_DESCRICAO
        defaultDescontoConvenioShouldNotBeFound("descricao.doesNotContain=" + DEFAULT_DESCRICAO);

        // Get all the descontoConvenioList where descricao does not contain UPDATED_DESCRICAO
        defaultDescontoConvenioShouldBeFound("descricao.doesNotContain=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllDescontoConveniosByConvenioIsEqualToSomething() throws Exception {
        Convenio convenio;
        if (TestUtil.findAll(em, Convenio.class).isEmpty()) {
            descontoConvenioRepository.saveAndFlush(descontoConvenio);
            convenio = ConvenioResourceIT.createEntity(em);
        } else {
            convenio = TestUtil.findAll(em, Convenio.class).get(0);
        }
        em.persist(convenio);
        em.flush();
        descontoConvenio.setConvenio(convenio);
        descontoConvenioRepository.saveAndFlush(descontoConvenio);
        Long convenioId = convenio.getId();

        // Get all the descontoConvenioList where convenio equals to convenioId
        defaultDescontoConvenioShouldBeFound("convenioId.equals=" + convenioId);

        // Get all the descontoConvenioList where convenio equals to (convenioId + 1)
        defaultDescontoConvenioShouldNotBeFound("convenioId.equals=" + (convenioId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDescontoConvenioShouldBeFound(String filter) throws Exception {
        restDescontoConvenioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(descontoConvenio.getId().intValue())))
            .andExpect(jsonPath("$.[*].desconto").value(hasItem(DEFAULT_DESCONTO)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));

        // Check, that the count call also returns 1
        restDescontoConvenioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDescontoConvenioShouldNotBeFound(String filter) throws Exception {
        restDescontoConvenioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDescontoConvenioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDescontoConvenio() throws Exception {
        // Get the descontoConvenio
        restDescontoConvenioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDescontoConvenio() throws Exception {
        // Initialize the database
        descontoConvenioRepository.saveAndFlush(descontoConvenio);

        int databaseSizeBeforeUpdate = descontoConvenioRepository.findAll().size();

        // Update the descontoConvenio
        DescontoConvenio updatedDescontoConvenio = descontoConvenioRepository.findById(descontoConvenio.getId()).get();
        // Disconnect from session so that the updates on updatedDescontoConvenio are not directly saved in db
        em.detach(updatedDescontoConvenio);
        updatedDescontoConvenio.desconto(UPDATED_DESCONTO).descricao(UPDATED_DESCRICAO);
        DescontoConvenioDTO descontoConvenioDTO = descontoConvenioMapper.toDto(updatedDescontoConvenio);

        restDescontoConvenioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, descontoConvenioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(descontoConvenioDTO))
            )
            .andExpect(status().isOk());

        // Validate the DescontoConvenio in the database
        List<DescontoConvenio> descontoConvenioList = descontoConvenioRepository.findAll();
        assertThat(descontoConvenioList).hasSize(databaseSizeBeforeUpdate);
        DescontoConvenio testDescontoConvenio = descontoConvenioList.get(descontoConvenioList.size() - 1);
        assertThat(testDescontoConvenio.getDesconto()).isEqualTo(UPDATED_DESCONTO);
        assertThat(testDescontoConvenio.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void putNonExistingDescontoConvenio() throws Exception {
        int databaseSizeBeforeUpdate = descontoConvenioRepository.findAll().size();
        descontoConvenio.setId(count.incrementAndGet());

        // Create the DescontoConvenio
        DescontoConvenioDTO descontoConvenioDTO = descontoConvenioMapper.toDto(descontoConvenio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDescontoConvenioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, descontoConvenioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(descontoConvenioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DescontoConvenio in the database
        List<DescontoConvenio> descontoConvenioList = descontoConvenioRepository.findAll();
        assertThat(descontoConvenioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDescontoConvenio() throws Exception {
        int databaseSizeBeforeUpdate = descontoConvenioRepository.findAll().size();
        descontoConvenio.setId(count.incrementAndGet());

        // Create the DescontoConvenio
        DescontoConvenioDTO descontoConvenioDTO = descontoConvenioMapper.toDto(descontoConvenio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDescontoConvenioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(descontoConvenioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DescontoConvenio in the database
        List<DescontoConvenio> descontoConvenioList = descontoConvenioRepository.findAll();
        assertThat(descontoConvenioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDescontoConvenio() throws Exception {
        int databaseSizeBeforeUpdate = descontoConvenioRepository.findAll().size();
        descontoConvenio.setId(count.incrementAndGet());

        // Create the DescontoConvenio
        DescontoConvenioDTO descontoConvenioDTO = descontoConvenioMapper.toDto(descontoConvenio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDescontoConvenioMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(descontoConvenioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DescontoConvenio in the database
        List<DescontoConvenio> descontoConvenioList = descontoConvenioRepository.findAll();
        assertThat(descontoConvenioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDescontoConvenioWithPatch() throws Exception {
        // Initialize the database
        descontoConvenioRepository.saveAndFlush(descontoConvenio);

        int databaseSizeBeforeUpdate = descontoConvenioRepository.findAll().size();

        // Update the descontoConvenio using partial update
        DescontoConvenio partialUpdatedDescontoConvenio = new DescontoConvenio();
        partialUpdatedDescontoConvenio.setId(descontoConvenio.getId());

        restDescontoConvenioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDescontoConvenio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDescontoConvenio))
            )
            .andExpect(status().isOk());

        // Validate the DescontoConvenio in the database
        List<DescontoConvenio> descontoConvenioList = descontoConvenioRepository.findAll();
        assertThat(descontoConvenioList).hasSize(databaseSizeBeforeUpdate);
        DescontoConvenio testDescontoConvenio = descontoConvenioList.get(descontoConvenioList.size() - 1);
        assertThat(testDescontoConvenio.getDesconto()).isEqualTo(DEFAULT_DESCONTO);
        assertThat(testDescontoConvenio.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void fullUpdateDescontoConvenioWithPatch() throws Exception {
        // Initialize the database
        descontoConvenioRepository.saveAndFlush(descontoConvenio);

        int databaseSizeBeforeUpdate = descontoConvenioRepository.findAll().size();

        // Update the descontoConvenio using partial update
        DescontoConvenio partialUpdatedDescontoConvenio = new DescontoConvenio();
        partialUpdatedDescontoConvenio.setId(descontoConvenio.getId());

        partialUpdatedDescontoConvenio.desconto(UPDATED_DESCONTO).descricao(UPDATED_DESCRICAO);

        restDescontoConvenioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDescontoConvenio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDescontoConvenio))
            )
            .andExpect(status().isOk());

        // Validate the DescontoConvenio in the database
        List<DescontoConvenio> descontoConvenioList = descontoConvenioRepository.findAll();
        assertThat(descontoConvenioList).hasSize(databaseSizeBeforeUpdate);
        DescontoConvenio testDescontoConvenio = descontoConvenioList.get(descontoConvenioList.size() - 1);
        assertThat(testDescontoConvenio.getDesconto()).isEqualTo(UPDATED_DESCONTO);
        assertThat(testDescontoConvenio.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void patchNonExistingDescontoConvenio() throws Exception {
        int databaseSizeBeforeUpdate = descontoConvenioRepository.findAll().size();
        descontoConvenio.setId(count.incrementAndGet());

        // Create the DescontoConvenio
        DescontoConvenioDTO descontoConvenioDTO = descontoConvenioMapper.toDto(descontoConvenio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDescontoConvenioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, descontoConvenioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(descontoConvenioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DescontoConvenio in the database
        List<DescontoConvenio> descontoConvenioList = descontoConvenioRepository.findAll();
        assertThat(descontoConvenioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDescontoConvenio() throws Exception {
        int databaseSizeBeforeUpdate = descontoConvenioRepository.findAll().size();
        descontoConvenio.setId(count.incrementAndGet());

        // Create the DescontoConvenio
        DescontoConvenioDTO descontoConvenioDTO = descontoConvenioMapper.toDto(descontoConvenio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDescontoConvenioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(descontoConvenioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DescontoConvenio in the database
        List<DescontoConvenio> descontoConvenioList = descontoConvenioRepository.findAll();
        assertThat(descontoConvenioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDescontoConvenio() throws Exception {
        int databaseSizeBeforeUpdate = descontoConvenioRepository.findAll().size();
        descontoConvenio.setId(count.incrementAndGet());

        // Create the DescontoConvenio
        DescontoConvenioDTO descontoConvenioDTO = descontoConvenioMapper.toDto(descontoConvenio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDescontoConvenioMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(descontoConvenioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DescontoConvenio in the database
        List<DescontoConvenio> descontoConvenioList = descontoConvenioRepository.findAll();
        assertThat(descontoConvenioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDescontoConvenio() throws Exception {
        // Initialize the database
        descontoConvenioRepository.saveAndFlush(descontoConvenio);

        int databaseSizeBeforeDelete = descontoConvenioRepository.findAll().size();

        // Delete the descontoConvenio
        restDescontoConvenioMockMvc
            .perform(delete(ENTITY_API_URL_ID, descontoConvenio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DescontoConvenio> descontoConvenioList = descontoConvenioRepository.findAll();
        assertThat(descontoConvenioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
