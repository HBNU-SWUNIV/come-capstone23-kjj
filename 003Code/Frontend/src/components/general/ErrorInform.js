import { FaExclamationCircle } from 'react-icons/fa';
import React from 'react';
import { styled } from 'styled-components';
import { flexICenter } from '../../styles/global.style';

const ErrorInform = ({ message, color }) => {
  return (
    <ErrorInformLayout $color={color}>
      <FaExclamationCircle />
      <span>{message}</span>
    </ErrorInformLayout>
  );
};

export default ErrorInform;

const ErrorInformLayout = styled.div`
  width: 100%;
  height: 20px;
  ${flexICenter};
  padding: 0 10px;

  color: ${({ $color }) => ($color ? 'green' : 'rgb(217, 48, 37)')};

  span {
    margin-left: 5px;

    font-weight: 600;
    font-size: 14px;
  }
`;
