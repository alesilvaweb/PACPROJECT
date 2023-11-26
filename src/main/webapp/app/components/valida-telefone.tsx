import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useAppSelector } from 'app/config/store';

const ValidaTelefone = () => {
  const [associado, setAssociado] = useState([]);
  const account = useAppSelector(state => state.authentication.account);

  async function verificaTelefone() {
    try {
      const response = await axios.get(`api/associados?id.equals=${account.id}&telefone.specified=false&page=0&size=20`);
      setAssociado(response.data);
    } catch (error) {
      console.error('Erro ao validar telefone do Associado :', error);
    }
  }

  useEffect(() => {
    verificaTelefone();
  }, []);
  return associado;
};

export default ValidaTelefone;
