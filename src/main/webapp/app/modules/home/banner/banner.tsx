import React, { useEffect } from 'react';
// Import Swiper React components
import { Swiper, SwiperSlide } from 'swiper/react';

// Import Swiper styles
import 'swiper/css';
import 'swiper/css/pagination';
import 'swiper/css/navigation';
import './banner.scss';

// import required modules
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { useNavigate } from 'react-router-dom';
import { getEntities } from 'app/entities/convenio/convenio.reducer';
import { Autoplay, Navigation, Pagination } from 'swiper/modules';
import { Card, Skeleton, Typography } from '@mui/material';

export default function Banner() {
  const convenioList = useAppSelector(state => state.convenio.entities);
  const loading = useAppSelector(state => state.convenio.loading);
  const navigate = useNavigate();
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const cardStyle = {
    padding: 1,
    backgroundColor: 'gray-100',
    borderRadius: 2,
    borderWidth: '1px',
    borderStyle: 'solid',
    borderColor: '#a1a1a1',
    ':hover': {
      boxShadow: 5,
      borderColor: '#1975d1',
    },
  };
  // @ts-ignore
  return (
    <div>
      <Typography variant={'h5'}>Convênios</Typography>
      <Card sx={cardStyle}>
        <Swiper
          onAutoplay={true}
          autoplay={{ delay: 3000 }}
          navigation={true}
          breakpoints={{
            200: {
              slidesPerView: 1,
              spaceBetween: 20,
            },
            750: {
              slidesPerView: 2,
              spaceBetween: 20,
            },
            950: {
              slidesPerView: 3,
              spaceBetween: 30,
            },
            1280: {
              slidesPerView: 4,
              spaceBetween: 30,
            },
            1600: {
              slidesPerView: 5,
              spaceBetween: 30,
            },
          }}
          modules={[Autoplay, Pagination, Navigation]}
        >
          <>
            {!loading ? (
              <>
                {convenioList.map(convenio => (
                  <SwiperSlide key={convenio.id} onClick={() => navigate(`/convenio/${convenio.id}`)} className={'hand'}>
                    <img
                      src={`data:${convenio.imagenContentType};base64,${convenio.imagem}`}
                      alt={convenio.nome}
                      width={100}
                      height={150}
                    />
                  </SwiperSlide>
                ))}
              </>
            ) : (
              <>
                <SwiperSlide key={1}>
                  <Skeleton variant="rectangular" width={'100%'} height={'100%'} sx={{ borderRadius: '4px' }} />
                </SwiperSlide>
                <SwiperSlide key={2}>
                  <Skeleton variant="rectangular" width={'100%'} height={'100%'} sx={{ borderRadius: '4px' }} />
                </SwiperSlide>
                <SwiperSlide key={3}>
                  <Skeleton variant="rectangular" width={'100%'} height={'100%'} sx={{ borderRadius: '4px' }} />
                </SwiperSlide>
                <SwiperSlide key={4}>
                  <Skeleton variant="rectangular" width={'100%'} height={'100%'} sx={{ borderRadius: '4px' }} />
                </SwiperSlide>
                <SwiperSlide key={4}>
                  <Skeleton variant="rectangular" width={'100%'} height={'100%'} sx={{ borderRadius: '4px' }} />
                </SwiperSlide>
              </>
            )}
          </>
        </Swiper>
      </Card>
    </div>
  );
}
