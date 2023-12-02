import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Badge, Breadcrumb, BreadcrumbItem, Button, Col, Modal, ModalBody, ModalFooter, ModalHeader, Row } from 'reactstrap';
import { Translate, translate, ValidatedBlobField, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getCategorias } from 'app/entities/categoria/categoria.reducer';
import { Status } from 'app/shared/model/enumerations/status.model';
import { createEntity, getEntity, reset, updateEntity } from './convenio.reducer';
import { getEntities as getIconsRedesSociais } from 'app/entities/icons-redes-sociais/icons-redes-sociais.reducer';
import {
  createEntity as createRedeSocial,
  deleteEntity as deleteRedeSocial,
} from 'app/entities/redes-sociais-convenio/redes-sociais-convenio.reducer';
import { createEntity as createDesconto, deleteEntity as deleteDesconto } from './../desconto-convenio/desconto-convenio.reducer';
import { createEntity as createCategoria } from './../categoria/categoria.reducer';
import axios from 'axios';
import Stack from '@mui/material/Stack';
import { Chip, Typography } from '@mui/material';

export const ConvenioUpdate = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const categorias = useAppSelector(state => state.categoria.entities);
  const convenioEntity = useAppSelector(state => state.convenio.entity);
  const iconsRedesSociais = useAppSelector(state => state.iconsRedesSociais.entities);

  const loading = useAppSelector(state => state.convenio.loading);
  const updating = useAppSelector(state => state.convenio.updating);
  const updateSuccess = useAppSelector(state => state.convenio.updateSuccess);

  const statusValues = Object.keys(Status);
  const [desconto, setDesconto] = useState([]);
  const [redeSocial, setRedeSocial] = useState([]);

  const [modal, setModal] = useState(false);
  const [modalCategoria, setModalCategoria] = useState(false);
  const [modalRedesSociais, setModalRedesSociais] = useState(false);

  const toggle = () => setModal(!modal);
  const toggleCategoria = () => setModalCategoria(!modalCategoria);
  const toggleRedesSociais = () => setModalRedesSociais(!modalRedesSociais);

  const handleClose = () => {
    navigate('/convenio/list' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id)).then(value => {
        fetchDescontos(value.payload['data'].id).then(r => null);
        fetchRedesSociais(value.payload['data'].id).then(r => null);
      });
    }
    dispatch(getIconsRedesSociais({}));
    dispatch(getCategorias({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  async function fetchDescontos(id) {
    try {
      const response = await axios.get(`api/desconto-convenios?convenioId.equals=${id}`);
      setDesconto(response.data);
    } catch (error) {
      console.error('Erro ao buscar Descontos:', error);
    }
  }

  async function fetchRedesSociais(id) {
    try {
      const response = await axios.get(`api/redes-sociais-convenios?convenioId.equals=${id}`);
      setRedeSocial(response.data);
    } catch (error) {
      console.error('Erro ao buscar redes Sociais:', error);
    }
  }

  async function saveDesconto(values) {
    if (!isNew) {
      await dispatch(
        createDesconto({
          desconto: values.desconto,
          descricao: values.descricao,
          convenio: convenioEntity,
        })
      ).then(() => {
        fetchDescontos(convenioEntity.id);
      });
    } else {
      const desc = [...desconto, values];
      setDesconto(desc);
    }
    toggle();
  }

  async function removerDesconto(id) {
    if (isNew) {
      setDesconto(prevItens => prevItens.filter(item => item !== id));
    } else {
      await dispatch(deleteDesconto(id)).then(() => {
        fetchDescontos(convenioEntity.id);
      });
    }
  }

  async function saveRedeSocial(values) {
    if (!isNew) {
      await dispatch(
        createRedeSocial({
          nome: values.nome,
          endereco: values.endereco,
          descricao: values.descricao,
          icon: iconsRedesSociais.find(it => it.id.toString() === values.icon.toString()),
          convenio: convenioEntity,
        })
      ).then(() => {
        fetchRedesSociais(convenioEntity.id);
      });
    } else {
      const rede = [...redeSocial, values];
      setRedeSocial(rede);
    }
    toggleRedesSociais();
  }

  async function removerRedeSocial(id): Promise<void> {
    if (isNew) {
      setRedeSocial(prevItens => prevItens.filter(item => item !== id));
    } else {
      await dispatch(deleteRedeSocial(id)).then(() => {
        fetchRedesSociais(convenioEntity.id);
      });
    }
  }

  const saveCategoria = values => {
    const cat = {
      categoria: values.categoria,
      descricao: values.descricao,
      created: convertDateTimeToServer(displayDefaultDateTime()),
      modified: convertDateTimeToServer(displayDefaultDateTime()),
    };
    // @ts-ignore
    dispatch(createCategoria(cat)).then(value => {
      dispatch(getCategorias({}));
    });
    toggleCategoria();
  };

  const saveEntity = values => {
    values.created = convertDateTimeToServer(values.created);
    values.modified = convertDateTimeToServer(values.modified);

    const entity = {
      ...convenioEntity,
      ...values,
      categoria: categorias.find(it => it.id.toString() === values.categoria.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity)).then(value => {
        desconto.map(desc => {
          dispatch(
            createDesconto({
              desconto: desc.desconto,
              descricao: desc.descricao,
              convenio: value.payload['data'],
            })
          );
        });
        redeSocial.map(rede => {
          dispatch(
            createRedeSocial({
              nome: rede.nome,
              endereco: rede.endereco,
              descricao: rede.descricao,
              icon: iconsRedesSociais.find(it => it.id.toString() === rede.icon.toString()),
              convenio: value.payload['data'],
            })
          );
        });
      });
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          created: displayDefaultDateTime(),
          modified: displayDefaultDateTime(),
        }
      : {
          status: 'Ativo',
          ...convenioEntity,
          created: convertDateTimeFromServer(convenioEntity.created),
          modified: convertDateTimeFromServer(convenioEntity.modified),
          categoria: convenioEntity?.categoria?.id,
        };

  return (
    <div>
      <Breadcrumb>
        <BreadcrumbItem onClick={() => navigate('/')}>
          <a>Início</a>
        </BreadcrumbItem>
        <BreadcrumbItem onClick={() => navigate('/convenio/list')}>
          <a>Convênios</a>
        </BreadcrumbItem>
        <BreadcrumbItem active>{isNew ? 'Novo' : convenioEntity.nome}</BreadcrumbItem>
      </Breadcrumb>
      <Row className="justify-content-center">
        <Col md="8">
          <h3 id="aapmApp.convenio.home.createOrEditLabel" data-cy="ConvenioCreateUpdateHeading">
            {isNew ? <>Novo Convênio</> : <>Editar Convênio</>}
          </h3>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  hidden
                  readOnly
                  id="convenio-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('aapmApp.convenio.nome')}
                id="convenio-nome"
                name="nome"
                required={true}
                data-cy="nome"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 2, message: translate('entity.validation.minlength', { min: 2 }) },
                }}
              />
              <ValidatedField
                label={translate('aapmApp.convenio.titulo')}
                id="convenio-titulo"
                name="titulo"
                data-cy="titulo"
                type="text"
              />

              <ValidatedField
                label={translate('aapmApp.convenio.descricao')}
                id="convenio-descricao"
                name="descricao"
                hidden={true}
                data-cy="descricao"
                type="textarea"
              />
              <ValidatedField
                label={translate('aapmApp.convenio.endereco')}
                id="convenio-endereco"
                name="endereco"
                data-cy="endereco"
                type="text"
              />
              <ValidatedField
                label={translate('aapmApp.convenio.telefone')}
                id="convenio-telefone"
                name="telefone"
                data-cy="telefone"
                type="tel"
              />
              <ValidatedField label={translate('aapmApp.convenio.email')} id="convenio-email" name="email" data-cy="email" type="text" />
              <ValidatedField
                label={translate('aapmApp.convenio.localizacao')}
                id="convenio-localizacao"
                name="localizacao"
                hidden={true}
                data-cy="localizacao"
                type="text"
              />
              <ValidatedField
                label={translate('aapmApp.convenio.status')}
                id="convenio-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {statusValues.map(status => (
                  <option value={status} key={status}>
                    {translate('aapmApp.Status.' + status)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('aapmApp.convenio.created')}
                id="convenio-created"
                name="created"
                hidden={true}
                data-cy="created"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('aapmApp.convenio.modified')}
                id="convenio-modified"
                name="modified"
                hidden={true}
                data-cy="modified"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="convenio-categoria"
                name="categoria"
                required={true}
                data-cy="categoria"
                label={translate('aapmApp.convenio.categoria')}
                type="select"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              >
                <br />
                <option value="" key="0" />
                {categorias
                  ? categorias.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.categoria}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Typography variant={'h6'}>Adicionar Categorias</Typography>
              <Col md={12} style={{ display: 'flex' }}>
                <Col md={1}>
                  <Button color="primary" type="button" onClick={toggleCategoria}>
                    <FontAwesomeIcon icon="plus" />
                  </Button>
                </Col>
              </Col>
              <hr />
              <Typography variant={'h6'}>Adicionar Descontos</Typography>
              {/* ************** */}
              {/* Lista Desconto */}
              {/* ************** */}
              <Col md={12} style={{ display: 'flex' }}>
                <Col md={1}>
                  <Button color="primary" type="button" onClick={toggle}>
                    <FontAwesomeIcon icon="plus" />
                  </Button>
                </Col>
                <br />
                <Col md={11} style={{ display: 'flex', flexWrap: 'wrap', justifyContent: 'stretch' }}>
                  {desconto.map((desc, i) => (
                    <>
                      <Stack direction="row" spacing={1} style={{ margin: '5px' }}>
                        <Chip
                          color="error"
                          variant="filled"
                          label={`Desconto de ${desc.desconto}% ${desc.descricao}`}
                          onDelete={() => removerDesconto(isNew ? desc : desc.id)}
                        />
                      </Stack>
                      &nbsp;
                    </>
                  ))}
                </Col>
              </Col>
              <hr />
              {/* ******************* */}
              {/* Lista Redes Sociais */}
              {/* ******************* */}
              <Typography variant={'h6'}>Adicionar Redes Sociais</Typography>
              <Col md={12} style={{ display: 'flex' }}>
                <Col md={1}>
                  <Button color="primary" type="button" onClick={toggleRedesSociais}>
                    <FontAwesomeIcon icon="plus" />
                  </Button>
                </Col>
                <br />
                <Col md={11} style={{ display: 'flex', flexWrap: 'wrap', justifyContent: 'stretch' }}>
                  {redeSocial.map((rede, i) => (
                    <>
                      <Stack direction="row" spacing={1} style={{ margin: '5px' }}>
                        <Chip
                          color="error"
                          variant="filled"
                          label={rede.endereco}
                          onDelete={() => removerRedeSocial(isNew ? rede : rede.id)}
                        />
                      </Stack>
                      &nbsp;
                    </>
                  ))}
                </Col>
              </Col>
              <hr />
              <ValidatedBlobField
                label={translate('aapmApp.convenio.imagem')}
                id="convenio-imagem"
                name="imagem"
                required={isNew}
                data-cy="imagem"
                isImage
                accept="image/*"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedBlobField
                label={translate('aapmApp.convenio.logo')}
                id="convenio-logo"
                name="logo"
                data-cy="logo"
                isImage
                accept="image/*"
              />
              <ValidatedBlobField
                label={translate('aapmApp.convenio.banner')}
                id="convenio-banner"
                name="banner"
                hidden={true}
                data-cy="banner"
                isImage
                accept="image/*"
              />

              <div>
                <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/convenio/list" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </div>
            </ValidatedForm>
          )}
        </Col>
      </Row>

      {/* *************** */}
      {/* Modal Descontos */}
      {/* *************** */}
      <div>
        <Modal isOpen={modal} toggle={toggle} style={{ marginTop: '20vh' }}>
          <ModalHeader toggle={toggle}>Desconto</ModalHeader>
          <ModalBody>
            <ValidatedForm onSubmit={saveDesconto}>
              <ValidatedField
                label={'Desconto (%) '}
                id="desconto"
                name="desconto"
                required={true}
                data-cy="desconto"
                type="number"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={'Descrição'}
                id="desconto-descricao"
                required={true}
                name="descricao"
                data-cy="descricao"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />

              <ModalFooter>
                <Button color="primary" type="submit">
                  <FontAwesomeIcon icon="plus" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
                <Button color="secondary" onClick={toggle}>
                  Cancel
                </Button>
              </ModalFooter>
            </ValidatedForm>
          </ModalBody>
        </Modal>
      </div>

      {/* *************** */}
      {/* Modal Categoria */}
      {/* *************** */}
      <div>
        <Modal isOpen={modalCategoria} toggle={toggleCategoria} style={{ marginTop: '20vh' }}>
          <ModalHeader toggle={toggleCategoria}>Nova Categoria</ModalHeader>
          <ModalBody>
            <ValidatedForm onSubmit={saveCategoria}>
              <ValidatedField
                label={'Categoria '}
                id="categoria"
                name="categoria"
                required={true}
                data-cy="categoria"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('aapmApp.convenio.created')}
                id="Categoria-created"
                name="created"
                hidden={true}
                data-cy="created"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('aapmApp.convenio.modified')}
                id="categoria-modified"
                name="modified"
                hidden={true}
                data-cy="modified"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ModalFooter>
                <Button color="primary" type="submit">
                  <FontAwesomeIcon icon="plus" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
                <Button color="secondary" onClick={toggleCategoria}>
                  Cancel
                </Button>
              </ModalFooter>
            </ValidatedForm>
          </ModalBody>
        </Modal>
      </div>

      {/* ******************* */}
      {/* Modal Redes Sociais */}
      {/* ******************* */}

      <div>
        <Modal isOpen={modalRedesSociais} toggle={toggleRedesSociais} style={{ marginTop: '20vh' }}>
          <ModalHeader toggle={toggleRedesSociais}>Nova Rede Social </ModalHeader>
          <ModalBody>
            <ValidatedForm onSubmit={saveRedeSocial}>
              <ValidatedField
                label={'Rede Social'}
                id="redes-sociais-convenio-icon"
                name="icon"
                required
                data-cy="icon"
                type="select"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              >
                <option value="" key="0" />
                {iconsRedesSociais
                  ? iconsRedesSociais.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nome}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('aapmApp.redesSociaisConvenio.nome')}
                id="redes-sociais-convenio-nome"
                name="nome"
                data-cy="nome"
                hidden={true}
                type="text"
                Value={'Rede Social'}
              />
              <ValidatedField
                label={translate('aapmApp.redesSociaisConvenio.descricao')}
                id="redes-sociais-convenio-descricao"
                name="descricao"
                hidden={true}
                data-cy="descricao"
                type="text"
              />
              <ValidatedField
                label={translate('aapmApp.redesSociaisConvenio.endereco')}
                id="redes-sociais-convenio-endereco"
                name="endereco"
                data-cy="endereco"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('aapmApp.redesSociaisConvenio.created')}
                id="redes-sociais-convenio-created"
                name="created"
                hidden={true}
                data-cy="created"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('aapmApp.redesSociaisConvenio.modified')}
                id="redes-sociais-convenio-modified"
                name="modified"
                data-cy="modified"
                hidden={true}
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />

              {/*<ValidatedField*/}
              {/*  id="redes-sociais-convenio-convenio"*/}
              {/*  name="convenio"*/}
              {/*  data-cy="convenio"*/}
              {/*  label={translate('aapmApp.redesSociaisConvenio.convenio')}*/}
              {/*  type="select"*/}
              {/*>*/}
              {/*  <option value="" key="0" />*/}
              {/*      <option value={convenioEntity.id} key={convenioEntity.id}>*/}
              {/*        {convenioEntity.nome}*/}
              {/*      </option>*/}
              {/*    ))*/}
              {/*    : null}*/}
              {/*</ValidatedField>*/}
              <ModalFooter>
                <Button color="primary" type="submit">
                  <FontAwesomeIcon icon="plus" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
                <Button color="secondary" onClick={toggleRedesSociais}>
                  Cancel
                </Button>
              </ModalFooter>
            </ValidatedForm>
          </ModalBody>
        </Modal>
      </div>
    </div>
  );
};

export default ConvenioUpdate;
