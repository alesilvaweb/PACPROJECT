import React, { useEffect, useRef, useState } from 'react';
import html2canvas from 'html2canvas';
import { getEntity } from 'app/entities/associado/associado.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Button, Container, Grid } from '@mui/material';
import axios from 'axios';
import CardActions from '@mui/material/CardActions';
import './cartao.scss';
import Breadcrunbs from 'app/components/breadcrunbs';

const AssociadoCard = () => {
  const cardRef = useRef(null);
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const [imgData, setImgData] = React.useState('');
  const associadoEntity = useAppSelector(state => state.associado.entity);
  const [dependentes, setDependentes] = useState([]);
  // const { id } = account.id;
  const id = 11650;

  async function fetchDependentes() {
    try {
      const response = await axios.get(`api/dependentes?associadoId.equals=${id}`);
      setDependentes(response.data);
    } catch (error) {
      console.error('Erro ao buscar Dependentes:', error);
    }
  }

  const handlePrint = () => {
    window.print();
  };

  useEffect(() => {
    // dispatch(getEntity(account.id));
    fetchDependentes();
    dispatch(getEntity(id));
  }, []);

  const handleShare = () => {
    if (cardRef.current) {
      html2canvas(cardRef.current).then(canvas => {
        const dataUrl = canvas.toDataURL('image/png');
        setImgData(dataUrl);
        const link = document.createElement('a');

        link.href = dataUrl;
        link.download = 'Cartão-associado.png';
        link.click();
      });
    }
  };

  const PrintButton = ({ imgData }) => {
    if (cardRef.current) {
      html2canvas(cardRef.current).then(canvas => {
        const dataUrl = canvas.toDataURL('image/png');
        setImgData(dataUrl);
      });
    }
    const handlePrint = () => {
      const printWindow = window.open('', '_blank');
      printWindow.document.write(`<img src="${imgData}" style="max-width: 100%; height: auto;" />`);
      printWindow.document.close();
    };

    return (
      <Button variant="contained" color="primary" onClick={handlePrint}>
        Imprimir
      </Button>
    );
  };

  return (
    <div>
      <Breadcrunbs atual={'Cartão Associado'} />

      <Container style={{ backgroundColor: '#860608', padding: '0' }} ref={cardRef} className={'containerCard'}>
        <img src="content/images/Cartao-associado.png" alt="AAPM" style={{ width: '100%', height: 'auto', borderRadius: '8px' }} />

        <Grid container justifyContent="center" alignItems="center" spacing={2} sx={{ padding: '10px' }}>
          {/* Campos com fundo branco */}
          <Grid item xs={2}></Grid>
          <Grid item xs={8}>
            <div className={'campoTitle'}>Nome do Associado</div>
            <div className={'campoInput'}>
              <div className={'campoField'}>{associadoEntity.nome}</div>
            </div>
          </Grid>
          <Grid item xs={2}></Grid>
          <Grid item xs={2}></Grid>
          <Grid item xs={2}>
            <div className={'campoTitle'}>Registro</div>
            <div className={'campoInput'}>
              <div className={'campoField'}>{associadoEntity.id}</div>
            </div>
          </Grid>

          {/* Lista de Dependentes */}
          {dependentes &&
            dependentes.length > 0 &&
            dependentes.map(dependent => (
              <>
                <Grid item key={dependent.id} xs={6}>
                  <div className={'campoTitle'}>Titular/Dependente</div>
                  <div className={'campoInput'}>
                    <div className={'campoField'}>{dependent.nome}</div>
                  </div>
                </Grid>
                <Grid item xs={2}></Grid>
              </>
            ))}
        </Grid>
        <br />
      </Container>
      <br />
      <CardActions className={'justify-content-between'}>
        <div>
          <PrintButton imgData={imgData} /> &nbsp;
          <Button variant="contained" color="primary" onClick={handleShare}>
            Baixar
          </Button>
        </div>
      </CardActions>
    </div>
  );
};

export default AssociadoCard;
