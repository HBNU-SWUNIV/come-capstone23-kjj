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
import Copyright from '../components/general/Copyright';
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
import DailyMenu from './DailyMenu';

const Alert = React.forwardRef(function Alert(props, ref) {
  return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

const defaultTheme = createTheme();

export default function Menus() {
  const navigate = useNavigate();
  const [onDelete, SetonDelete] = useState(false);
  const [deleteID, setDeleteId] = useState(0);
  const [addMenu, setAddMenu] = useState(false);
  const [menus, setMenus] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [imagesrc, setImagesrc] = useState('null');
  const [image, setImage] = useState([]);
  const [success, setSuccess] = useState(false);
  const [update, setUpdate] = useState(null);
  const [updateMenu, setUpdatemenu] = useState(false);
  const [오늘의메뉴, set일품] = useState(false);
  const [isplanner, setIsplanner] = useState(false);
  const [식재료open, set식재료open] = useState(false);
  const [식재료, set식재료] = useState(null);
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
    const fetchData = async () => {
      try {
        const response = await axios.get(`${ManagerBaseApi}/menu`, config);
        setMenus(response.data);

        if (response.data.length !== 0) {
          setTimeout(() => setIsLoading(false), 200);
        }

        axios
          .get(`${ManagerBaseApi}/menu/planner`, config)
          .then((res) => setIsplanner(res.data));
      } catch (err) {
        if (err.response.status === 403) {
          alert('다시 로그인 해주세요🌝');
          navigate('/');
        }
      }
    };

    fetchData();
  }, []);

  const handleDeleteOpen = (id) => {
    setDeleteId(id);
    SetonDelete(true);
  };
  const handleDeleteClose = () => {
    SetonDelete(false);
  };
  const handleAddOpen = () => {
    setAddMenu(true);
  };
  const handleAddClose = () => {
    setAddMenu(false);
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
  };
  const handle일품Open = () => {
    set일품(true);
  };
  const handle오늘의메뉴Close = () => {
    set일품(false);
  };
  const handle식재료Open = (menu, id) => {
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
          axios
            .patch(`${ManagerBaseApi}/menu/${id}/food`, body, config)
            .then((res) => console.log(res));
        }
      })
      .catch((err) => {
        if (err.response.status === 400) {
          axios
            .post(`${ManagerBaseApi}/menu/${id}/food`, body, config)
            .then((res) => console.log(res))
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
      .then(() => {
        axios
          .get(`${ManagerBaseApi}/menu/planner`, config)
          .then((res) => setIsplanner(res.data));
      });
    SetonDelete(false);
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
        axios.get(`${ManagerBaseApi}/menu`, config).then((res) => setMenus(res.data));
      })
      .catch((err) => {
        if (err.response.status === 400) {
          alert('파일의 용량이 너무큽니다.');
          return;
        } else if (err.response.status === 409) {
          alert('중복된 메뉴명이 있습니다.');
          return;
        }
      });
    setAddMenu(false);
    setImage(null);
  };
  const menuUpdate = () => {
    const formdata = new FormData();
    if (
      menus
        .filter((f_menu) => f_menu.id != update.id)
        .filter((n) => n.name === menuNameRef.current.value).length != 0
    ) {
      alert('중복된 메뉴명입니다.');
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
    })
      .then(() => {
        axios.get(`${ManagerBaseApi}/menu`, config).then((res) => setMenus(res.data));
      })
      .then(() => {
        axios
          .get(`${ManagerBaseApi}/menu/planner`, config)
          .then((res) => setIsplanner(res.data));
      });
    setImage('');
    handleUpdateClose();
  };
  const 오늘의메뉴Add = () => {
    const formdata = new FormData();
    let body = {
      name: '오늘의메뉴',
      cost: menuCostRef.current.value,
      details: menuDetailsRef.current.value,
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
      .then(() => {
        axios
          .get(`${ManagerBaseApi}/menu/planner`, config)
          .then((res) => setIsplanner(res.data));
      })
      .catch((err) => {
        if (err.response.status === 400) {
          alert('이미지 용량이 너무 크거나 입력하지 않은 필드가 있습니다.');
          return;
        }
      });
    set일품(false);
    setImage(null);
  };

  return (
    <ThemeProvider theme={defaultTheme}>
      <Box sx={{ display: 'flex' }}>
        <CssBaseline />
        <Drawerheader pages={'메뉴'} />

        <Box
          component="main"
          sx={{
            display: 'flex',
            flexDirection: 'column',
            backgroundColor: (theme) =>
              theme.palette.mode === 'light'
                ? theme.palette.grey[100]
                : theme.palette.grey[900],
            flexGrow: 1,
            height: '100%',
            overflow: 'auto',
            display: 'flex',
          }}
        >
          <Toolbar />
          <Container maxWidth="md" sx={{ mt: 4, mb: 4 }}>
            <Typography
              sx={{ whiteSpace: 'nowrap', marginLeft: '-1rem' }}
              variant="h4"
              color="#0288d1"
              align="start"
              paragraph
            >
              매일 매일 바뀌는 기본 메뉴가 있다면 오늘의메뉴로 등록해보세요.
            </Typography>
            <Typography
              sx={{ whiteSpace: 'nowrap', marginTop: '-0.5rem' }}
              variant="h5"
              align="start"
              color="text.error"
              paragraph
            >
              오늘의메뉴로 등록된 메뉴는 오늘의메뉴 페이지에서 요일별 식단표를 추가할 수
              있습니다.
            </Typography>
            <Stack sx={{ pt: 4 }} direction="row" spacing={2} justifyContent="center">
              <Button onClick={handleAddOpen} variant="contained">
                메뉴 등록
              </Button>
              {isplanner === true ? (
                <Button disabled onClick={handle일품Open} variant="outlined">
                  오늘의메뉴가 등록되어있습니다.
                </Button>
              ) : (
                <Button onClick={handle일품Open} variant="outlined">
                  오늘의메뉴 등록
                </Button>
              )}
            </Stack>
          </Container>

          <Container sx={{ py: 1 }} maxWidth="md">
            <Grid container spacing={4}>
              {menus.map((menu) => (
                <Grid item key={shortid.generate()} xs={12} sm={6} md={4}>
                  <Card
                    sx={{
                      height: '100%',
                      display: 'flex',
                      flexDirection: 'column',
                    }}
                  >
                    {!isLoading ? (
                      <CardMedia
                        component="div"
                        sx={{
                          opacity: menu.sold === true ? null : 0.3,
                          pt: '56.25%',
                        }}
                        image={'http://kjj.kjj.r-e.kr:8080/api/image?dir=' + menu?.image}
                      />
                    ) : (
                      <Skeleton variant="rectangular" sx={{ pt: '56.25%' }} />
                    )}
                    <CardContent sx={{ flexGrow: 1 }}>
                      <Typography
                        sx={{ opacity: menu.sold === true ? null : 0.3 }}
                        color={menu.usePlanner === true ? 'primary.dark' : 'inherit'}
                        gutterBottom
                        variant="h5"
                        component="h2"
                      >
                        {!isLoading ? menu.name : <Skeleton />}
                      </Typography>
                      {menu.sold === true ? (
                        <>
                          <Typography variant="body2">
                            {!isLoading ? menu.details : <Skeleton />}
                          </Typography>
                          <Typography>
                            {!isLoading ? menu.cost + '원' : <Skeleton />}
                          </Typography>
                        </>
                      ) : (
                        <>
                          <Typography variant="h4" color="error.dark">
                            {!isLoading ? '품 절 되었어요' : <Skeleton />}
                          </Typography>
                        </>
                      )}
                    </CardContent>

                    <CardActions
                      sx={{
                        display: 'flex',
                        justifyContent: 'center',
                      }}
                    >
                      {menu.sold === true ? (
                        <>
                          <Button
                            onClick={() => handleUpdateOpen(menu)}
                            sx={{ marginRight: '-1vw' }}
                            size="small"
                          >
                            수정
                          </Button>
                          <Button onClick={() => soldout(menu.id)} size="small">
                            품절
                          </Button>
                          {menu.usePlanner === true ? (
                            <Button
                              disabled
                              onClick={() => handle식재료Open(menu)}
                              sx={{ whiteSpace: 'nowrap' }}
                              size="small"
                            >
                              식재료 등록
                            </Button>
                          ) : (
                            <Button
                              onClick={() => handle식재료Open(menu, menu.id)}
                              sx={{ whiteSpace: 'nowrap' }}
                              size="small"
                            >
                              식재료 등록
                            </Button>
                          )}
                          <Button
                            onClick={() => handleDeleteOpen(menu.id)}
                            color="error"
                            size="small"
                          >
                            삭제
                          </Button>
                        </>
                      ) : (
                        <>
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
                        </>
                      )}
                    </CardActions>
                  </Card>
                </Grid>
              ))}
            </Grid>
            <Copyright sx={{ pt: 4 }} />
          </Container>
          {/* <DailyMenu /> */}
        </Box>
      </Box>

      <Dialog
        open={onDelete}
        onClose={handleDeleteClose}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
      >
        <DialogTitle id="alert-dialog-title">{'삭제 확인'}</DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description">
            정말 삭제하시겠습니까?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button color="error" onClick={menuDelete}>
            네
          </Button>
          <Button onClick={handleDeleteClose} autoFocus>
            아니요
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog open={addMenu} onClose={handleAddClose}>
        <DialogTitle>메뉴 등록</DialogTitle>
        <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: '1vh' }}>
          <DialogContentText>
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
          <TextField
            inputRef={menuDetailsRef}
            required
            id="outlined-required2"
            label="required"
            placeholder="메뉴 소개"
          />
          <TextField
            inputRef={menuCostRef}
            id="outlined-number"
            label="가격"
            type="number"
            placeholder="가격 - 숫자만 입력해주세요."
          />
        </DialogContent>
        <DialogActions>
          <Button color="error" onClick={handleAddClose}>
            닫기
          </Button>
          <Button onClick={menuAdd}>등록</Button>
        </DialogActions>
      </Dialog>

      <Dialog open={오늘의메뉴} onClose={handle오늘의메뉴Close}>
        <DialogTitle>오늘의메뉴 등록</DialogTitle>
        <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: '1vh' }}>
          <DialogContentText>
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
            required
            id="outlined-required2"
            label="required"
            placeholder="메뉴 소개"
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
          <Button color="error" onClick={handle오늘의메뉴Close}>
            닫기
          </Button>
          <Button onClick={오늘의메뉴Add}>등록</Button>
        </DialogActions>
      </Dialog>

      <Dialog open={updateMenu} onClose={handleUpdateClose}>
        <DialogTitle>메뉴 수정</DialogTitle>
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
              <DialogContentText>
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
          {update?.usePlanner === true ? (
            <TextField
              disabled
              inputRef={menuNameRef}
              id="outlined-required"
              label="오늘의메뉴"
            />
          ) : (
            <TextField
              required
              inputRef={menuNameRef}
              id="outlined-required"
              label="required"
              placeholder={update?.name}
            />
          )}

          <TextField
            inputRef={menuDetailsRef}
            required
            id="outlined-required2"
            label="required"
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
          <Button color="error" onClick={handleUpdateClose}>
            닫기
          </Button>
          <Button onClick={menuUpdate}>수정</Button>
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
          성공!
        </Alert>
      </Snackbar>
    </ThemeProvider>
  );
}
