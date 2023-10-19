import React, { useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { Col, Row } from 'reactstrap';
import { openFile, Translate } from 'react-jhipster';
import './locais.scss';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './local.reducer';
import BotaoVoltar from 'app/components/botaoVoltar';

export const LocalDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const localEntity = useAppSelector(state => state.local.entity);
  return (
    <div>
      <BotaoVoltar link={'/agenda'} top={'-43px'} />
      <Row className={'locais-detail'}>
        <Col md="8">
          <h2 data-cy="localDetailsHeading">{localEntity.nome}</h2>
          <dd>
            {localEntity.imagen ? (
              <div>
                {localEntity.imagenContentType ? (
                  <a onClick={openFile(localEntity.imagenContentType, localEntity.imagen)}>
                    <img src={`data:${localEntity.imagenContentType};base64,${localEntity.imagen}`} style={{ maxHeight: '300px' }} />
                  </a>
                ) : null}
              </div>
            ) : null}
          </dd>
          <dl className="jh-entity-details">
            <dt>
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

            <dd>{localEntity.observacoes}</dd>
            <dt>
              <span id="valor">
                <Translate contentKey="aapmApp.local.valor">Valor</Translate>
              </span>
            </dt>
            <dd>{localEntity.capacidade}</dd>
            <dt>
              <span id="observacoes">
                <Translate contentKey="aapmApp.local.observacoes">Observacoes</Translate>
              </span>
            </dt>
            <dd>{`R$ ${localEntity.valor},00`}</dd>
          </dl>
        </Col>
      </Row>
    </div>
  );
};

export default LocalDetail;
