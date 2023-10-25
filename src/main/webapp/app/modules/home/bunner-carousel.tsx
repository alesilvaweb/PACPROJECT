import React, { useState, useEffect } from 'react';
import 'primereact/resources/themes/lara-light-indigo/theme.css';
import { Carousel, CarouselResponsiveOption } from 'primereact/carousel';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { useNavigate } from 'react-router-dom';
import { getEntities } from 'app/entities/local/local.reducer';
import { getConvenios } from 'app/entities/convenio/convenio.reducer';
import Card from '@mui/material/Card';

interface Local {
  imagenContentType: string | undefined;
  id: string;
  nome: string;
  descricao: string;
  imagen: string | undefined;
}

export default function BunnerCarousel() {
  const convenioList = useAppSelector(state => state.convenio.entities);
  const loading = useAppSelector(state => state.convenio.loading);
  const locaisList = useAppSelector(state => state.local.entities);
  const navigate = useNavigate();
  const dispatch = useAppDispatch();

  const responsiveOptions: CarouselResponsiveOption[] = [
    {
      breakpoint: '1199px',
      numVisible: 2,
      numScroll: 1,
    },
    {
      breakpoint: '991px',
      numVisible: 2,
      numScroll: 1,
    },
    {
      breakpoint: '767px',
      numVisible: 1,
      numScroll: 1,
    },
  ];
  const getAllEntities = () => {
    dispatch(getConvenios({}));
  };

  useEffect(() => {
    getAllEntities();
  }, []);

  const localTemplate = (local: Local) => {
    return (
      <div className="border-2 surface-border border-round m-2 text-center pt-5 ">
        <div className="mb-2 br4 ">
          <img src={`data:${local.imagenContentType};base64,${local.imagen}`} alt={local.nome} width={200} height={150} />
        </div>

        <div>
          <h5 className="mb-1">{local.nome}</h5>
          <h6 className="mt-0 mb-1">{local.descricao}</h6>
        </div>
      </div>
    );
  };

  return (
    <Card
      sx={{
        borderWidth: '1px',
        borderStyle: 'solid',
        boxShadow: 5,
        borderRadius: 3,
        borderColor: '#a1a1a1',
        ':hover': {
          boxShadow: 10,
          position: 'relative',
          borderColor: '#1e77c5',
          // backgroundColor:'#f5f5f5',
          cursor: 'pointer',
        },
      }}
    >
      <Carousel
        value={locaisList}
        numScroll={1}
        numVisible={2}
        responsiveOptions={responsiveOptions}
        itemTemplate={localTemplate}
        circular
      />
    </Card>
  );
}
