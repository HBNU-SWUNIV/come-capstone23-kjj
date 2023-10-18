import axios from 'axios';

export const reIssuetoken = async (setCookieFn, config) => {
  const expirationDate = new Date();
  expirationDate.setTime(expirationDate.getTime() + 14 * 60 * 1000);
  try {
    const response = await axios({
      method: 'POST',
      url: '/api/user/login/refresh',
      ...config,
    });
    if (response.headers.authorization !== '')
      setCookieFn('accesstoken', response.headers.authorization, {
        expires: expirationDate,
      });
  } catch (err) {
    console.error('reissueToken_Error =', err);
  }
};
