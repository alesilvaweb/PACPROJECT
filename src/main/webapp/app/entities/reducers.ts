import local from 'app/entities/local/local.reducer';
import arquivo from 'app/entities/arquivo/arquivo.reducer';
import associado from 'app/entities/associado/associado.reducer';
import dependente from 'app/entities/dependente/dependente.reducer';
import contato from 'app/entities/contato/contato.reducer';
import convenio from 'app/entities/convenio/convenio.reducer';
import descontoConvenio from 'app/entities/desconto-convenio/desconto-convenio.reducer';
import imagensConvenio from 'app/entities/imagens-convenio/imagens-convenio.reducer';
import redesSociaisConvenio from 'app/entities/redes-sociais-convenio/redes-sociais-convenio.reducer';
import iconsRedesSociais from 'app/entities/icons-redes-sociais/icons-redes-sociais.reducer';
import reserva from 'app/entities/reserva/reserva.reducer';
import departamento from 'app/entities/departamento/departamento.reducer';
import parametro from 'app/entities/parametro/parametro.reducer';
import mensagem from 'app/entities/mensagem/mensagem.reducer';
import categoria from 'app/entities/categoria/categoria.reducer';
import tipo from 'app/entities/tipo/tipo.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  local,
  arquivo,
  associado,
  dependente,
  contato,
  convenio,
  descontoConvenio,
  imagensConvenio,
  redesSociaisConvenio,
  iconsRedesSociais,
  reserva,
  departamento,
  parametro,
  mensagem,
  categoria,
  tipo,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
