import React from 'react';
import { GoogleMap, Marker, useJsApiLoader } from '@react-google-maps/api';
import './mapa.scss';
import { Loader } from '@googlemaps/js-api-loader';
import BotaoVoltar from 'app/components/botaoVoltar';
import { Card } from '@mui/material';
import { WhatsApp } from '@mui/icons-material';
import Button from '@mui/material/Button';

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
    apiKey: 'AIzaSyChx4KBgoZYiZohZ7V6Fa96oVNZG-LfZ58',
    version: 'weekly',
  });

  const [map, setMap] = React.useState(null);

  const onLoad = React.useCallback(function callback(map) {
    // This is just an example of getting and using the map instance!!! don't just blindly copy!
    const bounds = new window.google.maps.LatLngBounds(center);
    map.fitBounds(bounds);

    setMap(map);
  }, []);

  const onUnmount = React.useCallback(function callback(map) {
    setMap(null);
  }, []);

  loader.load().then(async () => {
    const { Map } = (await google.maps.importLibrary('maps')) as google.maps.MapsLibrary;
    const map = new Map(document.getElementById('map') as HTMLElement, {
      center: { lat: this.lat, lng: this.lng },
      zoom: 8,
    });
  });

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between' }}>
        <BotaoVoltar link={'/agenda'} label={'Voltar'} />
        <Button
          onClick={() => {
            const { lat, lng } = center;
            const whatsappMessage = `https://wa.me/?text=Localização:%20https://maps.google.com/?q=${lat},${lng}`;
            window.open(whatsappMessage, '_blank');
          }}
          startIcon={<WhatsApp />}
          color={'success'}
        >
          Compartilhar no WhatsApp
        </Button>
      </div>
      <div id={'map'} className={'mapa'}></div>
    </div>
  );
}

export default React.memo(MapaGoogle);
