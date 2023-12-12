import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getConvenios } from 'app/entities/convenio/convenio.reducer';
import { createEntity, getEntity, reset, updateEntity } from './desconto-convenio.reducer';
import isAdm from 'app/components/is-adm';

export const DescontoConvenioUpdate = () => {
  const dispatch = useAppDispatch();
  isAdm();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();
  const isNew = id === undefined;
  const convenios = useAppSelector(state => state.convenio.entities);
  const descontoConvenioEntity = useAppSelector(state => state.descontoConvenio.entity);
  const loading = useAppSelector(state => state.descontoConvenio.loading);
  const updating = useAppSelector(state => state.descontoConvenio.updating);
  const updateSuccess = useAppSelector(state => state.descontoConvenio.updateSuccess);

  const handleClose = () => {
    navigate('/desconto-convenio' + location.search);
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
    const entity = {
      ...descontoConvenioEntity,
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
      ? {}
      : {
          ...descontoConvenioEntity,
          convenio: descontoConvenioEntity?.convenio?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="aapmApp.descontoConvenio.home.createOrEditLabel" data-cy="DescontoConvenioCreateUpdateHeading">
            <Translate contentKey="aapmApp.descontoConvenio.home.createOrEditLabel">Create or edit a DescontoConvenio</Translate>
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
                  id="desconto-convenio-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('aapmApp.descontoConvenio.desconto')}
                id="desconto-convenio-desconto"
                name="desconto"
                data-cy="desconto"
                type="text"
              />
              <ValidatedField
                label={translate('aapmApp.descontoConvenio.descricao')}
                id="desconto-convenio-descricao"
                name="descricao"
                data-cy="descricao"
                type="text"
              />
              <ValidatedField
                id="desconto-convenio-convenio"
                name="convenio"
                data-cy="convenio"
                label={translate('aapmApp.descontoConvenio.convenio')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/desconto-convenio" replace color="info">
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

export default DescontoConvenioUpdate;
