import { createTheme, ThemeProvider } from '@mui/material/styles';
import {
  Box,
  Button,
  Container,
  Stack,
  ToggleButton,
  ToggleButtonGroup,
  Toolbar,
} from '@mui/material';
import CssBaseline from '@mui/material/CssBaseline';
import Drawerheader from '../components/Drawerheader/Drawerheader';
import { useState, useRef, useEffect } from 'react';
import axios from 'axios';
import { ConfigWithToken, ManagerBaseApi } from '../auth/authConfig';
import Menulist from '../components/Menu/list/Menulist';
import ViewListIcon from '@mui/icons-material/ViewList';
import ViewModuleIcon from '@mui/icons-material/ViewModule';
import { useMutation } from 'react-query';
import IngredientsDialog from '../components/Menu/dialog/IngredientsDialog';
import MenuAddDialog from '../components/Menu/dialog/MenuAddDialog';
import MenuUpdateDialog from '../components/Menu/dialog/MenuUpdateDialog';
import DeleteDialog from '../components/general/DeleteDialog';
import MenuCard from '../components/Menu/\bMenuCard';
import UseImageHandler from '../hooks/UseImageHandler';
import UseOnOffHandler from '../hooks/UseOnOffHandler';
import Menu_api from '../components/Menu/api_update/Menu_api';
const defaultTheme = createTheme();
export const NanumFontStyle = {
  fontWeight: '600',
};
const MenusBoxStyle = {
  display: 'flex',
  flexDirection: 'column',
  backgroundColor: 'white',
  flexGrow: 1,
  height: '100%',
  minHeight: '100vh',
  overflow: 'auto',
  boxSizing: 'border-box',
  paddingBottom: 'var(--copyright-height)',
};
const MenusButtonStyle = {
  fontWeight: '500',
  fontSize: '16px',
  backgroundColor: 'rgb(0, 171, 85)',
};
const toggle_button_list_data = [
  {
    id: 0,
    value: 'card',
    aria_label: 'card',
    icon: <ViewModuleIcon />,
  },
  {
    id: 1,
    value: 'list',
    aria_label: 'list',
    icon: <ViewListIcon />,
  },
];

export default function Menus() {
  const [selectedFoodId, setSelectedFood] = useState('');
  const [updateID, setUpdateID] = useState('');
  const [addMenu, setAddMenu] = useState(false);
  const [updateMenu, setUpdatemenu] = useState(false);
  const [deleteMenu, setDeleteMenu] = useState(false);
  const [openIngredients, setOpenIngredients] = useState(false);
  const [nameDuplicate, setNameDuplicate] = useState(false);
  const [image, setImage] = useState([]);
  const [selectedImg, setSelectedImg] = useState(null);
  const menuNameRef = useRef('');
  const menuDetailsRef = useRef('');
  const menuCostRef = useRef('');
  const [view, setView] = useState('card');
  const handleView = (event, nextView) => {
    if (nextView !== null) setView(nextView);
  };
  const config = ConfigWithToken();
  const formdataConfig = {
    headers: {
      'Content-Type': 'multipart/form-data',
      ...config.headers,
    },
  };
  const {
    menus,
    refreshMenus,
    deleteMenus,
    soldoutMenus,
    resaleMenus,
    addMenus,
    success,
  } = Menu_api();

  const menuInputsIsNotNull =
    menuNameRef?.current?.value !== '' &&
    menuDetailsRef?.current?.value !== '' &&
    menuCostRef?.current?.value !== '';

  useEffect(() => {
    if (success.addmenus) handleAddClose();
    else if (success.isDuplicatedName) setNameDuplicate(true);
  }, [success]);

  const updateMenus = useMutation(
    (updateData) =>
      axios({
        method: 'PATCH',
        url: `${ManagerBaseApi}/menu/${updateID.id}`,
        data: updateData,
        ...formdataConfig,
      }),
    {
      onSuccess: () => {
        if (image?.length !== 0) {
          setImage('');
          window.location.reload();
          return;
        }
        handleUpdateClose();
      },
      onError: (err) => {
        console.log('updateMenu Error=', err);
      },
    }
  );

  const handleImageChange = (event) => {
    UseImageHandler(event, setImage, setSelectedImg);
  };
  const handleDeleteOpen = (id) => {
    setUpdateID(id);
    setDeleteMenu(true);
  };
  const handleAddClose = () => {
    setAddMenu(false);
    setSelectedImg(null);
    setImage(null);
    setNameDuplicate(false);
  };
  const handleUpdateOpen = (menu) => {
    setUpdateID(menu);
    setUpdatemenu(true);
  };
  const handleUpdateClose = () => {
    refreshMenus();
    setUpdatemenu(false);
    setNameDuplicate(false);
    setSelectedImg(null);
    setUpdateID('');
  };
  const handleIngredientsOpen = (menu) => {
    const foodId = menus.filter((item) => item.id == menu.id)[0].foodId;
    setUpdateID(foodId);
    setOpenIngredients(true);
  };
  const handleIngredientsClose = () => {
    setOpenIngredients(false);
    setUpdateID('');
  };

  const menuDelete = () => {
    deleteMenus.mutate(updateID);
    setDeleteMenu(false);
  };
  const soldout = (id) => {
    soldoutMenus.mutate(id);
  };
  const resale = (id) => {
    resaleMenus.mutate(id);
  };
  const onAddMenu = () => {
    const formdata = new FormData();
    const body = {
      name: menuNameRef?.current?.value,
      cost: menuCostRef?.current?.value,
      details: menuDetailsRef?.current?.value,
      usePlanner: false,
    };
    const blob = new Blob([JSON.stringify(body)], { type: 'application/json' });
    formdata.append('data', blob);
    formdata.append('file', image);
    if (menuInputsIsNotNull) addMenus.mutate(formdata);
  };
  const onUpdateMenu = () => {
    const formdata = new FormData();
    const validateDuplicatedName =
      updateID !== '' &&
      menus
        .filter((item) => item.id != updateID.id)
        .filter((n) => n.name === menuNameRef?.current?.value).length != 0;
    const nameValue =
      menuNameRef?.current?.value === '' ? updateID.name : menuNameRef?.current?.value;
    const detailsValue =
      menuDetailsRef?.current?.value === ''
        ? updateID.details
        : menuDetailsRef?.current?.value;
    const costValue =
      menuCostRef?.current?.value === '' ? updateID.cost : menuCostRef?.current?.value;

    if (validateDuplicatedName) return setNameDuplicate(true);
    const body = {
      name: nameValue,
      details: detailsValue,
      cost: costValue,
      usePlanner: false,
    };

    const blob = new Blob([JSON.stringify(body)], { type: 'application/json' });
    formdata.append('data', blob);
    image != null && formdata.append('file', image);
    updateMenus.mutate(formdata);

    if (selectedFoodId !== undefined) {
      axios({
        method: 'PATCH',
        url: `${ManagerBaseApi}/menu/${updateID.id}/food/${selectedFoodId}`,
        ...formdataConfig,
      })
        .then((res) => console.log(res))
        .catch((err) => console.error(err));
    }
  };

  return (
    <ThemeProvider theme={defaultTheme}>
      <Box sx={{ display: 'flex' }}>
        <CssBaseline />
        <Drawerheader pages={'메뉴'} />

        <Box component="main" sx={MenusBoxStyle}>
          <Toolbar />

          <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
            <Stack
              sx={{ pt: 4 }}
              direction="row"
              spacing={2}
              justifyContent="space-between"
            >
              <ToggleButtonGroup
                orientation="horizontal"
                value={view}
                exclusive
                onChange={handleView}
              >
                {toggle_button_list_data.map((item) => (
                  <ToggleButton
                    value={item.value}
                    key={item.id}
                    aria-label={item.aria_label}
                  >
                    {item.icon}
                  </ToggleButton>
                ))}
              </ToggleButtonGroup>

              <Button
                sx={MenusButtonStyle}
                onClick={() => UseOnOffHandler(true, setAddMenu)}
                variant="contained"
                color="success"
              >
                + 메뉴 등록
              </Button>
            </Stack>
          </Container>

          <Container sx={{ py: 1 }} maxWidth="lg">
            {view === 'card' && (
              <MenuCard
                menus={menus}
                handleIngredientsOpen={handleIngredientsOpen}
                handleUpdateOpen={handleUpdateOpen}
                soldout={soldout}
                handleDeleteOpen={handleDeleteOpen}
                resale={resale}
              />
            )}

            {view === 'list' && (
              <Menulist
                addIngredients={handleIngredientsOpen}
                soldout={soldout}
                resale={resale}
                onDelete={handleDeleteOpen}
                onUpdate={handleUpdateOpen}
                menus={menus}
              />
            )}
          </Container>
        </Box>
      </Box>

      <DeleteDialog
        open={deleteMenu}
        onClose={() => UseOnOffHandler(false, setDeleteMenu)}
        onDelete={menuDelete}
      />

      <MenuAddDialog
        open={addMenu}
        onClose={handleAddClose}
        selectedImg={selectedImg}
        handleImageChange={handleImageChange}
        menuNameRef={menuNameRef}
        menuDetailsRef={menuDetailsRef}
        menuCostRef={menuCostRef}
        nameDuplicate={nameDuplicate}
        onAddMenu={onAddMenu}
      />

      <MenuUpdateDialog
        open={updateMenu}
        onClose={handleUpdateClose}
        selectedImg={selectedImg}
        updateID={updateID}
        handleImageChange={handleImageChange}
        menuNameRef={menuNameRef}
        menuDetailsRef={menuDetailsRef}
        menuCostRef={menuCostRef}
        nameDuplicate={nameDuplicate}
        onUpdateMenu={onUpdateMenu}
        selectedFood={selectedFoodId}
        setSelectedFood={setSelectedFood}
      />

      <IngredientsDialog
        open={openIngredients}
        onClose={handleIngredientsClose}
        updateId={updateID}
      />
    </ThemeProvider>
  );
}
