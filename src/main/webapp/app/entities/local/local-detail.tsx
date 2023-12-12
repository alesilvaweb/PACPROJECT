import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Breadcrumb, BreadcrumbItem, Button, Col, Row } from 'reactstrap';
import { openFile, Translate } from 'react-jhipster';
import './locais.scss';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './local.reducer';
import { Card } from '@mui/material';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const LocalDetail = () => {
  const dispatch = useAppDispatch();
  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);
  const navigate = useNavigate();
  const localEntity = useAppSelector(state => state.local.entity);
  return (
    <div>
      <Breadcrumb>
        <BreadcrumbItem onClick={() => navigate('/')}>
          <a>Início</a>
        </BreadcrumbItem>
        <BreadcrumbItem onClick={() => navigate('/Cabanas')}>
          <a>Cabanas</a>
        </BreadcrumbItem>
        <BreadcrumbItem active>{localEntity.nome}</BreadcrumbItem>
      </Breadcrumb>
      <Row className={'locais-detail'}>
        <Col md="10" style={{ display: 'flex', justifyContent: 'space-between', flexWrap: 'wrap' }}>
          <dl className="jh-entity-details">
            <dt>
              <h4 data-cy="localDetailsHeading">{localEntity.nome}</h4>
              <hr />
              <span id="descricao">
                <Translate contentKey="aapmApp.local.descricao">Descricao</Translate>
              </span>
            </dt>
            <dd>{localEntity.descricao}</dd>
            <dt>
              <span id="capacidade">
                <Translate contentKey="aapmApp.local.capacidade">Capacidade</Translate>
              </span>
            </dt>
            <dd>{localEntity.capacidade} pessoas</dd>

            <dt>
              <span id="valor">
                <Translate contentKey="aapmApp.local.valor">Valor</Translate>
              </span>
            </dt>
            <dd>{`R$ ${localEntity.valor},00`}</dd>

            {localEntity.observacoes ? (
              <>
                <dt>
                  <span id="observacoes">
                    <Translate contentKey="aapmApp.local.observacoes">Observacoes</Translate>
                  </span>
                </dt>
                <dd>{localEntity.observacoes}</dd>
              </>
            ) : null}
          </dl>
          <dd>
            {localEntity.imagen ? (
              <Card>
                {localEntity.imagenContentType ? (
                  <a onClick={openFile(localEntity.imagenContentType, localEntity.imagen)}>
                    <img src={`data:${localEntity.imagenContentType};base64,${localEntity.imagen}`} style={{ maxHeight: '400px' }} />
                  </a>
                ) : null}
              </Card>
            ) : null}
          </dd>
          <hr />
          <div className={'col-md-12 d-flex justify-content-start'}>
            <Button tag={Link} id="cancel" data-cy="entityCreateCancelButton" to={'/cabanas'} replace color="info">
              <FontAwesomeIcon icon="arrow-left" />
              &nbsp; Voltar
            </Button>
          </div>
        </Col>
      </Row>
    </div>
  );
};

export default LocalDetail;
