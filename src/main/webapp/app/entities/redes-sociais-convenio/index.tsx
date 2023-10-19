import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import RedesSociaisConvenio from './redes-sociais-convenio';
import RedesSociaisConvenioDetail from './redes-sociais-convenio-detail';
import RedesSociaisConvenioUpdate from './redes-sociais-convenio-update';
import RedesSociaisConvenioDeleteDialog from './redes-sociais-convenio-delete-dialog';

const RedesSociaisConvenioRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<RedesSociaisConvenio />} />
    <Route path="new" element={<RedesSociaisConvenioUpdate />} />
    <Route path=":id">
      <Route index element={<RedesSociaisConvenioDetail />} />
      <Route path="edit" element={<RedesSociaisConvenioUpdate />} />
      <Route path="delete" element={<RedesSociaisConvenioDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default RedesSociaisConvenioRoutes;
