package com.aapm.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aapm.app.IntegrationTest;
import com.aapm.app.domain.Mensagem;
import com.aapm.app.domain.Tipo;
import com.aapm.app.repository.TipoRepository;
import com.aapm.app.service.criteria.TipoCriteria;
import com.aapm.app.service.dto.TipoDTO;
import com.aapm.app.service.mapper.TipoMapper;
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
 * Integration tests for the {@link TipoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TipoResourceIT {

    private static final String DEFAULT_TIPO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO = "BBBBBBBBBB";

    private static final String DEFAULT_CHAVE = "AAAAAAAAAA";
    private static final String UPDATED_CHAVE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tipos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TipoRepository tipoRepository;

    @Autowired
    private TipoMapper tipoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoMockMvc;

    private Tipo tipo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tipo createEntity(EntityManager em) {
        Tipo tipo = new Tipo().tipo(DEFAULT_TIPO).chave(DEFAULT_CHAVE);
        return tipo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tipo createUpdatedEntity(EntityManager em) {
        Tipo tipo = new Tipo().tipo(UPDATED_TIPO).chave(UPDATED_CHAVE);
        return tipo;
    }

    @BeforeEach
    public void initTest() {
        tipo = createEntity(em);
    }

    @Test
    @Transactional
    void createTipo() throws Exception {
        int databaseSizeBeforeCreate = tipoRepository.findAll().size();
        // Create the Tipo
        TipoDTO tipoDTO = tipoMapper.toDto(tipo);
        restTipoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoDTO)))
            .andExpect(status().isCreated());

        // Validate the Tipo in the database
        List<Tipo> tipoList = tipoRepository.findAll();
        assertThat(tipoList).hasSize(databaseSizeBeforeCreate + 1);
        Tipo testTipo = tipoList.get(tipoList.size() - 1);
        assertThat(testTipo.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testTipo.getChave()).isEqualTo(DEFAULT_CHAVE);
    }

    @Test
    @Transactional
    void createTipoWithExistingId() throws Exception {
        // Create the Tipo with an existing ID
        tipo.setId(1L);
        TipoDTO tipoDTO = tipoMapper.toDto(tipo);

        int databaseSizeBeforeCreate = tipoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tipo in the database
        List<Tipo> tipoList = tipoRepository.findAll();
        assertThat(tipoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoRepository.findAll().size();
        // set the field null
        tipo.setTipo(null);

        // Create the Tipo, which fails.
        TipoDTO tipoDTO = tipoMapper.toDto(tipo);

        restTipoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoDTO)))
            .andExpect(status().isBadRequest());

        List<Tipo> tipoList = tipoRepository.findAll();
        assertThat(tipoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkChaveIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoRepository.findAll().size();
        // set the field null
        tipo.setChave(null);

        // Create the Tipo, which fails.
        TipoDTO tipoDTO = tipoMapper.toDto(tipo);

        restTipoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoDTO)))
            .andExpect(status().isBadRequest());

        List<Tipo> tipoList = tipoRepository.findAll();
        assertThat(tipoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTipos() throws Exception {
        // Initialize the database
        tipoRepository.saveAndFlush(tipo);

        // Get all the tipoList
        restTipoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipo.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].chave").value(hasItem(DEFAULT_CHAVE)));
    }

    @Test
    @Transactional
    void getTipo() throws Exception {
        // Initialize the database
        tipoRepository.saveAndFlush(tipo);

        // Get the tipo
        restTipoMockMvc
            .perform(get(ENTITY_API_URL_ID, tipo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipo.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO))
            .andExpect(jsonPath("$.chave").value(DEFAULT_CHAVE));
    }

    @Test
    @Transactional
    void getTiposByIdFiltering() throws Exception {
        // Initialize the database
        tipoRepository.saveAndFlush(tipo);

        Long id = tipo.getId();

        defaultTipoShouldBeFound("id.equals=" + id);
        defaultTipoShouldNotBeFound("id.notEquals=" + id);

        defaultTipoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTipoShouldNotBeFound("id.greaterThan=" + id);

        defaultTipoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTipoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTiposByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        tipoRepository.saveAndFlush(tipo);

        // Get all the tipoList where tipo equals to DEFAULT_TIPO
        defaultTipoShouldBeFound("tipo.equals=" + DEFAULT_TIPO);

        // Get all the tipoList where tipo equals to UPDATED_TIPO
        defaultTipoShouldNotBeFound("tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllTiposByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        tipoRepository.saveAndFlush(tipo);

        // Get all the tipoList where tipo in DEFAULT_TIPO or UPDATED_TIPO
        defaultTipoShouldBeFound("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO);

        // Get all the tipoList where tipo equals to UPDATED_TIPO
        defaultTipoShouldNotBeFound("tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllTiposByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        tipoRepository.saveAndFlush(tipo);

        // Get all the tipoList where tipo is not null
        defaultTipoShouldBeFound("tipo.specified=true");

        // Get all the tipoList where tipo is null
        defaultTipoShouldNotBeFound("tipo.specified=false");
    }

    @Test
    @Transactional
    void getAllTiposByTipoContainsSomething() throws Exception {
        // Initialize the database
        tipoRepository.saveAndFlush(tipo);

        // Get all the tipoList where tipo contains DEFAULT_TIPO
        defaultTipoShouldBeFound("tipo.contains=" + DEFAULT_TIPO);

        // Get all the tipoList where tipo contains UPDATED_TIPO
        defaultTipoShouldNotBeFound("tipo.contains=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllTiposByTipoNotContainsSomething() throws Exception {
        // Initialize the database
        tipoRepository.saveAndFlush(tipo);

        // Get all the tipoList where tipo does not contain DEFAULT_TIPO
        defaultTipoShouldNotBeFound("tipo.doesNotContain=" + DEFAULT_TIPO);

        // Get all the tipoList where tipo does not contain UPDATED_TIPO
        defaultTipoShouldBeFound("tipo.doesNotContain=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllTiposByChaveIsEqualToSomething() throws Exception {
        // Initialize the database
        tipoRepository.saveAndFlush(tipo);

        // Get all the tipoList where chave equals to DEFAULT_CHAVE
        defaultTipoShouldBeFound("chave.equals=" + DEFAULT_CHAVE);

        // Get all the tipoList where chave equals to UPDATED_CHAVE
        defaultTipoShouldNotBeFound("chave.equals=" + UPDATED_CHAVE);
    }

    @Test
    @Transactional
    void getAllTiposByChaveIsInShouldWork() throws Exception {
        // Initialize the database
        tipoRepository.saveAndFlush(tipo);

        // Get all the tipoList where chave in DEFAULT_CHAVE or UPDATED_CHAVE
        defaultTipoShouldBeFound("chave.in=" + DEFAULT_CHAVE + "," + UPDATED_CHAVE);

        // Get all the tipoList where chave equals to UPDATED_CHAVE
        defaultTipoShouldNotBeFound("chave.in=" + UPDATED_CHAVE);
    }

    @Test
    @Transactional
    void getAllTiposByChaveIsNullOrNotNull() throws Exception {
        // Initialize the database
        tipoRepository.saveAndFlush(tipo);

        // Get all the tipoList where chave is not null
        defaultTipoShouldBeFound("chave.specified=true");

        // Get all the tipoList where chave is null
        defaultTipoShouldNotBeFound("chave.specified=false");
    }

    @Test
    @Transactional
    void getAllTiposByChaveContainsSomething() throws Exception {
        // Initialize the database
        tipoRepository.saveAndFlush(tipo);

        // Get all the tipoList where chave contains DEFAULT_CHAVE
        defaultTipoShouldBeFound("chave.contains=" + DEFAULT_CHAVE);

        // Get all the tipoList where chave contains UPDATED_CHAVE
        defaultTipoShouldNotBeFound("chave.contains=" + UPDATED_CHAVE);
    }

    @Test
    @Transactional
    void getAllTiposByChaveNotContainsSomething() throws Exception {
        // Initialize the database
        tipoRepository.saveAndFlush(tipo);

        // Get all the tipoList where chave does not contain DEFAULT_CHAVE
        defaultTipoShouldNotBeFound("chave.doesNotContain=" + DEFAULT_CHAVE);

        // Get all the tipoList where chave does not contain UPDATED_CHAVE
        defaultTipoShouldBeFound("chave.doesNotContain=" + UPDATED_CHAVE);
    }

    @Test
    @Transactional
    void getAllTiposByMensagemIsEqualToSomething() throws Exception {
        Mensagem mensagem;
        if (TestUtil.findAll(em, Mensagem.class).isEmpty()) {
            tipoRepository.saveAndFlush(tipo);
            mensagem = MensagemResourceIT.createEntity(em);
        } else {
            mensagem = TestUtil.findAll(em, Mensagem.class).get(0);
        }
        em.persist(mensagem);
        em.flush();
        tipo.addMensagem(mensagem);
        tipoRepository.saveAndFlush(tipo);
        Long mensagemId = mensagem.getId();

        // Get all the tipoList where mensagem equals to mensagemId
        defaultTipoShouldBeFound("mensagemId.equals=" + mensagemId);

        // Get all the tipoList where mensagem equals to (mensagemId + 1)
        defaultTipoShouldNotBeFound("mensagemId.equals=" + (mensagemId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTipoShouldBeFound(String filter) throws Exception {
        restTipoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipo.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].chave").value(hasItem(DEFAULT_CHAVE)));

        // Check, that the count call also returns 1
        restTipoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTipoShouldNotBeFound(String filter) throws Exception {
        restTipoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTipoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTipo() throws Exception {
        // Get the tipo
        restTipoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTipo() throws Exception {
        // Initialize the database
        tipoRepository.saveAndFlush(tipo);

        int databaseSizeBeforeUpdate = tipoRepository.findAll().size();

        // Update the tipo
        Tipo updatedTipo = tipoRepository.findById(tipo.getId()).get();
        // Disconnect from session so that the updates on updatedTipo are not directly saved in db
        em.detach(updatedTipo);
        updatedTipo.tipo(UPDATED_TIPO).chave(UPDATED_CHAVE);
        TipoDTO tipoDTO = tipoMapper.toDto(updatedTipo);

        restTipoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Tipo in the database
        List<Tipo> tipoList = tipoRepository.findAll();
        assertThat(tipoList).hasSize(databaseSizeBeforeUpdate);
        Tipo testTipo = tipoList.get(tipoList.size() - 1);
        assertThat(testTipo.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testTipo.getChave()).isEqualTo(UPDATED_CHAVE);
    }

    @Test
    @Transactional
    void putNonExistingTipo() throws Exception {
        int databaseSizeBeforeUpdate = tipoRepository.findAll().size();
        tipo.setId(count.incrementAndGet());

        // Create the Tipo
        TipoDTO tipoDTO = tipoMapper.toDto(tipo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tipo in the database
        List<Tipo> tipoList = tipoRepository.findAll();
        assertThat(tipoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipo() throws Exception {
        int databaseSizeBeforeUpdate = tipoRepository.findAll().size();
        tipo.setId(count.incrementAndGet());

        // Create the Tipo
        TipoDTO tipoDTO = tipoMapper.toDto(tipo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tipo in the database
        List<Tipo> tipoList = tipoRepository.findAll();
        assertThat(tipoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipo() throws Exception {
        int databaseSizeBeforeUpdate = tipoRepository.findAll().size();
        tipo.setId(count.incrementAndGet());

        // Create the Tipo
        TipoDTO tipoDTO = tipoMapper.toDto(tipo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tipo in the database
        List<Tipo> tipoList = tipoRepository.findAll();
        assertThat(tipoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTipoWithPatch() throws Exception {
        // Initialize the database
        tipoRepository.saveAndFlush(tipo);

        int databaseSizeBeforeUpdate = tipoRepository.findAll().size();

        // Update the tipo using partial update
        Tipo partialUpdatedTipo = new Tipo();
        partialUpdatedTipo.setId(tipo.getId());

        partialUpdatedTipo.chave(UPDATED_CHAVE);

        restTipoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipo))
            )
            .andExpect(status().isOk());

        // Validate the Tipo in the database
        List<Tipo> tipoList = tipoRepository.findAll();
        assertThat(tipoList).hasSize(databaseSizeBeforeUpdate);
        Tipo testTipo = tipoList.get(tipoList.size() - 1);
        assertThat(testTipo.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testTipo.getChave()).isEqualTo(UPDATED_CHAVE);
    }

    @Test
    @Transactional
    void fullUpdateTipoWithPatch() throws Exception {
        // Initialize the database
        tipoRepository.saveAndFlush(tipo);

        int databaseSizeBeforeUpdate = tipoRepository.findAll().size();

        // Update the tipo using partial update
        Tipo partialUpdatedTipo = new Tipo();
        partialUpdatedTipo.setId(tipo.getId());

        partialUpdatedTipo.tipo(UPDATED_TIPO).chave(UPDATED_CHAVE);

        restTipoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipo))
            )
            .andExpect(status().isOk());

        // Validate the Tipo in the database
        List<Tipo> tipoList = tipoRepository.findAll();
        assertThat(tipoList).hasSize(databaseSizeBeforeUpdate);
        Tipo testTipo = tipoList.get(tipoList.size() - 1);
        assertThat(testTipo.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testTipo.getChave()).isEqualTo(UPDATED_CHAVE);
    }

    @Test
    @Transactional
    void patchNonExistingTipo() throws Exception {
        int databaseSizeBeforeUpdate = tipoRepository.findAll().size();
        tipo.setId(count.incrementAndGet());

        // Create the Tipo
        TipoDTO tipoDTO = tipoMapper.toDto(tipo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tipo in the database
        List<Tipo> tipoList = tipoRepository.findAll();
        assertThat(tipoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipo() throws Exception {
        int databaseSizeBeforeUpdate = tipoRepository.findAll().size();
        tipo.setId(count.incrementAndGet());

        // Create the Tipo
        TipoDTO tipoDTO = tipoMapper.toDto(tipo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tipo in the database
        List<Tipo> tipoList = tipoRepository.findAll();
        assertThat(tipoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipo() throws Exception {
        int databaseSizeBeforeUpdate = tipoRepository.findAll().size();
        tipo.setId(count.incrementAndGet());

        // Create the Tipo
        TipoDTO tipoDTO = tipoMapper.toDto(tipo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tipoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tipo in the database
        List<Tipo> tipoList = tipoRepository.findAll();
        assertThat(tipoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTipo() throws Exception {
        // Initialize the database
        tipoRepository.saveAndFlush(tipo);

        int databaseSizeBeforeDelete = tipoRepository.findAll().size();

        // Delete the tipo
        restTipoMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tipo> tipoList = tipoRepository.findAll();
        assertThat(tipoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
