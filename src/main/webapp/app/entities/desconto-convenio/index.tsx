import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DescontoConvenio from './desconto-convenio';
import DescontoConvenioDetail from './desconto-convenio-detail';
import DescontoConvenioUpdate from './desconto-convenio-update';
import DescontoConvenioDeleteDialog from './desconto-convenio-delete-dialog';

const DescontoConvenioRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DescontoConvenio />} />
    <Route path="new" element={<DescontoConvenioUpdate />} />
    <Route path=":id">
      <Route index element={<DescontoConvenioDetail />} />
      <Route path="edit" element={<DescontoConvenioUpdate />} />
      <Route path="delete" element={<DescontoConvenioDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DescontoConvenioRoutes;
