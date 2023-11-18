import axios from 'axios';
import { useEffect, useRef, useState } from 'react';
import { useQuery } from 'react-query';
import { getMarketDetails } from '../api/apis';
import { ConfigWithToken, ManagerBaseApi } from '../utils/utils';

const UseNavApi = (closeFn) => {
  const config = ConfigWithToken();
  const formdataConfig = {
    headers: {
      'Content-Type': 'multipart/form-data',
      ...config.headers,
    },
  };
  const [newImage, setNewImage] = useState([]);
  const [isName, setIsName] = useState(false);
  const nameRef = useRef('');
  const InfoRef = useRef('');

  const [form, setForm] = useState({
    name: '',
    info: '',
    image: '',
  });
  const {
    data: marketDetails,
    refetch: refetchmarketDetails,
    isLoading,
  } = useQuery(['getmarket', config], () => getMarketDetails(config));

  const updateMarketName = () => {
    const body = {
      name: nameRef.current.value,
    };

    axios.patch(`${ManagerBaseApi}/store/title`, body, config).then((res) => {
      if (res.status === 200) {
        refetchmarketDetails();
        closeFn();
      }
    });
  };

  const updateMarketInfo = () => {
    const body = {
      info: InfoRef.current.value,
    };

    axios.patch(`${ManagerBaseApi}/store/info`, body, config).then((res) => {
      if (res.status === 200) {
        refetchmarketDetails();
        closeFn();
      }
    });
  };

  const updateMarketImage = () => {
    const formdata = new FormData();
    newImage !== null && formdata.append('file', newImage);

    axios({
      method: 'POST',
      url: `${ManagerBaseApi}/image`,
      data: formdata,
      ...formdataConfig,
    }).then((res) => {
      if (res.status === 200) refetchmarketDetails();
    });
    closeFn();
  };

  useEffect(() => {
    setForm({
      name: marketDetails?.name,
      info: marketDetails?.info,
      image: marketDetails?.image,
    });
  }, [isLoading, marketDetails]);

  useEffect(() => {
    if (form?.name !== '') setIsName(true);
    else if (form?.name === '') setIsName(false);
  }, [form?.name]);

  return {
    form: !isLoading && form,
    marketDetails: !isLoading && marketDetails,
    refetchmarketDetails,
    isName,
    nameRef,
    InfoRef,
    updateMarketName,
    updateMarketInfo,
    updateMarketImage,
    setNewImage,
  };
};

export default UseNavApi;
