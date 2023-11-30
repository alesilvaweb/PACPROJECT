import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm, isEmail } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { locales, languages } from 'app/config/translation';
import { getUser, getRoles, updateUser, createUser, reset } from './user-management.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { v4 as uuidv4 } from 'uuid';

export const UserManagementUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();
  const [idAle, setIdAleatorio] = useState();
  const { login } = useParams<'login'>();
  const isNew = login === undefined;

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
      gerarIdAleatorio();
    } else {
      dispatch(getUser(login));
    }
    dispatch(getRoles());
    return () => {
      dispatch(reset());
    };
  }, [login]);

  const handleClose = () => {
    navigate('/');
  };

  const saveUser = values => {
    if (isNew) {
      dispatch(createUser(values));
      navigate('/');
    } else {
      dispatch(updateUser(values));
      navigate('/');
    }
  };
  function gerarIdAleatorio() {
    const min = 8000; // Valor mínimo
    const max = 9999; // Valor máximo (altere conforme necessário)
    const idAleatorio = Math.floor(Math.random() * (max - min + 1)) + min;
    // @ts-ignore
    setIdAleatorio(idAleatorio);
  }
  console.log({ idAle });

  const isInvalid = false;
  const user = useAppSelector(state => state.userManagement.user);
  const loading = useAppSelector(state => state.userManagement.loading);
  const updating = useAppSelector(state => state.userManagement.updating);
  const updateSuccess = useAppSelector(state => state.userManagement.updateSuccess);
  const authorities = useAppSelector(state => state.userManagement.authorities);
  if (updateSuccess) {
    navigate('/');
  }
  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h1>
            <Translate contentKey="userManagement.home.createOrEditLabel">Create or edit a User</Translate>
          </h1>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm onSubmit={saveUser} defaultValues={user}>
              {isNew ? (
                <ValidatedField type="text" name="id" value={idAle} autoFocus={true} readOnly label={translate('global.field.id')} />
              ) : (
                <ValidatedField
                  type="text"
                  name="id"
                  required
                  readOnly
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                type="text"
                name="login"
                label={translate('userManagement.login')}
                validate={{
                  required: {
                    value: true,
                    message: translate('register.messages.validate.login.required'),
                  },
                  pattern: {
                    value: /^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$/,
                    message: translate('register.messages.validate.login.pattern'),
                  },
                  minLength: {
                    value: 1,
                    message: translate('register.messages.validate.login.minlength'),
                  },
                  maxLength: {
                    value: 50,
                    message: translate('register.messages.validate.login.maxlength'),
                  },
                }}
              />
              <ValidatedField
                type="text"
                name="firstName"
                label={translate('userManagement.firstName')}
                validate={{
                  maxLength: {
                    value: 50,
                    message: translate('entity.validation.maxlength', { max: 50 }),
                  },
                }}
              />
              <ValidatedField
                type="text"
                name="lastName"
                hidden
                label={translate('userManagement.lastName')}
                validate={{
                  maxLength: {
                    value: 100,
                    message: translate('entity.validation.maxlength', { max: 100 }),
                  },
                }}
              />
              <ValidatedField
                name="email"
                label={translate('global.form.email.label')}
                placeholder={translate('global.form.email.placeholder')}
                type="email"
                validate={{
                  required: {
                    value: true,
                    message: translate('global.messages.validate.email.required'),
                  },
                  minLength: {
                    value: 5,
                    message: translate('global.messages.validate.email.minlength'),
                  },
                  maxLength: {
                    value: 254,
                    message: translate('global.messages.validate.email.maxlength'),
                  },
                  validate: v => isEmail(v) || translate('global.messages.validate.email.invalid'),
                }}
              />
              <ValidatedField
                type="checkbox"
                name="activated"
                check
                value={true}
                disabled={!user.id}
                label={translate('userManagement.activated')}
              />
              <ValidatedField type="select" name="langKey" label={translate('userManagement.langKey')}>
                {locales.map(locale => (
                  <option value={locale} key={locale}>
                    {languages[locale].name}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField type="select" name="authorities" multiple label={translate('userManagement.profiles')}>
                {authorities.map(role => (
                  <option value={role} key={role}>
                    {role}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} to="/admin/user-management" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" type="submit" disabled={isInvalid || updating}>
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

export default UserManagementUpdate;
