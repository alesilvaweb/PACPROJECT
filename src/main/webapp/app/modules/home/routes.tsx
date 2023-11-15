import React from 'react';
import { Route } from 'react-router-dom';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import PaginaInicial from 'app/modules/home/pagina-inicial';
import TelaInicial from 'app/modules/home/aapm-dashboard';
import AAPMDashboard from 'app/modules/home/aapm-dashboard';
import Inicial from 'app/modules/home/inicial';
import Sobre from 'app/modules/home/sobre';
import Guia from 'app/modules/home/guia';
import Events from 'app/modules/home/events';
import MapaGoogle from 'app/modules/localizacao/MapaGoogle';
import ReservaListWithLocalizationProvider from 'app/entities/reserva/reserva-list';
import AssociadoCard from 'app/modules/CartaoAssociado/AssociadoCard';

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        <Route path="home/*" element={<AAPMDashboard />} />
        <Route index element={<PaginaInicial />} />
        <Route path="cabanas" element={<Inicial />} />
        <Route path="sobre" element={<Sobre />} />
        <Route path="guia" element={<Guia />} />
        <Route path="events" element={<Events />} />
        <Route path="mapa" element={<MapaGoogle />} />
        <Route path="report" element={<ReservaListWithLocalizationProvider />} />
        <Route path="cartao" element={<AssociadoCard />} />
      </ErrorBoundaryRoutes>
    </div>
  );
};
