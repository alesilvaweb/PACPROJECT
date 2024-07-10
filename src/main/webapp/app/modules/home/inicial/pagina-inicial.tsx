import React, { useEffect, useState } from 'react';
import { Grid } from '@mui/material';
import EventAvailableIcon from '@mui/icons-material/EventAvailable';
import { CorporateFare, CreditCard, Handshake } from '@mui/icons-material';
import { useAppSelector } from 'app/config/store';
import { useNavigate } from 'react-router-dom';
import CardDashboard from 'app/modules/home/inicial/card-dashboard';
import ModalInicial from 'app/modules/home/inicial/nodal-inicial';
import Banner from 'app/modules/home/banner/banner';
import '../home.scss';
import validaTelefone from 'app/components/valida-telefone';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { dataAtual } from 'app/shared/util/date-utils';
import axios from 'axios';
import { LoadingBar } from 'react-redux-loading-bar';

const PaginaInicial = () => {
  const navigate = useNavigate();
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);
  const authorities = useAppSelector(state => state.authentication.authorities);
  const account = useAppSelector(state => state.authentication.account);
  const isAuthorized = hasAnyAuthority(account.authorities, ['ROLE_VIEW_REPORT']);
  const [mensagens, setMensagens] = useState([]);
  const hasPopUpBeenShown = sessionStorage.getItem('popUpShow');
  const data = dataAtual();

  if (validaTelefone().length > 0) {
    navigate(`/associado/${account.id}/contato`);
  }

  async function fetchMensagens() {
    try {
      const response = await axios.get(
        `/api/mensagems?endDate.greaterThanOrEqual=${data}&startDate.lessThanOrEqual=${data}&tipoId.equals=11351&status.equals=Ativo&page=0&size=20`
      );
      setMensagens(response.data);
      console.log({ response });
    } catch (error) {
      console.error('Erro ao buscar Mensagens:', error);
    }
  }

  useEffect(() => {
    if (!hasPopUpBeenShown) {
      fetchMensagens();
    }
  }, []);

  const iconStyle = {
    // fontSize:"5vh", // Ajuste o tamanho do ícone conforme necessário
    // position: 'absolute', // Posicionamento absoluto
    // color: 'write',
    // marginTop: 1,  // Distância do topo
    // marginLeft: 1, // Distância da esquerda
    fontSize: '7vh',
    color: 'white',
  };

  if (isAuthorized) {
    navigate('/report');
  } else {
    return (
      <div>
        {isAuthenticated ? (
          <>
            <div>
              <div className={'tituloInicial'}>Bem vindo a plataforma PAC RESERVAS</div>
              <div className={'subtiTileCard'}>
                Explore os recursos exclusivos para membros da PAC RESERVAS
                <span className={'text-descricao'}> e aproveite ao máximo sua associação.</span>
              </div>
            </div>
            {mensagens.length > 0 ? <ModalInicial mensagens={mensagens} /> : null}

            <Grid container className={'grid-cards'}>
              <Grid
                container
                spacing={{ xs: 2, md: 2, sm: 1 }}
                columns={{ xs: 4, sm: 4, md: 12 }}
                sx={{ display: 'flex', flexWrap: 'wrap' }}
              >
                <CardDashboard
                  link={'/Locais'}
                  title={'Reservas'}
                  subtitle={'Faça reservas para eventos aqui.'}
                  icon={<EventAvailableIcon sx={iconStyle} />}
                />
                <CardDashboard
                  link={'/cartao'}
                  title={'Cartões'}
                  subtitle={'Gerencie seus cartões da PAC RESERVAS nesta seção.'}
                  icon={<CreditCard sx={iconStyle} />}
                />
                <CardDashboard
                  link={'/convenio/list'}
                  title={'Convênios'}
                  subtitle={'Descubra os convênios disponíveis para os membros da PAC RESERVAS.'}
                  icon={<Handshake sx={iconStyle} />}
                />
              </Grid>
              <Grid item xs={12} sm={12} md={12} style={{ marginTop: '5px' }}>
                <Banner />
              </Grid>
            </Grid>
            <br />
          </>
        ) : (
          <LoadingBar />
        )}
        <br />
        <br />
      </div>
    );
  }
};

export default PaginaInicial;
