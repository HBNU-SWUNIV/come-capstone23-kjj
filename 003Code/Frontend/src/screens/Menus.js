import * as React from 'react';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import Grid from '@mui/material/Grid';
import Button from '@mui/material/Button';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Stack from '@mui/material/Stack';
import Drawerheader from '../components/Drawerheader';
import Toolbar from '@mui/material/Toolbar';
import { useState, useRef } from 'react';
import axios from 'axios';
import shortid from 'shortid';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import TextField from '@mui/material/TextField';
import Skeleton from '@mui/material/Skeleton';
import Input from '@mui/material/Input';
import { ConfigWithToken, ManagerBaseApi } from '../auth/authConfig';
import { styled } from 'styled-components';
import Menulist from '../components/Menu/Menulist';
import ViewListIcon from '@mui/icons-material/ViewList';
import ViewModuleIcon from '@mui/icons-material/ViewModule';
import ToggleButton from '@mui/material/ToggleButton';
import ToggleButtonGroup from '@mui/material/ToggleButtonGroup';
import ErrorInform from '../components/general/ErrorInform';
import { useMutation, useQuery } from 'react-query';
import { getMenus } from '../api/apis';

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
        setImage('');
        handleUpdateClose();
        setNameDuplicate(false);
        refreshMenus();
      },
      onError: (err) => {
        console.log('updateMenu Error=', err);
      },
    }
  );

  const handleImageChange = (e) => {
    const selectedImage = e.target.files[0];

    if (selectedImage) {
      setImage(selectedImage);
      const reader = new FileReader();
      reader.readAsDataURL(selectedImage);

      reader.onload = (event) => {
        setSelectedImg(event.target.result);
      };
    }
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
  const handleIngredientsClose = () => {
    setIngredientsInputFields([{ key: '', value: '' }]);
    setOpenIngredients(false);
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
    if (menuNameRef.current.value === '') setMenuNameError(true);
    else setMenuNameError(false);
  };
  const menuInfoHandler = () => {
    if (menuDetailsRef.current.value === '') setMenuInfoError(true);
    else setMenuInfoError(false);
  };
  const menuCostHandler = () => {
    if (menuCostRef.current.value === '') setMenuCostError(true);
    else setMenuCostError(false);
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
              <Grid container spacing={4}>
                {menus?.map((menu) => (
                  <Grid item key={shortid.generate()} xs={12} sm={6} md={4} lg={3}>
                    <Card sx={cardStyle}>
                      <CardMedia
                        component="div"
                        sx={{
                          opacity: menu.sold === true ? null : 0.3,
                          width: '100%',
                          height: '260px',
                        }}
                        image={`http://kjj.kjj.r-e.kr:8080/api/image?dir=${menu?.image}`}
                      />

                      <CardContent
                        sx={{
                          display: 'flex',
                          justifyContent: 'space-between',
                          alignItems: 'center',
                        }}
                      >
                        <Typography
                          sx={{
                            opacity: menu.sold === true ? null : 0.3,
                            fontWeight: 600,
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
                                fontWeight: 600,
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
                                fontWeight: '600',
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
                                <Button
                                  onClick={() => handleIngredientsOpen(menu)}
                                  size="small"
                                >
                                  식재료
                                </Button>
                                <Button
                                  onClick={() => handleUpdateOpen(menu)}
                                  size="small"
                                >
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

      <Dialog
        width="md"
        open={deleteMenu}
        onClose={handleDeleteClose}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
      >
        <DialogContent>
          <DialogContentText
            sx={{
              width: '350px',
              fontSize: '20px',
              fontWeight: 600,
            }}
            id="alert-dialog-description"
          >
            정말 삭제하시겠습니까?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button sx={deleteButtonStyle} color="error" onClick={menuDelete}>
            네
          </Button>
          <Button sx={deleteButtonStyle} onClick={handleDeleteClose} autoFocus>
            아니요
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog open={addMenu} onClose={handleAddClose}>
        <DialogTitle sx={NanumFontStyle}>메뉴 등록</DialogTitle>
        <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: '1vh' }}>
          <DialogContentText sx={{ ...NanumFontStyle, fontSize: '15px' }}>
            이미지 파일을 추가하여 이미지를 등록해주세요.
          </DialogContentText>

          <div style={{ display: 'flex', alignItems: 'center' }}>
            {selectedImg ? (
              <img src={selectedImg} width={210} height={118} />
            ) : (
              <Skeleton variant="rectangular" width={210} height={118} />
            )}
            <input
              style={{ marginLeft: '2vw' }}
              type="file"
              accept="image/*"
              onChange={handleImageChange}
            />
          </div>

          <TextField
            required
            inputRef={menuNameRef}
            id="outlined-required"
            label="required"
            placeholder="메뉴명"
            onBlur={menuNameHandler}
          />
          {menuNameError && <ErrorInform message="메뉴 명은 필수 입력입니다" />}
          {nameDuplicate && <ErrorInform message={'중복된 메뉴명이 있습니다'} />}
          <TextField
            inputRef={menuDetailsRef}
            required
            id="outlined-required2"
            label="required"
            placeholder="메뉴 소개"
            onBlur={menuInfoHandler}
          />
          {menuInfoError && <ErrorInform message={'메뉴 소개는 필수 입력입니다'} />}
          <TextField
            inputRef={menuCostRef}
            id="outlined-number"
            label="가격"
            type="number"
            required
            placeholder="가격 - 숫자만 입력해주세요."
            onBlur={menuCostHandler}
          />
          {menuCostError && <ErrorInform message="가격은 필수 입력입니다." />}
        </DialogContent>
        <DialogActions>
          <Button sx={NanumFontStyle} onClick={menuAdd}>
            등록
          </Button>
          <Button sx={NanumFontStyle} color="error" onClick={handleAddClose}>
            닫기
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog open={updateMenu} onClose={handleUpdateClose}>
        <DialogTitle sx={{ ...NanumFontStyle }}>메뉴 수정</DialogTitle>
        <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: '1vh' }}>
          {updateMenu === true ? (
            <div style={{ display: 'flex', alignItems: 'center' }}>
              {selectedImg ? (
                <img src={selectedImg} width={210} height={120} />
              ) : (
                <img
                  width="210"
                  height="120"
                  src={'http://kjj.kjj.r-e.kr:8080/api/image?dir=' + updateID.image}
                />
              )}
              <input
                style={{ marginLeft: '2vw' }}
                type="file"
                accept="image/*"
                onChange={handleImageChange}
              />
            </div>
          ) : (
            <>
              <DialogContentText sx={NanumFontStyle}>
                이미지 파일을 추가하여 이미지를 등록해주세요.
              </DialogContentText>
              <div style={{ display: 'flex', alignItems: 'center' }}>
                <Skeleton variant="rectangular" width={210} height={118} />
                <input
                  style={{ marginLeft: '2vw' }}
                  type="file"
                  accept="image/*"
                  onChange={(e) => setImage(e.target.files[0])}
                />
              </div>
            </>
          )}

          <TextField
            required
            inputRef={menuNameRef}
            id="outlined-required"
            placeholder={updateID?.name}
          />

          {nameDuplicate && <ErrorInform message={'중복된 메뉴명이 있습니다'} />}

          <TextField
            inputRef={menuDetailsRef}
            required
            id="outlined-required2"
            placeholder={updateID?.details}
          />
          <TextField
            inputRef={menuCostRef}
            placeholder={updateID?.cost}
            id="outlined-number"
            label="가격"
            type="number"
          />
        </DialogContent>
        <DialogActions>
          <Button sx={NanumFontStyle} onClick={menuUpdate}>
            수정
          </Button>
          <Button sx={NanumFontStyle} color="error" onClick={handleUpdateClose}>
            닫기
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog open={openIngredients} onClose={handleIngredientsClose}>
        <DialogTitle>식재료 등록</DialogTitle>
        <DialogContent
          sx={{
            display: 'flex',
            flexDirection: 'column',
            gap: '1vh',
            overflow: 'auto',
            height: '40vh',
            minWidth: '45vw',
          }}
        >
          <DialogContentText>식재료무게는 KG단위로 등록해주세요.</DialogContentText>
          <DialogContentText
            sx={{ marginTop: '-10px', marginBottom: '20px', fontSize: '12px' }}
          >
            식재료 정보를 수정하면 이전 정보는 덮어쓰입니다.
          </DialogContentText>
          {ingredientsInputFields.map((field, index) => (
            <div key={index}>
              <Input
                name="key"
                value={field.key}
                onChange={(e) => handleInputChange(index, e)}
                placeholder="식재료 이름"
              />
              <Input
                name="value"
                value={field.value}
                onChange={(e) => handleInputChange(index, e)}
                placeholder="식재료 무게"
                type="number"
              />
              <Button onClick={() => handleRemoveFields(index)}>삭제</Button>
            </div>
          ))}
        </DialogContent>
        <DialogActions>
          <Button onClick={handleAddFields}>식재료 추가하기</Button>
          <Button onClick={() => addIngredients(ingredientsID)}>등록</Button>
          <Button color="error" onClick={handleIngredientsClose}>
            닫기
          </Button>
        </DialogActions>
      </Dialog>
    </ThemeProvider>
  );
}

const defaultTheme = createTheme();

export const NanumFontStyle = {
  fontWeight: '600',
};

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

const cardStyle = {
  width: '260px',
  height: '100%',
  display: 'flex',
  flexDirection: 'column',
};

const deleteButtonStyle = { fontWeight: 500, fontSize: '18px' };
