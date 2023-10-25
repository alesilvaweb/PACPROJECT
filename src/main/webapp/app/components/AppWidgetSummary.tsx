import PropTypes from 'prop-types';
import React from 'react';
import Box from '@mui/material/Box';
import Card from '@mui/material/Card';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';

import { useNavigate } from 'react-router-dom';

// ----------------------------------------------------------------------

export default function AppWidgetSummary({ link, title, total, icon, color = 'primary', sx, ...other }) {
  const navigate = useNavigate();
  return (
    <Card
      onClick={() => navigate(link)}
      component={Stack}
      spacing={3}
      direction="row"
      sx={{
        px: 3,
        py: 5,
        borderRadius: 3,
        boxShadow: 5,
        borderWidth: '1px',
        borderStyle: 'solid',
        borderColor: '#c6c6c6',
        ':hover': {
          boxShadow: 10,
          position: 'relative',
          backgroundColor: '#f5f5f5',
          borderColor: '#1e77c5',
          cursor: 'pointer',
        },
        ...sx,
      }}
      {...other}
    >
      {icon && <Box sx={{ width: 64, height: 64 }}>{icon}</Box>}

      <Stack spacing={0.5}>
        {/*<Typography variant="h4">{fShortenNumber(total)}</Typography>*/}

        <h5>{title}</h5>

        <hr />
      </Stack>
    </Card>
  );
}

AppWidgetSummary.propTypes = {
  color: PropTypes.string,
  icon: PropTypes.oneOfType([PropTypes.element, PropTypes.string]),
  sx: PropTypes.object,
  title: PropTypes.string,
  total: PropTypes.number,
};
