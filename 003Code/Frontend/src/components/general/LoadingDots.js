import { keyframes, styled } from 'styled-components';
import { flexCenter } from '../../styles/global.style';

const LoadingDots = (props) => {
  return (
    <LoadingDotsLayout>
      {[1, 2, 3].map((item, idx) => (
        <Dots key={idx} $isGray={props.isGray} />
      ))}
    </LoadingDotsLayout>
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

const LoadingDotsLayout = styled.div`
  width: 50px;
  height: 30px;
  ${flexCenter};
  gap: 5px;

  div:nth-child(1) {
    animation: ${puls} 0.3s ease 0s infinite alternate;
  }

  div:nth-child(2) {
    animation: ${puls} 0.3s ease 0.2s infinite alternate;
  }

  div:nth-child(3) {
    animation: ${puls} 0.3s ease 0.4s infinite alternate;
  }
`;

const Dots = styled.div`
  width: 5px;
  height: 5px;
  border-radius: 2.5px;
  background-color: ${({ $isGray }) => ($isGray ? 'gray' : 'white')};
`;
