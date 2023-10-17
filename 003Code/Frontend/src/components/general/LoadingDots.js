import React from 'react';
import { keyframes, styled } from 'styled-components';

const LoadingDots = (props) => {
  return (
    <DotsWrapper>
      {[1, 2, 3].map((item, idx) => (
        <Dots key={idx} $isGray={props.isGray} />
      ))}
    </DotsWrapper>
  );
};

export default LoadingDots;

const puls = keyframes`
    from{
        opacity:1;
        transform: scale(1);
    }
    to{
        opacity:0.3;
        transform: scale(0.6);
    }
`;

const DotsWrapper = styled.div`
  width: 50px;
  height: 30px;

  display: flex;
  justify-content: center;
  align-items: center;

  gap: 5px;

  div:first-child {
    animation: ${puls} 0.3s ease 0s infinite alternate;
  }

  div:nth-child(2) {
    animation: ${puls} 0.3s ease 0.2s infinite alternate;
  }

  div:last-child {
    animation: ${puls} 0.3s ease 0.4s infinite alternate;
  }
`;

const Dots = styled.div`
  width: 5px;
  height: 5px;

  border-radius: 2.5px;
  background-color: ${({ $isGray }) => ($isGray ? 'gray' : 'white')};
`;
