import React, { useState } from 'react';
import { GoogleMap, LoadScript, Marker } from '@react-google-maps/api';
import { MapWithShareButton } from 'app/localizacao/MapWithShareButton';

const MapContainer = () => {
  const [userLocation, setUserLocation] = useState({ lat: 0, lng: 0 });

  const handleShareLocationClick = () => {
    // Use a API de geolocalização do navegador para obter a localização do usuário
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(position => {
        const newUserLocation = {
          lat: position.coords.latitude,
          lng: position.coords.longitude,
        };
        setUserLocation(newUserLocation);
      });
    }
  };

  return (
    <div>
      <MapWithShareButton lat={userLocation.lat} lng={userLocation.lng} onShareLocationClick={handleShareLocationClick} />
    </div>
  );
};

export default MapContainer;
