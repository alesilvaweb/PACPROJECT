import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { byteSize, openFile, TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './arquivo.reducer';
import isAdm from 'app/components/is-adm';

export const ArquivoDetail = () => {
  isAdm();
  const dispatch = useAppDispatch();
  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const arquivoEntity = useAppSelector(state => state.arquivo.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="arquivoDetailsHeading">
          <Translate contentKey="aapmApp.arquivo.detail.title">Arquivo</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{arquivoEntity.id}</dd>
          <dt>
            <span id="nome">
              <Translate contentKey="aapmApp.arquivo.nome">Nome</Translate>
            </span>
          </dt>
          <dd>{arquivoEntity.nome}</dd>
          <dt>
            <span id="descricao">
              <Translate contentKey="aapmApp.arquivo.descricao">Descricao</Translate>
            </span>
          </dt>
          <dd>{arquivoEntity.descricao}</dd>
          <dt>
            <span id="arquivo">
              <Translate contentKey="aapmApp.arquivo.arquivo">Arquivo</Translate>
            </span>
          </dt>
          <dd>
            {arquivoEntity.arquivo ? (
              <div>
                {arquivoEntity.arquivoContentType ? (
                  <a onClick={openFile(arquivoEntity.arquivoContentType, arquivoEntity.arquivo)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {arquivoEntity.arquivoContentType}, {byteSize(arquivoEntity.arquivo)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="status">
              <Translate contentKey="aapmApp.arquivo.status">Status</Translate>
            </span>
          </dt>
          <dd>{arquivoEntity.status}</dd>
          <dt>
            <span id="created">
              <Translate contentKey="aapmApp.arquivo.created">Created</Translate>
            </span>
          </dt>
          <dd>{arquivoEntity.created ? <TextFormat value={arquivoEntity.created} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="modified">
              <Translate contentKey="aapmApp.arquivo.modified">Modified</Translate>
            </span>
          </dt>
          <dd>{arquivoEntity.modified ? <TextFormat value={arquivoEntity.modified} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/arquivo" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/arquivo/${arquivoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ArquivoDetail;
