import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, BreadcrumbItem, Breadcrumb } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ILocal } from 'app/shared/model/local.model';
import { Status } from 'app/shared/model/enumerations/status.model';
import { getEntity, updateEntity, createEntity, reset } from './local.reducer';
import Breadcrunbs from 'app/components/breadcrunbs';

export const LocalUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const localEntity = useAppSelector(state => state.local.entity);
  const loading = useAppSelector(state => state.local.loading);
  const updating = useAppSelector(state => state.local.updating);
  const updateSuccess = useAppSelector(state => state.local.updateSuccess);
  const statusValues = Object.keys(Status);

  const handleClose = () => {
    navigate('/local' + location.search);
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
      ...localEntity,
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
          ...localEntity,
          created: convertDateTimeFromServer(localEntity.created),
          modified: convertDateTimeFromServer(localEntity.modified),
        };

  return (
    <div>
      <Breadcrumb>
        <BreadcrumbItem onClick={() => navigate('/')}>
          <a>Início</a>
        </BreadcrumbItem>
        <BreadcrumbItem onClick={() => navigate('/local')}>
          <a>Locais</a>
        </BreadcrumbItem>
        <BreadcrumbItem active>{isNew ? 'Novo' : localEntity.nome}</BreadcrumbItem>
      </Breadcrumb>
      <Row className="justify-content-center">
        <Col md="8">
          <h3 id="aapmApp.local.home.createOrEditLabel" data-cy="LocalCreateUpdateHeading">
            <Translate contentKey="aapmApp.local.home.createOrEditLabel">Create or edit a Local</Translate>
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
                  hidden={true}
                  readOnly
                  id="local-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('aapmApp.local.nome')}
                id="local-nome"
                name="nome"
                data-cy="nome"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 2, message: translate('entity.validation.minlength', { min: 2 }) },
                }}
              />
              <ValidatedField
                label={translate('aapmApp.local.descricao')}
                id="local-descricao"
                name="descricao"
                data-cy="descricao"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('aapmApp.local.capacidade')}
                id="local-capacidade"
                name="capacidade"
                data-cy="capacidade"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedBlobField
                label={translate('aapmApp.local.imagen')}
                id="local-imagen"
                name="imagen"
                data-cy="imagen"
                isImage
                accept="image/*"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('aapmApp.local.observacoes')}
                id="local-observacoes"
                name="observacoes"
                data-cy="observacoes"
                type="textarea"
              />
              {/*<ValidatedField*/}
              {/*  label={translate('aapmApp.local.localizacao')}*/}
              {/*  id="local-localizacao"*/}
              {/*  name="localizacao"*/}
              {/*  data-cy="localizacao"*/}
              {/*  type="text"*/}
              {/*/>*/}
              <ValidatedField label={translate('aapmApp.local.status')} id="local-status" name="status" data-cy="status" type="select">
                {statusValues.map(status => (
                  <option value={status} key={status}>
                    {translate('aapmApp.Status.' + status)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('aapmApp.local.valor')}
                id="local-valor"
                name="valor"
                data-cy="valor"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              {/*<ValidatedField label={translate('aapmApp.local.cor')} id="local-cor" name="cor" data-cy="cor" type="text" />*/}
              <ValidatedField
                label={translate('aapmApp.local.created')}
                id="local-created"
                name="created"
                data-cy="created"
                hidden={true}
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('aapmApp.local.modified')}
                id="local-modified"
                name="modified"
                hidden={true}
                data-cy="modified"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/local" replace color="info">
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

export default LocalUpdate;
