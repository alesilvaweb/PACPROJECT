import * as React from 'react';
import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import List from '@mui/material/List';
import Divider from '@mui/material/Divider';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import InboxIcon from '@mui/icons-material/MoveToInbox';
import HomeIcon from '@mui/icons-material/Home';
import { AdminMenu } from 'app/shared/layout/menus';
import { useAppDispatch } from 'app/config/store';
import { Storage, Translate, translate } from 'react-jhipster';
import { setLocale } from 'app/shared/reducers/locale';
import { ListSubheader } from '@mui/material';
import ExpandLess from '@mui/icons-material/ExpandLess';
import ExpandMore from '@mui/icons-material/ExpandMore';

import Collapse from '@mui/material/Collapse';
import { useNavigate } from 'react-router-dom';
import { ItemSidebar } from 'app/shared/layout/menus/item-sidebar';
import {
  AddHomeWork,
  AdminPanelSettings,
  CalendarMonth,
  CorporateFare,
  CreditCard,
  Grading,
  GroupAdd,
  ManageAccounts,
  Schedule,
  Settings,
} from '@mui/icons-material';

type Anchor = 'top' | 'left' | 'bottom' | 'right';

export default function Sidebar({ isOpen, setIsOpen, currentLocale, isOpenAPIEnabled, isAdmin, isAuthenticated }) {
  const dispatch = useAppDispatch();
  const handleLocaleChange = event => {
    const langKey = event.target.value;
    Storage.session.set('locale', langKey);
    dispatch(setLocale(langKey));
  };
  const [open, setOpen] = React.useState(true);
  const navigate = useNavigate();

  const [state, setState] = React.useState({
    top: false,
    left: false,
    bottom: false,
    right: false,
  });
  const [stateMenu, setStateMenu] = React.useState({
    entity: false,
    report: false,
    admin: false,
    language: false,
  });

  /*Abertura do menu do sidebar*/
  const handleClick = (anchor: string, open: boolean) => {
    setStateMenu({ ...stateMenu, [anchor]: open });
  };
  /*Abertura do sidebar*/
  const toggleDrawer = (anchor: Anchor, open: boolean) => (event: React.KeyboardEvent | React.MouseEvent) => {
    if (event.type === 'keydown' && ((event as React.KeyboardEvent).key === 'Tab' || (event as React.KeyboardEvent).key === 'Shift')) {
      return;
    }
    setState({ ...state, ['left']: open });
    setIsOpen(open);
  };

  /* Lista de menus */
  const list = (anchor: Anchor) => (
    <Box sx={{ width: anchor === 'top' || anchor === 'bottom' ? 'auto' : 250 }} role="presentation">
      <List
        sx={{ width: '100%', maxWidth: 360 }}
        component="nav"
        aria-labelledby="nested-list-subheader"
        subheader={
          <ListSubheader component="div" id="nested-list-subheader">
            Associação Atlética Philip Morris
          </ListSubheader>
        }
      >
        {/* Menus Fixos */}
        <ItemSidebar title={'Página inicial'} link={'/'} icon={<HomeIcon />} setIsOpen={setIsOpen} />
        <ItemSidebar title={'Cartão Sócio'} link={'/cartao'} icon={<CreditCard />} setIsOpen={setIsOpen} />
        <ItemSidebar title={'Convênio'} link={'/convenio/list'} icon={<CorporateFare />} setIsOpen={setIsOpen} />
        <ItemSidebar title={'Reservas'} link={'/cabanas'} icon={<Schedule />} setIsOpen={setIsOpen} />
        <hr />

        {isAdmin ? (
          <>
            {/* Grupo de menus ADM */}
            <ListItemButton onClick={() => handleClick('admin', !stateMenu.admin)}>
              <ListItemIcon>
                <AdminPanelSettings />
              </ListItemIcon>
              <ListItemText primary={translate('global.menu.admin.main')} />
              {stateMenu ? <ExpandLess /> : <ExpandMore />}
            </ListItemButton>

            {/* SubMenus de ADM */}
            <Collapse in={stateMenu.admin} timeout="auto" unmountOnExit>
              <List component="div" disablePadding>
                <ItemSidebar title={'Associados'} link={'/associado'} icon={<GroupAdd />} setIsOpen={setIsOpen} />
                <ItemSidebar
                  title={<Translate contentKey="global.menu.admin.userManagement">User management</Translate>}
                  link={'/admin/user-management'}
                  icon={<ManageAccounts />}
                  setIsOpen={setIsOpen}
                />
                <ItemSidebar title={'Parâmetros'} link={'/parametro'} icon={<Settings />} setIsOpen={setIsOpen} />
              </List>
            </Collapse>

            {/* Grupo de menus de Cadastros */}
            <ListItemButton onClick={() => handleClick('entity', !stateMenu.entity)}>
              <ListItemIcon>
                <InboxIcon />
              </ListItemIcon>
              <ListItemText primary="Cadastros" />
              {stateMenu ? <ExpandLess /> : <ExpandMore />}
            </ListItemButton>

            {/* SubMenus de Cadastros*/}
            <Collapse in={stateMenu.entity} timeout="auto" unmountOnExit>
              <List component="div" disablePadding>
                <ItemSidebar title={'Convênios'} link={'/convenio/list'} icon={<CorporateFare />} setIsOpen={setIsOpen} />
                <ItemSidebar title={'Categorias'} link={'/categoria'} icon={<CorporateFare />} setIsOpen={setIsOpen} />
                <ItemSidebar title={'Departamentos'} link={'/departamento'} icon={<CorporateFare />} setIsOpen={setIsOpen} />
                <ItemSidebar title={'Locais'} link={'/local'} icon={<AddHomeWork />} setIsOpen={setIsOpen} />
                <ItemSidebar title={'Mensagens'} link={'/mensagem'} icon={<CorporateFare />} setIsOpen={setIsOpen} />
              </List>
            </Collapse>

            {/* Grupo de menus de Relatórios*/}
            <ListItemButton onClick={() => handleClick('report', !stateMenu.report)}>
              <ListItemIcon>
                <Grading />
              </ListItemIcon>
              <ListItemText primary="Relatórios" />
              {stateMenu ? <ExpandLess /> : <ExpandMore />}
            </ListItemButton>

            {/* SubMenus de Reservas */}
            <Collapse in={stateMenu.report} timeout="auto" unmountOnExit>
              <List component="div" disablePadding>
                <ItemSidebar title={'Reservas'} link={'/report'} icon={<CalendarMonth />} setIsOpen={setIsOpen} />
              </List>
            </Collapse>
          </>
        ) : null}
      </List>
    </Box>
  );

  return (
    <div>
      <Drawer anchor={'left'} open={isOpen} onClose={toggleDrawer('left', false)}>
        {list('left')}
        <Box sx={{ width: 240, p: 3 }}>
          {/*{isAuthenticated && <EntitiesMenu />}*/}
          {isAuthenticated && isAdmin && <AdminMenu showOpenAPI={isOpenAPIEnabled} />}
          {/*  <LocaleMenu currentLocale={currentLocale} onClick={handleLocaleChange} />*/}
        </Box>
        <Divider />
        <Box
          sx={{
            bottom: 10,
            position: 'fixed',
            textAlign: 'center',
          }}
        >
          <span className="navbar-version">Versão : {VERSION}</span>
        </Box>
      </Drawer>
    </div>
  );
}
