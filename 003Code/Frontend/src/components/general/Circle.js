import React from 'react';
import { styled } from 'styled-components';

const Wrapper = styled.div`
  position: absolute;
  margin-top: 2px;
  width: 25px;
  height: 25px;
  border-radius: 50%;
  background-color: ${({ $color }) => ($color === 'red' ? '#FCACAC' : '#a3f394')};
`;

const Circle = (props) => {
  return <Wrapper $color={props.color} />;
};

export default Circle;
