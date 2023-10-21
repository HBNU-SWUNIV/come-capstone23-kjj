import { useQuery } from 'react-query';
import { getMarketDetails, getMarketImage } from './apis';
import { ConfigWithToken } from '../auth/authConfig';

const Api_nav = () => {
  const config = ConfigWithToken();

  const {
    data: marketDetails,
    refetch: refetchmarketDetails,
    isLoading,
  } = useQuery(['getmarket', config], () => getMarketDetails(config));

  return {
    marketDetails: !isLoading && marketDetails,
    refetchmarketDetails,
    isLoading,
  };
};

export default Api_nav;
