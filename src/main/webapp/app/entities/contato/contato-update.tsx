import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAssociado } from 'app/shared/model/associado.model';
import { getEntities as getAssociados } from 'app/entities/associado/associado.reducer';
import { IContato } from 'app/shared/model/contato.model';
import { TipoContato } from 'app/shared/model/enumerations/tipo-contato.model';
import { getEntity, updateEntity, createEntity, reset } from './contato.reducer';

export const ContatoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const associados = useAppSelector(state => state.associado.entities);
  const contatoEntity = useAppSelector(state => state.contato.entity);
  const loading = useAppSelector(state => state.contato.loading);
  const updating = useAppSelector(state => state.contato.updating);
  const updateSuccess = useAppSelector(state => state.contato.updateSuccess);
  const tipoContatoValues = Object.keys(TipoContato);

  const handleClose = () => {
    navigate('/contato' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAssociados({}));
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
      ...contatoEntity,
      ...values,
      associado: associados.find(it => it.id.toString() === values.associado.toString()),
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
          tipo: 'Telefone',
          ...contatoEntity,
          created: convertDateTimeFromServer(contatoEntity.created),
          modified: convertDateTimeFromServer(contatoEntity.modified),
          associado: contatoEntity?.associado?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="aapmApp.contato.home.createOrEditLabel" data-cy="ContatoCreateUpdateHeading">
            <Translate contentKey="aapmApp.contato.home.createOrEditLabel">Create or edit a Contato</Translate>
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
                  id="contato-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('aapmApp.contato.tipo')} id="contato-tipo" name="tipo" data-cy="tipo" type="select">
                {tipoContatoValues.map(tipoContato => (
                  <option value={tipoContato} key={tipoContato}>
                    {translate('aapmApp.TipoContato.' + tipoContato)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('aapmApp.contato.contato')}
                id="contato-contato"
                name="contato"
                data-cy="contato"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('aapmApp.contato.created')}
                id="contato-created"
                name="created"
                data-cy="created"
                hidden={true}
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('aapmApp.contato.modified')}
                id="contato-modified"
                name="modified"
                data-cy="modified"
                hidden={true}
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="contato-associado"
                name="associado"
                data-cy="associado"
                label={translate('aapmApp.contato.associado')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/contato" replace color="info">
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

export default ContatoUpdate;
