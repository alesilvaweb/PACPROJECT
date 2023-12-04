import * as React from 'react';
import { useEffect, useState } from 'react';
import { styled } from '@mui/material/styles';
import Box from '@mui/material/Box';
import ButtonBase from '@mui/material/ButtonBase';
import Typography from '@mui/material/Typography';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { useNavigate } from 'react-router-dom';
import { getEntities } from 'app/entities/local/local.reducer';
import { BottomNavigation, BottomNavigationAction, Card, Grid } from '@mui/material';
import CardContent from '@mui/material/CardContent';
import CardActions from '@mui/material/CardActions';
import Divider from '@mui/material/Divider';
import { CalendarMonth, Info, LocationOn, Map, WhatsApp } from '@mui/icons-material';
import Breadcrunbs from 'app/components/breadcrunbs';
import Button from '@mui/material/Button';
import { Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import Link from '@mui/material/Link';
import Spinner from 'app/components/spinner';

const ImageButton = styled(ButtonBase)(({ theme }) => ({
  position: 'relative',
  height: 200,
  [theme.breakpoints.down('sm')]: {
    width: '100% !important', // Overrides inline-style
    height: 100,
  },
  '&:hover, &.Mui-focusVisible': {
    zIndex: 1,

    '& .MuiImageBackdrop-root': {
      opacity: 0.15,
    },
    '& .MuiImageMarked-root': {
      opacity: 0,
    },
    '& .MuiTypography-root': {
      border: '4px solid currentColor',
    },
  },
}));

const ImageSrc = styled('span')({
  position: 'absolute',
  left: 0,
  right: 0,
  top: 0,
  bottom: 0,
  backgroundSize: 'cover',
  backgroundPosition: 'center 40%',
});

const Image = styled('span')(({ theme }) => ({
  position: 'absolute',
  left: 0,
  right: 0,
  top: 0,
  bottom: 0,
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  color: theme.palette.common.white,
}));

const ImageBackdrop = styled('span')(({ theme }) => ({
  position: 'absolute',
  left: 0,
  right: 0,
  top: 0,
  bottom: 0,
  backgroundColor: theme.palette.common.black,
  opacity: 0.4,
  transition: theme.transitions.create('opacity'),
}));

const ImageMarked = styled('span')(({ theme }) => ({
  height: 3,
  width: 18,
  backgroundColor: theme.palette.common.white,
  position: 'absolute',
  bottom: -2,
  left: 'calc(50% - 9px)',
  transition: theme.transitions.create('opacity'),
}));

export default function CabanasList() {
  const account = useAppSelector(state => state.authentication.account);
  const locaisList = useAppSelector(state => state.local.entities);
  const loading = useAppSelector(state => state.local.loading);

  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const [modal, setModal] = useState(false);
  const toggle = () => setModal(!modal);

  const maps = `https://www.google.com/maps/place/Associac%C3%A3o+Atl%C3%A9tica+Philip+Morris+-+AAPM/@-29.7616726,-52.4307327,18z/data=!4m6!3m5!1s0x951ca3eb61944605:0x3d9978ee7389b7b!8m2!3d-29.7615903!4d-52.430465!16s%2Fg%2F11c32bznx8?authuser=0&entry=ttu`;

  const center = {
    lat: -29.761491,
    lng: -52.43024,
  };
  const { lat, lng } = center;
  const whatsappMessage = `https://wa.me/?text=Localização:%20https://maps.google.com/?q=${lat},${lng}`;
  const closeBtn = (
    <button className="close" onClick={toggle} type="button">
      &times;
    </button>
  );

  /* Carregamento de locais*/
  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: 1,
        size: 1,
      })
    );
  };

  // Executa função para carregamento das sedes se o usuário estiver logado
  if (account?.login) {
    useEffect(() => {
      getAllEntities();
    }, []);
  } else {
    navigate('/login');
  }

  return (
    <>
      {!loading ? (
        <div>
          <Breadcrunbs atual={'Cabanas'} />
          <Box sx={{ display: 'flex', flexWrap: 'wrap', width: '100%', marginTop: '2vh' }}>
            <h5>Cabanas</h5>
            <Grid container spacing={{ xs: 1, md: 3 }}>
              {locaisList.map(locais => (
                <>
                  {locais.status === 'Ativo' ? (
                    <Grid item xs={12} sm={4} md={4} key={locais.id}>
                      <Card
                        sx={{
                          backgroundColor: '#fafafa',
                          borderRadius: 2,
                          borderWidth: '2px',
                          borderStyle: 'solid',
                          borderColor: '#a1a1a1',
                          ':hover': {
                            boxShadow: 10,
                            position: 'relative',
                            borderColor: '#1975d1',
                          },
                        }}
                      >
                        <ImageButton
                          onClick={() => {
                            navigate('/local/' + locais.id);
                          }}
                          focusRipple
                          key={locais.nome}
                          style={{
                            width: '100%',
                            height: '20rem',
                          }}
                        >
                          <ImageSrc style={{ backgroundImage: `url(data:${locais.imagenContentType};base64,${locais.imagen})` }} />
                          <ImageBackdrop className="MuiImageBackdrop-root" />
                          <Image>
                            <Typography
                              component="span"
                              variant="subtitle1"
                              color="inherit"
                              fontSize={'200%'}
                              sx={{
                                position: 'relative',
                                p: 4,
                                pt: 2,
                                pb: theme => `calc(${theme.spacing(1)} + 6px)`,
                              }}
                            >
                              {locais.nome}
                              <ImageMarked className="MuiImageMarked-root" />
                            </Typography>
                          </Image>
                        </ImageButton>

                        <CardContent>
                          <Typography gutterBottom variant="subtitle2" component="div">
                            {/*{locais.descricao}*/}
                          </Typography>
                        </CardContent>
                        <Divider />
                        <CardActions></CardActions>
                        <BottomNavigation showLabels style={{ display: 'flex' }} sx={{ backgroundColor: '#fafafa' }}>
                          <BottomNavigationAction
                            label="Informações"
                            icon={<Info sx={{ fontSize: '30px' }} />}
                            onClick={() => {
                              navigate('/local/detail/' + locais.id);
                            }}
                          />
                          <BottomNavigationAction
                            label="Reservas"
                            icon={<CalendarMonth sx={{ fontSize: '30px' }} />}
                            onClick={() => {
                              navigate('/local/' + locais.id);
                            }}
                          />

                          <BottomNavigationAction
                            label="Localização"
                            icon={<LocationOn sx={{ fontSize: '30px' }} />}
                            onClick={() => {
                              toggle();
                            }}
                          />
                        </BottomNavigation>
                      </Card>
                    </Grid>
                  ) : null}
                </>
              ))}
            </Grid>
          </Box>

          <Modal isOpen={modal} toggle={toggle} style={{ marginTop: '30vh' }}>
            <ModalHeader toggle={toggle} close={closeBtn}>
              Localização
            </ModalHeader>
            <ModalBody>
              <div style={{ display: 'flex', justifyContent: 'space-around', flexWrap: 'wrap' }}>
                <Button
                  onClick={() => {
                    window.open(whatsappMessage, '_blank');
                    toggle();
                  }}
                  startIcon={<WhatsApp />}
                  color={'success'}
                >
                  Compartilhar
                </Button>
                &nbsp;
                <Button
                  onClick={() => {
                    window.open(maps, '_blank');
                    toggle();
                  }}
                  startIcon={<Map />}
                  color={'primary'}
                >
                  Abrir no Maps
                </Button>
              </div>
            </ModalBody>
            <ModalFooter>
              <Button color="primary" onClick={toggle}>
                Fechar
              </Button>
            </ModalFooter>
          </Modal>
        </div>
      ) : (
        // <div>Carregando...</div>
        <Spinner text={'cabanas'} />
      )}
    </>
  );
}
