package com.aapm.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aapm.app.IntegrationTest;
import com.aapm.app.domain.IconsRedesSociais;
import com.aapm.app.domain.RedesSociaisConvenio;
import com.aapm.app.repository.IconsRedesSociaisRepository;
import com.aapm.app.service.criteria.IconsRedesSociaisCriteria;
import com.aapm.app.service.dto.IconsRedesSociaisDTO;
import com.aapm.app.service.mapper.IconsRedesSociaisMapper;
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
 * Integration tests for the {@link IconsRedesSociaisResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IconsRedesSociaisResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/icons-redes-sociais";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IconsRedesSociaisRepository iconsRedesSociaisRepository;

    @Autowired
    private IconsRedesSociaisMapper iconsRedesSociaisMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIconsRedesSociaisMockMvc;

    private IconsRedesSociais iconsRedesSociais;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IconsRedesSociais createEntity(EntityManager em) {
        IconsRedesSociais iconsRedesSociais = new IconsRedesSociais()
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO)
            .icon(DEFAULT_ICON)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return iconsRedesSociais;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IconsRedesSociais createUpdatedEntity(EntityManager em) {
        IconsRedesSociais iconsRedesSociais = new IconsRedesSociais()
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .icon(UPDATED_ICON)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        return iconsRedesSociais;
    }

    @BeforeEach
    public void initTest() {
        iconsRedesSociais = createEntity(em);
    }

    @Test
    @Transactional
    void createIconsRedesSociais() throws Exception {
        int databaseSizeBeforeCreate = iconsRedesSociaisRepository.findAll().size();
        // Create the IconsRedesSociais
        IconsRedesSociaisDTO iconsRedesSociaisDTO = iconsRedesSociaisMapper.toDto(iconsRedesSociais);
        restIconsRedesSociaisMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(iconsRedesSociaisDTO))
            )
            .andExpect(status().isCreated());

        // Validate the IconsRedesSociais in the database
        List<IconsRedesSociais> iconsRedesSociaisList = iconsRedesSociaisRepository.findAll();
        assertThat(iconsRedesSociaisList).hasSize(databaseSizeBeforeCreate + 1);
        IconsRedesSociais testIconsRedesSociais = iconsRedesSociaisList.get(iconsRedesSociaisList.size() - 1);
        assertThat(testIconsRedesSociais.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testIconsRedesSociais.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testIconsRedesSociais.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testIconsRedesSociais.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testIconsRedesSociais.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createIconsRedesSociaisWithExistingId() throws Exception {
        // Create the IconsRedesSociais with an existing ID
        iconsRedesSociais.setId(1L);
        IconsRedesSociaisDTO iconsRedesSociaisDTO = iconsRedesSociaisMapper.toDto(iconsRedesSociais);

        int databaseSizeBeforeCreate = iconsRedesSociaisRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIconsRedesSociaisMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(iconsRedesSociaisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IconsRedesSociais in the database
        List<IconsRedesSociais> iconsRedesSociaisList = iconsRedesSociaisRepository.findAll();
        assertThat(iconsRedesSociaisList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = iconsRedesSociaisRepository.findAll().size();
        // set the field null
        iconsRedesSociais.setNome(null);

        // Create the IconsRedesSociais, which fails.
        IconsRedesSociaisDTO iconsRedesSociaisDTO = iconsRedesSociaisMapper.toDto(iconsRedesSociais);

        restIconsRedesSociaisMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(iconsRedesSociaisDTO))
            )
            .andExpect(status().isBadRequest());

        List<IconsRedesSociais> iconsRedesSociaisList = iconsRedesSociaisRepository.findAll();
        assertThat(iconsRedesSociaisList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIconsRedesSociais() throws Exception {
        // Initialize the database
        iconsRedesSociaisRepository.saveAndFlush(iconsRedesSociais);

        // Get all the iconsRedesSociaisList
        restIconsRedesSociaisMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(iconsRedesSociais.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    void getIconsRedesSociais() throws Exception {
        // Initialize the database
        iconsRedesSociaisRepository.saveAndFlush(iconsRedesSociais);

        // Get the iconsRedesSociais
        restIconsRedesSociaisMockMvc
            .perform(get(ENTITY_API_URL_ID, iconsRedesSociais.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(iconsRedesSociais.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getIconsRedesSociaisByIdFiltering() throws Exception {
        // Initialize the database
        iconsRedesSociaisRepository.saveAndFlush(iconsRedesSociais);

        Long id = iconsRedesSociais.getId();

        defaultIconsRedesSociaisShouldBeFound("id.equals=" + id);
        defaultIconsRedesSociaisShouldNotBeFound("id.notEquals=" + id);

        defaultIconsRedesSociaisShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultIconsRedesSociaisShouldNotBeFound("id.greaterThan=" + id);

        defaultIconsRedesSociaisShouldBeFound("id.lessThanOrEqual=" + id);
        defaultIconsRedesSociaisShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIconsRedesSociaisByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        iconsRedesSociaisRepository.saveAndFlush(iconsRedesSociais);

        // Get all the iconsRedesSociaisList where nome equals to DEFAULT_NOME
        defaultIconsRedesSociaisShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the iconsRedesSociaisList where nome equals to UPDATED_NOME
        defaultIconsRedesSociaisShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllIconsRedesSociaisByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        iconsRedesSociaisRepository.saveAndFlush(iconsRedesSociais);

        // Get all the iconsRedesSociaisList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultIconsRedesSociaisShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the iconsRedesSociaisList where nome equals to UPDATED_NOME
        defaultIconsRedesSociaisShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllIconsRedesSociaisByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        iconsRedesSociaisRepository.saveAndFlush(iconsRedesSociais);

        // Get all the iconsRedesSociaisList where nome is not null
        defaultIconsRedesSociaisShouldBeFound("nome.specified=true");

        // Get all the iconsRedesSociaisList where nome is null
        defaultIconsRedesSociaisShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllIconsRedesSociaisByNomeContainsSomething() throws Exception {
        // Initialize the database
        iconsRedesSociaisRepository.saveAndFlush(iconsRedesSociais);

        // Get all the iconsRedesSociaisList where nome contains DEFAULT_NOME
        defaultIconsRedesSociaisShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the iconsRedesSociaisList where nome contains UPDATED_NOME
        defaultIconsRedesSociaisShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllIconsRedesSociaisByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        iconsRedesSociaisRepository.saveAndFlush(iconsRedesSociais);

        // Get all the iconsRedesSociaisList where nome does not contain DEFAULT_NOME
        defaultIconsRedesSociaisShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the iconsRedesSociaisList where nome does not contain UPDATED_NOME
        defaultIconsRedesSociaisShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllIconsRedesSociaisByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        iconsRedesSociaisRepository.saveAndFlush(iconsRedesSociais);

        // Get all the iconsRedesSociaisList where descricao equals to DEFAULT_DESCRICAO
        defaultIconsRedesSociaisShouldBeFound("descricao.equals=" + DEFAULT_DESCRICAO);

        // Get all the iconsRedesSociaisList where descricao equals to UPDATED_DESCRICAO
        defaultIconsRedesSociaisShouldNotBeFound("descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllIconsRedesSociaisByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        iconsRedesSociaisRepository.saveAndFlush(iconsRedesSociais);

        // Get all the iconsRedesSociaisList where descricao in DEFAULT_DESCRICAO or UPDATED_DESCRICAO
        defaultIconsRedesSociaisShouldBeFound("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO);

        // Get all the iconsRedesSociaisList where descricao equals to UPDATED_DESCRICAO
        defaultIconsRedesSociaisShouldNotBeFound("descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllIconsRedesSociaisByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        iconsRedesSociaisRepository.saveAndFlush(iconsRedesSociais);

        // Get all the iconsRedesSociaisList where descricao is not null
        defaultIconsRedesSociaisShouldBeFound("descricao.specified=true");

        // Get all the iconsRedesSociaisList where descricao is null
        defaultIconsRedesSociaisShouldNotBeFound("descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllIconsRedesSociaisByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        iconsRedesSociaisRepository.saveAndFlush(iconsRedesSociais);

        // Get all the iconsRedesSociaisList where descricao contains DEFAULT_DESCRICAO
        defaultIconsRedesSociaisShouldBeFound("descricao.contains=" + DEFAULT_DESCRICAO);

        // Get all the iconsRedesSociaisList where descricao contains UPDATED_DESCRICAO
        defaultIconsRedesSociaisShouldNotBeFound("descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllIconsRedesSociaisByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        iconsRedesSociaisRepository.saveAndFlush(iconsRedesSociais);

        // Get all the iconsRedesSociaisList where descricao does not contain DEFAULT_DESCRICAO
        defaultIconsRedesSociaisShouldNotBeFound("descricao.doesNotContain=" + DEFAULT_DESCRICAO);

        // Get all the iconsRedesSociaisList where descricao does not contain UPDATED_DESCRICAO
        defaultIconsRedesSociaisShouldBeFound("descricao.doesNotContain=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllIconsRedesSociaisByIconIsEqualToSomething() throws Exception {
        // Initialize the database
        iconsRedesSociaisRepository.saveAndFlush(iconsRedesSociais);

        // Get all the iconsRedesSociaisList where icon equals to DEFAULT_ICON
        defaultIconsRedesSociaisShouldBeFound("icon.equals=" + DEFAULT_ICON);

        // Get all the iconsRedesSociaisList where icon equals to UPDATED_ICON
        defaultIconsRedesSociaisShouldNotBeFound("icon.equals=" + UPDATED_ICON);
    }

    @Test
    @Transactional
    void getAllIconsRedesSociaisByIconIsInShouldWork() throws Exception {
        // Initialize the database
        iconsRedesSociaisRepository.saveAndFlush(iconsRedesSociais);

        // Get all the iconsRedesSociaisList where icon in DEFAULT_ICON or UPDATED_ICON
        defaultIconsRedesSociaisShouldBeFound("icon.in=" + DEFAULT_ICON + "," + UPDATED_ICON);

        // Get all the iconsRedesSociaisList where icon equals to UPDATED_ICON
        defaultIconsRedesSociaisShouldNotBeFound("icon.in=" + UPDATED_ICON);
    }

    @Test
    @Transactional
    void getAllIconsRedesSociaisByIconIsNullOrNotNull() throws Exception {
        // Initialize the database
        iconsRedesSociaisRepository.saveAndFlush(iconsRedesSociais);

        // Get all the iconsRedesSociaisList where icon is not null
        defaultIconsRedesSociaisShouldBeFound("icon.specified=true");

        // Get all the iconsRedesSociaisList where icon is null
        defaultIconsRedesSociaisShouldNotBeFound("icon.specified=false");
    }

    @Test
    @Transactional
    void getAllIconsRedesSociaisByIconContainsSomething() throws Exception {
        // Initialize the database
        iconsRedesSociaisRepository.saveAndFlush(iconsRedesSociais);

        // Get all the iconsRedesSociaisList where icon contains DEFAULT_ICON
        defaultIconsRedesSociaisShouldBeFound("icon.contains=" + DEFAULT_ICON);

        // Get all the iconsRedesSociaisList where icon contains UPDATED_ICON
        defaultIconsRedesSociaisShouldNotBeFound("icon.contains=" + UPDATED_ICON);
    }

    @Test
    @Transactional
    void getAllIconsRedesSociaisByIconNotContainsSomething() throws Exception {
        // Initialize the database
        iconsRedesSociaisRepository.saveAndFlush(iconsRedesSociais);

        // Get all the iconsRedesSociaisList where icon does not contain DEFAULT_ICON
        defaultIconsRedesSociaisShouldNotBeFound("icon.doesNotContain=" + DEFAULT_ICON);

        // Get all the iconsRedesSociaisList where icon does not contain UPDATED_ICON
        defaultIconsRedesSociaisShouldBeFound("icon.doesNotContain=" + UPDATED_ICON);
    }

    @Test
    @Transactional
    void getAllIconsRedesSociaisByRedeSocialIsEqualToSomething() throws Exception {
        RedesSociaisConvenio redeSocial;
        if (TestUtil.findAll(em, RedesSociaisConvenio.class).isEmpty()) {
            iconsRedesSociaisRepository.saveAndFlush(iconsRedesSociais);
            redeSocial = RedesSociaisConvenioResourceIT.createEntity(em);
        } else {
            redeSocial = TestUtil.findAll(em, RedesSociaisConvenio.class).get(0);
        }
        em.persist(redeSocial);
        em.flush();
        iconsRedesSociais.addRedeSocial(redeSocial);
        iconsRedesSociaisRepository.saveAndFlush(iconsRedesSociais);
        Long redeSocialId = redeSocial.getId();

        // Get all the iconsRedesSociaisList where redeSocial equals to redeSocialId
        defaultIconsRedesSociaisShouldBeFound("redeSocialId.equals=" + redeSocialId);

        // Get all the iconsRedesSociaisList where redeSocial equals to (redeSocialId + 1)
        defaultIconsRedesSociaisShouldNotBeFound("redeSocialId.equals=" + (redeSocialId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIconsRedesSociaisShouldBeFound(String filter) throws Exception {
        restIconsRedesSociaisMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(iconsRedesSociais.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));

        // Check, that the count call also returns 1
        restIconsRedesSociaisMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIconsRedesSociaisShouldNotBeFound(String filter) throws Exception {
        restIconsRedesSociaisMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIconsRedesSociaisMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIconsRedesSociais() throws Exception {
        // Get the iconsRedesSociais
        restIconsRedesSociaisMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIconsRedesSociais() throws Exception {
        // Initialize the database
        iconsRedesSociaisRepository.saveAndFlush(iconsRedesSociais);

        int databaseSizeBeforeUpdate = iconsRedesSociaisRepository.findAll().size();

        // Update the iconsRedesSociais
        IconsRedesSociais updatedIconsRedesSociais = iconsRedesSociaisRepository.findById(iconsRedesSociais.getId()).get();
        // Disconnect from session so that the updates on updatedIconsRedesSociais are not directly saved in db
        em.detach(updatedIconsRedesSociais);
        updatedIconsRedesSociais
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .icon(UPDATED_ICON)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        IconsRedesSociaisDTO iconsRedesSociaisDTO = iconsRedesSociaisMapper.toDto(updatedIconsRedesSociais);

        restIconsRedesSociaisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, iconsRedesSociaisDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(iconsRedesSociaisDTO))
            )
            .andExpect(status().isOk());

        // Validate the IconsRedesSociais in the database
        List<IconsRedesSociais> iconsRedesSociaisList = iconsRedesSociaisRepository.findAll();
        assertThat(iconsRedesSociaisList).hasSize(databaseSizeBeforeUpdate);
        IconsRedesSociais testIconsRedesSociais = iconsRedesSociaisList.get(iconsRedesSociaisList.size() - 1);
        assertThat(testIconsRedesSociais.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testIconsRedesSociais.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testIconsRedesSociais.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testIconsRedesSociais.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testIconsRedesSociais.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingIconsRedesSociais() throws Exception {
        int databaseSizeBeforeUpdate = iconsRedesSociaisRepository.findAll().size();
        iconsRedesSociais.setId(count.incrementAndGet());

        // Create the IconsRedesSociais
        IconsRedesSociaisDTO iconsRedesSociaisDTO = iconsRedesSociaisMapper.toDto(iconsRedesSociais);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIconsRedesSociaisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, iconsRedesSociaisDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(iconsRedesSociaisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IconsRedesSociais in the database
        List<IconsRedesSociais> iconsRedesSociaisList = iconsRedesSociaisRepository.findAll();
        assertThat(iconsRedesSociaisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIconsRedesSociais() throws Exception {
        int databaseSizeBeforeUpdate = iconsRedesSociaisRepository.findAll().size();
        iconsRedesSociais.setId(count.incrementAndGet());

        // Create the IconsRedesSociais
        IconsRedesSociaisDTO iconsRedesSociaisDTO = iconsRedesSociaisMapper.toDto(iconsRedesSociais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIconsRedesSociaisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(iconsRedesSociaisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IconsRedesSociais in the database
        List<IconsRedesSociais> iconsRedesSociaisList = iconsRedesSociaisRepository.findAll();
        assertThat(iconsRedesSociaisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIconsRedesSociais() throws Exception {
        int databaseSizeBeforeUpdate = iconsRedesSociaisRepository.findAll().size();
        iconsRedesSociais.setId(count.incrementAndGet());

        // Create the IconsRedesSociais
        IconsRedesSociaisDTO iconsRedesSociaisDTO = iconsRedesSociaisMapper.toDto(iconsRedesSociais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIconsRedesSociaisMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(iconsRedesSociaisDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IconsRedesSociais in the database
        List<IconsRedesSociais> iconsRedesSociaisList = iconsRedesSociaisRepository.findAll();
        assertThat(iconsRedesSociaisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIconsRedesSociaisWithPatch() throws Exception {
        // Initialize the database
        iconsRedesSociaisRepository.saveAndFlush(iconsRedesSociais);

        int databaseSizeBeforeUpdate = iconsRedesSociaisRepository.findAll().size();

        // Update the iconsRedesSociais using partial update
        IconsRedesSociais partialUpdatedIconsRedesSociais = new IconsRedesSociais();
        partialUpdatedIconsRedesSociais.setId(iconsRedesSociais.getId());

        partialUpdatedIconsRedesSociais.descricao(UPDATED_DESCRICAO).image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restIconsRedesSociaisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIconsRedesSociais.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIconsRedesSociais))
            )
            .andExpect(status().isOk());

        // Validate the IconsRedesSociais in the database
        List<IconsRedesSociais> iconsRedesSociaisList = iconsRedesSociaisRepository.findAll();
        assertThat(iconsRedesSociaisList).hasSize(databaseSizeBeforeUpdate);
        IconsRedesSociais testIconsRedesSociais = iconsRedesSociaisList.get(iconsRedesSociaisList.size() - 1);
        assertThat(testIconsRedesSociais.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testIconsRedesSociais.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testIconsRedesSociais.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testIconsRedesSociais.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testIconsRedesSociais.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateIconsRedesSociaisWithPatch() throws Exception {
        // Initialize the database
        iconsRedesSociaisRepository.saveAndFlush(iconsRedesSociais);

        int databaseSizeBeforeUpdate = iconsRedesSociaisRepository.findAll().size();

        // Update the iconsRedesSociais using partial update
        IconsRedesSociais partialUpdatedIconsRedesSociais = new IconsRedesSociais();
        partialUpdatedIconsRedesSociais.setId(iconsRedesSociais.getId());

        partialUpdatedIconsRedesSociais
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .icon(UPDATED_ICON)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restIconsRedesSociaisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIconsRedesSociais.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIconsRedesSociais))
            )
            .andExpect(status().isOk());

        // Validate the IconsRedesSociais in the database
        List<IconsRedesSociais> iconsRedesSociaisList = iconsRedesSociaisRepository.findAll();
        assertThat(iconsRedesSociaisList).hasSize(databaseSizeBeforeUpdate);
        IconsRedesSociais testIconsRedesSociais = iconsRedesSociaisList.get(iconsRedesSociaisList.size() - 1);
        assertThat(testIconsRedesSociais.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testIconsRedesSociais.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testIconsRedesSociais.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testIconsRedesSociais.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testIconsRedesSociais.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingIconsRedesSociais() throws Exception {
        int databaseSizeBeforeUpdate = iconsRedesSociaisRepository.findAll().size();
        iconsRedesSociais.setId(count.incrementAndGet());

        // Create the IconsRedesSociais
        IconsRedesSociaisDTO iconsRedesSociaisDTO = iconsRedesSociaisMapper.toDto(iconsRedesSociais);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIconsRedesSociaisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, iconsRedesSociaisDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(iconsRedesSociaisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IconsRedesSociais in the database
        List<IconsRedesSociais> iconsRedesSociaisList = iconsRedesSociaisRepository.findAll();
        assertThat(iconsRedesSociaisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIconsRedesSociais() throws Exception {
        int databaseSizeBeforeUpdate = iconsRedesSociaisRepository.findAll().size();
        iconsRedesSociais.setId(count.incrementAndGet());

        // Create the IconsRedesSociais
        IconsRedesSociaisDTO iconsRedesSociaisDTO = iconsRedesSociaisMapper.toDto(iconsRedesSociais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIconsRedesSociaisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(iconsRedesSociaisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IconsRedesSociais in the database
        List<IconsRedesSociais> iconsRedesSociaisList = iconsRedesSociaisRepository.findAll();
        assertThat(iconsRedesSociaisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIconsRedesSociais() throws Exception {
        int databaseSizeBeforeUpdate = iconsRedesSociaisRepository.findAll().size();
        iconsRedesSociais.setId(count.incrementAndGet());

        // Create the IconsRedesSociais
        IconsRedesSociaisDTO iconsRedesSociaisDTO = iconsRedesSociaisMapper.toDto(iconsRedesSociais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIconsRedesSociaisMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(iconsRedesSociaisDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IconsRedesSociais in the database
        List<IconsRedesSociais> iconsRedesSociaisList = iconsRedesSociaisRepository.findAll();
        assertThat(iconsRedesSociaisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIconsRedesSociais() throws Exception {
        // Initialize the database
        iconsRedesSociaisRepository.saveAndFlush(iconsRedesSociais);

        int databaseSizeBeforeDelete = iconsRedesSociaisRepository.findAll().size();

        // Delete the iconsRedesSociais
        restIconsRedesSociaisMockMvc
            .perform(delete(ENTITY_API_URL_ID, iconsRedesSociais.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IconsRedesSociais> iconsRedesSociaisList = iconsRedesSociaisRepository.findAll();
        assertThat(iconsRedesSociaisList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
