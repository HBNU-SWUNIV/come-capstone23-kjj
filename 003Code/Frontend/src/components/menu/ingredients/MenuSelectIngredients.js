import Box from '@mui/material/Box';
import FormControl from '@mui/material/FormControl';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import Select from '@mui/material/Select';
import { useEffect, useState } from 'react';
import { getIngredientsInfo } from '../../../api/apis';
import UseGetAxios from '../../../hooks/UseGetAxios';

export default function MenuSelectIngredients({ setFn }) {
  const [food, setFood] = useState('');

  const { data: getFoods, isLoading } = UseGetAxios({
    name: 'getFoods',
    api: getIngredientsInfo,
  });

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
        <InputLabel id="demo-simple-select-label">식재료 선택</InputLabel>
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
