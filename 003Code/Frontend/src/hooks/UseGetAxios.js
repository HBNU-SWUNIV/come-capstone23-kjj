import { useQuery } from 'react-query';
import { ConfigWithToken } from '../utils/utils';

const UseGetAxios = ({ name, api }) => {
  const config = ConfigWithToken();

  const { data, isLoading } = useQuery([name, config], () => api(config));

  return { data, isLoading };
};
export default UseGetAxios;
