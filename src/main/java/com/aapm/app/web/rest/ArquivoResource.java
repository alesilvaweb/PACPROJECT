package com.aapm.app.web.rest;

import com.aapm.app.domain.Associado;
import com.aapm.app.domain.User;
import com.aapm.app.domain.enumeration.Status;
import com.aapm.app.domain.enumeration.StatusArquivo;
import com.aapm.app.repository.ArquivoRepository;
import com.aapm.app.repository.AssociadoRepository;
import com.aapm.app.repository.DependenteRepository;
import com.aapm.app.repository.UserRepository;
import com.aapm.app.service.*;
import com.aapm.app.service.criteria.ArquivoCriteria;
import com.aapm.app.service.dto.AdminUserDTO;
import com.aapm.app.service.dto.ArquivoDTO;
import com.aapm.app.service.dto.AssociadoDTO;
import com.aapm.app.service.dto.DependenteDTO;
import com.aapm.app.web.rest.errors.BadRequestAlertException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.aapm.app.domain.Arquivo}.
 */
@RestController
@RequestMapping("/api")
public class ArquivoResource {

    private final Logger log = LoggerFactory.getLogger(ArquivoResource.class);

    private static final String ENTITY_NAME = "arquivo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArquivoService arquivoService;

    private final ArquivoRepository arquivoRepository;

    private final UserRepository userRepository;

    private final DependenteRepository dependenteRepository;

    private final ArquivoQueryService arquivoQueryService;

    private final AssociadoService associadoService;

    private final DependenteService dependenteService;

    private final AssociadoRepository associadoRepository;
    private final UserService userService;
    private final MailService mailService;
    private String salvarAssociado;
    private String salvarDependente;
    private String salvarUser;
    private boolean salvarDependentes;
    private int usuariosSalvos;
    private int usuariosAtualizados;
    private final Set<String> ROLE = Collections.singleton("ROLE_USER");

    public ArquivoResource(
        ArquivoService arquivoService,
        ArquivoRepository arquivoRepository,
        UserRepository userRepository,
        DependenteRepository dependenteRepository,
        ArquivoQueryService arquivoQueryService,
        AssociadoService associadoService,
        DependenteService dependenteService,
        AssociadoRepository associadoRepository,
        UserService userService,
        MailService mailService
    ) {
        this.arquivoService = arquivoService;
        this.arquivoRepository = arquivoRepository;
        this.userRepository = userRepository;
        this.dependenteRepository = dependenteRepository;
        this.arquivoQueryService = arquivoQueryService;
        this.associadoService = associadoService;
        this.dependenteService = dependenteService;
        this.associadoRepository = associadoRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.usuariosSalvos = 0;
        this.usuariosAtualizados = 0;
    }

    @PostMapping(value = "/arquivos")
    public ResponseEntity<ArquivoDTO> createArquivo(@Valid @RequestBody ArquivoDTO arquivoDTO) throws URISyntaxException, IOException {
        usuariosSalvos = 0;
        usuariosAtualizados = 0;

        if (arquivoDTO.getId() != null) {
            throw new BadRequestAlertException("A new arquivo cannot already have an ID", ENTITY_NAME, "idexists");
        }

        /* Cria um arquivo temporário com os dados binários */
        File tempFile = File.createTempFile(UUID.randomUUID().toString(), ".xlsx", new File("src/main/resources/upload"));
        try (FileOutputStream fos = new FileOutputStream(String.valueOf(tempFile.toPath()))) {
            fos.write(arquivoDTO.getArquivo(), 0, arquivoDTO.getArquivo().length);
            fos.close();
        }

        /* Abre o arquivo temporário excel  -> src/main/resources/upload*/
        try (Workbook workbook = WorkbookFactory.create(new FileInputStream(String.valueOf(tempFile.toPath())))) {
            Sheet sheet = workbook.getSheetAt(0);

            /* Remove a primeira linha da planilha */
            sheet.removeRow(sheet.getRow(0));

            for (Row row : sheet) {
                AssociadoDTO associado = new AssociadoDTO();
                AdminUserDTO user = new AdminUserDTO();
                DependenteDTO dependente = new DependenteDTO();
                salvarAssociado = " ";
                salvarUser = " ";
                salvarDependente = " ";
                salvarDependentes = false;

                /* Percorre as linhas pegando as células */
                for (Cell cell : row) {
                    if (cell.getColumnIndex() == 0) {
                        Long id = (long) cell.getNumericCellValue();
                        if (associadoRepository.findById(id).isPresent()) {
                            salvarAssociado = "update";
                        }
                        if (userRepository.findById(id).isPresent()) {
                            salvarAssociado = "update";
                        }
                    }
                    if (cell.getColumnIndex() == 4) {
                        Long id = Long.valueOf(associado.getId().toString().concat(String.valueOf((long) cell.getNumericCellValue())));
                        if (dependenteRepository.findById(id).isPresent()) {
                            salvarDependente = "update";
                        }
                    }

                    int columnIndex = cell.getColumnIndex();
                    /* Coluna id */
                    if (columnIndex == 0) {
                        associado.setId((long) cell.getNumericCellValue());
                        associado.setMatricula(String.valueOf((long) cell.getNumericCellValue()));
                        user.setId((long) cell.getNumericCellValue());
                        /* Coluna nome */
                    } else if (columnIndex == 1) {
                        associado.setNome(cell.getStringCellValue());
                        user.setFirstName(cell.getStringCellValue());
                        /* Coluna Situação */
                    } else if (columnIndex == 2) {}/* Coluna Dependente Nome */ else if (columnIndex == 3) {
                        if (cell.getStringCellValue().isBlank()) {
                            salvarDependentes = false;
                        } else {
                            salvarDependentes = true;
                        }
                        if (salvarDependentes) {
                            dependente.setNome(cell.getStringCellValue());
                        }
                    }/* Coluna sequencia gera o id concatenado com o id do associado */ else if (columnIndex == 4) {
                        dependente.setId(
                            Long.valueOf(associado.getId().toString().concat(String.valueOf((long) cell.getNumericCellValue())))
                        );
                    }/* Coluna Parentesco */ else if (columnIndex == 5) {
                        if (salvarDependentes) {
                            dependente.setParentesco(cell.getStringCellValue());
                        }
                    }/* Coluna Data de nascimento Associado */ else if (columnIndex == 6) {
                        associado.setDataNascimento(cell.getLocalDateTimeCellValue().toLocalDate());
                    }/* Coluna Data de nascimento Dependente */ else if (columnIndex == 7) {
                        if (salvarDependentes) {
                            dependente.setDataNascimento(cell.getLocalDateTimeCellValue().toLocalDate());
                        }
                    }/* Coluna status */ else if (columnIndex == 8) {
                        if (cell.getStringCellValue().toLowerCase().trim().equals("ativo")) {
                            if (salvarDependentes) {
                                dependente.setStatus(Status.Ativo);
                            }
                        } else {
                            if (salvarDependentes) {
                                dependente.setStatus(Status.Inativo);
                            }
                        }
                    }/* Coluna Email */ else if (columnIndex == 9) {
                        String mail = cell.getStringCellValue().toLowerCase();
                        associado.setEmail(mail);

                        if (userRepository.findOneByLogin(mail).isPresent()) {
                            salvarUser = "update";
                        } else if (userRepository.findOneByEmailIgnoreCase(mail).isPresent()) {
                            salvarUser = "update";
                        }
                        user.setEmail(mail);
                        user.setLogin(mail);
                    }
                    associado.setStatus(Status.Ativo);
                    user.setAuthorities(ROLE);
                    user.setActivated(true);
                    user.setLangKey("pt-br");
                    if (salvarDependentes) {
                        dependente.setAssociado(associado);
                    }
                }

                /* Salva os usuários */
                if (Objects.equals(salvarUser, "update")) {
                    /* Update user */
                    userService.updateUser(user);
                    usuariosAtualizados = (usuariosAtualizados + 1);
                    log.debug("<<< UPDATE USER >>> : {},", user);
                } else {
                    /* Save user */
                    User newUser = userService.createUser(user);
                    usuariosSalvos = (usuariosSalvos + 1);
                    mailService.sendCreationEmail(newUser);
                    log.debug("<<< NEW USER >>> : {},", user);
                }

                /* Salva os associados */
                if (Objects.equals(salvarAssociado, "update")) {
                    /* Update Associado */
                    associadoService.update(associado);
                    salvarAssociado = "";
                    log.debug("<<< UPDATE ASSOCIADO >>> : {},", associado);
                } else {
                    /* Save Associado */
                    associadoService.save(associado);
                    log.debug("<<< NEW ASSOCIADO >>> : {},", associado);
                }

                /* Salva os Dependente */
                if (salvarDependentes) {
                    //                    List<Optional<Associado>> associado2 = Collections.singletonList(associadoRepository.findById(associado.getId())
                    //                        .filter(associado1 -> associado1.getDependentes()
                    //                            .stream().filter(dependente1 -> {
                    //                                return dependente.getNome().toLowerCase().trim().equals(dependente1.getNome().toLowerCase().trim());
                    //                            }).isParallel()));

                    //                    if (associado2.stream().findAny().isPresent()) {
                    //                        salvarDependente = "update";
                    //                        log.debug("<<< IS PRESENTE >>> : {},", associado2);
                    //                    } else {
                    if (Objects.equals(salvarDependente, "update")) {
                        /* Update Dependente */
                        dependenteService.update(dependente);
                        salvarDependente = "";
                        log.debug("<<< UPDATE DEPENDENTE >>> : {},", dependente);
                    } else {
                        /* Save Dependente */
                        dependenteService.save(dependente);
                        log.debug("<<< NEW DEPENDENTE >>> : {},", dependente);
                    }
                }
            }
            log.debug("<<< NOVOS >>> : {}, << ATUALIZADOS >> : {}", usuariosSalvos, usuariosAtualizados);
        } catch (IOException e) {
            e.printStackTrace();
        }
        arquivoDTO.setStatus(StatusArquivo.Processado);

        tempFile.delete();

        ArquivoDTO result = arquivoService.save(arquivoDTO);

        return ResponseEntity
            .created(new URI("/api/arquivos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    public List<?> toList(Iterator<?> iterator) {
        return IteratorUtils.toList(iterator);
    }

    /**
     * {@code PUT  /arquivos/:id} : Updates an existing arquivo.
     *
     * @param id         the id of the arquivoDTO to save.
     * @param arquivoDTO the arquivoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated arquivoDTO,
     * or with status {@code 400 (Bad Request)} if the arquivoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the arquivoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/arquivos/{id}")
    public ResponseEntity<ArquivoDTO> updateArquivo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ArquivoDTO arquivoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Arquivo : {}, {}", id, arquivoDTO);
        if (arquivoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, arquivoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!arquivoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ArquivoDTO result = arquivoService.update(arquivoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, arquivoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /arquivos/:id} : Partial updates given fields of an existing arquivo, field will ignore if it is null
     *
     * @param id         the id of the arquivoDTO to save.
     * @param arquivoDTO the arquivoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated arquivoDTO,
     * or with status {@code 400 (Bad Request)} if the arquivoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the arquivoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the arquivoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/arquivos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ArquivoDTO> partialUpdateArquivo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ArquivoDTO arquivoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Arquivo partially : {}, {}", id, arquivoDTO);
        if (arquivoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, arquivoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!arquivoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ArquivoDTO> result = arquivoService.partialUpdate(arquivoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, arquivoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /arquivos} : get all the arquivos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of arquivos in body.
     */
    @GetMapping("/arquivos")
    public ResponseEntity<List<ArquivoDTO>> getAllArquivos(
        ArquivoCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Arquivos by criteria: {}", criteria);
        Page<ArquivoDTO> page = arquivoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /arquivos/count} : count all the arquivos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/arquivos/count")
    public ResponseEntity<Long> countArquivos(ArquivoCriteria criteria) {
        log.debug("REST request to count Arquivos by criteria: {}", criteria);
        return ResponseEntity.ok().body(arquivoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /arquivos/:id} : get the "id" arquivo.
     *
     * @param id the id of the arquivoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the arquivoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/arquivos/{id}")
    public ResponseEntity<ArquivoDTO> getArquivo(@PathVariable Long id) {
        log.debug("REST request to get Arquivo : {}", id);
        Optional<ArquivoDTO> arquivoDTO = arquivoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(arquivoDTO);
    }

    /**
     * {@code DELETE  /arquivos/:id} : delete the "id" arquivo.
     *
     * @param id the id of the arquivoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/arquivos/{id}")
    public ResponseEntity<Void> deleteArquivo(@PathVariable Long id) {
        log.debug("REST request to delete Arquivo : {}", id);
        arquivoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
