import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IArquivo } from 'app/shared/model/arquivo.model';
import { StatusArquivo } from 'app/shared/model/enumerations/status-arquivo.model';
import { getEntity, updateEntity, createEntity, reset } from './arquivo.reducer';

export const ArquivoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const arquivoEntity = useAppSelector(state => state.arquivo.entity);
  const loading = useAppSelector(state => state.arquivo.loading);
  const updating = useAppSelector(state => state.arquivo.updating);
  const updateSuccess = useAppSelector(state => state.arquivo.updateSuccess);
  const statusArquivoValues = Object.keys(StatusArquivo);

  const handleClose = () => {
    navigate('/arquivo' + location.search);
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
      ...arquivoEntity,
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
          status: 'Carregado',
          ...arquivoEntity,
          created: convertDateTimeFromServer(arquivoEntity.created),
          modified: convertDateTimeFromServer(arquivoEntity.modified),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="aapmApp.arquivo.home.createOrEditLabel" data-cy="ArquivoCreateUpdateHeading">
            <Translate contentKey="aapmApp.arquivo.home.createOrEditLabel">Create or edit a Arquivo</Translate>
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
                  id="arquivo-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('aapmApp.arquivo.nome')}
                id="arquivo-nome"
                name="nome"
                data-cy="nome"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('aapmApp.arquivo.descricao')}
                id="arquivo-descricao"
                name="descricao"
                data-cy="descricao"
                type="text"
              />
              <ValidatedBlobField
                label={translate('aapmApp.arquivo.arquivo')}
                id="arquivo-arquivo"
                name="arquivo"
                data-cy="arquivo"
                openActionLabel={translate('entity.action.open')}
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField label={translate('aapmApp.arquivo.status')} id="arquivo-status" name="status" data-cy="status" type="select">
                {statusArquivoValues.map(statusArquivo => (
                  <option value={statusArquivo} key={statusArquivo}>
                    {translate('aapmApp.StatusArquivo.' + statusArquivo)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('aapmApp.arquivo.created')}
                id="arquivo-created"
                name="created"
                hidden={true}
                data-cy="created"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('aapmApp.arquivo.modified')}
                id="arquivo-modified"
                name="modified"
                hidden={true}
                data-cy="modified"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/arquivo" replace color="info">
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

export default ArquivoUpdate;
