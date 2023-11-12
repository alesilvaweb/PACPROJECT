import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Convenio from './convenio';
import ConvenioDetail from './convenio-detail';
import ConvenioUpdate from './convenio-update';
import ConvenioDeleteDialog from './convenio-delete-dialog';
import ConvenioList from 'app/entities/convenio/convenio-list';
import ConveniosFilter from 'app/entities/convenio/convenios-filter';

const ConvenioRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Convenio />} />
    <Route path="new" element={<ConvenioUpdate />} />
    <Route path="list" element={<ConveniosFilter />} />

    <Route path=":id">
      <Route index element={<ConvenioDetail />} />
      <Route path="edit" element={<ConvenioUpdate />} />
      <Route path="delete" element={<ConvenioDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ConvenioRoutes;
