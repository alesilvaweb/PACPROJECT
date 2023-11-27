import React, { useEffect } from 'react';
import { isEmail, translate, Translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { Alert, Button, Col, Row } from 'reactstrap';
import { toast } from 'react-toastify';

import { handlePasswordResetInit, reset } from '../password-reset.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const PasswordResetInit = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  useEffect(
    () => () => {
      dispatch(reset());
    },
    []
  );

  const handleValidSubmit = ({ email }) => {
    dispatch(handlePasswordResetInit(email));
  };

  const successMessage = useAppSelector(state => state.passwordReset.successMessage);

  useEffect(() => {
    if (successMessage) {
      toast.success(translate(successMessage));
    }
  }, [successMessage]);

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          {successMessage ? (
            <>
              <h2>E-mail enviado</h2>
              <Alert color="success">
                <p>Verifique seu e-mail para detalhes sobre a criação da sua senha.</p>
              </Alert>
            </>
          ) : (
            <>
              <h2>
                <Translate contentKey="reset.request.title">Reset your password</Translate>
              </h2>
              <Alert color="warning">
                <p>
                  <Translate contentKey="reset.request.messages.info">Enter the email address you used to register</Translate>
                </p>
              </Alert>
            </>
          )}

          {!successMessage ? (
            <ValidatedForm onSubmit={handleValidSubmit}>
              <ValidatedField
                name="email"
                label={translate('global.form.email.label')}
                placeholder={translate('global.form.email.placeholder')}
                type="email"
                validate={{
                  required: { value: true, message: translate('global.messages.validate.email.required') },
                  minLength: { value: 5, message: translate('global.messages.validate.email.minlength') },
                  maxLength: { value: 254, message: translate('global.messages.validate.email.maxlength') },
                  validate: v => isEmail(v) || translate('global.messages.validate.email.invalid'),
                }}
                data-cy="emailResetPassword"
              />
              <Button color="primary" type="submit" data-cy="submit">
                <Translate contentKey="reset.request.form.button">Reset password</Translate>
              </Button>
            </ValidatedForm>
          ) : (
            <Button color="primary" type="button" onClick={() => navigate('/')} data-cy="submit">
              <FontAwesomeIcon icon="lock" /> &nbsp; Login
            </Button>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PasswordResetInit;
