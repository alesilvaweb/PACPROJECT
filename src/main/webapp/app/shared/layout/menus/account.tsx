import React from 'react';
// import MenuItem from 'app/shared/layout/menus/menu-item';
import MenuItem from '@mui/material/MenuItem';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';
import Button from '@mui/material/Button';
import Menu from '@mui/material/Menu';
import Fade from '@mui/material/Fade';
import { useAppSelector } from 'app/config/store';
import { useNavigate } from 'react-router-dom';
import { Lock, Login, Person } from '@mui/icons-material';
import { LetterAvatar } from 'app/components/letterAvatar';
import { hasAnyAuthority } from 'app/shared/auth/private-route';

// const accountMenuItemsAuthenticated = () => (
//   <>
//     <MenuItem icon="wrench" to="/account/settings" data-cy="settings">
//       <Translate contentKey="global.menu.account.settings">Settings</Translate>
//     </MenuItem>
//     <MenuItem icon="lock" to="/account/password" data-cy="passwordItem">
//       <Translate contentKey="global.menu.account.password">Password</Translate>
//     </MenuItem>
//     <MenuItem icon="sign-out-alt" to="/logout" data-cy="logout">
//       <Translate contentKey="global.menu.account.logout">Sign out</Translate>
//     </MenuItem>
//   </>
// );
//
// const accountMenuItems = () => (
//   <>
//     <MenuItem id="login-item" icon="sign-in-alt" to="/login" data-cy="login">
//       <Translate contentKey="global.menu.account.login">Sign in</Translate>
//     </MenuItem>
//     <MenuItem icon="user-plus" to="/account/register" data-cy="register">
//       <Translate contentKey="global.menu.account.register">Register</Translate>
//     </MenuItem>
//   </>
// );
//
// export const AccountMenu = ({ isAuthenticated = false }) => (
//   <NavDropdown icon="user" name={' Alessandro'} id="account-menu" data-cy="accountMenu">
//     {isAuthenticated && accountMenuItemsAuthenticated()}
//     {!isAuthenticated && accountMenuItems()}
//   </NavDropdown>
// );
export const AccountMenuMaterial = ({ isAuthenticated = false }) => {
  const navigate = useNavigate();
  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
  const open = Boolean(anchorEl);
  const handleClick = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };
  const account = useAppSelector(state => state.authentication.account);
  const isReport = hasAnyAuthority(account.authorities, ['ROLE_VIEW_REPORT']);
  const handleClose = link => {
    setAnchorEl(null);
    navigate(link);
  };
  return (
    <div>
      <Button
        id="fade-button"
        aria-controls={open ? 'fade-menu' : undefined}
        aria-haspopup="true"
        aria-expanded={open ? 'true' : undefined}
        onClick={handleClick}
        color="inherit"
        // startIcon={}
      >
        <LetterAvatar>{account.firstName[0] + account.firstName[1]}</LetterAvatar>
        {/*{account.firstName}*/}
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
        {isAuthenticated && (
          <>
            <>
              {!isReport ? (
                <MenuItem
                  onClick={() => {
                    handleClose(`/associado/${account.id}/contato`);
                  }}
                >
                  <Translate contentKey="global.menu.account.settings">Settings</Translate>
                </MenuItem>
              ) : null}
            </>
            <MenuItem
              onClick={() => {
                handleClose('/account/password');
              }}
            >
              <Translate contentKey="global.menu.account.password">Password</Translate>
            </MenuItem>
            <MenuItem
              onClick={() => {
                handleClose('/logout');
              }}
            >
              <Translate contentKey="global.menu.account.logout">Sign out</Translate>
            </MenuItem>
          </>
        )}
      </Menu>
    </div>
  );

  // <NavDropdown icon="user" name={' Alessandro'} id="account-menu" data-cy="accountMenu">
  //   {isAuthenticated && accountMenuItemsAuthenticated()}
  //   {!isAuthenticated && accountMenuItems()}
  // </NavDropdown>
};

export default AccountMenuMaterial;
