import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import axios from 'axios';
import { toHtml } from '@fortawesome/fontawesome-svg-core';

export const ReservaMessageDialog = () => {
  const dispatch = useAppDispatch();
  const location = useLocation();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();
  const { local } = useParams<'local'>();
  const [loadModal, setLoadModal] = useState(false);
  const [parametro, setParametro] = useState([]);
  const [message, setMessage] = useState('');
  async function fetchParametros() {
    try {
      const response = await axios.get(`api/parametros?chave.in=msg-reserva&page=0&size=20&sort=id`);
      setParametro(response.data);
    } catch (error) {
      console.error('Erro ao buscar Parametros:', error);
    }
  }

  useEffect(() => {
    fetchParametros();
    setLoadModal(true);
  }, []);

  const handleClose = () => {
    navigate('/local/' + local);
  };
  useEffect(() => {
    parametro.map(a => {
      if (a.chave.toString() === 'msg-reserva') {
        setMessage(a.valor);
      }
    });
  }, [parametro]);

  return (
    <div>
      <Modal isOpen toggle={handleClose} style={{ marginTop: '15vh' }}>
        <ModalHeader toggle={handleClose}>Condições de uso e informações importantes</ModalHeader>
        <ModalBody id="aapmApp.reserva.delete.question">
          {parametro.map(a => (
            <p>{a.valor}</p>
          ))}
        </ModalBody>
        <ModalFooter>
          <Button color="primary" onClick={handleClose}>
            Fechar
          </Button>
        </ModalFooter>
      </Modal>
    </div>
  );
};

export default ReservaMessageDialog;
