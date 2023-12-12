import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { deleteEntity, getEntity } from './desconto-convenio.reducer';
import isAdm from 'app/components/is-adm';

export const DescontoConvenioDeleteDialog = () => {
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

  const descontoConvenioEntity = useAppSelector(state => state.descontoConvenio.entity);
  const updateSuccess = useAppSelector(state => state.descontoConvenio.updateSuccess);

  const handleClose = () => {
    navigate('/desconto-convenio' + location.search);
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(descontoConvenioEntity.id));
  };

  return (
    <Modal isOpen toggle={handleClose} style={{ marginTop: '30vh' }}>
      <ModalHeader toggle={handleClose} data-cy="descontoConvenioDeleteDialogHeading">
        <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
      </ModalHeader>
      <ModalBody id="aapmApp.descontoConvenio.delete.question">
        <Translate contentKey="aapmApp.descontoConvenio.delete.question" interpolate={{ id: descontoConvenioEntity.id }}>
          Are you sure you want to delete this DescontoConvenio?
        </Translate>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp;
          <Translate contentKey="entity.action.cancel">Cancel</Translate>
        </Button>
        <Button id="jhi-confirm-delete-descontoConvenio" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp;
          <Translate contentKey="entity.action.delete">Delete</Translate>
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default DescontoConvenioDeleteDialog;
