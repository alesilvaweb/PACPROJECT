import React, { useState, useEffect } from 'react';
import 'primereact/resources/themes/lara-light-indigo/theme.css';
import { Carousel, CarouselResponsiveOption } from 'primereact/carousel';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { useNavigate } from 'react-router-dom';
import { getEntities } from 'app/entities/convenio/convenio.reducer';
import Card from '@mui/material/Card';

interface Convenio {
  imagenContentType: string | undefined;
  id: string;
  nome: string;
  titulo: string;
  imagem: string | undefined;
}

export default function BannerCarousel() {
  const convenioList = useAppSelector(state => state.convenio.entities);
  const loading = useAppSelector(state => state.convenio.loading);
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

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const convenioTemplate = (convenio: Convenio) => {
    return (
      <div className="border-1 surface-border border-round m-2 text-center py-5 px-3">
        <div className="mb-3">
          <img src={`data:${convenio.imagenContentType};base64,${convenio.imagem}`} alt={convenio.nome} width={300} height={250} />
        </div>
        <div>
          <h5 className="mb-1">{convenio.nome}</h5>
          <h6 className="mt-0 mb-1">{convenio.titulo}</h6>
        </div>
      </div>
    );
  };

  // @ts-ignore
  return (
    <div
      className="card"
      style={{
        // @ts-ignore
        boxShadow: 10,
        borderWidth: '1px',
        borderStyle: 'solid',
        borderRadius: 3,
        borderColor: '#a1a1a1',
        // @ts-ignore
        ':hover': {
          boxShadow: 10,
          position: 'relative',
          borderColor: '#1e77c5',
          cursor: 'pointer',
        },
      }}
    >
      <Carousel
        value={convenioList}
        numScroll={1}
        numVisible={2}
        responsiveOptions={responsiveOptions}
        itemTemplate={convenioTemplate}
        circular
      />
    </div>
  );
}
