import { useCallback, useState } from 'react';

const UseInput = () => {
  const [data, setData] = useState(new Map());

  const handleDatas = useCallback(
    (e) => {
      const { name, value } = e.target;

      setData((prev) => {
        return new Map(prev).set(name, value);
      });
    },
    [data]
  );

  return { data, handleDatas };
};

export default UseInput;
