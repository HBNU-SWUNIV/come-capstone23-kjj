import styled from '@emotion/styled';

const Wrapper = styled.div`
  width: 95%;
  height: 100%;

  display: flex;
  align-items: center;
  background-color: white;
`;

const ContentWrapper = styled.div`
  width: 100%;
  height: 100%;

  border-radius: 5px;

  display: flex;
  justify-content: space-between;

  box-sizing: border-box;
  padding: 20px;

  border: 1px solid rgb(0, 0, 0, 0.1);
  position: relative;
`;

const Left = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  width: 45%;
  height: 100%;

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

const Right = styled.div`
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
  position: absolute;
  top: 0;
  right: 0;

  width: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 5px;
  font-size: 13px;

  background-color: rgb(71, 130, 218);
  color: white;
`;

const LastText = styled.div`
  display: flex;
  align-items: center;
  gap: 10px;

  div {
    background: ${({ $chipColor }) =>
      $chipColor ? 'rgba(244, 67, 54, 0.1)' : 'rgba(76, 175, 80, 0.1)'};
    height: 22px;
    border-radius: 5px;

    display: flex;
    justify-content: center;
    align-items: center;

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
    <Wrapper>
      <ContentWrapper>
        <Left>
          <span>{props.title}</span>
          <span>{props.data}</span>

          <LastText $chipColor={props.chipcolor}>
            <div>{props.chip}</div>
            <p>{props.isWeek ? 'Since last week' : 'Since yesterday'}</p>
          </LastText>
        </Left>

        <Right>
          <Image>{props.icon}</Image>
          <Label>{props.date}</Label>
        </Right>
      </ContentWrapper>
    </Wrapper>
  );
};

export default Statistics;
