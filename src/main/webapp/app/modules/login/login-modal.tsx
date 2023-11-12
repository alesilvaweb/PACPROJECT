import React from 'react';
import { Translate, translate, ValidatedField } from 'react-jhipster';
import Link from '@mui/material/Link';
import { type FieldError, useForm } from 'react-hook-form';
import { DialogProps } from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogActions from '@mui/material/DialogActions';
import { SelectChangeEvent, Typography } from '@mui/material';
import { Col, Form, Row } from 'reactstrap';
import Button from '@mui/material/Button';
import Alert from '@mui/material/Alert';
import { useAppSelector } from 'app/config/store';
import { useNavigate } from 'react-router-dom';

export interface ILoginModalProps {
  showModal: boolean;
  loginError: boolean;
  handleLogin: (username: string, password: string, rememberMe: boolean) => void;
  handleClose: () => void;
}

const LoginModal = (props: ILoginModalProps) => {
  const [fullWidth, setFullWidth] = React.useState(true);
  const [maxWidth, setMaxWidth] = React.useState<DialogProps['maxWidth']>('sm');
  const [open, setOpen] = React.useState(false);
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);
  const navigate = useNavigate();

  if (isAuthenticated) {
    navigate('/');
  }

  const handleMaxWidthChange = (event: SelectChangeEvent<typeof maxWidth>) => {
    setMaxWidth(
      // @ts-expect-error autofill of arbitrary value is not handled.
      event.target.value
    );
  };

  const handleFullWidthChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setFullWidth(event.target.checked);
  };

  const login = ({ username, password, rememberMe }) => {
    props.handleLogin(username, password, rememberMe);
  };

  const {
    handleSubmit,
    register,
    formState: { errors, touchedFields },
  } = useForm({ mode: 'onTouched' });

  const { loginError, handleClose } = props;

  const handleLoginSubmit = e => {
    handleSubmit(login)(e);
  };
  return (
    <div style={{ display: 'flex', justifyContent: 'center' }}>
      <Col xl={4} md={6} sm={8} xs={12} style={{ marginTop: '10vh' }}>
        <Form onSubmit={handleLoginSubmit}>
          <Typography variant={'h5'} className={'text-center'}>
            <Translate contentKey="login.title">Sign in</Translate>
          </Typography>
          <Row>
            <Row>
              <Col md="12">
                {loginError ? (
                  <Alert severity="error">
                    <Translate contentKey="login.messages.error.authentication">
                      <strong>Failed to sign in!</strong> Please check your credentials and try again.
                    </Translate>
                  </Alert>
                ) : null}
              </Col>
            </Row>

            <ValidatedField
              name="username"
              label={translate('global.form.username.label')}
              placeholder={translate('global.form.username.placeholder')}
              required
              autoFocus
              data-cy="username"
              validate={{ required: 'Informe o seu usuário!' }}
              register={register}
              error={errors.username as FieldError}
              isTouched={touchedFields.username}
            />
            <ValidatedField
              name="password"
              type="password"
              label={translate('login.form.password')}
              placeholder={translate('login.form.password.placeholder')}
              required
              data-cy="password"
              validate={{ required: 'Informe sua senha!' }}
              register={register}
              error={errors.password as FieldError}
              isTouched={touchedFields.password}
            />

            <Col md={12} style={{ display: 'flex', justifyContent: 'space-between' }}>
              <ValidatedField
                name="rememberMe"
                type="checkbox"
                check
                label={translate('login.form.rememberme')}
                value={true}
                register={register}
              />

              <Button variant="contained" color={'primary'} type="submit" className={'text-center'}>
                <Translate contentKey="login.form.button">Sign in</Translate>
              </Button>
            </Col>
          </Row>
          <Link href="/account/reset/request" underline="none">
            <Translate contentKey="login.password.forgot">Did you forget your password?</Translate>
          </Link>
        </Form>
      </Col>
    </div>
    // <Modal isOpen={props.showModal} toggle={handleClose} backdrop="static" id="login-page" autoFocus={false} style={{marginTop:'8%'}}>
    //   <Form onSubmit={handleLoginSubmit}>
    //     <ModalHeader id="login-title" data-cy="loginTitle">
    //       <Translate contentKey="login.title">Sign in</Translate>
    //     </ModalHeader>
    //     <ModalBody>
    //       <Row>
    //         <Col md="12">
    //           {loginError ? (
    //             <Alert color="danger" data-cy="loginError">
    //               <Translate contentKey="login.messages.error.authentication">
    //                 <strong>Failed to sign in!</strong> Please check your credentials and try again.
    //               </Translate>
    //             </Alert>
    //           ) : null}
    //         </Col>
    //         <Col md="12">
    //           <ValidatedField
    //             name="username"
    //             label={translate('global.form.username.label')}
    //             placeholder={translate('global.form.username.placeholder')}
    //             required
    //             autoFocus
    //             data-cy="username"
    //             validate={{ required: 'Informe o seu usuário!' }}
    //             register={register}
    //             error={errors.username as FieldError}
    //             isTouched={touchedFields.username}
    //           />
    //           <ValidatedField
    //             name="password"
    //             type="password"
    //             label={translate('login.form.password')}
    //             placeholder={translate('login.form.password.placeholder')}
    //             required
    //             data-cy="password"
    //             validate={{ required: 'Informe sua senha!' }}
    //             register={register}
    //             error={errors.password as FieldError}
    //             isTouched={touchedFields.password}
    //           />
    //           <ValidatedField
    //             name="rememberMe"
    //             type="checkbox"
    //             check
    //             label={translate('login.form.rememberme')}
    //             value={true}
    //             register={register}
    //           />
    //         </Col>
    //       </Row>
    //       <div className="mt-1">&nbsp;</div>
    //       <Alert color="warning">
    //         <Link to="/account/reset/request" data-cy="forgetYourPasswordSelector">
    //           <Translate contentKey="login.password.forgot">Did you forget your password?</Translate>
    //         </Link>
    //       </Alert>
    //       {/*<Alert color="warning">*/}
    //       {/*  <span>*/}
    //       {/*    <Translate contentKey="global.messages.info.register.noaccount">You don&apos;t have an account yet?</Translate>*/}
    //       {/*  </span>{' '}*/}
    //       {/*  <Link to="/account/register">*/}
    //       {/*    <Translate contentKey="global.messages.info.register.link">Register a new account</Translate>*/}
    //       {/*  </Link>*/}
    //       {/*</Alert>*/}
    //     </ModalBody>
    //     <ModalFooter>
    //       {/*<Button color="secondary" onClick={handleClose} tabIndex={1}>*/}
    //       {/*  <Translate contentKey="entity.action.cancel">Cancel</Translate>*/}
    //       {/*</Button>{' '}*/}
    //       <Button color="primary" type="submit" data-cy="submit">
    //         <Translate contentKey="login.form.button">Sign in</Translate>
    //       </Button>
    //     </ModalFooter>
    //   </Form>
    // </Modal>
  );
};

export default LoginModal;
