import { styled } from 'styled-components';

const Wrapper = styled.div`
  width: 25px;
  height: 25px;
  position: absolute;
  margin-top: 2px;
  border-radius: 50%;
  background-color: ${({ $color }) => ($color === 'red' ? '#FCACAC' : '#a3f394')};
`;

const Circle = (props) => {
  return <Wrapper $color={props.color} />;
};

export default Circle;
