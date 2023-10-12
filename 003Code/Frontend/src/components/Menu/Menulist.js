import * as React from 'react';
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
import AddCircleIcon from '@mui/icons-material/AddCircle';
import { styled } from 'styled-components';
import List from '@mui/material/List';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import Chip from '@mui/material/Chip';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import RefreshIcon from '@mui/icons-material/Refresh';
import NoMealsIcon from '@mui/icons-material/NoMeals';
import axios from 'axios';
import Menu from '@mui/material/Menu';
import { ConfigWithToken, ManagerBaseApi } from '../../auth/authConfig';

const Row = (props) => {
  const config = ConfigWithToken();

  const { row, onDelete, soldout, resale, onUpdate, addIngredients, regetIngreditents } =
    props;

  const [anchorEl, setAnchorEl] = React.useState(null);
  const openEdit = Boolean(anchorEl);
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

  const [open, setOpen] = React.useState(false);
  const [ingredients, setIngredients] = React.useState([]);
  const ingredientsArray = Object.entries(ingredients);

  const getIngredients = (id) => {
    axios
      .get(`${ManagerBaseApi}/menu/${id}/food`, config)
      .then((res) => setIngredients(res.data))
      .catch((err) => console.error(err));
  };

  React.useEffect(() => {
    getIngredients(row.id);
  }, [regetIngreditents]);

  const handlerOpen = (open, id) => {
    setOpen(!open);
    getIngredients(id);
  };

  return (
    <>
      <TableRow sx={{ '& > *': { borderBottom: 'unset' } }}>
        <TableCell>
          <IconButton
            aria-label="expand row"
            size="small"
            onClick={() => handlerOpen(open, row.id)}
          >
            {open ? <KeyboardArrowUpIcon /> : <KeyboardArrowDownIcon />}
          </IconButton>
        </TableCell>

        <TableCell
          component="th"
          scope="row"
          sx={{ fontWeight: 600, color: row.usePlanner ? '#1565c0' : 'inherit' }}
        >
          {row.name}
        </TableCell>

        <TableCell sx={{ fontWeight: 500 }} align="left">
          {row.details}
        </TableCell>
        <TableCell sx={{ fontWeight: 600 }} align="right">
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
              sx={{
                width: '170px',
                maxWidth: '180px',
                height: '150px',
                whiteSpace: 'nowrap',
                display: 'flex',
                flexDirection: 'column',
              }}
              component="nav"
              aria-labelledby="nested-list-subheader"
            >
              {row.usePlanner ? (
                ''
              ) : (
                <ListItemButton onClick={() => handleUpdate(row)}>
                  <ListItemIcon>
                    <EditIcon />
                  </ListItemIcon>
                  <ListItemText primary="수정" />
                </ListItemButton>
              )}

              {row.sold === true ? (
                <ListItemButton onClick={() => handleSoldout(row.id)}>
                  <ListItemIcon>
                    <NoMealsIcon />
                  </ListItemIcon>
                  <ListItemText primary="품절" />
                </ListItemButton>
              ) : (
                <ListItemButton onClick={() => handleResale(row.id)}>
                  <ListItemIcon>
                    <RefreshIcon />
                  </ListItemIcon>
                  <ListItemText primary="재판매" />
                </ListItemButton>
              )}

              <ListItemButton onClick={() => handleDelete(row.id)}>
                <ListItemIcon>
                  <DeleteIcon />
                </ListItemIcon>
                <ListItemText primary="삭제" />
              </ListItemButton>
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
                  {!row.usePlanner ? (
                    '식재료'
                  ) : (
                    <span style={{ color: '#1565c0', fontSize: '1.25rem' }}>
                      오늘의 메뉴는 식재료를 등록할 수 없습니다
                    </span>
                  )}
                </Typography>

                {!row.usePlanner && (
                  <span onClick={() => addIngredients(row)}>
                    <AddCircleIcon />
                  </span>
                )}
              </TableName>

              <Table size="small" aria-label="purchases">
                <TableHead>
                  <TableRow>
                    <TableCell sx={ingredientTableTitleStyle}>이름</TableCell>
                    <TableCell />
                    <TableCell />
                    <TableCell sx={ingredientTableTitleStyle} align="right">
                      무게 (kg)
                    </TableCell>
                  </TableRow>
                </TableHead>

                <TableBody>
                  {ingredientsArray?.map(([key, value]) => (
                    <TableRow key={key}>
                      <TableCell component="th" scope="row">
                        {key}
                      </TableCell>
                      <TableCell></TableCell>
                      <TableCell align="right"></TableCell>
                      <TableCell align="right">{value}</TableCell>
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

export default function Menulist({
  menus,
  onDelete,
  soldout,
  resale,
  onUpdate,
  addIngredients,
  regetIngreditents,
}) {
  return (
    <TableContainer component={Paper}>
      <Table aria-label="collapsible table">
        <TableHead>
          <TableRow>
            <TableCell />
            <TableCell>이름</TableCell>
            <TableCell align="left">정보</TableCell>
            <TableCell align="right">가격</TableCell>
            <TableCell align="right">상태</TableCell>
            <TableCell />
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
              addIngredients={addIngredients}
              regetIngreditents={regetIngreditents}
            />
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}

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

const ingredientTableTitleStyle = {
  fontWeight: 600,
  fontSize: '16px',
};
