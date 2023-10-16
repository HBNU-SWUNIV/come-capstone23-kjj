const UseOnOffHandler = (on, setFn) => {
  if (on) setFn(true);
  else setFn(false);
};

export default UseOnOffHandler;
