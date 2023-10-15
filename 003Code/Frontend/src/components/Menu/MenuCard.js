import {
  Button,
  Card,
  CardActions,
  CardContent,
  CardMedia,
  Grid,
  Typography,
} from '@mui/material';
import React from 'react';
import styled from 'styled-components';

const MenuButtonWrapper = styled.div`
  display: flex;
  justify-content: space-evenly;
  align-items: center;
  width: 100%;
  button {
    width: 20%;
    font-size: 13px;
    font-weight: 600;
    white-space: nowrap;
  }
`;

const cardStyle = {
  width: '260px',
  height: '100%',
  display: 'flex',
  flexDirection: 'column',
};

const cardMediaStyle = {
  width: '100%',
  height: '260px',
};

const cardContentStyle = {
  display: 'flex',
  justifyContent: 'space-between',
  alignItems: 'center',
};

const weightFontStyle = {
  fontWeight: 600,
};

const MenuCard = ({
  menus,
  handleIngredientsOpen,
  handleUpdateOpen,
  soldout,
  handleDeleteOpen,
  resale,
}) => {
  return (
    <Grid container spacing={4}>
      {menus?.map((menu, index) => (
        <Grid item key={index} xs={12} sm={6} md={4} lg={3}>
          <Card sx={cardStyle}>
            <CardMedia
              component="div"
              sx={{
                ...cardMediaStyle,
                opacity: menu.sold === true ? null : 0.3,
              }}
              image={`http://kjj.kjj.r-e.kr:8080/api/image?dir=${menu?.image}`}
            />

            <CardContent sx={cardContentStyle}>
              <Typography
                sx={{
                  opacity: menu.sold === true ? null : 0.3,
                  ...weightFontStyle,
                  fontSize: '15px',
                }}
                color={menu.usePlanner === true ? 'primary.dark' : 'inherit'}
                gutterBottom
                variant="h5"
                component="h2"
              >
                {menu.name}
              </Typography>
              {menu.sold === true ? (
                <>
                  <Typography
                    sx={{
                      ...weightFontStyle,
                      fontSize: '20px',
                    }}
                  >
                    {menu.cost + '원'}
                  </Typography>
                </>
              ) : (
                <>
                  <Typography
                    sx={{
                      ...weightFontStyle,
                      fontSize: '20px',
                    }}
                    variant="h4"
                    color="error.dark"
                  >
                    품 절
                  </Typography>
                </>
              )}
            </CardContent>

            <CardActions>
              {menu.sold === true ? (
                <MenuButtonWrapper>
                  {menu.usePlanner === false && (
                    <>
                      <Button onClick={() => handleIngredientsOpen(menu)} size="small">
                        식재료
                      </Button>
                      <Button onClick={() => handleUpdateOpen(menu)} size="small">
                        수정
                      </Button>
                    </>
                  )}

                  <Button onClick={() => soldout(menu.id)} size="small">
                    품절
                  </Button>

                  <Button
                    onClick={() => handleDeleteOpen(menu.id)}
                    color="error"
                    size="small"
                  >
                    삭제
                  </Button>
                </MenuButtonWrapper>
              ) : (
                <MenuButtonWrapper>
                  <Button onClick={() => resale(menu.id)} size="small">
                    재판매
                  </Button>
                  <Button
                    onClick={() => handleDeleteOpen(menu.id)}
                    color="error"
                    size="small"
                  >
                    삭제
                  </Button>
                </MenuButtonWrapper>
              )}
            </CardActions>
          </Card>
        </Grid>
      ))}
    </Grid>
  );
};

export default MenuCard;
