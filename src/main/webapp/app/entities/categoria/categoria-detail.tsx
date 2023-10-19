import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './categoria.reducer';

export const CategoriaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const categoriaEntity = useAppSelector(state => state.categoria.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="categoriaDetailsHeading">
          <Translate contentKey="aapmApp.categoria.detail.title">Categoria</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{categoriaEntity.id}</dd>
          <dt>
            <span id="categoria">
              <Translate contentKey="aapmApp.categoria.categoria">Categoria</Translate>
            </span>
          </dt>
          <dd>{categoriaEntity.categoria}</dd>
          <dt>
            <span id="descricao">
              <Translate contentKey="aapmApp.categoria.descricao">Descricao</Translate>
            </span>
          </dt>
          <dd>{categoriaEntity.descricao}</dd>
          <dt>
            <span id="created">
              <Translate contentKey="aapmApp.categoria.created">Created</Translate>
            </span>
          </dt>
          <dd>{categoriaEntity.created ? <TextFormat value={categoriaEntity.created} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="modified">
              <Translate contentKey="aapmApp.categoria.modified">Modified</Translate>
            </span>
          </dt>
          <dd>{categoriaEntity.modified ? <TextFormat value={categoriaEntity.modified} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/categoria" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/categoria/${categoriaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CategoriaDetail;
