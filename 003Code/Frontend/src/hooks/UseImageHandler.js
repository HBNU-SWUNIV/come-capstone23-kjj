const UseImageHandler = (event, setFn, setShowFn) => {
  const selectedImage = event.target.files[0];

  if (selectedImage) {
    setFn(selectedImage);
    const reader = new FileReader();
    reader.readAsDataURL(selectedImage);

    reader.onload = (event) => {
      setShowFn(event.target.result);
    };
  }
};

export default UseImageHandler;
