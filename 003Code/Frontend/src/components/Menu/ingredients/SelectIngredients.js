import Box from '@mui/material/Box';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';
import { useEffect, useState } from 'react';
import { ConfigWithToken } from '../../../auth/authConfig';
import { useQuery } from 'react-query';
import { getIngredientsInfo } from '../../../api/apis';

export default function SelectIngredients({ setFn }) {
  const [food, setFood] = useState('');

  const config = ConfigWithToken();
  const { data: getFoods, isLoading } = useQuery(['getFoods', config], () =>
    getIngredientsInfo(config)
  );

  const handleChange = (event) => {
    setFood(event.target.value);
  };

  const selectedFoodId =
    !isLoading && getFoods.filter((item) => item.name == food)[0]?.id;
  useEffect(() => {
    setFn(selectedFoodId);
  }, [food]);

  return (
    <Box sx={{ minWidth: 120 }}>
      <FormControl fullWidth>
        <InputLabel id="demo-simple-select-label">식재료 등록</InputLabel>
        <Select
          labelId="demo-simple-select-label"
          id="demo-simple-select"
          value={food}
          label="식재료"
          onChange={handleChange}
        >
          {!isLoading &&
            getFoods?.map((item, idx) => (
              <MenuItem key={idx} value={item.name}>
                {item.name}
              </MenuItem>
            ))}
        </Select>
      </FormControl>
    </Box>
  );
}
