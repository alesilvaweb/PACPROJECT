import React from 'react';
import { Breadcrumb, BreadcrumbItem } from 'reactstrap';
import { useLocation, useNavigate } from 'react-router-dom';

const Breadcrunbs = ({ atual }) => {
  const location = useLocation();
  const navigate = useNavigate();
  const atualValue = atual || location.pathname.split('/')[1];
  return (
    <Breadcrumb>
      <BreadcrumbItem onClick={() => navigate('/')}>
        <a>Início</a>
      </BreadcrumbItem>
      <BreadcrumbItem active>{atualValue}</BreadcrumbItem>
    </Breadcrumb>
  );
};

export default Breadcrunbs;
