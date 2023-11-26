import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import MaskedInput from 'react-text-mask';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAssociado } from 'app/shared/model/associado.model';
import { Status } from 'app/shared/model/enumerations/status.model';
import { getEntity, updateEntity, createEntity, reset } from './associado.reducer';
import Breadcrunbs from 'app/components/breadcrunbs';
const formatPhoneNumber = value => {
  // Telefone brasileiro (99) 9999-99999
  const match = value.replace(/\D/g, '').match(/(\d{0,2})(\d{0,4})(\d{0,5})/);
  return !match[2] ? match[1] : `(${match[1]}) ${match[2]}${match[3] ? `${match[3]}` : ''}`;
};
export const AssociadoUpdateTelefone = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const associadoEntity = useAppSelector(state => state.associado.entity);
  const loading = useAppSelector(state => state.associado.loading);
  const updating = useAppSelector(state => state.associado.updating);
  const updateSuccess = useAppSelector(state => state.associado.updateSuccess);
  const statusValues = Object.keys(Status);
  // const [phoneNumber, setPhoneNumber] = useState('');
  const handleClose = () => {
    navigate('/');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(reset());
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.created = convertDateTimeToServer(values.created);
    values.modified = convertDateTimeToServer(values.modified);

    const entity = {
      ...associadoEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () => {
    return isNew
      ? {
          created: displayDefaultDateTime(),
          modified: displayDefaultDateTime(),
        }
      : {
          status: 'Ativo',
          ...associadoEntity,
          // telefone: phoneNumber?phoneNumber:associadoEntity.telefone,
          created: convertDateTimeFromServer(associadoEntity.created),
          modified: convertDateTimeFromServer(associadoEntity.modified),
        };
  };

  // const handlePhoneNumberChange = (event) => {
  //   const rawValue = event.target.value.replace(/\D/g, '');
  //   const formattedValue = formatPhoneNumber(rawValue);
  //   setPhoneNumber(formattedValue);
  // };

  // @ts-ignore
  return (
    <div>
      <Breadcrunbs atual={'Configurações'} />
      <Row className="justify-content-center">
        <Col md="8">
          <h4 id="aapmApp.associado.home.createOrEditLabel" data-cy="AssociadoCreateUpdateHeading">
            Atualizar suas Informações
          </h4>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  hidden={true}
                  readOnly
                  id="associado-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('aapmApp.associado.matricula')}
                id="associado-matricula"
                name="matricula"
                data-cy="matricula"
                readOnly
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('aapmApp.associado.nome')}
                id="associado-nome"
                name="nome"
                data-cy="nome"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 2, message: translate('entity.validation.minlength', { min: 2 }) },
                }}
              />

              <ValidatedField
                label={translate('aapmApp.associado.status')}
                id="associado-status"
                name="status"
                hidden={true}
                data-cy="status"
                type="select"
              >
                {statusValues.map(status => (
                  <option value={status} key={status}>
                    {translate('aapmApp.Status.' + status)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('aapmApp.associado.telefone')}
                id="associado-telefone"
                name="telefone"
                placeholder="(99) 99999999"
                data-cy="telefone"
                // Value={phoneNumber}
                // onBlur={handlePhoneNumberChange}
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 9, message: 'O número de telefone deve ter no minimo 9 caracteres' },
                }}
                type="text"
              />
              <ValidatedField
                label={translate('aapmApp.associado.email')}
                id="associado-email"
                name="email"
                data-cy="email"
                readOnly
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('aapmApp.associado.dataNascimento')}
                id="associado-dataNascimento"
                name="dataNascimento"
                data-cy="dataNascimento"
                type="date"
              />
              <ValidatedField
                label={translate('aapmApp.associado.created')}
                id="associado-created"
                name="created"
                data-cy="created"
                hidden={true}
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('aapmApp.associado.modified')}
                id="associado-modified"
                name="modified"
                data-cy="modified"
                hidden={true}
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              {/*<Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/associado" replace color="info">*/}
              {/*  <FontAwesomeIcon icon="arrow-left" />*/}
              {/*  &nbsp;*/}
              {/*  <span className="d-none d-md-inline">*/}
              {/*    <Translate contentKey="entity.action.back">Back</Translate>*/}
              {/*  </span>*/}
              {/*</Button>*/}

              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default AssociadoUpdateTelefone;
