import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './redes-sociais-convenio.reducer';

export const RedesSociaisConvenioDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const redesSociaisConvenioEntity = useAppSelector(state => state.redesSociaisConvenio.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="redesSociaisConvenioDetailsHeading">
          <Translate contentKey="aapmApp.redesSociaisConvenio.detail.title">RedesSociaisConvenio</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{redesSociaisConvenioEntity.id}</dd>
          <dt>
            <span id="nome">
              <Translate contentKey="aapmApp.redesSociaisConvenio.nome">Nome</Translate>
            </span>
          </dt>
          <dd>{redesSociaisConvenioEntity.nome}</dd>
          <dt>
            <span id="descricao">
              <Translate contentKey="aapmApp.redesSociaisConvenio.descricao">Descricao</Translate>
            </span>
          </dt>
          <dd>{redesSociaisConvenioEntity.descricao}</dd>
          <dt>
            <span id="endereco">
              <Translate contentKey="aapmApp.redesSociaisConvenio.endereco">Endereco</Translate>
            </span>
          </dt>
          <dd>{redesSociaisConvenioEntity.endereco}</dd>
          <dt>
            <span id="created">
              <Translate contentKey="aapmApp.redesSociaisConvenio.created">Created</Translate>
            </span>
          </dt>
          <dd>
            {redesSociaisConvenioEntity.created ? (
              <TextFormat value={redesSociaisConvenioEntity.created} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="modified">
              <Translate contentKey="aapmApp.redesSociaisConvenio.modified">Modified</Translate>
            </span>
          </dt>
          <dd>
            {redesSociaisConvenioEntity.modified ? (
              <TextFormat value={redesSociaisConvenioEntity.modified} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="aapmApp.redesSociaisConvenio.icon">Icon</Translate>
          </dt>
          <dd>{redesSociaisConvenioEntity.icon ? redesSociaisConvenioEntity.icon.nome : ''}</dd>
          <dt>
            <Translate contentKey="aapmApp.redesSociaisConvenio.convenio">Convenio</Translate>
          </dt>
          <dd>{redesSociaisConvenioEntity.convenio ? redesSociaisConvenioEntity.convenio.nome : ''}</dd>
        </dl>
        <Button tag={Link} to="/redes-sociais-convenio" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/redes-sociais-convenio/${redesSociaisConvenioEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RedesSociaisConvenioDetail;
