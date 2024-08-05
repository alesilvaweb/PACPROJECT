import './../stylesEntities.scss';
import './reservas.scss';
import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Breadcrumb, BreadcrumbItem, Button, Col, Modal, ModalBody, ModalFooter, ModalHeader, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { StatusReserva } from 'app/shared/model/enumerations/status-reserva.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import Box from '@mui/material/Box';
import { getEntity as getLocal } from 'app/entities/local/local.reducer';
import { getEntities as getAssociados, getEntity as getAssociado } from 'app/entities/associado/associado.reducer';
import { getEntities as getDepartamentos } from 'app/entities/departamento/departamento.reducer';
import { createEntity, getEntity, reset, updateEntity } from './reserva.reducer';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';
import Spinner from 'app/components/spinner';

export const ReservaUpdateQuadra = () => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const navigate = useNavigate();
  const isAdmin = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.ADMIN]));

  const { id } = useParams<'id'>();
  const { local } = useParams<'local'>();
  const { start } = useParams<'start'>();
  const { end } = useParams<'end'>();

  const isNew = id === undefined;

  const locals = useAppSelector(state => state.local.entities);

  const departamentos = useAppSelector(state => state.departamento.entities);
  const reservaEntity = useAppSelector(state => state.reserva.entity);
  const associado = useAppSelector(state => state.associado.entity);
  const associados = useAppSelector(state => state.associado.entities);
  const associadoCount = useAppSelector(state => state.associado.totalItems);
  const localEntity = useAppSelector(state => state.local.entity);
  const loading = useAppSelector(state => state.reserva.loading);
  const loadingAssociado = useAppSelector(state => state.associado.loading);
  const updating = useAppSelector(state => state.reserva.updating);
  const updateSuccess = useAppSelector(state => state.reserva.updateSuccess);
  const [localId, setLocalId] = useState('');
  const statusReservaValues = Object.keys(StatusReserva);
  const [cancelarReserva, setCancelarReserva] = useState(false);
  const [bloqueioReserva, setBloqueioReserva] = useState(false);
  const [update, setUpdate] = useState(false);
  const [modal, setModal] = useState(false);

  const toggle = () => setModal(!modal);

  const handleClose = () => {
    dispatch(reset());
    setUpdate(false);
    if (bloqueioReserva) {
      navigate('/local/' + local + '/1');
    } else {
      navigate('/reserva/' + id + '/' + local + '/message');
    }
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
      dispatch(getLocal(local));
      dispatch(getAssociado(account.id));
      dispatch(getAssociados({ size: 1 })).then(value => {
        dispatch(
          getAssociados({
            size: value.payload['headers']['x-total-count'],
            sort: 'nome,asc',
          })
        );
      });
    } else {
      dispatch(getEntity(id)).then(value => {
        dispatch(getAssociado(value.payload['data'].associado.id));
        if (value.payload['data'].status === 'Bloqueado') {
          setBloqueioReserva(true);
        }
      });
    }
    dispatch(
      getAssociados({
        size: associadoCount,
        sort: 'nome,asc',
      })
    );

    dispatch(getDepartamentos({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    setUpdate(true);
    values.created = convertDateTimeToServer(values.created);
    values.modified = convertDateTimeToServer(values.modified);
    // eslint-disable-next-line no-console
    console.log({ values });
    const entity = {
      ...reservaEntity,
      ...values,
      local: localEntity,
      descricao: convertDateTimeFromServer(values.descricao),
      associado: isAdmin ? associados.find(it => it.id.toString() === values.associado.toString()) : associado,
      departamento: departamentos.find(it => it.id.toString() === values.departamento.toString()),
    };
// eslint-disable-next-line no-console
    console.log({ entity });
    if (isNew) {
      setLocalId(entity.local.id);
      dispatch(createEntity(entity));
    } else {
      setLocalId(local);
      dispatch(updateEntity(entity));
    }
  };

  if (!localEntity.id) {
    navigate('/local/' + local);
  }

  const defaultValues = () =>
    isNew
      ? {
          created: displayDefaultDateTime(),
          modified: displayDefaultDateTime(),
          associado: account.id,
        }
      : {
          ...reservaEntity,
          created: convertDateTimeFromServer(reservaEntity.created),
          modified: convertDateTimeFromServer(reservaEntity.modified),
          numPessoas: reservaEntity.numPessoas,
          local: reservaEntity?.local?.id,
          descricao: reservaEntity?.descricao,
          associado: reservaEntity?.associado?.id,
          departamento: reservaEntity?.departamento?.id,
        };

  return (
    <>
      {loading || loadingAssociado ? (
        <Spinner action={update ? 'Salvando' : 'Carregando'} text={loading ? 'reserva' : loadingAssociado ? 'associados' : null} />
      ) : (
        <div>
          <Breadcrumb>
            <BreadcrumbItem onClick={() => navigate('/')}>
              <a>início</a>
            </BreadcrumbItem>

            <BreadcrumbItem onClick={() => navigate(`/local/${local}/1`)}>
              <a>{localEntity.nome}</a>
            </BreadcrumbItem>
            <BreadcrumbItem active>Reserva</BreadcrumbItem>
          </Breadcrumb>

{/* ####################################################################################### */}

          <Row className="justify-content-center">
            <Col md="8" sx={{ textAlign: 'center' }}>
              {!isNew ? <h4>Editar Reserva {localEntity.nome}</h4> : <h4>Reserva {localEntity.nome} {end}</h4>}
            </Col>
          </Row>
          <br />
          <Row className="justify-content-center">
            <Col md="8">
              <Box component="fieldset">
                <legend>Preencha as informações abaixo</legend>

                <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity} key={'form-reserva'} name={'form-reserva'}>
                  {/* Motivo da Reserva */}
                  <ValidatedField
                    label={bloqueioReserva ? 'Motivo do Bloqueio' : translate('aapmApp.reserva.motivoReserva')}
                    id="reserva-motivoReserva"
                    name="motivoReserva"
                    className={'motivoReserva'}
                    defaultValue={reservaEntity.motivoReserva}
                    data-cy="motivoReserva"
                    type="text"
                    validate={{
                      required: { value: true, message: translate('entity.validation.required') },
                      minLength: { value: 2, message: translate('entity.validation.minlength', { min: 2 }) },
                    }}
                  />

                  {/* Numero de Pessoas */}
                  {bloqueioReserva ? (
                    <ValidatedField
                      label={translate('aapmApp.reserva.numPessoas')}
                      id="reserva-numPessoas"
                      name="numPessoas"
                      hidden={bloqueioReserva}
                      required={true}
                      data-cy="numPessoas"
                      type="number"
                      value={0}
                      validate={{
                        required: { value: true, message: translate('entity.validation.required') },
                        max: {
                          value: localEntity.capacidade,
                          message: `A ${localEntity.nome} comporta apenas ${localEntity.capacidade} pessoas.`,
                        },
                      }}
                    />
                  ) : (
                    <ValidatedField
                      label={translate('aapmApp.reserva.numPessoas')}
                      id="reserva-numPessoas"
                      name="numPessoas"
                      className={'reserva-numPessoas'}
                      required={true}
                      data-cy="numPessoas"
                      type="number"
                      validate={{
                        required: { value: true, message: translate('entity.validation.required') },
                        max: {
                          value: localEntity.capacidade,
                          message: `A ${localEntity.nome} comporta apenas ${localEntity.capacidade} pessoas.`,
                        },
                      }}
                    />
                  )}

                  {/* Data */}
                  <ValidatedField
                    label={bloqueioReserva ? 'Data Inicial' : translate('aapmApp.reserva.data')}
                    id="reservas-data"
                    name="data"
                    data-cy="data"
                    // hidden={true}
                    Value={isNew ? convertDateTimeFromServer(start)  : reservaEntity.data}
                    type="datetime-local"
                    readonly={true}
                    validate={{
                      required: { value: true, message: translate('entity.validation.required') },
                    }}
                  />
                  <ValidatedField
                    label={'Data Final '}
                    id="reserva-descricao"
                    // hidden={!bloqueioReserva}
                    name="descricao"
                    defaultValue={isNew ? convertDateTimeFromServer(end)  : reservaEntity?.descricao}
                    data-cy="descricao"
                    type="datetime-local"
                  />
                  <ValidatedField
                    label={bloqueioReserva ? 'Data inicial' : translate('aapmApp.reserva.data') }
                    id="reservas-data-see"
                    name="datasee"
                    className={'data-inicial-reserva'}
                    // disabled={true}
                    Value={isNew ?  convertDateTimeFromServer(start)  : convertDateTimeFromServer(reservaEntity.data)}
                    type="datetime-local"
                    // readonly={true}
                  />

                  {isAdmin ? (
                    <ValidatedField
                      label={'Data Final '}
                      id="reserva-descricao"
                      hidden={!bloqueioReserva}
                      name="descricao"
                      defaultValue={isNew ? start: reservaEntity?.descricao}
                      data-cy="descricao"
                      type="date"
                    />
                  ) : null}

                  {/* Departamento */}
                  <ValidatedField
                    id="reserva-departamento"
                    name="departamento"
                    className={'reserva-departamento'}
                    data-cy="departamento"
                    hidden={bloqueioReserva}
                    label={translate('aapmApp.reserva.departamento')}
                    type="select"
                  >
                    <option value="" key="0" />
                    {departamentos
                      ? departamentos.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.nome}
                          </option>
                        ))
                      : null}
                  </ValidatedField>

                  {isAdmin ? (
                    <ValidatedField
                      id="reserva-associado"
                      name="associado"
                      hidden={bloqueioReserva}
                      data-cy="associado"
                      label={translate('aapmApp.reserva.associado')}
                      type="select"
                    >
                      <option value="" key="0" />
                      {associados
                        ? associados.map(otherEntity => (
                            <option value={otherEntity.id} key={otherEntity.id}>
                              {otherEntity.nome}
                            </option>
                          ))
                        : null}
                    </ValidatedField>
                  ) : (
                    <ValidatedField
                      id="reserva-associado"
                      name="associado"
                      data-cy="associado"
                      hidden={bloqueioReserva}
                      value={associado.id}
                      label={translate('aapmApp.reserva.associado')}
                      type="select"
                    >
                      <option value={associado.id} key={associado.id}>
                        {associado.nome}
                      </option>
                    </ValidatedField>
                  )}

                  {/* Local */}
                  <ValidatedField
                    id="reservas-local"
                    name="local"
                    data-cy="local"
                    hidden={bloqueioReserva}
                    label={translate('aapmApp.reserva.local')}
                    type="select"
                    defaultValue={reservaEntity?.local?.id}
                    required={true}
                    validate={{
                      required: { value: true, message: translate('entity.validation.required') },
                    }}
                  >
                    <option value={localEntity.id} key={localEntity.id}>
                      {localEntity.nome}
                    </option>
                  </ValidatedField>

                  {/* Somente Funcionários */}
                  <ValidatedField
                    label={translate('aapmApp.reserva.somenteFuncionarios')}
                    id="reserva-somenteFuncionarios"
                    name="somenteFuncionarios"
                    hidden={bloqueioReserva}
                    data-cy="somenteFuncionarios"
                    check
                    type="checkbox"
                  />

                  {/* Cancelar Reserva */}
                  {!isNew ? (
                    <ValidatedField
                      label={bloqueioReserva ? 'Cancelar Bloqueio' : 'Cancelar Reserva'}
                      id="cancelarReserva"
                      name="cancelarReserva"
                      check
                      onChange={() => setCancelarReserva(!cancelarReserva)}
                      type="checkbox"
                    />
                  ) : null}

                  {/* Reserva  */}
                  {isAdmin ? (
                    <>
                      {isNew ? (
                        <ValidatedField
                          label={'Bloqueio de Datas'}
                          id="bloqueioReserva"
                          name="bloqueioReserva"
                          check
                          onChange={() => setBloqueioReserva(!bloqueioReserva)}
                          type="checkbox"
                        />
                      ) : null}
                    </>
                  ) : null}

                  {/* Campos Hidden */}
                  {!isNew ? (
                    <ValidatedField
                      name="id"
                      required
                      hidden={true}
                      readOnly
                      defaultValue={reservaEntity?.id}
                      somenteFuncionarios
                      label={translate('global.field.id')}
                      validate={{ required: true }}
                    />
                  ) : null}

                  <ValidatedField
                    label={translate('aapmApp.reserva.created')}
                    id="reserva-created"
                    name="created"
                    hidden={true}
                    data-cy="created"
                    type="datetime-local"
                    placeholder="YYYY-MM-DD HH:mm"
                  />

                  <ValidatedField
                    label={translate('aapmApp.reserva.modified')}
                    id="reserva-modified"
                    name="modified"
                    hidden={true}
                    data-cy="modified"
                    type="datetime-local"
                    placeholder="YYYY-MM-DD HH:mm"
                  />
                  {bloqueioReserva ? (
                    <ValidatedField
                      label={translate('aapmApp.reserva.status')}
                      id="reserva-status"
                      name="status"
                      value={'Bloqueado'}
                      data-cy="status"
                      type="select"
                      hidden={true}
                    >
                      {statusReservaValues.map(statusReserva => (
                        <option value={statusReserva} key={statusReserva}>
                          {translate('aapmApp.StatusReserva.' + statusReserva)}
                        </option>
                      ))}
                    </ValidatedField>
                  ) : (
                    <ValidatedField
                      label={translate('aapmApp.reserva.status')}
                      id="reserva-status"
                      name="status"
                      defaultValue={reservaEntity?.status}
                      data-cy="status"
                      type="select"
                      hidden={true}
                    >
                      {statusReservaValues.map(statusReserva => (
                        <option value={statusReserva} key={statusReserva}>
                          {translate('aapmApp.StatusReserva.' + statusReserva)}
                        </option>
                      ))}
                    </ValidatedField>
                  )}

                  <br />
                  <hr />
                  <ModalFooter>
                    {!isNew ? (
                      <div className="form-group row">
                        <div className="col-md-12 d-flex justify-content-end">
                          {cancelarReserva ? (
                            <Button
                              tag={Link}
                              id="cancel-save"
                              data-cy="entityCreateCancelButton"
                              to={'/reserva/' + id + '/' + local + '/delete'}
                              replace
                              color="danger"
                            >
                              <FontAwesomeIcon icon="trash" />
                              &nbsp; Cancelar
                              {/*<Translate contentKey="entity.action.back">Delete</Translate>*/}
                            </Button>
                          ) : null}
                        </div>
                      </div>
                    ) : null}
                    {!cancelarReserva ? (
                      <div className={'col-md-12 d-flex justify-content-end'}>
                        <Button tag={Link} id="cancel" data-cy="entityCreateCancelButton" to={'/local/' + local} replace color="info">
                          <FontAwesomeIcon icon="arrow-left" />
                          &nbsp; Voltar
                        </Button>
                        &nbsp; &nbsp;
                        <Button color="primary" id="save-entity2" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                          <FontAwesomeIcon icon="save" />
                          &nbsp;
                          <Translate contentKey="entity.action.save">Save</Translate>
                        </Button>
                      </div>
                    ) : null}
                  </ModalFooter>
                </ValidatedForm>
              </Box>
            </Col>
          </Row>
          {/*  </ModalBody>*/}

          {/*</Modal>*/}
        </div>
      )}
    </>
  );
};

export default ReservaUpdateQuadra;
