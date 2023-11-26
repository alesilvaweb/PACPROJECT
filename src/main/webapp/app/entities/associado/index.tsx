import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Associado from './associado';
import AssociadoDetail from './associado-detail';
import AssociadoUpdate from './associado-update';
import AssociadoDeleteDialog from './associado-delete-dialog';
import AssociadoFilter from 'app/entities/associado/associado-filter-list';
import AssociadoUpdateTelefone from 'app/entities/associado/associado-update-telefone';

const AssociadoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AssociadoFilter />} />
    <Route path="new" element={<AssociadoUpdate />} />
    <Route path=":id">
      <Route index element={<AssociadoDetail />} />
      <Route path="edit" element={<AssociadoUpdate />} />
      <Route path="contato" element={<AssociadoUpdateTelefone />} />
      <Route path="delete" element={<AssociadoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AssociadoRoutes;
