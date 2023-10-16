import { Button, Dialog, DialogActions, DialogContent, DialogTitle } from '@mui/material';
import styled from 'styled-components';

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
            <Dayoff>{dayId}</Dayoff>
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
                  ...weightFontStyle,
                  fontSize: '16px',
                  color: customRed,
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
                      ...weightFontStyle,
                      fontSize: '16px',
                      color: customRed,
                      marginRight: '10px',
                    }}
                  >
                    '{hol?.dateName}' 입니다
                  </span>
                )
            )}

        {offday?.filter((offday) => offday.date == dayId)[0]?.off == true ? (
          <Button
            sx={weightFontStyle}
            onClick={() =>
              onOnday(dayId.slice(0, 4), dayId.slice(4, 6), dayId.slice(6, 8))
            }
          >
            영업일 등록
          </Button>
        ) : (
          <Button
            sx={weightFontStyle}
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

        <Button sx={weightFontStyle} color="error" onClick={onClose}>
          닫기
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default DayoffDialog;

const customRed = '#f44336';
const customGray = 'rgba(0,0,0,0.2)';
const DayoffActionStyle = {
  display: 'flex',
  justifyContent: 'center',
  alignItems: 'center',
};

const weightFontStyle = { fontWeight: 600 };

const DayoffTitle = styled.div`
  width: 100%;

  display: flex;
  justify-content: flex-start;
  align-items: center;

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
  display: flex;
  flex-direction: column;

  border-top: 2px solid black;

  div {
    display: flex;
    align-items: center;

    div:first-child {
      height: 100px;
      width: 150px;

      background-color: #e3f2fd;

      border-right: 1px solid ${customGray};
      border-left: 1px solid ${customGray};

      display: flex;
      justify-content: flex-start;
      align-items: center;

      box-sizing: border-box;
      padding-left: 20px;

      font-weight: 600;
    }
  }
`;

const DayoffHr = styled.div`
  width: 100%;
  height: 1px;
  background-color: ${customGray};
`;

const Dayoff = styled.div`
  width: 120px;
  height: 30px;

  margin-left: 20px;
  border-radius: 10px;

  display: flex;
  justify-content: center;
  align-items: center;

  background-color: ${customGray};
`;

const DayoffInput = styled.input`
  margin-left: 20px;

  width: 250px;
  height: 30px;

  border: 1px solid ${customGray};
`;
