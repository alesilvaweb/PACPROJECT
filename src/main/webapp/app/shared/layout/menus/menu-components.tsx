import React from 'react';

import { UncontrolledDropdown, DropdownToggle, DropdownMenu } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEllipsisVertical } from '@fortawesome/free-solid-svg-icons';

export const NavDropdown = props => (
  <UncontrolledDropdown
    nav
    inNavbar
    className={'dropstart'}
    direction={'start'}
    id={props.id}
    data-cy={props['data-cy']}
    style={{ listStyle: 'none' }}
  >
    <DropdownToggle nav className="d-flex align-items-center ">
      <FontAwesomeIcon icon={faEllipsisVertical} />
      <span>{props.name}</span>
    </DropdownToggle>
    <DropdownMenu style={props.style}>{props.children}</DropdownMenu>
  </UncontrolledDropdown>
);
