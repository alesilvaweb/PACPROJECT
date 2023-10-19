import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './reserva.reducer';

export const ReservaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const reservaEntity = useAppSelector(state => state.reserva.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="reservaDetailsHeading">
          <Translate contentKey="aapmApp.reserva.detail.title">Reserva</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{reservaEntity.id}</dd>
          <dt>
            <span id="motivoReserva">
              <Translate contentKey="aapmApp.reserva.motivoReserva">Motivo Reserva</Translate>
            </span>
          </dt>
          <dd>{reservaEntity.motivoReserva}</dd>
          <dt>
            <span id="descricao">
              <Translate contentKey="aapmApp.reserva.descricao">Descricao</Translate>
            </span>
          </dt>
          <dd>{reservaEntity.descricao}</dd>
          <dt>
            <span id="numPessoas">
              <Translate contentKey="aapmApp.reserva.numPessoas">Num Pessoas</Translate>
            </span>
          </dt>
          <dd>{reservaEntity.numPessoas}</dd>
          <dt>
            <span id="data">
              <Translate contentKey="aapmApp.reserva.data">Data</Translate>
            </span>
          </dt>
          <dd>{reservaEntity.data ? <TextFormat value={reservaEntity.data} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="somenteFuncionarios">
              <Translate contentKey="aapmApp.reserva.somenteFuncionarios">Somente Funcionarios</Translate>
            </span>
          </dt>
          <dd>{reservaEntity.somenteFuncionarios ? 'true' : 'false'}</dd>
          <dt>
            <span id="created">
              <Translate contentKey="aapmApp.reserva.created">Created</Translate>
            </span>
          </dt>
          <dd>{reservaEntity.created ? <TextFormat value={reservaEntity.created} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="modified">
              <Translate contentKey="aapmApp.reserva.modified">Modified</Translate>
            </span>
          </dt>
          <dd>{reservaEntity.modified ? <TextFormat value={reservaEntity.modified} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="aapmApp.reserva.local">Local</Translate>
          </dt>
          <dd>{reservaEntity.local ? reservaEntity.local.nome : ''}</dd>
          <dt>
            <Translate contentKey="aapmApp.reserva.associado">Associado</Translate>
          </dt>
          <dd>{reservaEntity.associado ? reservaEntity.associado.nome : ''}</dd>
          <dt>
            <Translate contentKey="aapmApp.reserva.departamento">Departamento</Translate>
          </dt>
          <dd>{reservaEntity.departamento ? reservaEntity.departamento.nome : ''}</dd>
        </dl>
        <Button tag={Link} to="/reserva" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/reserva/${reservaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReservaDetail;
