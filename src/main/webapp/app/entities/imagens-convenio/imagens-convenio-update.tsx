import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, translate, ValidatedBlobField, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getConvenios } from 'app/entities/convenio/convenio.reducer';
import { createEntity, getEntity, reset, updateEntity } from './imagens-convenio.reducer';
import isAdm from 'app/components/is-adm';

export const ImagensConvenioUpdate = () => {
  const dispatch = useAppDispatch();
  isAdm();
  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const convenios = useAppSelector(state => state.convenio.entities);
  const imagensConvenioEntity = useAppSelector(state => state.imagensConvenio.entity);
  const loading = useAppSelector(state => state.imagensConvenio.loading);
  const updating = useAppSelector(state => state.imagensConvenio.updating);
  const updateSuccess = useAppSelector(state => state.imagensConvenio.updateSuccess);

  const handleClose = () => {
    navigate('/imagens-convenio' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getConvenios({}));
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
      ...imagensConvenioEntity,
      ...values,
      convenio: convenios.find(it => it.id.toString() === values.convenio.toString()),
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
          ...imagensConvenioEntity,
          created: convertDateTimeFromServer(imagensConvenioEntity.created),
          modified: convertDateTimeFromServer(imagensConvenioEntity.modified),
          convenio: imagensConvenioEntity?.convenio?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="aapmApp.imagensConvenio.home.createOrEditLabel" data-cy="ImagensConvenioCreateUpdateHeading">
            <Translate contentKey="aapmApp.imagensConvenio.home.createOrEditLabel">Create or edit a ImagensConvenio</Translate>
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
                  id="imagens-convenio-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('aapmApp.imagensConvenio.titulo')}
                id="imagens-convenio-titulo"
                name="titulo"
                data-cy="titulo"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('aapmApp.imagensConvenio.descricao')}
                id="imagens-convenio-descricao"
                name="descricao"
                data-cy="descricao"
                type="text"
              />
              <ValidatedBlobField
                label={translate('aapmApp.imagensConvenio.imagem')}
                id="imagens-convenio-imagem"
                name="imagem"
                data-cy="imagem"
                isImage
                accept="image/*"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('aapmApp.imagensConvenio.created')}
                id="imagens-convenio-created"
                name="created"
                data-cy="created"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('aapmApp.imagensConvenio.modified')}
                id="imagens-convenio-modified"
                name="modified"
                data-cy="modified"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="imagens-convenio-convenio"
                name="convenio"
                data-cy="convenio"
                label={translate('aapmApp.imagensConvenio.convenio')}
                type="select"
              >
                <option value="" key="0" />
                {convenios
                  ? convenios.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nome}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/imagens-convenio" replace color="info">
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

export default ImagensConvenioUpdate;
