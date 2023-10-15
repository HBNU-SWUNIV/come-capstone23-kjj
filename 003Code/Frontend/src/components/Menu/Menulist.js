import Box from '@mui/material/Box';
import Collapse from '@mui/material/Collapse';
import IconButton from '@mui/material/IconButton';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Typography from '@mui/material/Typography';
import Paper from '@mui/material/Paper';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp';
import MoreVertIcon from '@mui/icons-material/MoreVert';
import { styled } from 'styled-components';
import List from '@mui/material/List';
import Chip from '@mui/material/Chip';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import RefreshIcon from '@mui/icons-material/Refresh';
import NoMealsIcon from '@mui/icons-material/NoMeals';
import axios from 'axios';
import Menu from '@mui/material/Menu';
import { ConfigWithToken, ManagerBaseApi } from '../../auth/authConfig';
import { useState } from 'react';
import Menu_ListItemButton from './list/Menu_ListItemButton';

const fontWeight500 = { fontWeight: 500 };
const fontWeight600 = { fontWeight: 600 };
const ingredientTableTitleStyle = {
  fontWeight: 600,
  fontSize: '16px',
};

const menuListStyle = {
  width: '170px',
  maxWidth: '180px',
  height: '150px',
  whiteSpace: 'nowrap',
  display: 'flex',
  flexDirection: 'column',
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

const MenuEdit = styled.div`
  position: relative;

  &:hover {
    cursor: pointer;
  }
`;

const Sale = styled.div`
  margin-right: -5px;
`;

const table_header_data = [
  {
    id: 0,
  },
  {
    id: 1,
    text: '이름',
  },
  {
    id: 2,
    text: '정보',
    align: 'left',
  },
  {
    id: 3,
    text: '가격',
    align: 'right',
  },
  {
    id: 4,
    text: '상태',
    align: 'right',
  },
  {
    id: 5,
  },
];

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

const Row = (props) => {
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

  return (
    <>
      <TableRow sx={{ '& > *': { borderBottom: 'unset' } }}>
        <TableCell>
          <IconButton
            aria-label="expand row"
            size="small"
            onClick={() => handlerOpen(open)}
          >
            {open ? <KeyboardArrowUpIcon /> : <KeyboardArrowDownIcon />}
          </IconButton>
        </TableCell>

        <TableCell
          component="th"
          scope="row"
          sx={{ ...fontWeight600, color: row.usePlanner ? '#1565c0' : 'inherit' }}
        >
          {row.name}
        </TableCell>

        <TableCell sx={fontWeight500} align="left">
          {row.details}
        </TableCell>

        <TableCell sx={fontWeight600} align="right">
          {row.cost}
        </TableCell>

        <TableCell align="right">
          {row.sold === true ? (
            <Sale>
              <Chip label="판매중" color="success" />
            </Sale>
          ) : (
            <Chip label="품 절" color="error" />
          )}
        </TableCell>

        <TableCell align="right">
          <MenuEdit onClick={onOpenEdit}>
            <MoreVertIcon />
          </MenuEdit>

          <Menu anchorEl={anchorEl} open={openEdit} onClose={onOffEdit}>
            <List
              sx={menuListStyle}
              component="nav"
              aria-labelledby="nested-list-subheader"
            >
              {menu_list_Items_data.map(
                (item) =>
                  item.condition && (
                    <Menu_ListItemButton
                      key={item.id}
                      isAllData={item.isAllData}
                      onClick={item.onClick}
                      icon={item.icon}
                      text={item.text}
                      data={item.data}
                    />
                  )
              )}
            </List>
          </Menu>
        </TableCell>
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

export default function Menulist({ menus, onDelete, soldout, resale, onUpdate }) {
  return (
    <TableContainer component={Paper}>
      <Table aria-label="collapsible table">
        <TableHead>
          <TableRow>
            {table_header_data.map((item) => (
              <TableCell align={item.align}>{item.text}</TableCell>
            ))}
          </TableRow>
        </TableHead>

        <TableBody>
          {menus?.map((row) => (
            <Row
              key={row.name}
              row={row}
              onDelete={onDelete}
              soldout={soldout}
              resale={resale}
              onUpdate={onUpdate}
            />
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}
