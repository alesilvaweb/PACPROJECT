import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Tipo from './tipo';
import TipoDetail from './tipo-detail';
import TipoUpdate from './tipo-update';
import TipoDeleteDialog from './tipo-delete-dialog';

const TipoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Tipo />} />
    <Route path="new" element={<TipoUpdate />} />
    <Route path=":id">
      <Route index element={<TipoDetail />} />
      <Route path="edit" element={<TipoUpdate />} />
      <Route path="delete" element={<TipoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TipoRoutes;
