import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import IconsRedesSociais from './icons-redes-sociais';
import IconsRedesSociaisDetail from './icons-redes-sociais-detail';
import IconsRedesSociaisUpdate from './icons-redes-sociais-update';
import IconsRedesSociaisDeleteDialog from './icons-redes-sociais-delete-dialog';

const IconsRedesSociaisRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<IconsRedesSociais />} />
    <Route path="new" element={<IconsRedesSociaisUpdate />} />
    <Route path=":id">
      <Route index element={<IconsRedesSociaisDetail />} />
      <Route path="edit" element={<IconsRedesSociaisUpdate />} />
      <Route path="delete" element={<IconsRedesSociaisDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default IconsRedesSociaisRoutes;
