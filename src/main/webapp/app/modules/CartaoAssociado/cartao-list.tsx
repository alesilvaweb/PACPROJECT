import * as React from 'react';
import { useEffect, useRef, useState } from 'react';
import { experimentalStyled as styled } from '@mui/material/styles';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import Grid from '@mui/material/Grid';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import axios from 'axios';
import { getEntity } from 'app/entities/associado/associado.reducer';
import AssociadoCard from 'app/modules/CartaoAssociado/AssociadoCard';
import Breadcrunbs from 'app/components/breadcrunbs';

const Item = styled(Paper)(({ theme }) => ({
  backgroundColor: theme.palette.mode === 'dark' ? '#1A2027' : '#fff',
  ...theme.typography.body2,
  padding: theme.spacing(2),
  textAlign: 'center',
  color: theme.palette.text.secondary,
}));

export default function CartaoList() {
  const dispatch = useAppDispatch();
  const [dependentes, setDependentes] = useState([]);
  const account = useAppSelector(state => state.authentication.account);
  // const { id } = account.id;
  // const id = 3963; //sem dependentes
  const id = 15660; //dois dependentes

  const associadoEntity = useAppSelector(state => state.associado.entity);
  const cardRef = useRef(null);
  const [imgData, setImgData] = React.useState('');

  async function fetchDependentes() {
    try {
      const response = await axios.get(`api/dependentes?associadoId.equals=${id}`);
      setDependentes(response.data);
    } catch (error) {
      console.error('Erro ao buscar Dependentes:', error);
    }
  }

  useEffect(() => {
    dispatch(getEntity(id));
    fetchDependentes().then(r => null);
  }, []);

  const [size, setSize] = useState(6);

  return (
    <div>
      <Breadcrunbs atual={'Cartões'} />
      <Box sx={{ flexGrow: 1 }}>
        <Grid container spacing={{ xs: 2, md: 3 }} columns={{ xs: 4, sm: 8, md: 12 }}>
          <Grid item xs={12} sm={12} md={size} xl={size}>
            <Item>
              <AssociadoCard dependent={null} idAssociado={id} />
            </Item>
          </Grid>
          {/* Lista de Dependentes */}
          {dependentes &&
            dependentes.length > 0 &&
            dependentes.map(dependent => (
              <>
                {dependent.status != 'Inativo' ? (
                  <Grid item xs={12} sm={12} md={size} key={dependent.id}>
                    <Item>
                      <AssociadoCard dependent={dependent} idAssociado={id} />
                    </Item>
                  </Grid>
                ) : null}
              </>
            ))}
        </Grid>
      </Box>
    </div>
  );
}
