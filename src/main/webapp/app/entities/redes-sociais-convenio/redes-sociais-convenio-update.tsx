import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getIconsRedesSociais } from 'app/entities/icons-redes-sociais/icons-redes-sociais.reducer';
import { getEntities as getConvenios } from 'app/entities/convenio/convenio.reducer';
import { createEntity, getEntity, reset, updateEntity } from './redes-sociais-convenio.reducer';
import isAdm from 'app/components/is-adm';

export const RedesSociaisConvenioUpdate = () => {
  const dispatch = useAppDispatch();
  isAdm();
  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const iconsRedesSociais = useAppSelector(state => state.iconsRedesSociais.entities);
  const convenios = useAppSelector(state => state.convenio.entities);
  const redesSociaisConvenioEntity = useAppSelector(state => state.redesSociaisConvenio.entity);
  const loading = useAppSelector(state => state.redesSociaisConvenio.loading);
  const updating = useAppSelector(state => state.redesSociaisConvenio.updating);
  const updateSuccess = useAppSelector(state => state.redesSociaisConvenio.updateSuccess);

  const handleClose = () => {
    navigate('/redes-sociais-convenio' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getIconsRedesSociais({}));
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
      ...redesSociaisConvenioEntity,
      ...values,
      icon: iconsRedesSociais.find(it => it.id.toString() === values.icon.toString()),
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
          ...redesSociaisConvenioEntity,
          created: convertDateTimeFromServer(redesSociaisConvenioEntity.created),
          modified: convertDateTimeFromServer(redesSociaisConvenioEntity.modified),
          icon: redesSociaisConvenioEntity?.icon?.id,
          convenio: redesSociaisConvenioEntity?.convenio?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="aapmApp.redesSociaisConvenio.home.createOrEditLabel" data-cy="RedesSociaisConvenioCreateUpdateHeading">
            <Translate contentKey="aapmApp.redesSociaisConvenio.home.createOrEditLabel">Create or edit a RedesSociaisConvenio</Translate>
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
                  id="redes-sociais-convenio-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('aapmApp.redesSociaisConvenio.nome')}
                id="redes-sociais-convenio-nome"
                name="nome"
                data-cy="nome"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('aapmApp.redesSociaisConvenio.descricao')}
                id="redes-sociais-convenio-descricao"
                name="descricao"
                data-cy="descricao"
                type="text"
              />
              <ValidatedField
                label={translate('aapmApp.redesSociaisConvenio.endereco')}
                id="redes-sociais-convenio-endereco"
                name="endereco"
                data-cy="endereco"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('aapmApp.redesSociaisConvenio.created')}
                id="redes-sociais-convenio-created"
                name="created"
                hidden={true}
                data-cy="created"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('aapmApp.redesSociaisConvenio.modified')}
                id="redes-sociais-convenio-modified"
                name="modified"
                data-cy="modified"
                hidden={true}
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="redes-sociais-convenio-icon"
                name="icon"
                data-cy="icon"
                label={translate('aapmApp.redesSociaisConvenio.icon')}
                type="select"
              >
                <option value="" key="0" />
                {iconsRedesSociais
                  ? iconsRedesSociais.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nome}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="redes-sociais-convenio-convenio"
                name="convenio"
                data-cy="convenio"
                label={translate('aapmApp.redesSociaisConvenio.convenio')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/redes-sociais-convenio" replace color="info">
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

export default RedesSociaisConvenioUpdate;
