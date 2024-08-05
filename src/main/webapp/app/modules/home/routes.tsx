import React from 'react';
import { Route, useNavigate } from 'react-router-dom';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import PaginaInicial from 'app/modules/home/inicial/pagina-inicial';
// import AAPMDashboard from 'app/modules/home/inicial/aapm-dashboard';
import CabanasList from 'app/modules/home/cabanas-list';
import Sobre from 'app/modules/home/info/sobre';
import Guia from 'app/modules/home/info/guia';
import MapaGoogle from 'app/modules/localizacao/MapaGoogle';
import CartaoList from 'app/modules/CartaoAssociado/cartao-list';
import App from 'app/modules/callendar/callendar';
import ScheduleCourt from 'app/modules/quadras/ScheduleCourt';

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/*<Route path="home/*" element={<AAPMDashboard />} />*/}
        <Route index element={<CabanasList />} />
        <Route path="Locais" element={<CabanasList />} />
        <Route path="calendar" element={<App />} />
        <Route path="quadra" element={<ScheduleCourt />} />

        <Route path="sobre" element={<Sobre />} />
        <Route path="guia" element={<Guia />} />
        <Route path="mapa" element={<MapaGoogle />} />
        {/* <Route path="cartao" element={<CartaoList />} /> */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
