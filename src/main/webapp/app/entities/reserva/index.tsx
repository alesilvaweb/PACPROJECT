import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Reservas from './reserva';
import ReservasDetail from './reserva-detail';
import ReservasUpdate from './reserva-update';
import ReservasDeleteDialog from './reserva-delete-dialog';
import ReservaMessageDialog from 'app/entities/reserva/reserva-message-dialog';
import ReservaUpdateQuadra from 'app/entities/reserva/reserva-update-quadra';

const ReservasRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Reservas />} />
    <Route path="new" element={<ReservasUpdate />} />
    <Route path="new/:local/:start/:end" element={<ReservaUpdateQuadra />} />
    <Route path=":id/:local/delete" element={<ReservasDeleteDialog />} />
    <Route path=":id/:local/message" element={<ReservaMessageDialog />} />
    <Route path=":id/:local/edit" element={<ReservaUpdateQuadra />} />

    <Route path=":id/">
      <Route index element={<ReservasDetail />} />
      <Route path="edit" element={<ReservasUpdate />} />
      <Route path="delete" element={<ReservasDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ReservasRoutes;
