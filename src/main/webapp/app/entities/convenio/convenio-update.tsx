import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICategoria } from 'app/shared/model/categoria.model';
import { getEntities as getCategorias } from 'app/entities/categoria/categoria.reducer';
import { IConvenio } from 'app/shared/model/convenio.model';
import { Status } from 'app/shared/model/enumerations/status.model';
import { getEntity, updateEntity, createEntity, reset } from './convenio.reducer';

export const ConvenioUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const categorias = useAppSelector(state => state.categoria.entities);
  const convenioEntity = useAppSelector(state => state.convenio.entity);
  const loading = useAppSelector(state => state.convenio.loading);
  const updating = useAppSelector(state => state.convenio.updating);
  const updateSuccess = useAppSelector(state => state.convenio.updateSuccess);
  const statusValues = Object.keys(Status);

  const handleClose = () => {
    navigate('/convenio' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCategorias({}));
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
      ...convenioEntity,
      ...values,
      categoria: categorias.find(it => it.id.toString() === values.categoria.toString()),
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
          ...convenioEntity,
          created: convertDateTimeFromServer(convenioEntity.created),
          modified: convertDateTimeFromServer(convenioEntity.modified),
          categoria: convenioEntity?.categoria?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="aapmApp.convenio.home.createOrEditLabel" data-cy="ConvenioCreateUpdateHeading">
            <Translate contentKey="aapmApp.convenio.home.createOrEditLabel">Create or edit a Convenio</Translate>
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
                  id="convenio-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('aapmApp.convenio.nome')}
                id="convenio-nome"
                name="nome"
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
                type="text"
              />
              <ValidatedField label={translate('aapmApp.convenio.email')} id="convenio-email" name="email" data-cy="email" type="text" />
              <ValidatedField
                label={translate('aapmApp.convenio.localizacao')}
                id="convenio-localizacao"
                name="localizacao"
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
                data-cy="created"
                hidden={true}
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('aapmApp.convenio.modified')}
                id="convenio-modified"
                name="modified"
                data-cy="modified"
                hidden={true}
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="convenio-categoria"
                name="categoria"
                data-cy="categoria"
                label={translate('aapmApp.convenio.categoria')}
                type="select"
              >
                <option value="" key="0" />
                {categorias
                  ? categorias.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.categoria}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/convenio" replace color="info">
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

export default ConvenioUpdate;
