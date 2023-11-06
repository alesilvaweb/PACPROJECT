import './../stylesEntities.scss';
import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Breadcrumb, BreadcrumbItem, Button, Col, ModalFooter, Row } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { StatusReserva } from 'app/shared/model/enumerations/status-reserva.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import Box from '@mui/material/Box';
import { getEntity as getLocal } from 'app/entities/local/local.reducer';
import { getEntity as getAssociado } from 'app/entities/associado/associado.reducer';
import { getEntities as getDepartamentos } from 'app/entities/departamento/departamento.reducer';
import { createEntity, getEntity, reset, updateEntity } from './reserva.reducer';
import { values } from 'lodash';

export const ReservaUpdate = () => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const { local } = useParams<'local'>();
  const { start } = useParams<'start'>();
  const isNew = id === undefined;

  const locals = useAppSelector(state => state.local.entities);

  const departamentos = useAppSelector(state => state.departamento.entities);
  const reservaEntity = useAppSelector(state => state.reserva.entity);
  const associado = useAppSelector(state => state.associado.entity);
  const localEntity = useAppSelector(state => state.local.entity);
  const loading = useAppSelector(state => state.reserva.loading);
  const updating = useAppSelector(state => state.reserva.updating);
  const updateSuccess = useAppSelector(state => state.reserva.updateSuccess);
  const [localId, setLocalId] = useState('');
  const statusReservaValues = Object.keys(StatusReserva);
  const [cancelarReserva, setCancelarReserva] = useState(false);

  const handleClose = () => {
    dispatch(reset());
    navigate('/local/' + local + '/1');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
      // @ts-ignore
      dispatch(getLocal(local));
    } else {
      dispatch(getEntity(id));
    }
    dispatch(getAssociado(account.id));
    dispatch(getDepartamentos({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.created = convertDateTimeToServer(values.created);
    values.modified = convertDateTimeToServer(values.modified);
    console.log({ values });
    const entity = {
      ...reservaEntity,
      ...values,
      local: localEntity,
      associado: associado,
      departamento: departamentos.find(it => it.id.toString() === values.departamento.toString()),
    };

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
        }
      : {
          ...reservaEntity,
          created: convertDateTimeFromServer(reservaEntity.created),
          modified: convertDateTimeFromServer(reservaEntity.modified),
          numPessoas: reservaEntity.numPessoas,
          local: reservaEntity?.local?.id,
          associado: reservaEntity?.associado?.id,
          departamento: reservaEntity?.departamento?.id,
        };

  console.log({ localEntity });
  // @ts-ignore
  return (
    <>
      {loading ? (
        <p>Loading...</p>
      ) : (
        <div>
          <Breadcrumb>
            <BreadcrumbItem onClick={() => navigate('/')}>
              <a>início</a>
            </BreadcrumbItem>
            <BreadcrumbItem onClick={() => navigate('/cabanas')}>
              <a>Cabanas</a>
            </BreadcrumbItem>
            <BreadcrumbItem onClick={() => navigate(`/local/${local}/1`)}>
              <a>{localEntity.nome}</a>
            </BreadcrumbItem>
            <BreadcrumbItem active>Reserva</BreadcrumbItem>
          </Breadcrumb>
          {/*<Modal isOpen toggle={handleClose}  fullscreen={"sm"} style={{ marginTop: '10vh' }} >*/}
          {/*  <ModalHeader toggle={handleClose}>*/}
          <Row className="justify-content-center">
            <Col md="8" sx={{ textAlign: 'center' }}>
              {/*<Translate contentKey="aapmApp.reserva.home.createOrEditLabel">Create or edit a Reserva</Translate>*/}
              {!isNew ? <h4>Editar Reserva {localEntity.nome}</h4> : <h4>Reserva {localEntity.nome}</h4>}
            </Col>
          </Row>
          {/*</ModalHeader>*/}
          {/*<ModalBody>*/}
          {/*<Row className="justify-content-center">*/}
          {/*  <Col md="8">*/}
          {/*    /!*<Translate contentKey="aapmApp.reserva.home.createOrEditLabel">Create or edit a Reserva</Translate>*!/*/}
          {/*    {!isNew ? (*/}
          {/*      <>*/}
          {/*        <div>*/}
          {/*          <div style={{display: 'flex', justifyContent: 'space-between'}}>*/}
          {/*            <span className={'info-reserva'}>{`Nome : ${account.firstName} ${account.lastName} `}</span>*/}
          {/*            <span className={'info-reserva'}>{`Data : ${ reservaEntity.data  } N ${ localEntity.capacidade  }`}</span>*/}
          {/*          </div>*/}
          {/*        </div>*/}
          {/*      </>*/}
          {/*    ) : (*/}
          {/*      <>*/}
          {/*        <div>*/}
          {/*          <div style={{display: 'flex', justifyContent: 'space-between'}}>*/}
          {/*            <span className={'info-reserva'}>{`Nome : ${associado.nome} N ${ localEntity.numPessoas }`}</span>*/}
          {/*            /!*<span className={'info-reserva'}>{`Data : ${formatData(start)}`}</span>*!/*/}
          {/*          </div>*/}
          {/*        </div>*/}
          {/*      </>*/}
          {/*    )}*/}
          {/*  </Col>*/}

          {/*</Row>*/}
          <br />
          <Row className="justify-content-center">
            <Col md="8">
              <Box component="fieldset">
                <legend>Preencha as informações abaixo</legend>

                <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity} key={'form1'}>
                  {/* Motivo da Reserva */}
                  <ValidatedField
                    label={translate('aapmApp.reserva.motivoReserva')}
                    id="reserva-motivoReserva"
                    name="motivoReserva"
                    defaultValue={reservaEntity.motivoReserva}
                    data-cy="motivoReserva"
                    type="text"
                    validate={{
                      required: { value: true, message: translate('entity.validation.required') },
                      minLength: { value: 2, message: translate('entity.validation.minlength', { min: 2 }) },
                    }}
                  />

                  {/* Numero de Pessoas */}
                  <ValidatedField
                    label={translate('aapmApp.reserva.numPessoas')}
                    id="reserva-numPessoas"
                    name="numPessoas"
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

                  {/* Data */}
                  <ValidatedField
                    label={translate('aapmApp.reserva.data')}
                    id="reservas-data"
                    name="data"
                    data-cy="data"
                    hidden={true}
                    Value={isNew ? start : reservaEntity.data}
                    type="date"
                    readonly={true}
                    validate={{
                      required: { value: true, message: translate('entity.validation.required') },
                    }}
                  />
                  <ValidatedField
                    label={translate('aapmApp.reserva.data')}
                    id="reservas-data-see"
                    name="datasee"
                    disabled={true}
                    Value={isNew ? start : reservaEntity.data}
                    type="date"
                    readonly={true}
                  />

                  {/* Departamento */}
                  <ValidatedField
                    id="reserva-departamento"
                    name="departamento"
                    data-cy="departamento"
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

                  <div className="form-group row">
                    <div className="col-md-6">
                      {/* Associado */}
                      <ValidatedField
                        id="reserva-associado"
                        name="associado"
                        data-cy="associado"
                        defaultValue={account.id}
                        value={account.id}
                        label={translate('aapmApp.reserva.associado')}
                        type="select"
                      >
                        <option value={associado.id} key={associado.id}>
                          {associado.nome}
                        </option>
                      </ValidatedField>
                    </div>

                    <div className="col-md-6">
                      {/* Local */}
                      <ValidatedField
                        id="reservas-local"
                        name="local"
                        data-cy="local"
                        label={translate('aapmApp.reserva.local')}
                        type="select"
                        defaultValue={reservaEntity?.local?.id}
                        required={true}
                        hidden={false}
                        validate={{
                          required: { value: true, message: translate('entity.validation.required') },
                        }}
                      >
                        <option value={localEntity.id} key={localEntity.id}>
                          {localEntity.nome}
                        </option>
                      </ValidatedField>
                    </div>
                  </div>

                  {/* Somente Funcionários */}
                  <ValidatedField
                    label={translate('aapmApp.reserva.somenteFuncionarios')}
                    id="reserva-somenteFuncionarios"
                    name="somenteFuncionarios"
                    data-cy="somenteFuncionarios"
                    defaultValue={reservaEntity?.somenteFuncionarios}
                    check
                    type="checkbox"
                  />

                  {/* Cancelar Reserva */}
                  {!isNew ? (
                    <ValidatedField
                      label={'Cancelar reserva'}
                      id="cancelarReserva"
                      name="cancelarReserva"
                      check
                      onChange={() => setCancelarReserva(!cancelarReserva)}
                      type="checkbox"
                    />
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
                    label={translate('aapmApp.reserva.descricao')}
                    id="reserva-descricao"
                    name="descricao"
                    defaultValue={reservaEntity?.descricao}
                    hidden={true}
                    data-cy="descricao"
                    type="text"
                  />

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
                              &nbsp; Cancelar Reserva
                              {/*<Translate contentKey="entity.action.back">Delete</Translate>*/}
                            </Button>
                          ) : null}
                        </div>
                      </div>
                    ) : null}
                    {!cancelarReserva ? (
                      <div className={'d-flex justify-content-between'}>
                        <Button tag={Link} id="cancel" data-cy="entityCreateCancelButton" to={'/local/' + local} replace color="info">
                          <FontAwesomeIcon icon="cancel" />
                          &nbsp; Cancelar
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

export default ReservaUpdate;
