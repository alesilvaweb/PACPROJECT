import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Locais from './local';
import LocaisDetail from './local-detail';
import LocaisUpdate from './local-update';
import LocaisDeleteDialog from './local-delete-dialog';
import LocaisAgenda from 'app/entities/local/locais-agenda';
import LocalDetail from 'app/entities/local/local-detail';

const LocaisRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Locais />} />
    <Route path="new" element={<LocaisUpdate />} />
    <Route path="detail/:id" element={<LocalDetail />} />
    <Route path=":id/:ok" element={<LocaisAgenda />} />
    <Route path=":id">
      <Route index element={<LocaisAgenda />} />
      <Route path="edit" element={<LocaisUpdate />} />
      <Route path="delete" element={<LocaisDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default LocaisRoutes;
