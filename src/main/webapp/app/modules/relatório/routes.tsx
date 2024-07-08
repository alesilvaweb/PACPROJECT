import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import ReservaListWithLocalizationProvider from 'app/modules/relatório/reserva-list';

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        <Route path="*" element={<ReservaListWithLocalizationProvider />} />
      </ErrorBoundaryRoutes>
    </div>
  );
};
