import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './icons-redes-sociais.reducer';
import isAdm from 'app/components/is-adm';

export const IconsRedesSociaisDetail = () => {
  const dispatch = useAppDispatch();
  isAdm();
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
          {/*<dt>*/}
          {/*  <span id="descricao">*/}
          {/*    <Translate contentKey="aapmApp.iconsRedesSociais.descricao">Descricao</Translate>*/}
          {/*  </span>*/}
          {/*</dt>*/}
          {/*<dd>{iconsRedesSociaisEntity.descricao}</dd>*/}
          {/*<dt>*/}
          {/*  <span id="icon">*/}
          {/*    <Translate contentKey="aapmApp.iconsRedesSociais.icon">Icon</Translate>*/}
          {/*  </span>*/}
          {/*</dt>*/}
          {/*<dd>{iconsRedesSociaisEntity.icon}</dd>*/}
          {/*<dt>*/}
          {/*  <span id="image">*/}
          {/*    <Translate contentKey="aapmApp.iconsRedesSociais.image">Image</Translate>*/}
          {/*  </span>*/}
          {/*</dt>*/}
          {/*<dd>*/}
          {/*  {iconsRedesSociaisEntity.image ? (*/}
          {/*    <div>*/}
          {/*      {iconsRedesSociaisEntity.imageContentType ? (*/}
          {/*        <a onClick={openFile(iconsRedesSociaisEntity.imageContentType, iconsRedesSociaisEntity.image)}>*/}
          {/*          <img*/}
          {/*            src={`data:${iconsRedesSociaisEntity.imageContentType};base64,${iconsRedesSociaisEntity.image}`}*/}
          {/*            style={{ maxHeight: '30px' }}*/}
          {/*          />*/}
          {/*        </a>*/}
          {/*      ) : null}*/}
          {/*      <span>*/}
          {/*        {iconsRedesSociaisEntity.imageContentType}, {byteSize(iconsRedesSociaisEntity.image)}*/}
          {/*      </span>*/}
          {/*    </div>*/}
          {/*  ) : null}*/}
          {/*</dd>*/}
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
