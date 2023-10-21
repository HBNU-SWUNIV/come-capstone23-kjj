import { useSetRecoilState } from 'recoil';
import { isloginAtom } from '../../atom/loginAtom';
import { useKeycloak } from '@react-keycloak/web';
import { useCookies } from 'react-cookie';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';

const UseNav = () => {
  // logout
  const setIslogin = useSetRecoilState(isloginAtom);
  const { keycloak } = useKeycloak();
  const [cookies, setCookie] = useCookies();
  const navigate = useNavigate();

  const onLogout = () => {
    if (keycloak.authenticated) keycloak.logout();
    else setIslogin(false);

    setCookie('accesstoken', '');
    setCookie('refreshtoken', '');
    navigate('/');
  };

  // updates
  const [anchorEl, setAnchorEl] = useState(null);
  const usermodalOpen = Boolean(anchorEl);

  const openUsermenuModal = (e) => {
    setAnchorEl(e.currentTarget);
  };

  const closeUsermenuModal = () => {
    setAnchorEl(null);
  };

  const [updateforms, setUpdateforms] = useState({
    name: false,
    info: false,
    image: false,
  });

  const openUpdateModal = (event) => {
    setUpdateforms((prev) => ({
      ...prev,
      [event.currentTarget.htmlFor]: true,
    }));
  };

  const closeAllUpdateModals = () => {
    setUpdateforms((prev) => ({
      ...prev,
      name: false,
      info: false,
      image: false,
    }));
    closeUsermenuModal();
  };

  return {
    onLogout,
    openUpdateModal,
    closeAllUpdateModals,
    updateforms,
    openUsermenuModal,
    closeUsermenuModal,
    usermodalOpen,
    anchorEl,
  };
};

export default UseNav;
