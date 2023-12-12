import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './desconto-convenio.reducer';
import isAdm from 'app/components/is-adm';

export const DescontoConvenioDetail = () => {
  const dispatch = useAppDispatch();
  isAdm();
  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const descontoConvenioEntity = useAppSelector(state => state.descontoConvenio.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="descontoConvenioDetailsHeading">
          <Translate contentKey="aapmApp.descontoConvenio.detail.title">DescontoConvenio</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{descontoConvenioEntity.id}</dd>
          <dt>
            <span id="desconto">
              <Translate contentKey="aapmApp.descontoConvenio.desconto">Desconto</Translate>
            </span>
          </dt>
          <dd>{descontoConvenioEntity.desconto}</dd>
          <dt>
            <span id="descricao">
              <Translate contentKey="aapmApp.descontoConvenio.descricao">Descricao</Translate>
            </span>
          </dt>
          <dd>{descontoConvenioEntity.descricao}</dd>
          <dt>
            <Translate contentKey="aapmApp.descontoConvenio.convenio">Convenio</Translate>
          </dt>
          <dd>{descontoConvenioEntity.convenio ? descontoConvenioEntity.convenio.nome : ''}</dd>
        </dl>
        <Button tag={Link} to="/desconto-convenio" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/desconto-convenio/${descontoConvenioEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DescontoConvenioDetail;
