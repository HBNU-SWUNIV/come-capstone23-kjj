import * as React from 'react';
import { useTheme } from '@mui/material/styles';
import Title from './Title';
import ApexCharts from "react-apexcharts";
import axios from 'axios';
import { useState } from 'react';

export default function Chart() {
  const theme = useTheme();
  const [predictitems, setPredictItems] = useState([]);
  
  React.useEffect(()=>{
    axios.get(`/api/manager/state/predict/food`)
        .then(res => setPredictItems(res.data))
  })
  
  const predictItemsArray = Object.entries(predictitems);

  return (
    <React.Fragment>
      <Title>내일 식재료 예측</Title>
      <ApexCharts
                type="bar"
                series={ [
                    {
                        name:'',
                        data:predictItemsArray.map(items => items[1])
                    }
                ]}
                width={800}
                height={170}
                options={{
                    chart:{
                        toolbar:{show:false}
                    },
                    stroke:{
                        curve:'smooth',
                        width:1
                    },
                    xaxis:{
                        type:'category',
                        categories:predictItemsArray.map(items => items[0])
                    },
                    fill:{
                        colors:'black',
                        opacity:1
                    }
                }}
            />
    </React.Fragment>
  );
}
