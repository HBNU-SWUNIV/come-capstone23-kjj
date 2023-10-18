import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';

function createData(name, value) {
  return { name, value };
}

export default function MenuIngredientsTable(props) {
  const rows =
    Array.isArray(props.data) && props.data.map((a) => createData(a[0], a[1] + 'g'));
  return (
    <TableContainer component={Paper}>
      <Table aria-label="simple table">
        <TableHead>
          <TableRow>
            <TableCell>이름 </TableCell>
            <TableCell align="right">무게(1인분 기준)</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {Array.isArray(props.data) &&
            rows.map((row) => (
              <TableRow
                key={row.name}
                sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
              >
                <TableCell component="th" scope="row">
                  {row.name}
                </TableCell>
                <TableCell align="right">{row.value}</TableCell>
              </TableRow>
            ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}
