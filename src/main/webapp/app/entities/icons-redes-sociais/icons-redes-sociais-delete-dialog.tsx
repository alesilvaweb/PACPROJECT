import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { deleteEntity, getEntity } from './icons-redes-sociais.reducer';
import isAdm from 'app/components/is-adm';

export const IconsRedesSociaisDeleteDialog = () => {
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

  const iconsRedesSociaisEntity = useAppSelector(state => state.iconsRedesSociais.entity);
  const updateSuccess = useAppSelector(state => state.iconsRedesSociais.updateSuccess);

  const handleClose = () => {
    navigate('/icons-redes-sociais' + location.search);
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(iconsRedesSociaisEntity.id));
  };

  return (
    <Modal isOpen toggle={handleClose} style={{ marginTop: '30vh' }}>
      <ModalHeader toggle={handleClose} data-cy="iconsRedesSociaisDeleteDialogHeading">
        <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
      </ModalHeader>
      <ModalBody id="aapmApp.iconsRedesSociais.delete.question">
        <Translate contentKey="aapmApp.iconsRedesSociais.delete.question" interpolate={{ id: iconsRedesSociaisEntity.id }}>
          Are you sure you want to delete this IconsRedesSociais?
        </Translate>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp;
          <Translate contentKey="entity.action.cancel">Cancel</Translate>
        </Button>
        <Button id="jhi-confirm-delete-iconsRedesSociais" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp;
          <Translate contentKey="entity.action.delete">Delete</Translate>
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default IconsRedesSociaisDeleteDialog;
