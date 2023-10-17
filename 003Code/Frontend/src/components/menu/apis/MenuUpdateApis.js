import { useMutation, useQuery } from 'react-query';
import { ConfigWithToken, ManagerBaseApi } from '../../../auth/authConfig';
import axios from 'axios';
import { getMenus } from '../../../api/apis';
import { useState } from 'react';

const MenuUpdateApis = () => {
  const [success, setSuccess] = useState({
    addmenus: false,
    isDuplicatedName: false,
  });

  const config = ConfigWithToken();
  const formdataConfig = {
    headers: {
      'Content-Type': 'multipart/form-data',
      ...config.headers,
    },
  };

  const { data: menus, refetch: refreshMenus } = useQuery(['getMenus', config], () =>
    getMenus(config)
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
        setSuccess((prev) => ({
          ...prev,
          addmenus: true,
        }));
      },
      onError: (err) => {
        if (err.response.status === 400) {
          alert('이미지 파일 용량이 너무 큽니다.');
          return;
        } else if (err.response.status === 409) {
          setSuccess((prev) => ({
            ...prev,
            isDuplicatedName: true,
          }));
          return;
        }
      },
    }
  );

  const deleteMenus = useMutation(
    (updateID) => axios.delete(`${ManagerBaseApi}/menu/${updateID}`, config),
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

  return {
    menus,
    refreshMenus,
    deleteMenus,
    soldoutMenus,
    resaleMenus,
    addMenus,
    success,
  };
};

export default MenuUpdateApis;
