import { ConfigWithToken } from '../auth/authConfig';
import { useQuery } from 'react-query';

const UseGetAxios = ({ name, api }) => {
  const config = ConfigWithToken();

  const { data, isLoading } = useQuery([name, config], () => api(config));

  return { data, isLoading };
};
export default UseGetAxios;
