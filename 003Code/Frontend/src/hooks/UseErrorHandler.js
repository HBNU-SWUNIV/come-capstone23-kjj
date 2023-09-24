const UseErrorHandler = ({ condition, setFn }) => {
  if (condition) setFn(true);
  else setFn(false);
};

export default UseErrorHandler;
