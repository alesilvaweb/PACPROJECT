import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Breadcrumb, BreadcrumbItem, Button, Col, Row } from 'reactstrap';
import { openFile, TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './mensagem.reducer';
import isAdm from 'app/components/is-adm';

export const MensagemDetail = () => {
  const dispatch = useAppDispatch();
  isAdm();
  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);
  const navigate = useNavigate();
  const mensagemEntity = useAppSelector(state => state.mensagem.entity);
  return (
    <Row>
      <Breadcrumb>
        <BreadcrumbItem onClick={() => navigate('/')}>
          <a>Início</a>
        </BreadcrumbItem>
        <BreadcrumbItem onClick={() => navigate('/mensagem')}>
          <a>Mensagens</a>
        </BreadcrumbItem>
        <BreadcrumbItem active>{mensagemEntity.titulo}</BreadcrumbItem>
      </Breadcrumb>
      <Col md="8">
        <h2 data-cy="mensagemDetailsHeading">
          <Translate contentKey="aapmApp.mensagem.detail.title">Mensagem</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{mensagemEntity.id}</dd>
          <dt>
            <span id="titulo">
              <Translate contentKey="aapmApp.mensagem.titulo">Titulo</Translate>
            </span>
          </dt>
          <dd>{mensagemEntity.titulo}</dd>
          <dt>
            <span id="descricao">
              <Translate contentKey="aapmApp.mensagem.descricao">Descricao</Translate>
            </span>
          </dt>
          <dd>{mensagemEntity.descricao}</dd>
          <dt>
            <span id="conteudo">
              <Translate contentKey="aapmApp.mensagem.conteudo">Conteudo</Translate>
            </span>
          </dt>
          <dd>{mensagemEntity.conteudo}</dd>
          <dt>
            <span id="imagen">
              <Translate contentKey="aapmApp.mensagem.imagen">Imagen</Translate>
            </span>
          </dt>
          <dd>
            {mensagemEntity.imagen ? (
              <div>
                {mensagemEntity.imagenContentType ? (
                  <a onClick={openFile(mensagemEntity.imagenContentType, mensagemEntity.imagen)}>
                    <img src={`data:${mensagemEntity.imagenContentType};base64,${mensagemEntity.imagen}`} style={{ maxHeight: '200px' }} />
                  </a>
                ) : null}
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="link">
              <Translate contentKey="aapmApp.mensagem.link">Link</Translate>
            </span>
          </dt>
          <dd>{mensagemEntity.link}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="aapmApp.mensagem.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {mensagemEntity.startDate ? <TextFormat value={mensagemEntity.startDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="aapmApp.mensagem.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>
            {mensagemEntity.endDate ? <TextFormat value={mensagemEntity.endDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="status">
              <Translate contentKey="aapmApp.mensagem.status">Status</Translate>
            </span>
          </dt>
          <dd>{mensagemEntity.status}</dd>
          <dt>
            <span id="created">
              <Translate contentKey="aapmApp.mensagem.created">Created</Translate>
            </span>
          </dt>
          <dd>{mensagemEntity.created ? <TextFormat value={mensagemEntity.created} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="modified">
              <Translate contentKey="aapmApp.mensagem.modified">Modified</Translate>
            </span>
          </dt>
          <dd>{mensagemEntity.modified ? <TextFormat value={mensagemEntity.modified} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="aapmApp.mensagem.tipo">Tipo</Translate>
          </dt>
          <dd>{mensagemEntity.tipo ? mensagemEntity.tipo.tipo : ''}</dd>
        </dl>
        <Button tag={Link} to="/mensagem" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/mensagem/${mensagemEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MensagemDetail;
