import * as React from 'react';
import Button from '@mui/material/Button';
import { styled } from '@mui/material/styles';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogActions from '@mui/material/DialogActions';
import IconButton from '@mui/material/IconButton';
import CloseIcon from '@mui/icons-material/Close';
import Typography from '@mui/material/Typography';
import { Card, CardActionArea, useMediaQuery, useTheme } from '@mui/material';
import CardContent from '@mui/material/CardContent';
import { useNavigate } from 'react-router-dom';

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
        {/*<DialogTitle sx={{ m: 0, p: 2 }} id="customized-dialog-title">*/}

        {/*</DialogTitle>*/}
        {/*<IconButton*/}
        {/*  aria-label="close"*/}
        {/*  onClick={handleClose}*/}
        {/*  sx={{*/}
        {/*    position: 'absolute',*/}
        {/*    right: 8,*/}
        {/*    top: 8,*/}
        {/*    color: (theme) => theme.palette.grey[500],*/}
        {/*  }}*/}
        {/*>*/}
        {/*  <CloseIcon />*/}
        {/*</IconButton>*/}
        <DialogContent dividers>
          <Card
            sx={{
              width: '100%',
              height: '100%',
            }}
          >
            <CardActionArea>
              <CardContent>
                <Typography gutterBottom variant="h5" component="div">
                  Cartão do associado
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Lizards are a widespread group of squamate reptiles, with over 6,000 species, ranging across all continents except
                  Antarctica
                </Typography>
              </CardContent>
            </CardActionArea>
          </Card>
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
