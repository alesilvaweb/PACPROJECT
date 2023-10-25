package com.aapm.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aapm.app.IntegrationTest;
import com.aapm.app.domain.Convenio;
import com.aapm.app.domain.ImagensConvenio;
import com.aapm.app.repository.ImagensConvenioRepository;
import com.aapm.app.service.ImagensConvenioService;
import com.aapm.app.service.criteria.ImagensConvenioCriteria;
import com.aapm.app.service.dto.ImagensConvenioDTO;
import com.aapm.app.service.mapper.ImagensConvenioMapper;
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
 * Integration tests for the {@link ImagensConvenioResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ImagensConvenioResourceIT {

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGEM = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGEM = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGEM_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGEM_CONTENT_TYPE = "image/png";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/imagens-convenios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ImagensConvenioRepository imagensConvenioRepository;

    @Mock
    private ImagensConvenioRepository imagensConvenioRepositoryMock;

    @Autowired
    private ImagensConvenioMapper imagensConvenioMapper;

    @Mock
    private ImagensConvenioService imagensConvenioServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImagensConvenioMockMvc;

    private ImagensConvenio imagensConvenio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImagensConvenio createEntity(EntityManager em) {
        ImagensConvenio imagensConvenio = new ImagensConvenio()
            .titulo(DEFAULT_TITULO)
            .descricao(DEFAULT_DESCRICAO)
            .imagem(DEFAULT_IMAGEM)
            .imagemContentType(DEFAULT_IMAGEM_CONTENT_TYPE)
            .created(DEFAULT_CREATED)
            .modified(DEFAULT_MODIFIED);
        return imagensConvenio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImagensConvenio createUpdatedEntity(EntityManager em) {
        ImagensConvenio imagensConvenio = new ImagensConvenio()
            .titulo(UPDATED_TITULO)
            .descricao(UPDATED_DESCRICAO)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);
        return imagensConvenio;
    }

    @BeforeEach
    public void initTest() {
        imagensConvenio = createEntity(em);
    }

    @Test
    @Transactional
    void createImagensConvenio() throws Exception {
        int databaseSizeBeforeCreate = imagensConvenioRepository.findAll().size();
        // Create the ImagensConvenio
        ImagensConvenioDTO imagensConvenioDTO = imagensConvenioMapper.toDto(imagensConvenio);
        restImagensConvenioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imagensConvenioDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ImagensConvenio in the database
        List<ImagensConvenio> imagensConvenioList = imagensConvenioRepository.findAll();
        assertThat(imagensConvenioList).hasSize(databaseSizeBeforeCreate + 1);
        ImagensConvenio testImagensConvenio = imagensConvenioList.get(imagensConvenioList.size() - 1);
        assertThat(testImagensConvenio.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testImagensConvenio.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testImagensConvenio.getImagem()).isEqualTo(DEFAULT_IMAGEM);
        assertThat(testImagensConvenio.getImagemContentType()).isEqualTo(DEFAULT_IMAGEM_CONTENT_TYPE);
        assertThat(testImagensConvenio.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testImagensConvenio.getModified()).isEqualTo(DEFAULT_MODIFIED);
    }

    @Test
    @Transactional
    void createImagensConvenioWithExistingId() throws Exception {
        // Create the ImagensConvenio with an existing ID
        imagensConvenio.setId(1L);
        ImagensConvenioDTO imagensConvenioDTO = imagensConvenioMapper.toDto(imagensConvenio);

        int databaseSizeBeforeCreate = imagensConvenioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restImagensConvenioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imagensConvenioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImagensConvenio in the database
        List<ImagensConvenio> imagensConvenioList = imagensConvenioRepository.findAll();
        assertThat(imagensConvenioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTituloIsRequired() throws Exception {
        int databaseSizeBeforeTest = imagensConvenioRepository.findAll().size();
        // set the field null
        imagensConvenio.setTitulo(null);

        // Create the ImagensConvenio, which fails.
        ImagensConvenioDTO imagensConvenioDTO = imagensConvenioMapper.toDto(imagensConvenio);

        restImagensConvenioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imagensConvenioDTO))
            )
            .andExpect(status().isBadRequest());

        List<ImagensConvenio> imagensConvenioList = imagensConvenioRepository.findAll();
        assertThat(imagensConvenioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllImagensConvenios() throws Exception {
        // Initialize the database
        imagensConvenioRepository.saveAndFlush(imagensConvenio);

        // Get all the imagensConvenioList
        restImagensConvenioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imagensConvenio.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].imagemContentType").value(hasItem(DEFAULT_IMAGEM_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imagem").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGEM))))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllImagensConveniosWithEagerRelationshipsIsEnabled() throws Exception {
        when(imagensConvenioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restImagensConvenioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(imagensConvenioServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllImagensConveniosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(imagensConvenioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restImagensConvenioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(imagensConvenioRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getImagensConvenio() throws Exception {
        // Initialize the database
        imagensConvenioRepository.saveAndFlush(imagensConvenio);

        // Get the imagensConvenio
        restImagensConvenioMockMvc
            .perform(get(ENTITY_API_URL_ID, imagensConvenio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(imagensConvenio.getId().intValue()))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.imagemContentType").value(DEFAULT_IMAGEM_CONTENT_TYPE))
            .andExpect(jsonPath("$.imagem").value(Base64Utils.encodeToString(DEFAULT_IMAGEM)))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.modified").value(DEFAULT_MODIFIED.toString()));
    }

    @Test
    @Transactional
    void getImagensConveniosByIdFiltering() throws Exception {
        // Initialize the database
        imagensConvenioRepository.saveAndFlush(imagensConvenio);

        Long id = imagensConvenio.getId();

        defaultImagensConvenioShouldBeFound("id.equals=" + id);
        defaultImagensConvenioShouldNotBeFound("id.notEquals=" + id);

        defaultImagensConvenioShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultImagensConvenioShouldNotBeFound("id.greaterThan=" + id);

        defaultImagensConvenioShouldBeFound("id.lessThanOrEqual=" + id);
        defaultImagensConvenioShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllImagensConveniosByTituloIsEqualToSomething() throws Exception {
        // Initialize the database
        imagensConvenioRepository.saveAndFlush(imagensConvenio);

        // Get all the imagensConvenioList where titulo equals to DEFAULT_TITULO
        defaultImagensConvenioShouldBeFound("titulo.equals=" + DEFAULT_TITULO);

        // Get all the imagensConvenioList where titulo equals to UPDATED_TITULO
        defaultImagensConvenioShouldNotBeFound("titulo.equals=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllImagensConveniosByTituloIsInShouldWork() throws Exception {
        // Initialize the database
        imagensConvenioRepository.saveAndFlush(imagensConvenio);

        // Get all the imagensConvenioList where titulo in DEFAULT_TITULO or UPDATED_TITULO
        defaultImagensConvenioShouldBeFound("titulo.in=" + DEFAULT_TITULO + "," + UPDATED_TITULO);

        // Get all the imagensConvenioList where titulo equals to UPDATED_TITULO
        defaultImagensConvenioShouldNotBeFound("titulo.in=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllImagensConveniosByTituloIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagensConvenioRepository.saveAndFlush(imagensConvenio);

        // Get all the imagensConvenioList where titulo is not null
        defaultImagensConvenioShouldBeFound("titulo.specified=true");

        // Get all the imagensConvenioList where titulo is null
        defaultImagensConvenioShouldNotBeFound("titulo.specified=false");
    }

    @Test
    @Transactional
    void getAllImagensConveniosByTituloContainsSomething() throws Exception {
        // Initialize the database
        imagensConvenioRepository.saveAndFlush(imagensConvenio);

        // Get all the imagensConvenioList where titulo contains DEFAULT_TITULO
        defaultImagensConvenioShouldBeFound("titulo.contains=" + DEFAULT_TITULO);

        // Get all the imagensConvenioList where titulo contains UPDATED_TITULO
        defaultImagensConvenioShouldNotBeFound("titulo.contains=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllImagensConveniosByTituloNotContainsSomething() throws Exception {
        // Initialize the database
        imagensConvenioRepository.saveAndFlush(imagensConvenio);

        // Get all the imagensConvenioList where titulo does not contain DEFAULT_TITULO
        defaultImagensConvenioShouldNotBeFound("titulo.doesNotContain=" + DEFAULT_TITULO);

        // Get all the imagensConvenioList where titulo does not contain UPDATED_TITULO
        defaultImagensConvenioShouldBeFound("titulo.doesNotContain=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllImagensConveniosByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        imagensConvenioRepository.saveAndFlush(imagensConvenio);

        // Get all the imagensConvenioList where descricao equals to DEFAULT_DESCRICAO
        defaultImagensConvenioShouldBeFound("descricao.equals=" + DEFAULT_DESCRICAO);

        // Get all the imagensConvenioList where descricao equals to UPDATED_DESCRICAO
        defaultImagensConvenioShouldNotBeFound("descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllImagensConveniosByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        imagensConvenioRepository.saveAndFlush(imagensConvenio);

        // Get all the imagensConvenioList where descricao in DEFAULT_DESCRICAO or UPDATED_DESCRICAO
        defaultImagensConvenioShouldBeFound("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO);

        // Get all the imagensConvenioList where descricao equals to UPDATED_DESCRICAO
        defaultImagensConvenioShouldNotBeFound("descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllImagensConveniosByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagensConvenioRepository.saveAndFlush(imagensConvenio);

        // Get all the imagensConvenioList where descricao is not null
        defaultImagensConvenioShouldBeFound("descricao.specified=true");

        // Get all the imagensConvenioList where descricao is null
        defaultImagensConvenioShouldNotBeFound("descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllImagensConveniosByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        imagensConvenioRepository.saveAndFlush(imagensConvenio);

        // Get all the imagensConvenioList where descricao contains DEFAULT_DESCRICAO
        defaultImagensConvenioShouldBeFound("descricao.contains=" + DEFAULT_DESCRICAO);

        // Get all the imagensConvenioList where descricao contains UPDATED_DESCRICAO
        defaultImagensConvenioShouldNotBeFound("descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllImagensConveniosByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        imagensConvenioRepository.saveAndFlush(imagensConvenio);

        // Get all the imagensConvenioList where descricao does not contain DEFAULT_DESCRICAO
        defaultImagensConvenioShouldNotBeFound("descricao.doesNotContain=" + DEFAULT_DESCRICAO);

        // Get all the imagensConvenioList where descricao does not contain UPDATED_DESCRICAO
        defaultImagensConvenioShouldBeFound("descricao.doesNotContain=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllImagensConveniosByCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        imagensConvenioRepository.saveAndFlush(imagensConvenio);

        // Get all the imagensConvenioList where created equals to DEFAULT_CREATED
        defaultImagensConvenioShouldBeFound("created.equals=" + DEFAULT_CREATED);

        // Get all the imagensConvenioList where created equals to UPDATED_CREATED
        defaultImagensConvenioShouldNotBeFound("created.equals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllImagensConveniosByCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        imagensConvenioRepository.saveAndFlush(imagensConvenio);

        // Get all the imagensConvenioList where created in DEFAULT_CREATED or UPDATED_CREATED
        defaultImagensConvenioShouldBeFound("created.in=" + DEFAULT_CREATED + "," + UPDATED_CREATED);

        // Get all the imagensConvenioList where created equals to UPDATED_CREATED
        defaultImagensConvenioShouldNotBeFound("created.in=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    void getAllImagensConveniosByCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagensConvenioRepository.saveAndFlush(imagensConvenio);

        // Get all the imagensConvenioList where created is not null
        defaultImagensConvenioShouldBeFound("created.specified=true");

        // Get all the imagensConvenioList where created is null
        defaultImagensConvenioShouldNotBeFound("created.specified=false");
    }

    @Test
    @Transactional
    void getAllImagensConveniosByModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        imagensConvenioRepository.saveAndFlush(imagensConvenio);

        // Get all the imagensConvenioList where modified equals to DEFAULT_MODIFIED
        defaultImagensConvenioShouldBeFound("modified.equals=" + DEFAULT_MODIFIED);

        // Get all the imagensConvenioList where modified equals to UPDATED_MODIFIED
        defaultImagensConvenioShouldNotBeFound("modified.equals=" + UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void getAllImagensConveniosByModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        imagensConvenioRepository.saveAndFlush(imagensConvenio);

        // Get all the imagensConvenioList where modified in DEFAULT_MODIFIED or UPDATED_MODIFIED
        defaultImagensConvenioShouldBeFound("modified.in=" + DEFAULT_MODIFIED + "," + UPDATED_MODIFIED);

        // Get all the imagensConvenioList where modified equals to UPDATED_MODIFIED
        defaultImagensConvenioShouldNotBeFound("modified.in=" + UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void getAllImagensConveniosByModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagensConvenioRepository.saveAndFlush(imagensConvenio);

        // Get all the imagensConvenioList where modified is not null
        defaultImagensConvenioShouldBeFound("modified.specified=true");

        // Get all the imagensConvenioList where modified is null
        defaultImagensConvenioShouldNotBeFound("modified.specified=false");
    }

    @Test
    @Transactional
    void getAllImagensConveniosByConvenioIsEqualToSomething() throws Exception {
        Convenio convenio;
        if (TestUtil.findAll(em, Convenio.class).isEmpty()) {
            imagensConvenioRepository.saveAndFlush(imagensConvenio);
            convenio = ConvenioResourceIT.createEntity(em);
        } else {
            convenio = TestUtil.findAll(em, Convenio.class).get(0);
        }
        em.persist(convenio);
        em.flush();
        imagensConvenio.setConvenio(convenio);
        imagensConvenioRepository.saveAndFlush(imagensConvenio);
        Long convenioId = convenio.getId();

        // Get all the imagensConvenioList where convenio equals to convenioId
        defaultImagensConvenioShouldBeFound("convenioId.equals=" + convenioId);

        // Get all the imagensConvenioList where convenio equals to (convenioId + 1)
        defaultImagensConvenioShouldNotBeFound("convenioId.equals=" + (convenioId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultImagensConvenioShouldBeFound(String filter) throws Exception {
        restImagensConvenioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imagensConvenio.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].imagemContentType").value(hasItem(DEFAULT_IMAGEM_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imagem").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGEM))))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())));

        // Check, that the count call also returns 1
        restImagensConvenioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultImagensConvenioShouldNotBeFound(String filter) throws Exception {
        restImagensConvenioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restImagensConvenioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingImagensConvenio() throws Exception {
        // Get the imagensConvenio
        restImagensConvenioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingImagensConvenio() throws Exception {
        // Initialize the database
        imagensConvenioRepository.saveAndFlush(imagensConvenio);

        int databaseSizeBeforeUpdate = imagensConvenioRepository.findAll().size();

        // Update the imagensConvenio
        ImagensConvenio updatedImagensConvenio = imagensConvenioRepository.findById(imagensConvenio.getId()).get();
        // Disconnect from session so that the updates on updatedImagensConvenio are not directly saved in db
        em.detach(updatedImagensConvenio);
        updatedImagensConvenio
            .titulo(UPDATED_TITULO)
            .descricao(UPDATED_DESCRICAO)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);
        ImagensConvenioDTO imagensConvenioDTO = imagensConvenioMapper.toDto(updatedImagensConvenio);

        restImagensConvenioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imagensConvenioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imagensConvenioDTO))
            )
            .andExpect(status().isOk());

        // Validate the ImagensConvenio in the database
        List<ImagensConvenio> imagensConvenioList = imagensConvenioRepository.findAll();
        assertThat(imagensConvenioList).hasSize(databaseSizeBeforeUpdate);
        ImagensConvenio testImagensConvenio = imagensConvenioList.get(imagensConvenioList.size() - 1);
        assertThat(testImagensConvenio.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testImagensConvenio.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testImagensConvenio.getImagem()).isEqualTo(UPDATED_IMAGEM);
        assertThat(testImagensConvenio.getImagemContentType()).isEqualTo(UPDATED_IMAGEM_CONTENT_TYPE);
        assertThat(testImagensConvenio.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testImagensConvenio.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void putNonExistingImagensConvenio() throws Exception {
        int databaseSizeBeforeUpdate = imagensConvenioRepository.findAll().size();
        imagensConvenio.setId(count.incrementAndGet());

        // Create the ImagensConvenio
        ImagensConvenioDTO imagensConvenioDTO = imagensConvenioMapper.toDto(imagensConvenio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImagensConvenioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imagensConvenioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imagensConvenioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImagensConvenio in the database
        List<ImagensConvenio> imagensConvenioList = imagensConvenioRepository.findAll();
        assertThat(imagensConvenioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchImagensConvenio() throws Exception {
        int databaseSizeBeforeUpdate = imagensConvenioRepository.findAll().size();
        imagensConvenio.setId(count.incrementAndGet());

        // Create the ImagensConvenio
        ImagensConvenioDTO imagensConvenioDTO = imagensConvenioMapper.toDto(imagensConvenio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImagensConvenioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imagensConvenioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImagensConvenio in the database
        List<ImagensConvenio> imagensConvenioList = imagensConvenioRepository.findAll();
        assertThat(imagensConvenioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImagensConvenio() throws Exception {
        int databaseSizeBeforeUpdate = imagensConvenioRepository.findAll().size();
        imagensConvenio.setId(count.incrementAndGet());

        // Create the ImagensConvenio
        ImagensConvenioDTO imagensConvenioDTO = imagensConvenioMapper.toDto(imagensConvenio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImagensConvenioMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imagensConvenioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImagensConvenio in the database
        List<ImagensConvenio> imagensConvenioList = imagensConvenioRepository.findAll();
        assertThat(imagensConvenioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateImagensConvenioWithPatch() throws Exception {
        // Initialize the database
        imagensConvenioRepository.saveAndFlush(imagensConvenio);

        int databaseSizeBeforeUpdate = imagensConvenioRepository.findAll().size();

        // Update the imagensConvenio using partial update
        ImagensConvenio partialUpdatedImagensConvenio = new ImagensConvenio();
        partialUpdatedImagensConvenio.setId(imagensConvenio.getId());

        partialUpdatedImagensConvenio.titulo(UPDATED_TITULO).imagem(UPDATED_IMAGEM).imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE);

        restImagensConvenioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImagensConvenio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImagensConvenio))
            )
            .andExpect(status().isOk());

        // Validate the ImagensConvenio in the database
        List<ImagensConvenio> imagensConvenioList = imagensConvenioRepository.findAll();
        assertThat(imagensConvenioList).hasSize(databaseSizeBeforeUpdate);
        ImagensConvenio testImagensConvenio = imagensConvenioList.get(imagensConvenioList.size() - 1);
        assertThat(testImagensConvenio.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testImagensConvenio.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testImagensConvenio.getImagem()).isEqualTo(UPDATED_IMAGEM);
        assertThat(testImagensConvenio.getImagemContentType()).isEqualTo(UPDATED_IMAGEM_CONTENT_TYPE);
        assertThat(testImagensConvenio.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testImagensConvenio.getModified()).isEqualTo(DEFAULT_MODIFIED);
    }

    @Test
    @Transactional
    void fullUpdateImagensConvenioWithPatch() throws Exception {
        // Initialize the database
        imagensConvenioRepository.saveAndFlush(imagensConvenio);

        int databaseSizeBeforeUpdate = imagensConvenioRepository.findAll().size();

        // Update the imagensConvenio using partial update
        ImagensConvenio partialUpdatedImagensConvenio = new ImagensConvenio();
        partialUpdatedImagensConvenio.setId(imagensConvenio.getId());

        partialUpdatedImagensConvenio
            .titulo(UPDATED_TITULO)
            .descricao(UPDATED_DESCRICAO)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);

        restImagensConvenioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImagensConvenio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImagensConvenio))
            )
            .andExpect(status().isOk());

        // Validate the ImagensConvenio in the database
        List<ImagensConvenio> imagensConvenioList = imagensConvenioRepository.findAll();
        assertThat(imagensConvenioList).hasSize(databaseSizeBeforeUpdate);
        ImagensConvenio testImagensConvenio = imagensConvenioList.get(imagensConvenioList.size() - 1);
        assertThat(testImagensConvenio.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testImagensConvenio.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testImagensConvenio.getImagem()).isEqualTo(UPDATED_IMAGEM);
        assertThat(testImagensConvenio.getImagemContentType()).isEqualTo(UPDATED_IMAGEM_CONTENT_TYPE);
        assertThat(testImagensConvenio.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testImagensConvenio.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingImagensConvenio() throws Exception {
        int databaseSizeBeforeUpdate = imagensConvenioRepository.findAll().size();
        imagensConvenio.setId(count.incrementAndGet());

        // Create the ImagensConvenio
        ImagensConvenioDTO imagensConvenioDTO = imagensConvenioMapper.toDto(imagensConvenio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImagensConvenioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, imagensConvenioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(imagensConvenioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImagensConvenio in the database
        List<ImagensConvenio> imagensConvenioList = imagensConvenioRepository.findAll();
        assertThat(imagensConvenioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImagensConvenio() throws Exception {
        int databaseSizeBeforeUpdate = imagensConvenioRepository.findAll().size();
        imagensConvenio.setId(count.incrementAndGet());

        // Create the ImagensConvenio
        ImagensConvenioDTO imagensConvenioDTO = imagensConvenioMapper.toDto(imagensConvenio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImagensConvenioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(imagensConvenioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImagensConvenio in the database
        List<ImagensConvenio> imagensConvenioList = imagensConvenioRepository.findAll();
        assertThat(imagensConvenioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImagensConvenio() throws Exception {
        int databaseSizeBeforeUpdate = imagensConvenioRepository.findAll().size();
        imagensConvenio.setId(count.incrementAndGet());

        // Create the ImagensConvenio
        ImagensConvenioDTO imagensConvenioDTO = imagensConvenioMapper.toDto(imagensConvenio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImagensConvenioMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(imagensConvenioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImagensConvenio in the database
        List<ImagensConvenio> imagensConvenioList = imagensConvenioRepository.findAll();
        assertThat(imagensConvenioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteImagensConvenio() throws Exception {
        // Initialize the database
        imagensConvenioRepository.saveAndFlush(imagensConvenio);

        int databaseSizeBeforeDelete = imagensConvenioRepository.findAll().size();

        // Delete the imagensConvenio
        restImagensConvenioMockMvc
            .perform(delete(ENTITY_API_URL_ID, imagensConvenio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ImagensConvenio> imagensConvenioList = imagensConvenioRepository.findAll();
        assertThat(imagensConvenioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
