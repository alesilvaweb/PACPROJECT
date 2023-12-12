import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './tipo.reducer';
import isAdm from 'app/components/is-adm';

export const TipoDetail = () => {
  isAdm();
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const tipoEntity = useAppSelector(state => state.tipo.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tipoDetailsHeading">
          <Translate contentKey="aapmApp.tipo.detail.title">Tipo</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{tipoEntity.id}</dd>
          <dt>
            <span id="tipo">
              <Translate contentKey="aapmApp.tipo.tipo">Tipo</Translate>
            </span>
          </dt>
          <dd>{tipoEntity.tipo}</dd>
          <dt>
            <span id="chave">
              <Translate contentKey="aapmApp.tipo.chave">Chave</Translate>
            </span>
          </dt>
          <dd>{tipoEntity.chave}</dd>
        </dl>
        <Button tag={Link} to="/tipo" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tipo/${tipoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TipoDetail;
