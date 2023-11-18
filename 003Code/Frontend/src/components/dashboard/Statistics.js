import styled from '@emotion/styled';
import { Tooltip } from '@mui/material';
import {
  flexCenter,
  flexColumn,
  flexICenter,
  flexJBetween,
  fullSize,
} from '../../styles/global.style';

const StatisticsLayout = styled.div`
  width: 95%;
  height: 100%;
  ${flexICenter};
  background-color: white;
`;

const ContentBox = styled.div`
  ${fullSize};
  ${flexJBetween};
  border: 1px solid rgb(0, 0, 0, 0.1);
  border-radius: 5px;
  box-sizing: border-box;
  padding: 20px;
  position: relative;
`;

const ContentLeft = styled.div`
  width: 45%;
  height: 100%;
  ${flexColumn};
  justify-content: space-around;

  white-space: nowrap;

  span:first-child {
    font-size: 16px;
    font-weight: 600;
  }

  span:nth-child(2) {
    font-size: 22px;
    font-weight: 600;
  }
`;

const ContentRight = styled.div`
  height: 100%;
  width: 50%;
  position: relative;
`;

const Image = styled.div`
  position: absolute;
  bottom: -20px;
  right: -20px;
`;

const Label = styled.div`
  width: 40px;
  ${flexCenter};
  border-radius: 5px;
  background-color: rgb(71, 130, 218);
  position: absolute;
  top: 0;
  right: 0;

  font-size: 13px;
  color: white;
`;

const StatisticsValue = styled.div`
  ${flexICenter};
  gap: 10px;

  div {
    height: 22px;
    ${flexCenter};
    border-radius: 5px;
    background: ${({ $chipColor }) =>
      $chipColor ? 'rgba(244, 67, 54, 0.1)' : 'rgba(76, 175, 80, 0.1)'};

    font-size: 12px;
    font-weight: 600;
    color: ${({ $chipColor }) => ($chipColor ? 'rgb(244, 67, 54)' : 'rgb(76, 175, 80)')};
  }

  p {
    color: rgb(0, 0, 0, 0.6);
    font-size: 12px;
    font-weight: 500;
  }
`;

const Statistics = (props) => {
  return (
    <StatisticsLayout>
      <ContentBox>
        <ContentLeft>
          <span>{props.title}</span>
          <Tooltip title={props.tip}>
            <span>{props.data}</span>
          </Tooltip>

          <StatisticsValue $chipColor={props.chipcolor}>
            <div>{props.chip}</div>
            <p>{props.isWeek ? 'Since last week' : 'Since yesterday'}</p>
          </StatisticsValue>
        </ContentLeft>

        <ContentRight>
          <Image>{props.icon}</Image>
          <Label>{props.date}</Label>
        </ContentRight>
      </ContentBox>
    </StatisticsLayout>
  );
};

export default Statistics;
