import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './contato.reducer';
import isAdm from 'app/components/is-adm';

export const ContatoDetail = () => {
  isAdm();
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const contatoEntity = useAppSelector(state => state.contato.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="contatoDetailsHeading">
          <Translate contentKey="aapmApp.contato.detail.title">Contato</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{contatoEntity.id}</dd>
          <dt>
            <span id="tipo">
              <Translate contentKey="aapmApp.contato.tipo">Tipo</Translate>
            </span>
          </dt>
          <dd>{contatoEntity.tipo}</dd>
          <dt>
            <span id="contato">
              <Translate contentKey="aapmApp.contato.contato">Contato</Translate>
            </span>
          </dt>
          <dd>{contatoEntity.contato}</dd>
          <dt>
            <span id="created">
              <Translate contentKey="aapmApp.contato.created">Created</Translate>
            </span>
          </dt>
          <dd>{contatoEntity.created ? <TextFormat value={contatoEntity.created} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="modified">
              <Translate contentKey="aapmApp.contato.modified">Modified</Translate>
            </span>
          </dt>
          <dd>{contatoEntity.modified ? <TextFormat value={contatoEntity.modified} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="aapmApp.contato.associado">Associado</Translate>
          </dt>
          <dd>{contatoEntity.associado ? contatoEntity.associado.nome : ''}</dd>
        </dl>
        <Button tag={Link} to="/contato" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/contato/${contatoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ContatoDetail;
