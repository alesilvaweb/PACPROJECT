import './../stylesEntities.scss';
import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { StatusReserva } from 'app/shared/model/enumerations/status-reserva.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime, formatData } from 'app/shared/util/date-utils';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import Box from '@mui/material/Box';
import { getEntities as getLocals } from 'app/entities/local/local.reducer';
import { getEntities as getAssociados } from 'app/entities/associado/associado.reducer';
import { getEntities as getDepartamentos } from 'app/entities/departamento/departamento.reducer';
import { createEntity, getEntity, reset, updateEntity } from './reserva.reducer';

export const ReservaUpdate = () => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const { local } = useParams<'local'>();
  const { start } = useParams<'start'>();
  const isNew = id === undefined;

  const locals = useAppSelector(state => state.local.entities);
  const associados = useAppSelector(state => state.associado.entities);
  const departamentos = useAppSelector(state => state.departamento.entities);
  const reservaEntity = useAppSelector(state => state.reserva.entity);
  const locaisEntity = useAppSelector(state => state.local.entity);
  const loading = useAppSelector(state => state.reserva.loading);
  const updating = useAppSelector(state => state.reserva.updating);
  const updateSuccess = useAppSelector(state => state.reserva.updateSuccess);
  const [localId, setLocalId] = useState('');
  const statusReservaValues = Object.keys(StatusReserva);

  const handleClose = () => {
    dispatch(reset());
    navigate('/local/' + localId + '/1');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
      // @ts-ignore
      dispatch(getLocals(local));
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getLocals({}));
    dispatch(getAssociados({}));
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

    const entity = {
      ...reservaEntity,
      ...values,
      local: locals.find(it => it.id.toString() === values.local.toString()),
      associado: associados.find(it => it.id.toString() === values.associado.toString()),
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
          local: reservaEntity?.local?.id,
          associado: reservaEntity?.associado?.id,
          departamento: reservaEntity?.departamento?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          {/*<Translate contentKey="aapmApp.reserva.home.createOrEditLabel">Create or edit a Reserva</Translate>*/}
          {!isNew ? (
            <>
              <h4 id="aapmApp.reserva.home.createOrEditLabel" data-cy="ReservaCreateUpdateHeading">
                <span>Editar reserva</span>
              </h4>
              <Box component="fieldset">
                <legend>Informações de reserva</legend>
                {`Nome : ${account.firstName} ${account.lastName}`}&nbsp;&nbsp;- &nbsp; {`Local : ${locaisEntity.nome} `}&nbsp;&nbsp;-
                &nbsp; {`Data : ${reservaEntity.data}`}
              </Box>
            </>
          ) : (
            <>
              <h4 id="aapmApp.reserva.home.createOrEditLabel" data-cy="ReservaCreateUpdateHeading">
                Nova reserva
              </h4>
              <Box component="fieldset">
                {`Nome : ${account.firstName} ${account.lastName}`}&nbsp;&nbsp;- &nbsp; {`Local : ${locaisEntity.nome} `}&nbsp;&nbsp;-
                &nbsp; {`Data : ${formatData(start)}`}
              </Box>
            </>
          )}
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <Box component="fieldset">
              <legend>Informações adicionais</legend>
              <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
                {!isNew ? (
                  <ValidatedField
                    name="id"
                    required
                    hidden={true}
                    readOnly
                    id="reserva-id"
                    label={translate('global.field.id')}
                    validate={{ required: true }}
                  />
                ) : null}
                <ValidatedField
                  label={translate('aapmApp.reserva.motivoReserva')}
                  id="reserva-motivoReserva"
                  name="motivoReserva"
                  data-cy="motivoReserva"
                  type="text"
                  validate={{
                    required: { value: true, message: translate('entity.validation.required') },
                    minLength: { value: 2, message: translate('entity.validation.minlength', { min: 2 }) },
                  }}
                />
                <ValidatedField
                  label={translate('aapmApp.reserva.descricao')}
                  id="reserva-descricao"
                  name="descricao"
                  hidden={true}
                  data-cy="descricao"
                  type="text"
                />

                <ValidatedField
                  label={translate('aapmApp.reserva.numPessoas')}
                  id="reserva-numPessoas"
                  name="numPessoas"
                  data-cy="numPessoas"
                  type="text"
                  validate={{
                    required: { value: true, message: translate('entity.validation.required') },
                    validate: v => isNumber(v) || translate('entity.validation.number'),
                  }}
                />
                <ValidatedField
                  label={translate('aapmApp.reserva.status')}
                  id="reserva-status"
                  name="status"
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

                <ValidatedField
                  label={translate('aapmApp.reserva.data')}
                  id="reservas-data"
                  name="data"
                  data-cy="data"
                  hidden={true}
                  defaultValue={start}
                  type="date"
                  validate={{
                    required: { value: true, message: translate('entity.validation.required') },
                  }}
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
                {isNew ? (
                  <ValidatedField
                    label={translate('aapmApp.reserva.local')}
                    id="reservas-local"
                    name="local"
                    required={true}
                    hidden={true}
                    data-cy="local"
                    value={locaisEntity.id}
                    type="text"
                    validate={{
                      required: { value: true, message: translate('entity.validation.required') },
                    }}
                  />
                ) : (
                  <ValidatedField
                    id="reservas-local"
                    name="local"
                    data-cy="local"
                    label={translate('aapmApp.reserva.local')}
                    type="select"
                    required={true}
                    hidden={true}
                    validate={{
                      required: { value: true, message: translate('entity.validation.required') },
                    }}
                  >
                    <option value="" key="0" />
                    {locals
                      ? locals.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.nome}
                          </option>
                        ))
                      : null}
                  </ValidatedField>
                )}
                <ValidatedField
                  id="reserva-associado"
                  name="associado"
                  hidden={true}
                  data-cy="associado"
                  value={account.id}
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

                <ValidatedField
                  label={translate('aapmApp.reserva.somenteFuncionarios')}
                  id="reserva-somenteFuncionarios"
                  name="somenteFuncionarios"
                  data-cy="somenteFuncionarios"
                  check
                  type="checkbox"
                />
                <br />
                <Col md={12} style={{ display: 'flex', justifyContent: 'flex-end' }}>
                  {!isNew ? (
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
                  &nbsp;
                  <Button tag={Link} id="cancel" data-cy="entityCreateCancelButton" to={'/local/' + local} replace color="secondary">
                    &nbsp; Cancelar
                  </Button>
                  &nbsp;
                  <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                    <FontAwesomeIcon icon="save" />
                    &nbsp;
                    <Translate contentKey="entity.action.save">Save</Translate>
                  </Button>
                </Col>
              </ValidatedForm>
            </Box>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ReservaUpdate;
