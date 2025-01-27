import 'react-toastify/dist/ReactToastify.css';
import './app.scss';
import './../content/css/loading.css';
import 'app/config/dayjs.ts';

import React, { useEffect } from 'react';
import { BrowserRouter } from 'react-router-dom';
import { ToastContainer, toast } from 'react-toastify';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getSession } from 'app/shared/reducers/authentication';
import { getProfile } from 'app/shared/reducers/application-profile';
import Header from 'app/shared/layout/header/header';
import Footer from 'app/shared/layout/footer/footer';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import ErrorBoundary from 'app/shared/error/error-boundary';
import { AUTHORITIES } from 'app/config/constants';
import AppRoutes from 'app/routes';
import { createTheme, ThemeProvider } from '@mui/material/styles';

const baseHref = document.querySelector('base').getAttribute('href').replace(/\/$/, '');

export const App = () => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getSession());
    dispatch(getProfile());
  }, []);

  const currentLocale = useAppSelector(state => state.locale.currentLocale);
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);
  const isAdmin = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.ADMIN]));
  const ribbonEnv = useAppSelector(state => state.applicationProfile.ribbonEnv);
  const isInProduction = useAppSelector(state => state.applicationProfile.inProduction);
  const isOpenAPIEnabled = useAppSelector(state => state.applicationProfile.isOpenAPIEnabled);
  const darkTheme = createTheme({
    palette: {
      mode: 'light',
      primary: {
        main: '#1e77c5',
      },
    },
  });
  const paddingTop = '60px';
  return (
    <ThemeProvider theme={darkTheme}>
      <BrowserRouter basename={baseHref}>
        <div className="app-container" style={{ paddingTop }}>
          <ToastContainer
            position={toast.POSITION.BOTTOM_LEFT}
            style={{ marginBottom: '10px' }}
            className="toastify-container"
            toastClassName="toastify-toast"
          />
          <ErrorBoundary>
            <Header
              isAuthenticated={isAuthenticated}
              isAdmin={isAdmin}
              currentLocale={currentLocale}
              ribbonEnv={ribbonEnv}
              isInProduction={isInProduction}
              isOpenAPIEnabled={isOpenAPIEnabled}
            />
          </ErrorBoundary>
          <div className="container-fluid view-container" id="app-view-container">
            {/*<Card className="jh-card">*/}
            <ErrorBoundary>
              <AppRoutes />
            </ErrorBoundary>
            {/*</Card>*/}
          </div>
          <Footer />
        </div>
      </BrowserRouter>
    </ThemeProvider>
  );
};

export default App;
