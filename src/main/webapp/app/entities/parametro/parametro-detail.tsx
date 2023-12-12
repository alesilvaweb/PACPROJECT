import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Breadcrumb, BreadcrumbItem, Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './parametro.reducer';
import isAdm from 'app/components/is-adm';

export const ParametroDetail = () => {
  const dispatch = useAppDispatch();
  isAdm();
  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);
  const navigate = useNavigate();
  const parametroEntity = useAppSelector(state => state.parametro.entity);
  return (
    <Row>
      <Breadcrumb>
        <BreadcrumbItem onClick={() => navigate('/')}>
          <a>Início</a>
        </BreadcrumbItem>
        <BreadcrumbItem onClick={() => navigate('/parametro')}>
          <a>Parâmetros</a>
        </BreadcrumbItem>
        <BreadcrumbItem active>{parametroEntity.parametro}</BreadcrumbItem>
      </Breadcrumb>
      <Col md="8">
        <h3 data-cy="parametroDetailsHeading">
          <Translate contentKey="aapmApp.parametro.detail.title">Parametro</Translate>
        </h3>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{parametroEntity.id}</dd>
          <dt>
            <span id="parametro">
              <Translate contentKey="aapmApp.parametro.parametro">Parametro</Translate>
            </span>
          </dt>
          <dd>{parametroEntity.parametro}</dd>
          <dt>
            <span id="descricao">
              <Translate contentKey="aapmApp.parametro.descricao">Descricao</Translate>
            </span>
          </dt>
          <dd>{parametroEntity.descricao}</dd>
          <dt>
            <span id="chave">
              <Translate contentKey="aapmApp.parametro.chave">Chave</Translate>
            </span>
          </dt>
          <dd>{parametroEntity.chave}</dd>
          <dt>
            <span id="valor">
              <Translate contentKey="aapmApp.parametro.valor">Valor</Translate>
            </span>
          </dt>
          <dd>{parametroEntity.valor}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="aapmApp.parametro.status">Status</Translate>
            </span>
          </dt>
          <dd>{parametroEntity.status}</dd>
          <dt>
            <span id="created">
              <Translate contentKey="aapmApp.parametro.created">Created</Translate>
            </span>
          </dt>
          <dd>{parametroEntity.created ? <TextFormat value={parametroEntity.created} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="modified">
              <Translate contentKey="aapmApp.parametro.modified">Modified</Translate>
            </span>
          </dt>
          <dd>{parametroEntity.modified ? <TextFormat value={parametroEntity.modified} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/parametro" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/parametro/${parametroEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ParametroDetail;
