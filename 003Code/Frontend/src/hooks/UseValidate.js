import { useState } from 'react';

const UseValidate = ({ data, rules }) => {
  const [error, setError] = useState(new Map());

  const validateNull = (e) => {
    const { name } = e.target;

    if (data.get(name) == '' || data.get(name) == undefined)
      setError((prev) => {
        return new Map(prev).set(name, true);
      });
    else if (data.get(name) !== '')
      setError((prev) => {
        return new Map(prev).set(name, false);
      });
  };

  const validateWithRules = (e) => {
    const { name, value } = e.target;

    const validateFunc = rules[name];
    const error = validateFunc(value);

    setError((prev) => {
      return new Map(prev).set(name, error);
    });
  };

  return { error, validateNull, validateWithRules };
};

export default UseValidate;
