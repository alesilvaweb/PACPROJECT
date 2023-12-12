import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAppSelector } from 'app/config/store';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';
import { toast } from 'react-toastify';

function IsAdm() {
  const navigate = useNavigate();
  const isAdmin = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.ADMIN]));

  if (!isAdmin) {
    useEffect(() => {
      toast.error('Acesso não autorizado');
    }, []);

    return navigate('/');
  }

  return <div></div>;
}

export default IsAdm;
