import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './imagens-convenio.reducer';

export const ImagensConvenioDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const imagensConvenioEntity = useAppSelector(state => state.imagensConvenio.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="imagensConvenioDetailsHeading">
          <Translate contentKey="aapmApp.imagensConvenio.detail.title">ImagensConvenio</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{imagensConvenioEntity.id}</dd>
          <dt>
            <span id="titulo">
              <Translate contentKey="aapmApp.imagensConvenio.titulo">Titulo</Translate>
            </span>
          </dt>
          <dd>{imagensConvenioEntity.titulo}</dd>
          <dt>
            <span id="descricao">
              <Translate contentKey="aapmApp.imagensConvenio.descricao">Descricao</Translate>
            </span>
          </dt>
          <dd>{imagensConvenioEntity.descricao}</dd>
          <dt>
            <span id="imagen">
              <Translate contentKey="aapmApp.imagensConvenio.imagen">Imagen</Translate>
            </span>
          </dt>
          <dd>
            {imagensConvenioEntity.imagen ? (
              <div>
                {imagensConvenioEntity.imagenContentType ? (
                  <a onClick={openFile(imagensConvenioEntity.imagenContentType, imagensConvenioEntity.imagen)}>
                    <img
                      src={`data:${imagensConvenioEntity.imagenContentType};base64,${imagensConvenioEntity.imagen}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {imagensConvenioEntity.imagenContentType}, {byteSize(imagensConvenioEntity.imagen)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="created">
              <Translate contentKey="aapmApp.imagensConvenio.created">Created</Translate>
            </span>
          </dt>
          <dd>
            {imagensConvenioEntity.created ? (
              <TextFormat value={imagensConvenioEntity.created} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="modified">
              <Translate contentKey="aapmApp.imagensConvenio.modified">Modified</Translate>
            </span>
          </dt>
          <dd>
            {imagensConvenioEntity.modified ? (
              <TextFormat value={imagensConvenioEntity.modified} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="aapmApp.imagensConvenio.convenio">Convenio</Translate>
          </dt>
          <dd>{imagensConvenioEntity.convenio ? imagensConvenioEntity.convenio.nome : ''}</dd>
        </dl>
        <Button tag={Link} to="/imagens-convenio" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/imagens-convenio/${imagensConvenioEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ImagensConvenioDetail;
