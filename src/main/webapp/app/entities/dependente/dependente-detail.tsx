import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './dependente.reducer';
import isAdm from 'app/components/is-adm';

export const DependenteDetail = () => {
  isAdm();
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const dependenteEntity = useAppSelector(state => state.dependente.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="dependenteDetailsHeading">
          <Translate contentKey="aapmApp.dependente.detail.title">Dependente</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{dependenteEntity.id}</dd>
          <dt>
            <span id="nome">
              <Translate contentKey="aapmApp.dependente.nome">Nome</Translate>
            </span>
          </dt>
          <dd>{dependenteEntity.nome}</dd>
          <dt>
            <span id="dataNascimento">
              <Translate contentKey="aapmApp.dependente.dataNascimento">Data Nascimento</Translate>
            </span>
          </dt>
          <dd>
            {dependenteEntity.dataNascimento ? (
              <TextFormat value={dependenteEntity.dataNascimento} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="parentesco">
              <Translate contentKey="aapmApp.dependente.parentesco">Parentesco</Translate>
            </span>
          </dt>
          <dd>{dependenteEntity.parentesco}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="aapmApp.dependente.status">Status</Translate>
            </span>
          </dt>
          <dd>{dependenteEntity.status}</dd>
          {/*<dt>*/}
          {/*  <span id="created">*/}
          {/*    <Translate contentKey="aapmApp.dependente.created">Created</Translate>*/}
          {/*  </span>*/}
          {/*</dt>*/}
          {/*<dd>{dependenteEntity.created ? <TextFormat value={dependenteEntity.created} type="date" format={APP_DATE_FORMAT} /> : null}</dd>*/}
          {/*<dt>*/}
          {/*  <span id="modified">*/}
          {/*    <Translate contentKey="aapmApp.dependente.modified">Modified</Translate>*/}
          {/*  </span>*/}
          {/*</dt>*/}
          <dd>
            {dependenteEntity.modified ? <TextFormat value={dependenteEntity.modified} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="aapmApp.dependente.associado">Associado</Translate>
          </dt>
          <dd>{dependenteEntity.associado ? dependenteEntity.associado.nome : ''}</dd>
        </dl>
        <Button
          tag={Link}
          to={`/associado/${dependenteEntity.associado ? dependenteEntity.associado.id : ''}`}
          replace
          color="info"
          data-cy="entityDetailsBackButton"
        >
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/dependente/${dependenteEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DependenteDetail;
