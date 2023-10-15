import React from 'react';
import { GoogleMap, Marker, useJsApiLoader } from '@react-google-maps/api';
import './mapa.scss';
import BotaoVoltar from 'app/components/botaoVoltar';
import { Card } from '@mui/material';

const containerStyle = {
  width: '50vh',
  height: '80vh',
};

const center = {
  lat: -29.761491,
  lng: -52.43024,
};

function Mapa() {
  const { isLoaded } = useJsApiLoader({
    id: 'google-map-script',
    googleMapsApiKey: 'AIzaSyChx4KBgoZYiZohZ7V6Fa96oVNZG-LfZ58',
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

  return isLoaded ? (
    <div>
      <BotaoVoltar link={'/agenda'} top={'-43px'} />
      <div className={'mapa'}>
        <Card>
          <GoogleMap
            mapContainerStyle={containerStyle}
            center={center}
            zoom={15}
            // onLoad={onLoad}
            // onUnmount={onUnmount}
          >
            {/* Child components, such as markers, info windows, etc. */}
            <Marker
              position={center}
              options={{
                label: {
                  text: 'AAPM',
                  className: 'map-marker',
                },
              }}
            />
          </GoogleMap>
        </Card>
      </div>
    </div>
  ) : (
    <></>
  );
}

export default React.memo(Mapa);
