import React from 'react';
import { Translate } from 'react-jhipster';

import { NavbarBrand, NavItem, NavLink } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppSelector } from 'app/config/store';

export const BrandIcon = props => (
  <div {...props} className="brand-icon">
    <img src="content/images/logo-jhipster.jpeg" alt="Logo" />
  </div>
  // <span className="brand-title">
  //    AASC
  //   </span>
);

export const BrandLogin = () => (
  <NavbarBrand className="brand-logo">
    {/* <BrandIcon /> */}
    <span className="brand-title">
     AASC
    </span>
    {/*<span className="navbar-version">{VERSION}</span>*/}
  </NavbarBrand>
);
export const Brand = () => (
  <NavbarBrand tag={Link} to="/" className="brand-logo">
    <BrandIcon />
    {/*<span className="brand-title">*/}
    {/*  <Translate contentKey="global.title">AAPM</Translate>*/}
    {/*</span>*/}
    {/*<span className="navbar-version">{VERSION}</span>*/}
  </NavbarBrand>
);

export const Home = () => (
  <NavItem>
    <NavLink tag={Link} to="/" className="d-flex align-items-center">
      <FontAwesomeIcon icon="home" />
      <span>
        <Translate contentKey="global.menu.home">Home</Translate>
      </span>
    </NavLink>
  </NavItem>
);
