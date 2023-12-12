import React from 'react';
import { Route } from 'react-router-dom';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import Arquivo from './arquivo';
import ArquivoDetail from './arquivo-detail';
import ArquivoUpdate from './arquivo-update';
import ArquivoDeleteDialog from './arquivo-delete-dialog';

const ArquivoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Arquivo />} />
    <Route path="new" element={<ArquivoUpdate />} />
    <Route path=":id">
      <Route index element={<ArquivoDetail />} />
      <Route path="edit" element={<ArquivoUpdate />} />
      <Route path="delete" element={<ArquivoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ArquivoRoutes;
