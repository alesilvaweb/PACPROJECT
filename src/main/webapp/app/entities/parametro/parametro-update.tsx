import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IParametro } from 'app/shared/model/parametro.model';
import { Status } from 'app/shared/model/enumerations/status.model';
import { getEntity, updateEntity, createEntity, reset } from './parametro.reducer';
import Breadcrunbs from 'app/components/breadcrunbs';

export const ParametroUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const parametroEntity = useAppSelector(state => state.parametro.entity);
  const loading = useAppSelector(state => state.parametro.loading);
  const updating = useAppSelector(state => state.parametro.updating);
  const updateSuccess = useAppSelector(state => state.parametro.updateSuccess);
  const statusValues = Object.keys(Status);

  const handleClose = () => {
    navigate('/parametro' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
      ...parametroEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
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
          ...parametroEntity,
          created: convertDateTimeFromServer(parametroEntity.created),
          modified: convertDateTimeFromServer(parametroEntity.modified),
        };

  return (
    <div>
      <Breadcrunbs atual={!isNew ? parametroEntity.parametro : 'Novo'} />
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="aapmApp.parametro.home.createOrEditLabel" data-cy="ParametroCreateUpdateHeading">
            <Translate contentKey="aapmApp.parametro.home.createOrEditLabel">Create or edit a Parametro</Translate>
          </h2>
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
                  readOnly
                  id="parametro-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('aapmApp.parametro.parametro')}
                id="parametro-parametro"
                name="parametro"
                data-cy="parametro"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('aapmApp.parametro.descricao')}
                id="parametro-descricao"
                name="descricao"
                data-cy="descricao"
                type="text"
              />
              <ValidatedField
                label={translate('aapmApp.parametro.chave')}
                id="parametro-chave"
                readOnly={!isNew}
                name="chave"
                data-cy="chave"
                type="text"
              />
              <ValidatedField
                label={translate('aapmApp.parametro.valor')}
                id="parametro-valor"
                name="valor"
                data-cy="valor"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('aapmApp.parametro.status')}
                id="parametro-status"
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
                label={translate('aapmApp.parametro.created')}
                id="parametro-created"
                name="created"
                data-cy="created"
                hidden={true}
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('aapmApp.parametro.modified')}
                id="parametro-modified"
                name="modified"
                data-cy="modified"
                hidden={true}
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/parametro" replace color="info">
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
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ParametroUpdate;
