import React from 'react';
import { styled } from 'styled-components';

const Wrapper = styled.div`
  width: 25px;
  height: 22px;
  border-radius: 50%;
  background-color: ${({ $color }) => ($color === 'red' ? '#FCACAC' : '#a3f394')};
`;

const Circle = (props) => {
  console.log(props.color);
  return <Wrapper $color={props.color} />;
};

export default Circle;
