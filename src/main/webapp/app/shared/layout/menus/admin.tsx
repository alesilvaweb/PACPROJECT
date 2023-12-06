import React from 'react';
import MenuItens from 'app/shared/layout/menus/menu-item';
import { Translate, translate } from 'react-jhipster';
import { Button, Menu } from '@mui/material';
import Fade from '@mui/material/Fade';
import MoreVertIcon from '@mui/icons-material/MoreVert';

const adminMenuItems = () => (
  <>
    <MenuItens icon="users" to="/admin/user-management">
      <Translate contentKey="global.menu.admin.userManagement">User management</Translate>
    </MenuItens>
    <MenuItens icon="eye" to="/admin/tracker">
      <Translate contentKey="global.menu.admin.tracker">User tracker</Translate>
    </MenuItens>
    <MenuItens icon="tachometer-alt" to="/admin/metrics">
      <Translate contentKey="global.menu.admin.metrics">Metrics</Translate>
    </MenuItens>
    <MenuItens icon="heart" to="/admin/health">
      <Translate contentKey="global.menu.admin.health">Health</Translate>
    </MenuItens>
    <MenuItens icon="cogs" to="/admin/configuration">
      <Translate contentKey="global.menu.admin.configuration">Configuration</Translate>
    </MenuItens>
    <MenuItens icon="tasks" to="/admin/logs">
      <Translate contentKey="global.menu.admin.logs">Logs</Translate>
    </MenuItens>
  </>
);

const openAPIItem = () => (
  <MenuItens icon="book" to="/admin/docs">
    <Translate contentKey="global.menu.admin.apidocs">API</Translate>
  </MenuItens>
);

export const AdminMenu = ({ showOpenAPI }) => {
  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
  const open = Boolean(anchorEl);
  const handleClick = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };
  const handleClose = () => {
    setAnchorEl(null);
  };

  return (
    // <NavDropdown icon="faEllipsisVertical" name={''} id="admin-menu" data-cy="adminMenu" style={{listStyle:"none"}}>
    //   {adminMenuItems()}
    //   {showOpenAPI && openAPIItem()}
    // </NavDropdown>
    <div>
      <Button
        id="fade-button"
        aria-controls={open ? 'fade-menu' : undefined}
        aria-haspopup="true"
        aria-expanded={open ? 'true' : undefined}
        onClick={handleClick}
        sx={{ color: '#ffffff' }}
      >
        <MoreVertIcon />
      </Button>
      <Menu
        id="fade-menu"
        MenuListProps={{
          'aria-labelledby': 'fade-button',
        }}
        anchorEl={anchorEl}
        open={open}
        onClose={handleClose}
        TransitionComponent={Fade}
      >
        {adminMenuItems()}
        {showOpenAPI && openAPIItem()}
      </Menu>
    </div>
  );
};

export default AdminMenu;
