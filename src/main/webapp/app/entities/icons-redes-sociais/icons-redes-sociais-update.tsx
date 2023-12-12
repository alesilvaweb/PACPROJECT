import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, translate, ValidatedBlobField, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { createEntity, getEntity, reset, updateEntity } from './icons-redes-sociais.reducer';
import isAdm from 'app/components/is-adm';

export const IconsRedesSociaisUpdate = () => {
  const dispatch = useAppDispatch();
  isAdm();
  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const iconsRedesSociaisEntity = useAppSelector(state => state.iconsRedesSociais.entity);
  const loading = useAppSelector(state => state.iconsRedesSociais.loading);
  const updating = useAppSelector(state => state.iconsRedesSociais.updating);
  const updateSuccess = useAppSelector(state => state.iconsRedesSociais.updateSuccess);

  const handleClose = () => {
    navigate('/icons-redes-sociais' + location.search);
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
    const entity = {
      ...iconsRedesSociaisEntity,
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
      ? {}
      : {
          ...iconsRedesSociaisEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="aapmApp.iconsRedesSociais.home.createOrEditLabel" data-cy="IconsRedesSociaisCreateUpdateHeading">
            {isNew ? 'Nova Rede Social' : iconsRedesSociaisEntity.nome}
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
                  id="icons-redes-sociais-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('aapmApp.iconsRedesSociais.nome')}
                id="icons-redes-sociais-nome"
                name="nome"
                data-cy="nome"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('aapmApp.iconsRedesSociais.descricao')}
                id="icons-redes-sociais-descricao"
                name="descricao"
                hidden
                data-cy="descricao"
                type="text"
              />
              <ValidatedField
                label={translate('aapmApp.iconsRedesSociais.icon')}
                id="icons-redes-sociais-icon"
                name="icon"
                hidden
                data-cy="icon"
                type="text"
              />
              <ValidatedBlobField
                label={translate('aapmApp.iconsRedesSociais.image')}
                id="icons-redes-sociais-image"
                name="image"
                hidden
                data-cy="image"
                isImage
                accept="image/*"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/icons-redes-sociais" replace color="info">
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

export default IconsRedesSociaisUpdate;
