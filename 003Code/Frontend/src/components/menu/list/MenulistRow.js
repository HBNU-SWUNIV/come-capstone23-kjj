import Box from '@mui/material/Box';
import Collapse from '@mui/material/Collapse';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Typography from '@mui/material/Typography';
import { styled } from 'styled-components';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import RefreshIcon from '@mui/icons-material/Refresh';
import NoMealsIcon from '@mui/icons-material/NoMeals';
import axios from 'axios';
import { ConfigWithToken, ManagerBaseApi } from '../../../auth/authConfig';
import { useState } from 'react';
import MenulistUpDownArrow from './MenulistUpDownArrow';
import MenulistSaleStatus from './MenulistSaleStatus';
import MenulistMenusbar from './MenulistMenusbar';

const ingredientTableTitleStyle = {
  fontWeight: 600,
  fontSize: '16px',
};

const TableName = styled.div`
  display: flex;
  width: 100%;
  justify-content: space-between;
  span {
    color: rgb(0, 171, 85);
    font-size: 30px;
    &:hover {
      cursor: pointer;
    }
  }
`;

const table_head_data = [
  {
    id: 0,
    text: '이름',
    sx: ingredientTableTitleStyle,
  },
  {
    id: 1,
  },
  {
    id: 2,
  },
  {
    id: 3,
    text: '무게 (1인분 기준)',
    sx: ingredientTableTitleStyle,
    align: 'right',
  },
];

const table_body_data = [
  {
    id: 0,
    component: 'th',
    scope: 'row',
  },
  {
    id: 1,
  },
  {
    id: 2,
  },
  {
    id: 3,
    align: 'right',
  },
];
const MenulistRow = (props) => {
  const { row, onDelete, soldout, resale, onUpdate } = props;
  const config = ConfigWithToken();
  const [anchorEl, setAnchorEl] = useState(null);
  const openEdit = Boolean(anchorEl);
  const [open, setOpen] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [ingredients, setIngredients] = useState([]);
  const ingredientsArray = !isLoading && Object.entries(ingredients?.food);
  const onOpenEdit = (event) => {
    setAnchorEl(event.currentTarget);
  };
  const onOffEdit = () => {
    setAnchorEl(null);
  };
  const handleDelete = (id) => {
    onOffEdit();
    onDelete(id);
  };
  const handleUpdate = (menu) => {
    onOffEdit();
    onUpdate(menu);
  };
  const handleSoldout = (id) => {
    onOffEdit();
    soldout(id);
  };
  const handleResale = (id) => {
    onOffEdit();
    resale(id);
  };
  const getIngredients = (id) => {
    axios
      .get(`${ManagerBaseApi}/menu/food/${id}`, config)
      .then((res) => setIngredients(res.data))
      .then(() => setIsLoading(false))
      .catch((err) => console.error(err));
  };
  const handlerOpen = (open) => {
    setOpen(!open);
    getIngredients(row?.foodId);
  };

  const menu_list_Items_data = [
    {
      condition: row.usePlanner === false,
      id: 0,
      onClick: handleUpdate,
      icon: <EditIcon />,
      text: '수정',
      isAllData: true,
      data: row,
    },
    {
      condition: row.sold === true,
      id: 1,
      onClick: handleSoldout,
      icon: <NoMealsIcon />,
      text: '품절',
      data: row,
    },
    {
      condition: row.sold === false,
      id: 2,
      onClick: handleResale,
      icon: <RefreshIcon />,
      text: '재판매',
      data: row,
    },
    {
      condition: true,
      id: 3,
      onClick: handleDelete,
      icon: <DeleteIcon />,
      text: '삭제',
      data: row,
    },
  ];

  const menu_table_row_cell_datas = [
    {
      text: <MenulistUpDownArrow open={open} onClick={handlerOpen} />,
    },
    {
      sx_fontWeight: 600,
      sx_color: row.usePlanner ? '#1565c0' : 'inherit',
      text: row.name,
    },
    {
      sx_fontWeight: 500,
      text: row.details,
      align: 'left',
    },
    {
      sx_fontWeight: 600,
      text: row.cost,
      align: 'right',
    },
    {
      text: <MenulistSaleStatus condition={row.sold === true} />,
      align: 'right',
    },
    {
      text: (
        <MenulistMenusbar
          onClick={onOpenEdit}
          anchorEl={anchorEl}
          open={openEdit}
          onClose={onOffEdit}
          listData={menu_list_Items_data}
        />
      ),
      align: 'right',
    },
  ];

  return (
    <>
      <TableRow sx={{ '& > *': { borderBottom: 'unset' } }}>
        {menu_table_row_cell_datas.map((item, idx) => (
          <TableCell
            key={idx}
            sx={{ fontWeight: item.sx_fontWeight, color: item.sx_color }}
            align={item.align}
          >
            {item.text}
          </TableCell>
        ))}
      </TableRow>

      <TableRow>
        <TableCell style={{ paddingBottom: 0, paddingTop: 0 }} colSpan={6}>
          <Collapse in={open} timeout="auto" unmountOnExit>
            <Box sx={{ margin: 1 }}>
              <TableName>
                <Typography variant="h6" gutterBottom component="div">
                  식재료 - {ingredients.name}
                </Typography>
              </TableName>

              <Table size="small" aria-label="purchases">
                <TableHead>
                  <TableRow>
                    {table_head_data.map((item) => (
                      <TableCell key={item.id} sx={item.sx} align={item.align}>
                        {item.text}
                      </TableCell>
                    ))}
                  </TableRow>
                </TableHead>

                <TableBody>
                  {Array.isArray(ingredientsArray) &&
                    ingredientsArray?.map((a, idx) => (
                      <TableRow key={idx}>
                        {table_body_data.map((item) => (
                          <TableCell
                            component={item.component}
                            scope={item.scope}
                            align={item.align}
                          >
                            {item.id == 0 ? a[0] : item.id == 3 ? `${a[1]}g` : null}
                          </TableCell>
                        ))}
                      </TableRow>
                    ))}
                </TableBody>
              </Table>
            </Box>
          </Collapse>
        </TableCell>
      </TableRow>
    </>
  );
};

export default MenulistRow;
