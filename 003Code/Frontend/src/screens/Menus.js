import * as React from 'react';
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
import Drawerheader from '../components/Drawerheader';
import { useState, useRef } from 'react';
import axios from 'axios';
import { ConfigWithToken, ManagerBaseApi } from '../auth/authConfig';
import Menulist from '../components/Menu/Menulist';
import ViewListIcon from '@mui/icons-material/ViewList';
import ViewModuleIcon from '@mui/icons-material/ViewModule';
import { useMutation, useQuery } from 'react-query';
import { getMenus } from '../api/apis';
import IngredientsDialog from '../components/Menu/IngredientsDialog';
import MenuAddDialog from '../components/Menu/MenuAddDialog';
import MenuUpdateDialog from '../components/Menu/MenuUpdateDialog';
import DeleteDialog from '../components/general/DeleteDialog';
import MenuCard from '../components/Menu/\bMenuCard';
import UseErrorHandler from '../hooks/UseErrorHandler';
import UseImageHandler from '../hooks/UseImageHandler';

export default function Menus() {
  const [deleteID, setDeleteID] = useState(0);
  const [updateID, setUpdateID] = useState(0);
  const [ingredientsID, setIngredientsID] = useState(0);
  const [addMenu, setAddMenu] = useState(false);
  const [updateMenu, setUpdatemenu] = useState(false);
  const [deleteMenu, setDeleteMenu] = useState(false);
  const [refreshIngredients, setRefreshIngredients] = useState(false);
  const [openIngredients, setOpenIngredients] = useState(false);
  const [ingredientsInputFields, setIngredientsInputFields] = useState([
    { key: '', value: '' },
  ]);
  const menuNameRef = useRef('');
  const menuDetailsRef = useRef('');
  const menuCostRef = useRef('');
  const [image, setImage] = useState([]);
  const [selectedImg, setSelectedImg] = useState(null);
  const [nameDuplicate, setNameDuplicate] = useState(false);
  const [menuNameError, setMenuNameError] = useState(false);
  const [menuInfoError, setMenuInfoError] = useState(false);
  const [menuCostError, setMenuCostError] = useState(false);
  const config = ConfigWithToken();
  const formdataConfig = {
    headers: {
      'Content-Type': 'multipart/form-data',
      ...config.headers,
    },
  };
  const [view, setView] = useState('card');
  const handleView = (event, nextView) => {
    if (nextView !== null) setView(nextView);
  };

  const { data: menus, refetch: refreshMenus } = useQuery(['getMenus', config], () =>
    getMenus(config)
  );
  const deleteMenus = useMutation(
    (deleteID) => axios.delete(`${ManagerBaseApi}/menu/${deleteID}`, config),
    {
      onSuccess: () => {
        refreshMenus();
      },
      onError: (error) => {
        console.log('deleteMenu Error =', error);
      },
    }
  );
  const soldoutMenus = useMutation(
    (id) =>
      axios({
        method: 'PATCH',
        url: `${ManagerBaseApi}/menu/${id}/sold/n`,
        ...formdataConfig,
      }),
    {
      onSuccess: () => {
        refreshMenus();
      },
      onError: (err) => {
        console.log('soldoutMenu Error=', err);
      },
    }
  );
  const resaleMenus = useMutation(
    (id) =>
      axios({
        method: 'PATCH',
        url: `${ManagerBaseApi}/menu/${id}/sold/y`,
        ...formdataConfig,
      }),
    {
      onSuccess: () => {
        refreshMenus();
      },
      onError: (err) => {
        console.log('resaleMenu Error =', err);
      },
    }
  );
  const addMenus = useMutation(
    (addData) =>
      axios({
        method: 'POST',
        url: `${ManagerBaseApi}/menu`,
        data: addData,
        ...formdataConfig,
      }),
    {
      onSuccess: () => {
        refreshMenus();
        setAddMenu(false);
        setImage(null);
        setNameDuplicate(false);
        setSelectedImg(null);
      },
      onError: (err) => {
        if (err.response.status === 400) {
          alert('이미지 파일 용량이 너무 큽니다.');
          return;
        } else if (err.response.status === 409) {
          setNameDuplicate(true);
          return;
        }
      },
    }
  );
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
        setNameDuplicate(false);
        refreshMenus();
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
    setDeleteID(id);
    setDeleteMenu(true);
  };
  const handleDeleteClose = () => {
    setDeleteMenu(false);
  };
  const handleAddOpen = () => {
    setAddMenu(true);
  };
  const handleAddClose = () => {
    setAddMenu(false);
    setMenuInfoError(false);
    setSelectedImg(null);
  };
  const handleUpdateOpen = (menu) => {
    setUpdateID(menu);
    setUpdatemenu(true);
  };
  const handleUpdateClose = () => {
    setUpdatemenu(false);
    setNameDuplicate(false);
    setSelectedImg(null);
  };

  const handleIngredientsOpen = (menu) => {
    setIngredientsID(menu?.id);
    onNowIngredients(menu?.id);
    setOpenIngredients(true);
  };
  const handleIngredientsClose = () => {
    setIngredientsInputFields([{ key: '', value: '' }]);
    setOpenIngredients(false);
  };
  const onNowIngredients = async (id) => {
    try {
      const res = await axios.get(`${ManagerBaseApi}/menu/${id}/food`, config);
      const ingredients = Object.entries(res.data);
      const nowIngredients = ingredients.map(([key, value]) => ({ key, value }));
      setIngredientsInputFields(nowIngredients);
    } catch (err) {
      console.log('ingredients Error', err);
    }
  };
  const handleInputChange = (index, e) => {
    const { name, value } = e.target;
    const fields = [...ingredientsInputFields];
    fields[index][name] = value;
    setIngredientsInputFields(fields);
  };
  const handleAddFields = (e) => {
    e.preventDefault();
    setIngredientsInputFields([...ingredientsInputFields, { key: '', value: '' }]);
  };
  const handleRemoveFields = (index) => {
    const fields = [...ingredientsInputFields];
    fields.splice(index, 1);
    setIngredientsInputFields(fields);
  };

  const menuDelete = () => {
    deleteMenus.mutate(deleteID);
    setDeleteMenu(false);
  };
  const soldout = (id) => {
    soldoutMenus.mutate(id);
  };
  const resale = (id) => {
    resaleMenus.mutate(id);
  };
  const menuNameHandler = () => {
    UseErrorHandler({
      condition: menuNameRef.current.value === '',
      setFn: setMenuNameError,
    });
  };
  const menuInfoHandler = () => {
    UseErrorHandler({
      condition: menuDetailsRef.current.value === '',
      setFn: setMenuInfoError,
    });
  };
  const menuCostHandler = () => {
    UseErrorHandler({
      condition: menuCostRef.current.value === '',
      setFn: setMenuCostError,
    });
  };

  const menuAdd = () => {
    const formdata = new FormData();
    const menuInputsIsNotNull =
      menuNameRef.current.value !== '' &&
      menuDetailsRef.current.value !== '' &&
      menuCostRef.current.value !== '';

    const body = {
      name: menuNameRef.current.value,
      cost: menuCostRef.current.value,
      details: menuDetailsRef.current.value,
      usePlanner: false,
    };
    const blob = new Blob([JSON.stringify(body)], { type: 'application/json' });
    formdata.append('data', blob);
    formdata.append('file', image);
    menuNameHandler();
    menuInfoHandler();
    menuCostHandler();

    if (menuInputsIsNotNull) addMenus.mutate(formdata);
  };
  const menuUpdate = () => {
    const formdata = new FormData();
    const validateDuplicatedName =
      menus
        .filter((f_menu) => f_menu.id != updateID.id)
        .filter((n) => n.name === menuNameRef.current.value).length != 0;

    if (validateDuplicatedName) {
      setNameDuplicate(true);
      return;
    }
    const body = {
      name: menuNameRef.current.value === '' ? updateID.name : menuNameRef.current.value,
      details:
        menuDetailsRef.current.value === ''
          ? updateID.details
          : menuDetailsRef.current.value,
      cost: menuCostRef.current.value === '' ? updateID.cost : menuCostRef.current.value,
      usePlanner: false,
    };

    const blob = new Blob([JSON.stringify(body)], { type: 'application/json' });
    formdata.append('data', blob);
    image != null && formdata.append('file', image);
    updateMenus.mutate(formdata);
  };
  const addIngredients = (id) => {
    const body = {};
    ingredientsInputFields.forEach((field) => {
      const { key, value } = field;
      if (key && value) {
        body[key] = value;
      }
    });
    axios
      .get(`${ManagerBaseApi}/menu/${id}/food`, config)
      .then((res) => {
        if (Object.keys(res.data).length === 0) {
          axios.post(`${ManagerBaseApi}/menu/${id}/food`, body, config).then((res) => {
            if (res.status === 200) {
              setRefreshIngredients((prev) => !prev);
              handleIngredientsClose();
            }
          });
        } else {
          axios.patch(`${ManagerBaseApi}/menu/${id}/food`, body, config).then((res) => {
            if (res.status === 200) {
              setRefreshIngredients((prev) => !prev);
              handleIngredientsClose();
            }
          });
        }
      })
      .catch((err) => {
        if (err.response.status === 400) {
          console.error('ingredientsID add Error =', err);
        }
      });
    setIngredientsInputFields([{ key: '', value: '' }]);
    handleIngredientsClose();
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
                <ToggleButton value="card" aria-label="card">
                  <ViewModuleIcon />
                </ToggleButton>
                <ToggleButton value="list" aria-label="list">
                  <ViewListIcon />
                </ToggleButton>
              </ToggleButtonGroup>
              <Button
                sx={MenusButtonStyle}
                onClick={handleAddOpen}
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
                regetIngreditents={refreshIngredients}
                soldout={soldout}
                resale={resale}
                deleteMenu={handleDeleteOpen}
                onUpdate={handleUpdateOpen}
                menus={menus}
              />
            )}
          </Container>
        </Box>
      </Box>

      <DeleteDialog open={deleteMenu} onClose={handleDeleteClose} onDelete={menuDelete} />

      <MenuAddDialog
        open={addMenu}
        onClose={handleAddClose}
        selectedImg={selectedImg}
        handleImageChange={handleImageChange}
        menuNameRef={menuNameRef}
        menuNameHandler={menuNameHandler}
        menuNameError={menuNameError}
        nameDuplicate={nameDuplicate}
        menuDetailsRef={menuDetailsRef}
        menuInfoHandler={menuInfoHandler}
        menuInfoError={menuInfoError}
        menuCostRef={menuCostRef}
        menuCostHandler={menuCostHandler}
        menuCostError={menuCostError}
        menuAdd={menuAdd}
      />

      <MenuUpdateDialog
        open={updateMenu}
        onClose={handleUpdateClose}
        selectedImg={selectedImg}
        updateID={updateID}
        handleImageChange={handleImageChange}
        setImage={setImage}
        menuNameRef={menuNameRef}
        nameDuplicate={nameDuplicate}
        menuDetailsRef={menuDetailsRef}
        menuCostRef={menuCostRef}
        menuUpdate={menuUpdate}
      />
      <IngredientsDialog
        open={openIngredients}
        onClose={handleIngredientsClose}
        ingredientsID={ingredientsID}
        addIngredients={addIngredients}
        handleAddFields={handleAddFields}
        handleRemoveFields={handleRemoveFields}
        ingredientsInputFields={ingredientsInputFields}
        handleInputChange={handleInputChange}
      />
    </ThemeProvider>
  );
}

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
