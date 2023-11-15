import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Local from './local';
import Arquivo from './arquivo';
import Associado from './associado';
import Dependente from './dependente';
import Contato from './contato';
import Convenio from './convenio';
import DescontoConvenio from './desconto-convenio';
import ImagensConvenio from './imagens-convenio';
import RedesSociaisConvenio from './redes-sociais-convenio';
import IconsRedesSociais from './icons-redes-sociais';
import Reserva from './reserva';
import Departamento from './departamento';
import Parametro from './parametro';
import Mensagem from './mensagem';
import Categoria from './categoria';
import Tipo from './tipo';

/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="local/*" element={<Local />} />
        <Route path="arquivo/*" element={<Arquivo />} />
        <Route path="associado/*" element={<Associado />} />
        <Route path="dependente/*" element={<Dependente />} />
        <Route path="contato/*" element={<Contato />} />
        <Route path="convenio/*" element={<Convenio />} />
        <Route path="imagens-convenio/*" element={<ImagensConvenio />} />
        <Route path="desconto-convenio/*" element={<DescontoConvenio />} />
        <Route path="redes-sociais-convenio/*" element={<RedesSociaisConvenio />} />
        <Route path="icons-redes-sociais/*" element={<IconsRedesSociais />} />
        <Route path="reserva/*" element={<Reserva />} />
        <Route path="departamento/*" element={<Departamento />} />
        <Route path="parametro/*" element={<Parametro />} />
        <Route path="mensagem/*" element={<Mensagem />} />
        <Route path="categoria/*" element={<Categoria />} />
        <Route path="tipo/*" element={<Tipo />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
