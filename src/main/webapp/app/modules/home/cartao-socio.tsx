import * as React from 'react';
import Button from '@mui/material/Button';
import { styled } from '@mui/material/styles';
import Dialog from '@mui/material/Dialog';
import DialogContent from '@mui/material/DialogContent';
import DialogActions from '@mui/material/DialogActions';
import { Card, CardActionArea, useMediaQuery, useTheme } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import AssociadoCard from 'app/modules/CartaoAssociado/AssociadoCard';

const BootstrapDialog = styled(Dialog)(({ theme }) => ({
  '& .MuiDialogContent-root': {
    padding: theme.spacing(2),
  },
  '& .MuiDialogActions-root': {
    padding: theme.spacing(1),
  },
}));

export default function CustomizedDialogs() {
  const [open, setOpen] = React.useState(true);
  const navigate = useNavigate();
  const handleClickOpen = () => {
    setOpen(true);
  };
  const handleClose = () => {
    setOpen(false);
    navigate('/');
  };
  const theme = useTheme();
  const fullScreen = useMediaQuery(theme.breakpoints.down('xs'));
  return (
    <div>
      <BootstrapDialog fullScreen={true} onClose={handleClose} aria-labelledby="customized-dialog-title" open={open}>
        <DialogContent dividers>
          <AssociadoCard name={'Associado'} memberSince={'2013'} imageUrl={'content/images/CartaoAssociado.png'} />
        </DialogContent>
        <DialogActions>
          <Button
            onClick={() => {
              window.print();
            }}
          >
            Imprimir
          </Button>
          <Button
            autoFocus
            onClick={() => {
              window.location.replace('https://api.whatsapp.com/send/?text=CartaodoassociadoAAPM&type=custom_url&app_absent=0');
            }}
          >
            compartilhar
          </Button>
          <Button autoFocus onClick={handleClose}>
            fechar
          </Button>
        </DialogActions>
      </BootstrapDialog>
    </div>
  );
}
