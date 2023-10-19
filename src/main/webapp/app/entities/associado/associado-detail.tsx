import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './associado.reducer';

export const AssociadoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const associadoEntity = useAppSelector(state => state.associado.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="associadoDetailsHeading">
          <Translate contentKey="aapmApp.associado.detail.title">Associado</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{associadoEntity.id}</dd>
          <dt>
            <span id="nome">
              <Translate contentKey="aapmApp.associado.nome">Nome</Translate>
            </span>
          </dt>
          <dd>{associadoEntity.nome}</dd>
          <dt>
            <span id="matricula">
              <Translate contentKey="aapmApp.associado.matricula">Matricula</Translate>
            </span>
          </dt>
          <dd>{associadoEntity.matricula}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="aapmApp.associado.status">Status</Translate>
            </span>
          </dt>
          <dd>{associadoEntity.status}</dd>
          <dt>
            <span id="telefone">
              <Translate contentKey="aapmApp.associado.telefone">Telefone</Translate>
            </span>
          </dt>
          <dd>{associadoEntity.telefone}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="aapmApp.associado.email">Email</Translate>
            </span>
          </dt>
          <dd>{associadoEntity.email}</dd>
          <dt>
            <span id="dataNascimento">
              <Translate contentKey="aapmApp.associado.dataNascimento">Data Nascimento</Translate>
            </span>
          </dt>
          <dd>
            {associadoEntity.dataNascimento ? (
              <TextFormat value={associadoEntity.dataNascimento} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="created">
              <Translate contentKey="aapmApp.associado.created">Created</Translate>
            </span>
          </dt>
          <dd>{associadoEntity.created ? <TextFormat value={associadoEntity.created} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="modified">
              <Translate contentKey="aapmApp.associado.modified">Modified</Translate>
            </span>
          </dt>
          <dd>{associadoEntity.modified ? <TextFormat value={associadoEntity.modified} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/associado" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/associado/${associadoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AssociadoDetail;
