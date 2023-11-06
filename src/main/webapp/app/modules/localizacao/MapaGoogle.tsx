import React from 'react';
import './mapa.scss';
import { Loader } from '@googlemaps/js-api-loader';
import BotaoVoltar from 'app/components/botaoVoltar';
import { WhatsApp } from '@mui/icons-material';
import Button from '@mui/material/Button';
import Breadcrunbs from 'app/components/breadcrunbs';

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

  loader.load().then(async () => {
    const { Map } = (await google.maps.importLibrary('maps')) as google.maps.MapsLibrary;
    const { AdvancedMarkerElement, PinElement } = (await google.maps.importLibrary('marker')) as google.maps.MarkerLibrary;

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

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between' }}>
        <Breadcrunbs />
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
      <div style={{ display: 'flex', justifyContent: 'center' }}>
        <div id="map" className={'mapa'}></div>
        {/*<iframe src="https://www.google.com/maps/embed?pb=!1m14!1m8!1m3!1d13854.233611992202!2d-52.43024!3d-29.761491!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x951ca3eb61944605%3A0x3d9978ee7389b7b!2sAssociac%C3%A3o%20Atl%C3%A9tica%20Philip%20Morris%20-%20AAPM!5e0!3m2!1spt-BR!2sus!4v1697936078352!5m2!1spt-BR!2sus"*/}
        {/*        width="900vh" height="600vh" loading="lazy" referrerPolicy="no-referrer-when-downgrade">*/}
        {/*</iframe>*/}
      </div>
    </div>
  );
}

export default React.memo(MapaGoogle);