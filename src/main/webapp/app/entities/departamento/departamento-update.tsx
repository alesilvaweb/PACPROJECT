import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Breadcrumb, BreadcrumbItem, Button, Col, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Status } from 'app/shared/model/enumerations/status.model';
import { createEntity, getEntity, reset, updateEntity } from './departamento.reducer';
import isAdm from 'app/components/is-adm';

export const DepartamentoUpdate = () => {
  isAdm();
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const departamentoEntity = useAppSelector(state => state.departamento.entity);
  const loading = useAppSelector(state => state.departamento.loading);
  const updating = useAppSelector(state => state.departamento.updating);
  const updateSuccess = useAppSelector(state => state.departamento.updateSuccess);
  const statusValues = Object.keys(Status);

  const handleClose = () => {
    navigate('/departamento' + location.search);
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
      ...departamentoEntity,
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
          ...departamentoEntity,
          created: convertDateTimeFromServer(departamentoEntity.created),
          modified: convertDateTimeFromServer(departamentoEntity.modified),
        };

  return (
    <div>
      <Breadcrumb>
        <BreadcrumbItem onClick={() => navigate('/')}>
          <a>Início</a>
        </BreadcrumbItem>
        <BreadcrumbItem onClick={() => navigate('/departamento')}>
          <a>Departamentos</a>
        </BreadcrumbItem>
        <BreadcrumbItem active>{isNew ? 'Novo' : departamentoEntity.nome}</BreadcrumbItem>
      </Breadcrumb>
      <Row className="justify-content-center">
        <Col md="8">
          <h3 id="aapmApp.departamento.home.createOrEditLabel" data-cy="DepartamentoCreateUpdateHeading">
            {isNew ? 'Novo Departamento' : departamentoEntity.nome}
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
                  id="departamento-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('aapmApp.departamento.nome')}
                id="departamento-nome"
                name="nome"
                data-cy="nome"
                type="text"
              />
              <ValidatedField
                label={translate('aapmApp.departamento.descricao')}
                id="departamento-descricao"
                name="descricao"
                data-cy="descricao"
                type="text"
              />
              <ValidatedField
                label={translate('aapmApp.departamento.status')}
                id="departamento-status"
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
                label={translate('aapmApp.departamento.created')}
                id="departamento-created"
                name="created"
                data-cy="created"
                hidden={true}
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('aapmApp.departamento.modified')}
                id="departamento-modified"
                name="modified"
                data-cy="modified"
                hidden={true}
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/departamento" replace color="info">
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

export default DepartamentoUpdate;
