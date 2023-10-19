import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './convenio.reducer';

export const ConvenioDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const convenioEntity = useAppSelector(state => state.convenio.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="convenioDetailsHeading">
          <Translate contentKey="aapmApp.convenio.detail.title">Convenio</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{convenioEntity.id}</dd>
          <dt>
            <span id="nome">
              <Translate contentKey="aapmApp.convenio.nome">Nome</Translate>
            </span>
          </dt>
          <dd>{convenioEntity.nome}</dd>
          <dt>
            <span id="titulo">
              <Translate contentKey="aapmApp.convenio.titulo">Titulo</Translate>
            </span>
          </dt>
          <dd>{convenioEntity.titulo}</dd>
          <dt>
            <span id="descricao">
              <Translate contentKey="aapmApp.convenio.descricao">Descricao</Translate>
            </span>
          </dt>
          <dd>{convenioEntity.descricao}</dd>
          <dt>
            <span id="endereco">
              <Translate contentKey="aapmApp.convenio.endereco">Endereco</Translate>
            </span>
          </dt>
          <dd>{convenioEntity.endereco}</dd>
          <dt>
            <span id="telefone">
              <Translate contentKey="aapmApp.convenio.telefone">Telefone</Translate>
            </span>
          </dt>
          <dd>{convenioEntity.telefone}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="aapmApp.convenio.email">Email</Translate>
            </span>
          </dt>
          <dd>{convenioEntity.email}</dd>
          <dt>
            <span id="localizacao">
              <Translate contentKey="aapmApp.convenio.localizacao">Localizacao</Translate>
            </span>
          </dt>
          <dd>{convenioEntity.localizacao}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="aapmApp.convenio.status">Status</Translate>
            </span>
          </dt>
          <dd>{convenioEntity.status}</dd>
          <dt>
            <span id="created">
              <Translate contentKey="aapmApp.convenio.created">Created</Translate>
            </span>
          </dt>
          <dd>{convenioEntity.created ? <TextFormat value={convenioEntity.created} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="modified">
              <Translate contentKey="aapmApp.convenio.modified">Modified</Translate>
            </span>
          </dt>
          <dd>{convenioEntity.modified ? <TextFormat value={convenioEntity.modified} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="aapmApp.convenio.categoria">Categoria</Translate>
          </dt>
          <dd>{convenioEntity.categoria ? convenioEntity.categoria.categoria : ''}</dd>
        </dl>
        <Button tag={Link} to="/convenio" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/convenio/${convenioEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ConvenioDetail;
