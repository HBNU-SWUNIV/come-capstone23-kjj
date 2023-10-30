import { useEffect, useState } from 'react';

const UseSize = () => {
  const [innerheight, setInnerheight] = useState(window.innerHeight);

  useEffect(() => {
    const handleResize = () => {
      setInnerheight(window.innerHeight);
    };

    window.addEventListener('resize', handleResize);

    return () => {
      window.removeEventListener('resize', handleResize);
    };
  }, []);

  return {
    innerheight,
  };
};

export default UseSize;
