import React from 'react';
import { Breadcrumb, BreadcrumbItem } from 'reactstrap';
import { useLocation, useNavigate } from 'react-router-dom';

const Breadcrunbs = () => {
  const location = useLocation();
  const navigate = useNavigate();
  return (
    <Breadcrumb>
      <BreadcrumbItem onClick={() => navigate('/')}>
        <a>Início</a>
      </BreadcrumbItem>
      <BreadcrumbItem active>{location.pathname.split('/')[1]}</BreadcrumbItem>
    </Breadcrumb>
  );
};

export default Breadcrunbs;
