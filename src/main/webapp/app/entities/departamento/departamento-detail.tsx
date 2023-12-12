import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, BreadcrumbItem, Breadcrumb } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './departamento.reducer';
import isAdm from 'app/components/is-adm';

export const DepartamentoDetail = () => {
  isAdm();
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);
  const navigate = useNavigate();
  const departamentoEntity = useAppSelector(state => state.departamento.entity);
  return (
    <Row>
      <Breadcrumb>
        <BreadcrumbItem onClick={() => navigate('/')}>
          <a>Início</a>
        </BreadcrumbItem>
        <BreadcrumbItem onClick={() => navigate('/departamento')}>
          <a>Departamentos</a>
        </BreadcrumbItem>
        <BreadcrumbItem active>{departamentoEntity.nome}</BreadcrumbItem>
      </Breadcrumb>
      <Col md="8">
        <h3 data-cy="departamentoDetailsHeading">
          <Translate contentKey="aapmApp.departamento.detail.title">Departamento</Translate>
        </h3>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{departamentoEntity.id}</dd>
          <dt>
            <span id="nome">
              <Translate contentKey="aapmApp.departamento.nome">Nome</Translate>
            </span>
          </dt>
          <dd>{departamentoEntity.nome}</dd>
          <dt>
            <span id="descricao">
              <Translate contentKey="aapmApp.departamento.descricao">Descricao</Translate>
            </span>
          </dt>
          <dd>{departamentoEntity.descricao}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="aapmApp.departamento.status">Status</Translate>
            </span>
          </dt>
          <dd>{departamentoEntity.status}</dd>
          <dt>
            <span id="created">
              <Translate contentKey="aapmApp.departamento.created">Created</Translate>
            </span>
          </dt>
          <dd>
            {departamentoEntity.created ? <TextFormat value={departamentoEntity.created} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="modified">
              <Translate contentKey="aapmApp.departamento.modified">Modified</Translate>
            </span>
          </dt>
          <dd>
            {departamentoEntity.modified ? <TextFormat value={departamentoEntity.modified} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        <Button tag={Link} to="/departamento" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/departamento/${departamentoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DepartamentoDetail;
