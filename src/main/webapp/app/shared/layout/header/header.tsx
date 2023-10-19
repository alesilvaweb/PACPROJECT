import './header.scss';

import React, { useState } from 'react';
import { Storage, Translate } from 'react-jhipster';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { Brand } from './header-components';
import { AccountMenuMaterial } from '../menus';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { setLocale } from 'app/shared/reducers/locale';
import { AppBar, Box, Container, IconButton, LinearProgress, Toolbar } from '@mui/material';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import Sidebar from 'app/shared/layout/header/sidebar';
import { Lock, Menu } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';

export interface IHeaderProps {
  isAuthenticated: boolean;
  isAdmin: boolean;
  ribbonEnv: string;
  isInProduction: boolean;
  isOpenAPIEnabled: boolean;
  currentLocale: string;
}

const Header = ({ currentLocale, isInProduction, ribbonEnv, isOpenAPIEnabled, isAdmin, isAuthenticated }: IHeaderProps) => {
  const [menuOpen, setMenuOpen] = useState(false);

  const dispatch = useAppDispatch();

  const darkTheme = createTheme({
    palette: {
      mode: 'dark',
      primary: {
        main: '#1976d2',
      },
    },
  });
  const handleLocaleChange = event => {
    const langKey = event.target.value;
    Storage.session.set('locale', langKey);
    dispatch(setLocale(langKey));
  };

  const renderDevRibbon = () =>
    isInProduction === false ? (
      <div className="ribbon dev">
        <a href="">
          <Translate contentKey={`global.ribbon.${ribbonEnv}`} />
        </a>
      </div>
    ) : null;

  const toggleMenu = () => setMenuOpen(!menuOpen);
  const navigate = useNavigate();
  const [isOpen, setIsOpen] = useState(false);
  const loading = useAppSelector(state => state.reserva.loading);
  const loadingLocal = useAppSelector(state => state.local.loading);
  return (
    <ThemeProvider theme={darkTheme}>
      <AppBar>
        <Container maxWidth="lg">
          <Toolbar disableGutters>
            <Box sx={{ mr: 1 }}>
              <IconButton size="large" color="inherit" onClick={() => setIsOpen(true)}>
                <Menu />
              </IconButton>
            </Box>
            <Typography variant="h6" component="h1" noWrap sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' } }}>
              <Brand />
            </Typography>
            <Typography variant="h6" component="h1" noWrap sx={{ flexGrow: 1, display: { xs: 'flex', md: 'none' } }}>
              <Brand />
            </Typography>

            {!isAuthenticated ? (
              <Button color="inherit" startIcon={<Lock />} onClick={() => navigate('/login')}>
                Login
              </Button>
            ) : (
              <>
                <AccountMenuMaterial isAuthenticated={isAuthenticated} />
              </>
            )}
          </Toolbar>
        </Container>
        {loading ? <LinearProgress color={'error'} sx={{ borderRadius: 5 }} /> : null}
        {loadingLocal ? <LinearProgress color={'error'} sx={{ borderRadius: 5 }} /> : null}
      </AppBar>

      {/* <Sidebar {...{ isOpen, setIsOpen, currentLocale, isOpenAPIEnabled, isAdmin, isAuthenticated }} /> */}
      <Sidebar {...{ isOpen, setIsOpen, currentLocale, isOpenAPIEnabled, isAdmin, isAuthenticated }} />
    </ThemeProvider>
  );

  // <div id="app-header">
  //   {/*{renderDevRibbon()}*/}
  //   <LoadingBar className="loading-bar" />
  //   <Navbar data-cy="navbar" dark expand="md" fixed="top" className="bg-primary">
  //     <NavbarToggler aria-label="Menu" onClick={toggleMenu} />
  //     <Brand />
  //     <Collapse isOpen={menuOpen} navbar>
  //       <Nav id="header-tabs" className="ms-auto" navbar>
  //         {props.isAuthenticated && <EntitiesMenu />}
  //         {props.isAuthenticated && props.isAdmin && <AdminMenu showOpenAPI={props.isOpenAPIEnabled} />}
  //         <LocaleMenu currentLocale={props.currentLocale} onClick={handleLocaleChange} />
  //         <AccountMenu isAuthenticated={props.isAuthenticated} />
  //       </Nav>
  //     </Collapse>
  //   </Navbar>
  // </div>
  // );
};

export default Header;
