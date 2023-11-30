import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import ReservaListWithLocalizationProvider from 'app/modules/relatório/reserva-list';

const TipoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ReservaListWithLocalizationProvider />} />
  </ErrorBoundaryRoutes>
);

export default TipoRoutes;
