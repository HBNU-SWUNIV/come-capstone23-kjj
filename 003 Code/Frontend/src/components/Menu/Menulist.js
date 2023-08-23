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
import ListSubheader from '@mui/material/ListSubheader';
import List from '@mui/material/List';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import InboxIcon from '@mui/icons-material/MoveToInbox';
import DraftsIcon from '@mui/icons-material/Drafts';
import SendIcon from '@mui/icons-material/Send';
import ExpandLess from '@mui/icons-material/ExpandLess';
import ExpandMore from '@mui/icons-material/ExpandMore';
import StarBorder from '@mui/icons-material/StarBorder';
import Dialog from '@mui/material/Dialog';
import DialogContent from '@mui/material/DialogContent';
import Chip from '@mui/material/Chip';
import zIndex from '@mui/material/styles/zIndex';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import RefreshIcon from '@mui/icons-material/Refresh';
import NoMealsIcon from '@mui/icons-material/NoMeals';
import axios from 'axios';
import { ConfigWithToken, ManagerBaseApi } from '../../auth/authConfig';
import Fab from '@mui/material/Fab';

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

const MenuEditList = styled.div`
  position: absolute;
  width: 170px;
  height: 160px;
  border-radius: 10px;
  box-shadow: 1px 1px 5px black;

  background-color: white;
  z-index: 9999;
`;

const Sale = styled.div`
  margin-right: -5px;
`;

const Row = (props) => {
  const config = ConfigWithToken();

  const { row, onDelete, soldout, resale, onUpdate, addIngredients, regetIngreditents } =
    props;
  const [open, setOpen] = React.useState(false);

  const [openEdit, setopenEdit] = React.useState(false);
  const onOpenEdit = () => {
    setopenEdit(true);
  };
  const onOffEdit = () => {
    setopenEdit(false);
  };
  const handleEdit = () => {
    setopenEdit(!openEdit);
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

  const [ingredients, setIngredients] = React.useState([]);

  React.useEffect(() => {
    getIngredients(row.id);
  }, [regetIngreditents]);

  const getIngredients = (id) => {
    axios
      .get(`${ManagerBaseApi}/menu/${id}/food`, config)
      .then((res) => setIngredients(res.data))
      .catch((err) => console.error(err));
  };
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

        <TableCell component="th" scope="row">
          {row.name}
        </TableCell>

        <TableCell align="left">{row.details}</TableCell>
        <TableCell align="right">{row.cost}</TableCell>
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
          <MenuEdit onClick={handleEdit}>
            <MoreVertIcon />
          </MenuEdit>
          {openEdit && (
            <MenuEditList>
              <Dialog sx={{ opacity: 0 }} onClose={onOffEdit} open={onOpenEdit} />
              <List
                sx={{
                  width: '100%',
                  maxWidth: 360,
                  whiteSpace: 'nowrap',
                  display: 'flex',
                  flexDirection: 'column',
                  justifyContent: 'space-around',
                }}
                component="nav"
                aria-labelledby="nested-list-subheader"
              >
                <ListItemButton onClick={() => handleUpdate(row)}>
                  <ListItemIcon>
                    <EditIcon />
                  </ListItemIcon>
                  <ListItemText primary="수정" />
                </ListItemButton>

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
            </MenuEditList>
          )}
        </TableCell>
      </TableRow>

      <TableRow>
        <TableCell style={{ paddingBottom: 0, paddingTop: 0 }} colSpan={6}>
          <Collapse in={open} timeout="auto" unmountOnExit>
            <Box sx={{ margin: 1 }}>
              <TableName>
                <Typography variant="h6" gutterBottom component="div">
                  식재료
                </Typography>

                <span onClick={() => addIngredients(row)}>
                  <AddCircleIcon />
                </span>
              </TableName>

              <Table size="small" aria-label="purchases">
                <TableHead>
                  <TableRow>
                    <TableCell sx={{ fontWeight: 600, fontSize: '16px' }}>이름</TableCell>
                    <TableCell />
                    <TableCell />
                    <TableCell sx={{ fontWeight: 600, fontSize: '16px' }} align="right">
                      무게 (kg)
                    </TableCell>
                  </TableRow>
                </TableHead>

                <TableBody>
                  {ingredients.map((ingredient) => (
                    <TableRow key={ingredient.name}>
                      <TableCell component="th" scope="row">
                        {ingredient.name}
                      </TableCell>
                      <TableCell></TableCell>
                      <TableCell align="right"></TableCell>
                      <TableCell align="right">{ingredient.kg}</TableCell>
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
          {menus.map((row) => (
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
