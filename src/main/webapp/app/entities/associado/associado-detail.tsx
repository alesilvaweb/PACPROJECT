import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './associado.reducer';
import axios from 'axios';
import { formatData } from 'app/shared/util/date-utils';

export const AssociadoDetail = () => {
  const dispatch = useAppDispatch();
  const [dependentes, setDependentes] = useState([]);
  const { id } = useParams<'id'>();

  async function fetchDependentes() {
    try {
      const response = await axios.get(`api/dependentes?associadoId.equals=${id}`);
      setDependentes(response.data);
    } catch (error) {
      console.error('Erro ao buscar Dependentes:', error);
    }
  }
  useEffect(() => {
    dispatch(getEntity(id));
    fetchDependentes().then(r => null);
  }, []);

  const associadoEntity = useAppSelector(state => state.associado.entity);
  return (
    <Row>
      <Col md="12">
        <h2 data-cy="associadoDetailsHeading">
          <Translate contentKey="aapmApp.associado.detail.title">Associado</Translate>
        </h2>
        <dl style={{ display: 'flex', justifyContent: 'space-between', flexWrap: 'wrap' }}>
          {/*<dt>*/}
          {/*  <span id="id">*/}
          {/*    <Translate contentKey="global.field.id">ID</Translate>*/}
          {/*  </span>*/}
          {/*</dt>*/}
          {/*<dd>{associadoEntity.id}</dd>*/}
          <Col xs={12} sm={12} md={12}>
            <dt>
              <span id="nome">
                <Translate contentKey="aapmApp.associado.nome">Nome</Translate>
              </span>
            </dt>
            <dd>{associadoEntity.nome}</dd>
          </Col>
          <Col xs={4} sm={4} md={2}>
            <dt>
              <span id="matricula">
                <Translate contentKey="aapmApp.associado.matricula">Matricula</Translate>
              </span>
            </dt>
            <dd>{associadoEntity.matricula}</dd>
          </Col>
          <Col xs={4} sm={4} md={2}>
            <dt>
              <span id="status">
                <Translate contentKey="aapmApp.associado.status">Status</Translate>
              </span>
            </dt>
            <dd>{associadoEntity.status}</dd>
          </Col>
          <Col xs={4} sm={4} md={2}>
            <dt>
              <span id="telefone">
                <Translate contentKey="aapmApp.associado.telefone">Telefone</Translate>
              </span>
            </dt>
            <dd>{associadoEntity.telefone}</dd>
          </Col>
          <Col xs={8} sm={8} md={4}>
            <dt>
              <span id="email">
                <Translate contentKey="aapmApp.associado.email">Email</Translate>
              </span>
            </dt>
            <dd>{associadoEntity.email}</dd>
          </Col>
          <Col xs={4} sm={4} md={2}>
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
          </Col>
        </dl>
        {dependentes.length > 0 ? <h4>Dependentes</h4> : null}
        <>
          {dependentes.map(dep => (
            <ul>
              <li>
                <a href={`/dependente/${dep.id}`}>
                  {dep.id} - {dep.nome} - {dep.parentesco} - {formatData(dep.dataNascimento)} - {dep.status}{' '}
                </a>{' '}
              </li>
            </ul>
          ))}
        </>
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
