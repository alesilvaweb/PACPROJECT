import * as React from 'react';
import { useEffect, useState } from 'react';
import Button from '@mui/material/Button';
import { styled } from '@mui/material/styles';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogActions from '@mui/material/DialogActions';
import IconButton from '@mui/material/IconButton';
import CloseIcon from '@mui/icons-material/Close';
import Typography from '@mui/material/Typography';
import { Card, CardContent, CardMedia, useMediaQuery, useTheme } from '@mui/material';
import axios from 'axios';
import { dataAtual } from 'app/shared/util/date-utils';

const BootstrapDialog = styled(Dialog)(({ theme }) => ({
  '& .MuiDialogContent-root': {
    padding: theme.spacing(2),
  },
  '& .MuiDialogActions-root': {
    padding: theme.spacing(1),
  },
}));

export default function ModalInicial({ mensagens }) {
  const [open, setOpen] = useState(false);

  const handleClose = () => {
    sessionStorage.setItem('popUpShow', 'true');
    setOpen(false);
  };

  console.log({ mensagens });

  useEffect(() => {
    const hasPopUpBeenShown = sessionStorage.getItem('popUpShow');

    if (!hasPopUpBeenShown) {
      setOpen(true);
    }
  }, []);

  const theme = useTheme();
  const fullScreen = useMediaQuery(theme.breakpoints.down('xs'));
  return (
    <div>
      <BootstrapDialog fullScreen={fullScreen} onClose={handleClose} aria-labelledby="customized-dialog-title" open={open}>
        <DialogTitle sx={{ m: 0, p: 2 }} id="customized-dialog-title">
          Novidades
        </DialogTitle>
        <IconButton
          aria-label="close"
          onClick={handleClose}
          sx={{
            position: 'absolute',
            right: 8,
            top: 8,
            color: theme => theme.palette.grey[500],
          }}
        >
          <CloseIcon />
        </IconButton>
        <DialogContent dividers>
          {mensagens ? (
            <div>
              {mensagens.map(message => (
                <>
                  <Card>
                    <CardMedia component="img" image={`data:${message.imagenContentType};base64,${message.imagen}`} />
                    <CardContent>
                      <Typography variant="h6" component="div">
                        {message.titulo}
                      </Typography>
                      <Typography color="textSecondary">{message.descricao}</Typography>
                    </CardContent>
                  </Card>
                  <hr />
                </>
              ))}
            </div>
          ) : null}
        </DialogContent>

        <DialogActions>
          <Button autoFocus color={'primary'} onClick={handleClose}>
            Fechar
          </Button>
        </DialogActions>
      </BootstrapDialog>
    </div>
  );
}
