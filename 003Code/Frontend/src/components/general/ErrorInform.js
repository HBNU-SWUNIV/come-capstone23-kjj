import { FaExclamationCircle } from 'react-icons/fa';
import React from 'react';
import { styled } from 'styled-components';

const ErrorInform = ({ message, color }) => {
  return (
    <ErrorWrapper $color={color}>
      <FaExclamationCircle />
      <span>{message}</span>
    </ErrorWrapper>
  );
};

export default ErrorInform;

const ErrorWrapper = styled.div`
  padding: 0 10px;

  width: 100%;
  height: 20px;
  display: flex;
  align-items: center;
  // color: rgb(217, 48, 37);

  color: ${({ $color }) => ($color ? 'green' : 'rgb(217, 48, 37)')};
  span {
    margin-left: 5px;
    font-weight: 600;
    font-size: 14px;
  }
`;
