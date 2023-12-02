import React from 'react';
import './mapa.scss';
import { Loader } from '@googlemaps/js-api-loader';
import BotaoVoltar from 'app/components/botaoVoltar';
import { Map, WhatsApp } from '@mui/icons-material';
import Button from '@mui/material/Button';
import Breadcrunbs from 'app/components/breadcrunbs';
import { Breadcrumb, BreadcrumbItem } from 'reactstrap';
import { useNavigate } from 'react-router-dom';

const containerStyle = {
  width: '100%',
  height: '80vh',
};

const center = {
  lat: -29.761491,
  lng: -52.43024,
};

function MapaGoogle() {
  // const { isLoaded } = useJsApiLoader({
  //   id: 'google-map-script',
  //   googleMapsApiKey: 'AIzaSyChx4KBgoZYiZohZ7V6Fa96oVNZG-LfZ58',
  // });
  const loader = new Loader({
    version: 'weekly',
    apiKey: 'AIzaSyChx4KBgoZYiZohZ7V6Fa96oVNZG-LfZ58',
  });

  const [map, setMap] = React.useState(null);

  const onLoad = React.useCallback(function callback(map) {
    // This is just an example of getting and using the map instance!!! don't just blindly copy!
    const bounds = new window.google.maps.LatLngBounds(center);
    map.fitBounds(bounds);

    setMap(map);
  }, []);

  loader.loadPromise().then(async () => {
    const { Map } = (await google.maps.importLibrary('maps')) as google.maps.MapsLibrary;

    const map = new Map(document.getElementById('map') as HTMLElement, {
      center: { lat: -29.761491, lng: -52.43024 },
      zoom: 15,
      fullscreenControl: true,
    });
    // const marker = new AdvancedMarkerElement({
    //   map,
    //   position: { lat: -29.761491, lng: -52.43024 },
    // });
  });
  const navigate = useNavigate();
  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', flexWrap: 'wrap' }}>
        <Breadcrumb>
          <BreadcrumbItem onClick={() => navigate('/')}>
            <a>Início</a>
          </BreadcrumbItem>
          <BreadcrumbItem onClick={() => navigate('/cabanas')}>
            <a>Cabanas</a>
          </BreadcrumbItem>
          <BreadcrumbItem active>{'Localização'}</BreadcrumbItem>
        </Breadcrumb>
        <div>
          <Button
            onClick={() => {
              const { lat, lng } = center;
              const whatsappMessage = `https://wa.me/?text=Localização:%20https://maps.google.com/?q=${lat},${lng}`;
              window.open(whatsappMessage, '_blank');
            }}
            startIcon={<WhatsApp />}
            color={'success'}
          >
            Compartilhar
          </Button>
          &nbsp;
          <Button
            onClick={() => {
              const { lat, lng } = center;
              const maps = `https://www.google.com/maps/place/Associac%C3%A3o+Atl%C3%A9tica+Philip+Morris+-+AAPM/@-29.7616726,-52.4307327,18z/data=!4m6!3m5!1s0x951ca3eb61944605:0x3d9978ee7389b7b!8m2!3d-29.7615903!4d-52.430465!16s%2Fg%2F11c32bznx8?authuser=0&entry=ttu`;
              window.open(maps, '_blank');
            }}
            startIcon={<Map />}
            color={'primary'}
          >
            Abrir no Maps
          </Button>
        </div>
      </div>
      <div style={{ display: 'flex', justifyContent: 'center' }}>
        <iframe
          src="https://www.google.com/maps/embed?pb=!1m14!1m8!1m3!1d13854.233611992202!2d-52.43024!3d-29.761491!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x951ca3eb61944605%3A0x3d9978ee7389b7b!2sAssociac%C3%A3o%20Atl%C3%A9tica%20Philip%20Morris%20-%20AAPM!5e0!3m2!1spt-BR!2sus!4v1697936078352!5m2!1spt-BR!2sus"
          width="80%"
          height="500vh"
          loading="lazy"
          referrerPolicy="no-referrer-when-downgrade"
        ></iframe>
      </div>
    </div>
  );
}

export default MapaGoogle;
