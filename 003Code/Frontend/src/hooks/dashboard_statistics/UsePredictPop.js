import { useQuery } from 'react-query';
import { getPredictUsers } from '../../api/apis';
import { ConfigWithToken } from '../../utils/utils';

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
