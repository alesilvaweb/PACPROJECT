import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ImagensConvenio from './imagens-convenio';
import ImagensConvenioDetail from './imagens-convenio-detail';
import ImagensConvenioUpdate from './imagens-convenio-update';
import ImagensConvenioDeleteDialog from './imagens-convenio-delete-dialog';

const ImagensConvenioRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ImagensConvenio />} />
    <Route path="new" element={<ImagensConvenioUpdate />} />
    <Route path=":id">
      <Route index element={<ImagensConvenioDetail />} />
      <Route path="edit" element={<ImagensConvenioUpdate />} />
      <Route path="delete" element={<ImagensConvenioDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ImagensConvenioRoutes;
