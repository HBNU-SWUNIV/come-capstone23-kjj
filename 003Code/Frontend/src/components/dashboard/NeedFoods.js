import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from '@mui/material';
import styled from 'styled-components';
import UseGetCharts from '../../hooks/UseGetCharts';
import Title from '../general/Title';

const NeedFoods = () => {
  const { predictfoodsArray } = UseGetCharts();

  return (
    <>
      <TableName>
        <Title>익일 필요 식재료</Title>
      </TableName>

      <TableContainer component={Paper} sx={{ overflow: 'scroll', maxHeight: '400px' }}>
        <Table aria-label="simple table">
          <TableHead>
            <TableRow>
              <TableCell sx={tableheadStyle}>이름</TableCell>
              <TableCell sx={tableheadStyle} align="right">
                무게
              </TableCell>
            </TableRow>
          </TableHead>

          <TableBody>
            {predictfoodsArray?.map((item, idx) => (
              <TableRow key={idx}>
                <TableCell component="th" scope="row">
                  {item[0]}
                </TableCell>
                <TableCell align="right">{item[1]}g</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </>
  );
};

export default NeedFoods;

const TableName = styled.div`
  margin-bottom: 20px;
`;

const tableheadStyle = {
  fontWeight: 600,
  fontSize: '16px',
};
