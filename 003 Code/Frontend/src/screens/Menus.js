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
import { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import shortid from 'shortid';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import TextField from '@mui/material/TextField';
import Skeleton from '@mui/material/Skeleton';
import Snackbar from '@mui/material/Snackbar';
import MuiAlert from '@mui/material/Alert';
import Input from '@mui/material/Input';
import { ConfigWithToken, ManagerBaseApi } from '../auth/authConfig';
import { useNavigate } from 'react-router-dom';
import { styled } from 'styled-components';
import Menulist from '../components/Menu/Menulist';
import ViewListIcon from '@mui/icons-material/ViewList';
import ViewModuleIcon from '@mui/icons-material/ViewModule';
import ToggleButton from '@mui/material/ToggleButton';
import ToggleButtonGroup from '@mui/material/ToggleButtonGroup';
import ErrorInform from '../components/general/ErrorInform';

export default function Menus() {
  const navigate = useNavigate();

  const [onDelete, setonDelete] = useState(false);
  const [deleteID, setDeleteId] = useState(0);

  const [addMenu, setAddMenu] = useState(false);
  const [menus, setMenus] = useState([]);
  const [image, setImage] = useState([]);

  const [update, setUpdate] = useState(null);
  const [updateMenu, setUpdatemenu] = useState(false);

  const [오늘의메뉴, set일품] = useState(false);

  const [식재료open, set식재료open] = useState(false);
  const [식재료, set식재료] = useState(null);

  const [success, setSuccess] = useState(false);

  const [inputfields, setInputfields] = useState([{ key: '', value: '' }]);

  const menuNameRef = useRef('');
  const menuDetailsRef = useRef('');
  const menuCostRef = useRef('');

  const config = ConfigWithToken();
  const formdataConfig = {
    headers: {
      'Content-Type': 'multipart/form-data',
      ...config.headers,
    },
  };

  useEffect(() => {
    axios
      .get(`${ManagerBaseApi}/menu`, config)
      .then((res) => setMenus(res.data))
      .catch((err) => console.log('MenuError', err));
  }, []);

  const handleDeleteOpen = (id) => {
    setDeleteId(id);
    setonDelete(true);
  };
  const handleDeleteClose = () => {
    setonDelete(false);
  };
  const handleAddOpen = () => {
    setAddMenu(true);
  };
  const handleAddClose = () => {
    setAddMenu(false);
    setRequired(false);
  };
  const handleSuccessOpen = () => {
    setSuccess(true);
  };
  const handleSuccessClose = (event, reason) => {
    if (reason === 'clickaway') {
      return;
    }
    setSuccess(false);
  };
  const handleUpdateOpen = (menu) => {
    setUpdate(menu);
    setUpdatemenu(true);
  };
  const handleUpdateClose = () => {
    setUpdatemenu(false);
    setNameDuplicate(false);
  };
  const handle오늘의메뉴Close = () => {
    set일품(false);
  };
  const handle식재료Open = (menu) => {
    set식재료(menu);
    set식재료open(true);
  };
  const handle식재료Close = () => {
    setInputfields([{ key: '', value: '' }]);
    set식재료open(false);
  };
  const handleInputChange = (index, e) => {
    const { name, value } = e.target;
    const fields = [...inputfields];
    fields[index][name] = value;
    setInputfields(fields);
  };
  const handleAddFields = (e) => {
    e.preventDefault();
    setInputfields([...inputfields, { key: '', value: '' }]);
  };
  const handleRemoveFields = (index) => {
    const fields = [...inputfields];
    fields.splice(index, 1);
    setInputfields(fields);
  };

  const [reGet, setReGet] = useState(false);
  const 식재료add = (id) => {
    let body = {};
    inputfields.forEach((field) => {
      const { key, value } = field;
      if (key && value) {
        body[key] = value;
      }
    });
    axios
      .get(`${ManagerBaseApi}/menu/${id}/food`, config)
      .then((res) => {
        if (res.status === 200) {
          axios.patch(`${ManagerBaseApi}/menu/${id}/food`, body, config).then((res) => {
            if (res.status === 200) {
              setReGet((prev) => !prev);
              handle식재료Close();
            }
          });
        }
      })
      .catch((err) => {
        if (err.response.status === 400) {
          axios
            .post(`${ManagerBaseApi}/menu/${id}/food`, body, config)
            .then(handle식재료Close());
        }
      });
    setInputfields([{ key: '', value: '' }]);
    handle식재료Close();
  };
  const menuDelete = () => {
    axios
      .delete(`${ManagerBaseApi}/menu/${deleteID}`, config)
      .then(() => {
        axios.get(`${ManagerBaseApi}/menu`, config).then((res) => setMenus(res.data));
      })
      .catch((err) => console.log('menuDeleteError', err));
    setonDelete(false);
  };
  const soldout = (id) => {
    axios({
      method: 'PATCH',
      url: `${ManagerBaseApi}/menu/${id}/sold/n`,
      ...formdataConfig,
    })
      .then(() => {
        axios.get(`${ManagerBaseApi}/menu`, config).then((res) => setMenus(res.data));
      })
      .catch((err) => err.response.status === 401 && navigate('/'));
  };
  const resale = (id) => {
    axios({
      method: 'PATCH',
      url: `${ManagerBaseApi}/menu/${id}/sold/y`,
      ...formdataConfig,
    }).then(() => {
      axios.get(`${ManagerBaseApi}/menu`, config).then((res) => setMenus(res.data));
    });
  };

  const [nameDuplicate, setNameDuplicate] = useState(false);
  const [required, setRequired] = useState(false);
  const infoHandler = () => {
    if (menuDetailsRef.current.value === '') setRequired(true);
    else setRequired(false);
  };

  const menuAdd = () => {
    const formdata = new FormData();
    let body = {
      name: menuNameRef.current.value,
      cost: menuCostRef.current.value,
      details: menuDetailsRef.current.value,
      usePlanner: false,
    };
    const blob = new Blob([JSON.stringify(body)], { type: 'application/json' });
    formdata.append('data', blob);
    formdata.append('file', image);
    axios({
      method: 'POST',
      url: `${ManagerBaseApi}/menu`,
      data: formdata,
      ...formdataConfig,
    })
      .then((res) => res.status === 200 && handleSuccessOpen())
      .then(() => {
        axios.get(`${ManagerBaseApi}/menu`, config).then((res) => {
          setMenus(res.data);
          setAddMenu(false);
          setImage(null);
          setNameDuplicate(false);
        });
      })
      .catch((err) => {
        if (err.response.status === 400) {
          alert('파일의 용량이 너무큽니다.');
          return;
        } else if (err.response.status === 409) {
          setNameDuplicate(true);
          return;
        }
      });
  };
  const menuUpdate = () => {
    const formdata = new FormData();
    if (
      menus
        .filter((f_menu) => f_menu.id != update.id)
        .filter((n) => n.name === menuNameRef.current.value).length != 0
    ) {
      setNameDuplicate(true);
      return;
    }
    let body = {
      name: menuNameRef.current.value === '' ? update.name : menuNameRef.current.value,
      details:
        menuDetailsRef.current.value === ''
          ? update.details
          : menuDetailsRef.current.value,
      cost: menuCostRef.current.value === '' ? update.cost : menuCostRef.current.value,
      usePlanner: false,
    };
    const blob = new Blob([JSON.stringify(body)], { type: 'application/json' });
    formdata.append('data', blob);
    image != null && formdata.append('file', image);

    axios({
      method: 'PATCH',
      url: `${ManagerBaseApi}/menu/${update.id}`,
      data: formdata,
      ...formdataConfig,
    }).then(() => {
      axios.get(`${ManagerBaseApi}/menu`, config).then((res) => {
        setMenus(res.data);
        setImage('');
        handleUpdateClose();
        setNameDuplicate(false);
      });
    });
  };
  const 오늘의메뉴Add = () => {
    const formdata = new FormData();
    let body = {
      name: '오늘의메뉴',
      cost: menuCostRef.current.value,
      details: '매일 바뀌는 메뉴입니다',
      usePlanner: true,
    };
    const blob = new Blob([JSON.stringify(body)], { type: 'application/json' });
    formdata.append('data', blob);
    formdata.append('file', image);
    axios({
      method: 'POST',
      url: `${ManagerBaseApi}/menu`,
      data: formdata,
      ...formdataConfig,
    })
      .then((res) => res.status === 200 && handleSuccessOpen())
      .then(() => {
        axios.get(`${ManagerBaseApi}/menu`, config).then((res) => setMenus(res.data));
      })
      .catch((err) => {
        if (err.response.status === 400) {
          alert('이미지 용량이 너무 큽니다.');
          return;
        }
      });
    set일품(false);
    setImage(null);
  };

  const [view, setView] = useState('card');
  const handleView = (event, nextView) => {
    if (nextView !== null) setView(nextView);
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
                {menus.map((menu) => (
                  <Grid item key={shortid.generate()} xs={12} sm={6} md={4} lg={3}>
                    <Card sx={cardStyle}>
                      <CardMedia
                        component="div"
                        sx={{
                          opacity: menu.sold === true ? null : 0.3,
                          width: '100%',
                          height: '260px',
                        }}
                        image={'http://kjj.kjj.r-e.kr:8080/api/image?dir=' + menu?.image}
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
                              품 절 되었어요
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
                              <Button onClick={() => handleUpdateOpen(menu)} size="small">
                                수정
                              </Button>
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
                addIngredients={handle식재료Open}
                regetIngreditents={reGet}
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

      <Dialog
        width="md"
        open={onDelete}
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
            <Skeleton variant="rectangular" width={210} height={118} />
            <input
              style={{ marginLeft: '2vw' }}
              type="file"
              accept="image/*"
              onChange={(e) => setImage(e.target.files[0])}
            />
          </div>

          <TextField
            required
            inputRef={menuNameRef}
            id="outlined-required"
            label="required"
            placeholder="메뉴명"
          />
          {nameDuplicate && <ErrorInform message={'중복된 메뉴명이 있습니다'} />}
          <TextField
            inputRef={menuDetailsRef}
            required
            id="outlined-required2"
            label="required"
            placeholder="메뉴 소개"
            onBlur={infoHandler}
          />
          {required && <ErrorInform message={'메뉴 소개는 필수 입력입니다'} />}
          <TextField
            inputRef={menuCostRef}
            id="outlined-number"
            label="가격"
            type="number"
            placeholder="가격 - 숫자만 입력해주세요."
          />
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

      <Dialog open={오늘의메뉴} onClose={handle오늘의메뉴Close}>
        <DialogTitle>오늘의메뉴 등록</DialogTitle>
        <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: '1vh' }}>
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

          <TextField disabled id="outlined-required" label="오늘의메뉴명은 고정입니다." />
          <TextField
            inputRef={menuDetailsRef}
            disabled
            id="outlined-required2"
            label="오늘의 메뉴명과 메뉴 정보는 바꿀 수 없습니다"
          />
          <TextField
            inputRef={menuCostRef}
            id="outlined-number"
            label="가격"
            type="number"
            placeholder="가격"
          />
        </DialogContent>
        <DialogActions>
          <Button sx={NanumFontStyle} onClick={오늘의메뉴Add}>
            등록
          </Button>
          <Button sx={NanumFontStyle} color="error" onClick={handle오늘의메뉴Close}>
            닫기
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog open={updateMenu} onClose={handleUpdateClose}>
        <DialogTitle sx={{ ...NanumFontStyle }}>메뉴 수정</DialogTitle>
        <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: '1vh' }}>
          {updateMenu === true ? (
            <div style={{ display: 'flex', alignItems: 'center' }}>
              <img
                width="210"
                height="120"
                src={'http://kjj.kjj.r-e.kr:8080/api/image?dir=' + update.image}
              />
              <input
                style={{ marginLeft: '2vw' }}
                type="file"
                accept="image/*"
                onChange={(e) => setImage(e.target.files[0])}
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
            placeholder={update?.name}
          />

          {nameDuplicate && <ErrorInform message={'중복된 메뉴명이 있습니다'} />}

          <TextField
            inputRef={menuDetailsRef}
            required
            id="outlined-required2"
            placeholder={update?.details}
          />
          <TextField
            inputRef={menuCostRef}
            placeholder={update?.cost}
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

      <Dialog open={식재료open} onClose={handle식재료Close}>
        <DialogTitle>식재료 등록</DialogTitle>
        <DialogContent
          sx={{
            display: 'flex',
            flexDirection: 'column',
            gap: '1vh',
            overflow: 'auto',
            height: '40vh',
          }}
        >
          <DialogContentText>식재료무게는 KG단위로 등록해주세요.</DialogContentText>
          {inputfields.map((field, index) => (
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
          <Button onClick={handleAddFields}>식재료 추가</Button>
          <Button onClick={() => 식재료add(식재료?.id)}>등록</Button>
          <Button color="error" onClick={handle식재료Close}>
            닫기
          </Button>
        </DialogActions>
      </Dialog>

      <Snackbar open={success} autoHideDuration={6000} onClose={handleSuccessClose}>
        <Alert onClose={handleSuccessClose} severity="success" sx={{ width: '100%' }}>
          success
        </Alert>
      </Snackbar>
    </ThemeProvider>
  );
}

const Alert = React.forwardRef(function Alert(props, ref) {
  return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

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
    font-family: NotoSans;
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
  fontFamily: 'NotoSans',
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
