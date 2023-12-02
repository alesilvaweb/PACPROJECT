import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, Breadcrumb, BreadcrumbItem } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITipo } from 'app/shared/model/tipo.model';
import { getEntities as getTipos } from 'app/entities/tipo/tipo.reducer';
import { IMensagem } from 'app/shared/model/mensagem.model';
import { Status } from 'app/shared/model/enumerations/status.model';
import { getEntity, updateEntity, createEntity, reset } from './mensagem.reducer';

export const MensagemUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const tipos = useAppSelector(state => state.tipo.entities);
  const mensagemEntity = useAppSelector(state => state.mensagem.entity);
  const loading = useAppSelector(state => state.mensagem.loading);
  const updating = useAppSelector(state => state.mensagem.updating);
  const updateSuccess = useAppSelector(state => state.mensagem.updateSuccess);
  const statusValues = Object.keys(Status);

  const handleClose = () => {
    navigate('/mensagem' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTipos({}));
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
      ...mensagemEntity,
      ...values,
      tipo: tipos.find(it => it.id.toString() === values.tipo.toString()),
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
          ...mensagemEntity,
          created: convertDateTimeFromServer(mensagemEntity.created),
          modified: convertDateTimeFromServer(mensagemEntity.modified),
          tipo: mensagemEntity?.tipo?.id,
        };

  return (
    <div>
      <Breadcrumb>
        <BreadcrumbItem onClick={() => navigate('/')}>
          <a>Início</a>
        </BreadcrumbItem>
        <BreadcrumbItem onClick={() => navigate('/mensagem')}>
          <a>Mensagens</a>
        </BreadcrumbItem>
        <BreadcrumbItem active>{isNew ? 'Novo' : mensagemEntity.titulo}</BreadcrumbItem>
      </Breadcrumb>
      <Row className="justify-content-center">
        <Col md="8">
          <h3 id="aapmApp.mensagem.home.createOrEditLabel" data-cy="MensagemCreateUpdateHeading">
            {isNew ? 'Nova Mensagem' : 'Editar Mensagem'}
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
                  id="mensagem-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('aapmApp.mensagem.titulo')}
                id="mensagem-titulo"
                name="titulo"
                data-cy="titulo"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('aapmApp.mensagem.descricao')}
                id="mensagem-descricao"
                name="descricao"
                data-cy="descricao"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('aapmApp.mensagem.conteudo')}
                id="mensagem-conteudo"
                name="conteudo"
                hidden
                data-cy="conteudo"
                type="textarea"
              />
              <ValidatedBlobField
                label={translate('aapmApp.mensagem.imagen')}
                id="mensagem-imagen"
                name="imagen"
                data-cy="imagen"
                isImage
                accept="image/*"
              />
              <ValidatedField label={translate('aapmApp.mensagem.link')} id="mensagem-link" name="link" data-cy="link" type="text" />
              <ValidatedField
                label={translate('aapmApp.mensagem.startDate')}
                id="mensagem-startDate"
                name="startDate"
                data-cy="startDate"
                type="date"
              />
              <ValidatedField
                label={translate('aapmApp.mensagem.endDate')}
                id="mensagem-endDate"
                name="endDate"
                data-cy="endDate"
                type="date"
              />
              <ValidatedField
                label={translate('aapmApp.mensagem.status')}
                id="mensagem-status"
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
                label={translate('aapmApp.mensagem.created')}
                id="mensagem-created"
                name="created"
                hidden={true}
                data-cy="created"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('aapmApp.mensagem.modified')}
                id="mensagem-modified"
                name="modified"
                data-cy="modified"
                hidden={true}
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="mensagem-tipo" name="tipo" data-cy="tipo" label={translate('aapmApp.mensagem.tipo')} type="select">
                <option value="" key="0" />
                {tipos
                  ? tipos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.tipo}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/mensagem" replace color="info">
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

export default MensagemUpdate;
