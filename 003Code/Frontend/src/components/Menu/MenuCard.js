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
                  {menu.usePlanner ? (
                    ''
                  ) : (
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
=======
const MenuCard = (props) => {
  return (
    <Grid container spacing={4}>
      {props.menus?.map((menu, index) => {
        const isSale = menu.sold === true;
        const isTodayMenu = menu.usePlanner === true;

        const menucard_name_and_costs = [
          {
            condition: true,
            sx_opacity: isSale ? null : 0.3,
            sx_fontSize: '15px',
            text: menu.name,
            color: isTodayMenu ? 'primary.dark' : 'inherit',
          },
          {
            condition: isSale,
            sx_fontSize: '20px',
            text: menu.cost + '원',
          },
          {
            condition: !isSale,
            sx_fontSize: '20px',
            text: '품 절',
            color: 'error.dark',
          },
        ];

        const menucard_buttons = [
          {
            condition: isSale && !isTodayMenu,
            onClick: props.handleIngredientsOpen,
            text: '식재료',
            isAllMenu: true,
          },
          {
            condition: isSale && !isTodayMenu,
            onClick: props.handleUpdateOpen,
            text: '수정',
            isAllMenu: true,
          },
          {
            condition: isSale,
            onClick: props.soldout,
            text: '품절',
          },
          {
            condition: !isSale,
            onClick: props.resale,
            text: '재판매',
          },
          {
            condition: true,
            onClick: props.handleDeleteOpen,
            text: '삭제',
            color: 'error',
          },
        ];

        return (
          <Grid item key={index} xs={12} sm={6} md={4} lg={3}>
            <Card sx={cardStyle}>
              <CardMedia
                component="div"
                sx={{
                  ...cardMediaStyle,
                  opacity: isSale ? null : 0.3,
                }}
                image={`http://kjj.kjj.r-e.kr:8080/api/image?dir=${menu?.image}`}
              />

              <CardContent sx={cardContentStyle}>
                {menucard_name_and_costs.map(
                  (item, idx) =>
                    item.condition && (
                      <Typography
                        key={idx}
                        sx={{
                          ...weightFontStyle,
                          opacity: item.sx_opacity,
                          fontSize: item.sx_fontSize,
                        }}
                        color={item.color}
                      >
                        {item.text}
                      </Typography>
                    )
                )}
              </CardContent>

              <CardActions>
                <MenuButtonWrapper>
                  {menucard_buttons.map(
                    (item, idx) =>
                      item.condition && (
                        <Button
                          key={idx}
                          onClick={() => item.onClick(item.isAllMenu ? menu : menu.id)}
                          color={item.color}
                          size="small"
                        >
                          {item.text}
                        </Button>
                      )
                  )}
                </MenuButtonWrapper>
              </CardActions>
            </Card>
          </Grid>
        );
      })}
    </Grid>
  );
};

export default MenuCard;
