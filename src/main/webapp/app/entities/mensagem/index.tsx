import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Mensagem from './mensagem';
import MensagemDetail from './mensagem-detail';
import MensagemUpdate from './mensagem-update';
import MensagemDeleteDialog from './mensagem-delete-dialog';

const MensagemRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Mensagem />} />
    <Route path="new" element={<MensagemUpdate />} />
    <Route path=":id">
      <Route index element={<MensagemDetail />} />
      <Route path="edit" element={<MensagemUpdate />} />
      <Route path="delete" element={<MensagemDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MensagemRoutes;
