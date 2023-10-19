import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Dependente from './dependente';
import DependenteDetail from './dependente-detail';
import DependenteUpdate from './dependente-update';
import DependenteDeleteDialog from './dependente-delete-dialog';

const DependenteRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Dependente />} />
    <Route path="new" element={<DependenteUpdate />} />
    <Route path=":id">
      <Route index element={<DependenteDetail />} />
      <Route path="edit" element={<DependenteUpdate />} />
      <Route path="delete" element={<DependenteDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DependenteRoutes;
