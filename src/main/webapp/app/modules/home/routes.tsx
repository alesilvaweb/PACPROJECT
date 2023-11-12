import React from 'react';
import { Route } from 'react-router-dom';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import PaginaInicial from 'app/modules/home/pagina-inicial';
import TelaInicial from 'app/modules/home/aapm-dashboard';
import AAPMDashboard from 'app/modules/home/aapm-dashboard';

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        <Route path="home/*" element={<AAPMDashboard />} />
      </ErrorBoundaryRoutes>
    </div>
  );
};
