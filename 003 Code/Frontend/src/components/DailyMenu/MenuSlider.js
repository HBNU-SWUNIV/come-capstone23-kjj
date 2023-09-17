import styled from 'styled-components';
import img1 from '../../image/1.png';
import img2 from '../../image/2.png';
import img3 from '../../image/3.png';
import img4 from '../../image/4.png';
import img5 from '../../image/5.png';
import img6 from '../../image/6.png';
import img7 from '../../image/7.png';
import img8 from '../../image/8.png';
import img9 from '../../image/9.png';
import * as React from 'react';
import { useTheme } from '@mui/material/styles';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import SwipeableViews from 'react-swipeable-views';
import { autoPlay } from 'react-swipeable-views-utils';

const AutoPlaySwipeableViews = autoPlay(SwipeableViews);
const SliderImgArray = [
  {
    id: 0,
    src: img1,
  },
  {
    id: 1,
    src: img2,
  },
  {
    id: 2,
    src: img3,
  },
  {
    id: 3,
    src: img4,
  },
  {
    id: 4,
    src: img5,
  },
  {
    id: 5,
    src: img6,
  },
  {
    id: 6,
    src: img7,
  },
  {
    id: 7,
    src: img8,
  },
  {
    id: 8,
    src: img9,
  },
];

const imgStyle = {
  height: 300,
  display: 'block',
  maxWidth: 500,
  overflow: 'hidden',
  width: '100%',
  borderRadius: '30px',
};

const MenuSlider = () => {
  const theme = useTheme();
  const [activeStep, setActiveStep] = React.useState(0);

  const handleStepChange = (step) => {
    setActiveStep(step);
  };

  return (
    <Box sx={{ maxWidth: 400, flexGrow: 1 }}>
      <Paper
        square
        elevation={0}
        sx={{
          display: 'flex',
          alignItems: 'center',
          height: 50,
          pl: 2,
          bgcolor: 'background.default',
        }}
      />
      <AutoPlaySwipeableViews
        axis={theme.direction === 'rtl' ? 'x-reverse' : 'x'}
        onChangeIndex={handleStepChange}
        enableMouseEvents
      >
        {SliderImgArray.map((img, index) => (
          <div key={img.id}>
            {Math.abs(activeStep - index) <= 2 ? (
              <Box component="img" sx={imgStyle} src={img.src} alt={'이미지 없음'} />
            ) : null}
          </div>
        ))}
      </AutoPlaySwipeableViews>
    </Box>
  );
};

export default MenuSlider;
