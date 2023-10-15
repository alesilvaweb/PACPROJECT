import React from 'react';
import { Route } from 'react-router-dom';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import PaginaInicial from 'app/modules/home/pagina-inicial';

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        <Route path="home/*" element={<PaginaInicial />} />
      </ErrorBoundaryRoutes>
    </div>
  );
};
