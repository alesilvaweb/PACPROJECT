import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './icons-redes-sociais.reducer';

export const IconsRedesSociaisDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const iconsRedesSociaisEntity = useAppSelector(state => state.iconsRedesSociais.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="iconsRedesSociaisDetailsHeading">
          <Translate contentKey="aapmApp.iconsRedesSociais.detail.title">IconsRedesSociais</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{iconsRedesSociaisEntity.id}</dd>
          <dt>
            <span id="nome">
              <Translate contentKey="aapmApp.iconsRedesSociais.nome">Nome</Translate>
            </span>
          </dt>
          <dd>{iconsRedesSociaisEntity.nome}</dd>
          <dt>
            <span id="descricao">
              <Translate contentKey="aapmApp.iconsRedesSociais.descricao">Descricao</Translate>
            </span>
          </dt>
          <dd>{iconsRedesSociaisEntity.descricao}</dd>
          <dt>
            <span id="icon">
              <Translate contentKey="aapmApp.iconsRedesSociais.icon">Icon</Translate>
            </span>
          </dt>
          <dd>{iconsRedesSociaisEntity.icon}</dd>
          <dt>
            <span id="imagem">
              <Translate contentKey="aapmApp.iconsRedesSociais.imagem">Imagem</Translate>
            </span>
          </dt>
          <dd>
            {iconsRedesSociaisEntity.imagem ? (
              <div>
                {iconsRedesSociaisEntity.imagemContentType ? (
                  <a onClick={openFile(iconsRedesSociaisEntity.imagemContentType, iconsRedesSociaisEntity.imagem)}>
                    <img
                      src={`data:${iconsRedesSociaisEntity.imagemContentType};base64,${iconsRedesSociaisEntity.imagem}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {iconsRedesSociaisEntity.imagemContentType}, {byteSize(iconsRedesSociaisEntity.imagem)}
                </span>
              </div>
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/icons-redes-sociais" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/icons-redes-sociais/${iconsRedesSociaisEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default IconsRedesSociaisDetail;
