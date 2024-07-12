import React, { useEffect, useRef, useState } from 'react';
import html2canvas from 'html2canvas';
import { getEntity } from 'app/entities/associado/associado.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { BottomNavigation, BottomNavigationAction, Button, Container, Grid } from '@mui/material';
import axios from 'axios';
import CardActions from '@mui/material/CardActions';
import './cartao.scss';
import { Download, Print, Share } from '@mui/icons-material';
import { dE } from '@fullcalendar/core/internal-common';
import { formatDate } from '@fullcalendar/core';
import { now } from 'lodash';
import { Row } from 'reactstrap';

const AssociadoCard = ({ dependent, idAssociado, associadoEntity }) => {
  const cardRef = useRef(null);
  const dispatch = useAppDispatch();
  const [imgData, setImgData] = React.useState('');

  // const { id } = account.id;
  const id = idAssociado;
  const ano = formatDate(new Date(now()), { year: 'numeric' });
  const handleShare = nome => {
    if (cardRef.current) {
      html2canvas(cardRef.current).then(canvas => {
        const dataUrl = canvas.toDataURL('image/png');
        setImgData(dataUrl);
        const link = document.createElement('a');

        link.href = dataUrl;
        link.download = `Cartão-${dependent ? dependent.nome : associadoEntity.nome}.png`;
        link.click();
      });
    }
  };

  const captureAndShare = async () => {
    try {
      // Captura a imagem do componente
      const canvas = await html2canvas(cardRef.current);

      // Converte a imagem para um URL de dados
      const imageSrc = canvas.toDataURL('image/png');

      // Cria um link para a imagem
      const link = document.createElement('a');
      link.href = imageSrc;
      link.download = 'screenshot.png';

      // Simula um clique no link para iniciar o download
      link.click();

      // Abre o WhatsApp com a imagem anexada
      window.open(`https://api.whatsapp.com/send?text=Confira a imagem!&amp;source=&amp;data=`, '_blank');
    } catch (error) {
      console.error('Erro ao capturar e compartilhar a imagem:', error);
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
      <Button variant="contained" color="primary" hidden={true} onClick={handlePrint}>
        Imprimir
      </Button>
    );
  };

  const handlePrint = () => {
    const printWindow = window.open('', '_blank');
    printWindow.document.write(`<img src="${imgData}" style="max-width: 100%; height: auto;" />`);
    printWindow.document.close();
  };

  return (
    <div>
      {/*<Breadcrunbs atual={'Cartão Associado'} />*/}

      <Container key={associadoEntity.id} style={{ backgroundColor: '#35539c', padding: '0' }} ref={cardRef} className={'containerCard'}>
        <img
          src="content/images/logo-jhipster.png"
          alt="Cartão Associado"
          style={{
            width: '50%',
            marginLeft:'25%',
            height: '50%',
            borderRadius: '8px',
          }}
        />

        <Grid container justifyContent="center" alignItems="center" spacing={2} sx={{ padding: '10px' }}>
          {/* Campos com fundo branco */}
          {dependent ? (
            <>
              <Grid item xs={2}></Grid>
              <Grid item xs={8}>
                <div className={'campoTitle'}>Nome do Associado</div>
                <div className={'campoInput'}>
                  <div className={'campoField'}>{dependent.nome}</div>
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
              <Grid item key={dependent.id} xs={6}>
                <div className={'campoTitle'}>Titular/Dependente</div>
                <div className={'campoInput'}>
                  <div className={'campoField'}>DEPENDENTE</div>
                </div>
              </Grid>
              <Grid item xs={2}></Grid>
            </>
          ) : (
            <>
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
              <Grid item key={9999} xs={6}>
                <div className={'campoTitle'}>Titular/Dependente</div>
                <div className={'campoInput'}>
                  <div className={'campoField'}>TITULAR</div>
                </div>
              </Grid>

              <Grid item xs={2}></Grid>
            </>
          )}
          <Row style={{ display: 'flex', justifyContent: 'flex-end', color: 'white', marginLeft: '85%', fontSize: '0.8rem' }}>
            <span>{ano}</span>
          </Row>
        </Grid>
      </Container>
      <br />
      <div className={'justify-content-between'}>
        <BottomNavigation showLabels style={{ display: 'flex', justifyContent: 'space-around' }} sx={{ backgroundColor: '#fafafa' }}>
          <BottomNavigationAction label="Imprimir" icon={<Print sx={{ fontSize: '30px' }} />} onClick={handlePrint} />

          <BottomNavigationAction label="Baixar" icon={<Download sx={{ fontSize: '30px' }} />} onClick={handleShare} />

          {/*<BottomNavigationAction label="Compartilhar" icon={<Share sx={{ fontSize: '30px' }} />} onClick={handleShare} />*/}
        </BottomNavigation>
        <PrintButton imgData={imgData} /> &nbsp;
      </div>
    </div>
  );
};

export default AssociadoCard;
