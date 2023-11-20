import { Link } from 'react-router-dom';
import styled from 'styled-components';
import { FaGithub } from 'react-icons/fa';
import { flexCenter, flexICenter } from '../../styles/global.style';

const Developer = (user) => {
  const { imgsrc, name, github, position } = user;

  return (
    <DeveloperLayout>
      <img src={imgsrc} alt="이미지없음" />
      <DeveloperRow>
        <DeveloperName>{name}</DeveloperName>
        <DeveloperGithub>
          <Link to={github} target="_blank">
            <FaGithub />
          </Link>
        </DeveloperGithub>
      </DeveloperRow>
      <DeveloperInfo>{position}</DeveloperInfo>
    </DeveloperLayout>
  );
};

export default Developer;

const DeveloperLayout = styled.div`
  width: 30%;
  min-height: 150px;
  ${flexCenter};
  flex-direction: column;

  img {
    width: 130px;
    height: 130px;
    border-radius: 50%;
  }
`;

const DeveloperRow = styled.div`
  width: 90%;
  ${flexICenter};
  justify-content: space-between;

  a {
    color: black;
  }
`;

const DeveloperName = styled.span`
  font-size: 20px;
  font-weight: 600;
`;

const DeveloperInfo = styled.span`
  width: 90%;
  margin-top: -10px;

  font-size: 14px;
  font-weight: 500;
`;

const DeveloperGithub = styled.span`
  font-size: 24px;
`;
