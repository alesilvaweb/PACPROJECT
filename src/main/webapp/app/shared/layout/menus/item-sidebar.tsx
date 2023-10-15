import * as React from 'react';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import { useNavigate } from 'react-router-dom';

export const ItemSidebar = ({ title, icon, link, setIsOpen }) => {
  const navigate = useNavigate();
  return (
    <ListItemButton
      sx={{ pl: 4 }}
      onClick={() => {
        navigate(link);
        setIsOpen(false);
      }}
    >
      <ListItemIcon>{icon}</ListItemIcon>
      <ListItemText primary={title} />
    </ListItemButton>
  );
};
