import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Contato from './contato';
import ContatoDetail from './contato-detail';
import ContatoUpdate from './contato-update';
import ContatoDeleteDialog from './contato-delete-dialog';

const ContatoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Contato />} />
    <Route path="new" element={<ContatoUpdate />} />
    <Route path=":id">
      <Route index element={<ContatoDetail />} />
      <Route path="edit" element={<ContatoUpdate />} />
      <Route path="delete" element={<ContatoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ContatoRoutes;
