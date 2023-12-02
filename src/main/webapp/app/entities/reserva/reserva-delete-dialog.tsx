import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { deleteEntity, getEntity, reset } from './reserva.reducer';
import Spinner from 'app/components/spinner';

export const ReservaDeleteDialog = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();
  const { local } = useParams<'local'>();
  const loading = useAppSelector(state => state.reserva.loading);
  const [loadModal, setLoadModal] = useState(false);

  useEffect(() => {
    dispatch(getEntity(id));
    setLoadModal(true);
  }, []);

  const reservaEntity = useAppSelector(state => state.reserva.entity);
  const updateSuccess = useAppSelector(state => state.reserva.updateSuccess);

  const handleClose = () => {
    dispatch(reset());
    navigate('/local/' + local);
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(reservaEntity.id));
  };

  return (
    <div>
      {loading ? (
        <Spinner />
      ) : (
        <Modal isOpen toggle={handleClose} style={{ marginTop: '20vh' }}>
          <ModalHeader toggle={handleClose} data-cy="reservaDeleteDialogHeading">
            <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
          </ModalHeader>
          <ModalBody id="aapmApp.reserva.delete.question">
            <Translate contentKey="aapmApp.reserva.delete.question" interpolate={{ id: reservaEntity.id }}>
              Are you sure you want to delete this Reserva?
            </Translate>
          </ModalBody>
          <ModalFooter>
            <Button color="secondary" onClick={handleClose}>
              <FontAwesomeIcon icon="ban" />
              &nbsp;
              <Translate contentKey="entity.action.cancel">Cancel</Translate>
            </Button>
            <Button id="jhi-confirm-delete-reserva" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
              <FontAwesomeIcon icon="trash" />
              &nbsp;
              <Translate contentKey="entity.action.delete">Delete</Translate>
            </Button>
          </ModalFooter>
        </Modal>
      )}
    </div>
  );
};

export default ReservaDeleteDialog;
