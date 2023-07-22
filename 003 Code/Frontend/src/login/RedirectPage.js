import React, { useEffect, useState } from 'react';
import axios from 'axios';

const RedirectPage = () => {
  const [data, setData] = useState([]);
  const [data2, setData2] = useState([]);
  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const code = urlParams.get('code');
    console.log(data, data2);
    if (code) {
      axios
        .post('/api/user/login/keycloak', { code })
        .then((response) => {
          // Access Token과 Refresh Token 사용하기
          const { accessToken, refreshToken } = response.data;
          setData(accessToken);
          setData2(refreshToken);
        })
        .catch((error) => {
          console.error('Error:', error);
        });
    }
  }, []);

  return (
    <div>
      <h1>Redirect Page</h1>
    </div>
  );
};

export default RedirectPage;
