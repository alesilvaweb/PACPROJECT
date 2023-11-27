import React, { useState, useEffect } from 'react';
import { Col, Row, Button, ModalHeader, Modal, ModalBody, ModalFooter } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { toast } from 'react-toastify';

import { handlePasswordResetFinish, reset } from '../password-reset.reducer';
import PasswordStrengthBar from 'app/shared/layout/password/password-strength-bar';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { savePassword } from 'app/modules/account/password/password.reducer';

export const PasswordResetFinishAdm = () => {
  const dispatch = useAppDispatch();

  const [searchParams] = useSearchParams();
  const key = searchParams.get('key');

  const [password, setPassword] = useState('');
  const [success, setSuccess] = useState(false);

  useEffect(
    () => () => {
      dispatch(reset());
    },
    []
  );

  const handleValidSubmit = ({ newPassword }) => {
    dispatch(handlePasswordResetFinish({ key, newPassword })).then(() => {
      handleClose();
    });
  };

  const updatePassword = event => setPassword(event.target.value);

  const getResetForm = () => {
    return (
      <ValidatedForm onSubmit={handleValidSubmit}>
        <ValidatedField
          name="newPassword"
          label={translate('global.form.newpassword.label')}
          placeholder={translate('global.form.newpassword.placeholder')}
          type="password"
          validate={{
            required: { value: true, message: translate('global.messages.validate.newpassword.required') },
            minLength: { value: 4, message: translate('global.messages.validate.newpassword.minlength') },
            maxLength: { value: 50, message: translate('global.messages.validate.newpassword.maxlength') },
          }}
          onChange={updatePassword}
          data-cy="resetPassword"
        />
        <PasswordStrengthBar password={password} />
        <ValidatedField
          name="confirmPassword"
          label={translate('global.form.confirmpassword.label')}
          placeholder={translate('global.form.confirmpassword.placeholder')}
          type="password"
          validate={{
            required: { value: true, message: translate('global.messages.validate.confirmpassword.required') },
            minLength: { value: 4, message: translate('global.messages.validate.confirmpassword.minlength') },
            maxLength: { value: 50, message: translate('global.messages.validate.confirmpassword.maxlength') },
            validate: v => v === password || translate('global.messages.error.dontmatch'),
          }}
          data-cy="confirmResetPassword"
        />

        <Button color="primary" type="submit" data-cy="submit" style={{ float: 'right' }}>
          <Translate contentKey="reset.finish.form.button">Validate new password</Translate>
        </Button>
      </ValidatedForm>
    );
  };

  const successMessage = useAppSelector(state => state.passwordReset.successMessage);

  const navigate = useNavigate();

  useEffect(() => {
    if (successMessage === 'reset.finish.messages.success') {
      toast.success(translate(successMessage));
    }
  }, [successMessage]);

  const handleClose = () => {
    navigate('/admin/user-management' + location.search);
  };
  return (
    <div>
      <Modal isOpen toggle={handleClose} style={{ marginTop: '18vh' }}>
        <ModalHeader toggle={handleClose} data-cy="mensagemDeleteDialogHeading">
          <Translate contentKey="reset.finish.title">Reset password</Translate>
        </ModalHeader>
        <ModalBody id="aapmApp.mensagem.delete.question">
          <Row className="justify-content-center">
            <Col md="12">
              <div>{key ? getResetForm() : null}</div>
            </Col>
          </Row>
        </ModalBody>
      </Modal>
    </div>
  );
};

export default PasswordResetFinishAdm;
