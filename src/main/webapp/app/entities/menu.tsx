import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/local">
        <Translate contentKey="global.menu.entities.local" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/arquivo">
        <Translate contentKey="global.menu.entities.arquivo" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/associado">
        <Translate contentKey="global.menu.entities.associado" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/dependente">
        <Translate contentKey="global.menu.entities.dependente" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/contato">
        <Translate contentKey="global.menu.entities.contato" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/convenio">
        <Translate contentKey="global.menu.entities.convenio" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/imagens-convenio">
        <Translate contentKey="global.menu.entities.imagensConvenio" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/redes-sociais-convenio">
        <Translate contentKey="global.menu.entities.redesSociaisConvenio" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/icons-redes-sociais">
        <Translate contentKey="global.menu.entities.iconsRedesSociais" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/reserva">
        <Translate contentKey="global.menu.entities.reserva" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/departamento">
        <Translate contentKey="global.menu.entities.departamento" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/parametro">
        <Translate contentKey="global.menu.entities.parametro" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/mensagem">
        <Translate contentKey="global.menu.entities.mensagem" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/categoria">
        <Translate contentKey="global.menu.entities.categoria" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/tipo">
        <Translate contentKey="global.menu.entities.tipo" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
