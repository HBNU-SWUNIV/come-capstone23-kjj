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
import { flexICenter } from '../../styles/global.style';

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
                          fontWeight: 600,
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
                <MenuButtonBox>
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
                </MenuButtonBox>
              </CardActions>
            </Card>
          </Grid>
        );
      })}
    </Grid>
  );
};

export default MenuCard;

const MenuButtonBox = styled.div`
  width: 100%;
  ${flexICenter};
  justify-content: space-evenly;

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
