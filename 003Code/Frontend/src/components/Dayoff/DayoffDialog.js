import { Button, Dialog, DialogActions, DialogContent, DialogTitle } from '@mui/material';
import styled from 'styled-components';
import { flexCenter, flexColumn, flexICenter } from '../../styles/global.style';

const DayoffDialog = ({
  open,
  onClose,
  dayId,
  offNameRef,
  isArray,
  holiday,
  offday,
  onOnday,
  onOffday,
}) => {
  const isOffday = offday?.filter((offday) => offday.date == dayId)[0]?.off == true;
  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle sx={{ width: '600px' }}>
        <DayoffTitle>
          <span>휴일등록</span>
          <span>우리 식당의 휴일을 지정합니다.</span>
        </DayoffTitle>
      </DialogTitle>

      <DialogContent>
        <DayoffContent>
          <div>
            <div>선택일자</div>
            <DayoffDate>{dayId}</DayoffDate>
          </div>
          <DayoffHr />
          <div>
            <div>휴일명</div>
            <DayoffInput ref={offNameRef} placeholder="휴일 명칭을 입력하세요" />
          </div>
          <DayoffHr />
        </DayoffContent>
      </DialogContent>

      <DialogActions sx={DayoffActionStyle}>
        {!isArray
          ? holiday?.locdate == dayId && (
              <span
                style={{
                  fontWeight: 600,
                  fontSize: '16px',
                  color: (props) => props.theme.colors.red,
                  marginRight: '10px',
                }}
              >
                '{holiday?.dateName}' 입니다
              </span>
            )
          : holiday?.map(
              (hol) =>
                hol?.locdate == dayId && (
                  <span
                    style={{
                      fontWeight: 600,
                      fontSize: '16px',
                      color: (props) => props.theme.colors.red,
                      marginRight: '10px',
                    }}
                  >
                    '{hol?.dateName}' 입니다
                  </span>
                )
            )}

        {isOffday ? (
          <Button
            sx={{ fontWeight: 600 }}
            onClick={() =>
              onOnday(dayId.slice(0, 4), dayId.slice(4, 6), dayId.slice(6, 8))
            }
          >
            영업일 등록
          </Button>
        ) : (
          <Button
            sx={{ fontWeight: 600 }}
            disabled={
              (!isArray && holiday?.locdate == dayId) ||
              (isArray && holiday.some((hol) => hol?.locdate == dayId))
            }
            onClick={() =>
              onOffday(dayId.slice(0, 4), dayId.slice(4, 6), dayId.slice(6, 8))
            }
          >
            휴일 등록
          </Button>
        )}

        <Button sx={{ fontWeight: 600 }} color="error" onClick={onClose}>
          닫기
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default DayoffDialog;

const DayoffActionStyle = {
  display: 'flex',
  justifyContent: 'center',
  alignItems: 'center',
};

const DayoffTitle = styled.div`
  width: 100%;
  ${flexICenter};
  justify-content: flex-start;

  span:first-child {
    font-size: 22px;
    font-weight: 600;
  }

  span:last-child {
    margin-left: 15px;

    font-size: 16px;
    color: gray;
  }
`;

const DayoffContent = styled.div`
  ${flexColumn};
  border-top: 2px solid black;

  div {
    ${flexICenter};

    div:first-child {
      width: 150px;
      height: 100px;
      ${flexICenter};
      justify-content: flex-start;
      box-sizing: border-box;
      padding-left: 20px;
      border-right: 1px solid ${(props) => props.theme.colors.gray};
      border-left: 1px solid ${(props) => props.theme.colors.gray};
      background-color: #e3f2fd;

      font-weight: 600;
    }
  }
`;

const DayoffHr = styled.div`
  width: 100%;
  height: 1px;
  background-color: ${(props) => props.theme.colors.gray};
`;

const DayoffDate = styled.div`
  width: 120px;
  height: 30px;
  ${flexCenter};
  margin-left: 20px;
  border-radius: 10px;
  background-color: ${(props) => props.theme.colors.gray};
`;

const DayoffInput = styled.input`
  width: 250px;
  height: 30px;
  margin-left: 20px;
  border: 1px solid ${(props) => props.theme.colors.gray};
`;
