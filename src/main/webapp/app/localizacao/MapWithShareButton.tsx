import React, { useState } from 'react';
import { GoogleMap, LoadScript, Marker } from '@react-google-maps/api';

export const MapWithShareButton = ({ lat, lng, onShareLocationClick }) => {
  return (
    <div>
      <LoadScript googleMapsApiKey="AIzaSyChx4KBgoZYiZohZ7V6Fa96oVNZG-LfZ58">
        <GoogleMap mapContainerStyle={{ width: '100%', height: '400px' }} center={{ lat, lng }} zoom={5}>
          <Marker position={{ lat, lng }} />
        </GoogleMap>
      </LoadScript>
      <button onClick={onShareLocationClick}>Compartilhar Localização</button>
    </div>
  );
};
