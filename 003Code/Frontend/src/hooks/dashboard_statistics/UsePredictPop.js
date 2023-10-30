import { useQuery } from 'react-query';
import { ConfigWithToken } from '../../auth/authConfig';
import { getPredictUsers } from '../../api/apis';

const UsePredictPop = () => {
  const config = ConfigWithToken();

  const { data: predictpop, isLoading: predictpoploading } = useQuery(
    ['getpredictpop', config],
    () => getPredictUsers(config)
  );

  const calculatedpredictpop = predictpop?.tomorrow - predictpop?.today;

  return {
    predictpop: !predictpoploading && predictpop,
    calculatedpredictpop,
  };
};

export default UsePredictPop;
