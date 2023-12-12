import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { deleteEntity, getEntity } from './parametro.reducer';
import isAdm from 'app/components/is-adm';

export const ParametroDeleteDialog = () => {
  const dispatch = useAppDispatch();
  isAdm();
  const location = useLocation();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();

  const [loadModal, setLoadModal] = useState(false);

  useEffect(() => {
    dispatch(getEntity(id));
    setLoadModal(true);
  }, []);

  const parametroEntity = useAppSelector(state => state.parametro.entity);
  const updateSuccess = useAppSelector(state => state.parametro.updateSuccess);

  const handleClose = () => {
    navigate('/parametro' + location.search);
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(parametroEntity.id));
  };

  return (
    <Modal isOpen toggle={handleClose} style={{ marginTop: '30vh' }}>
      <ModalHeader toggle={handleClose} data-cy="parametroDeleteDialogHeading">
        <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
      </ModalHeader>
      <ModalBody id="aapmApp.parametro.delete.question">
        <Translate contentKey="aapmApp.parametro.delete.question" interpolate={{ id: parametroEntity.id }}>
          Are you sure you want to delete this Parametro?
        </Translate>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp;
          <Translate contentKey="entity.action.cancel">Cancel</Translate>
        </Button>
        <Button id="jhi-confirm-delete-parametro" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp;
          <Translate contentKey="entity.action.delete">Delete</Translate>
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default ParametroDeleteDialog;
