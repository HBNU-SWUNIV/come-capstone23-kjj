import React from 'react';
import { Link } from 'react-router-dom';
import styled from 'styled-components';
import { FaGithub } from 'react-icons/fa';

const Developer = (user) => {
  const { imgsrc, name, github, position } = user;

  return (
    <DeveloperWrapper>
      <img src={imgsrc} alt="이미지없음" />
      <Contact>
        <DeveloperName>{name}</DeveloperName>
        <DeveloperGithub>
          <Link to={github} target="_blank">
            <FaGithub />
          </Link>
        </DeveloperGithub>
      </Contact>
      <DeveloperInfo>{position}</DeveloperInfo>
    </DeveloperWrapper>
  );
};

export default Developer;

const DeveloperWrapper = styled.div`
  width: 30%;
  min-height: 150px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  img {
    width: 130px;
    height: 130px;
    border-radius: 50%;
  }
`;

const Contact = styled.div`
  width: 90%;
  display: flex;
  align-items: center;
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
  font-size: 14px;
  font-weight: 500;
  margin-top: -10px;
`;

const DeveloperGithub = styled.span`
  font-size: 24px;
`;
