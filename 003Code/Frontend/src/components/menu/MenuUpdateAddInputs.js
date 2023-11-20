import { Skeleton, TextField } from '@mui/material';
import ErrorInform from '../general/ErrorInform';

const MenuUpdateAddInputs = (props) => {
  return (
    <>
      <div style={imgStyle}>
        {props.selectedImg ? (
          <img src={props.selectedImg} width={210} height={118} />
        ) : (
          <>
            {props.isUpdate ? (
              <img
                width={210}
                height={120}
                src={`http://kjj.kjj.r-e.kr:8080/api/image?dir=${props.prev_img}`}
              />
            ) : (
              <Skeleton variant="rectangular" width={210} height={118} />
            )}
          </>
        )}

        <input
          style={{ marginLeft: '2vw' }}
          type="file"
          accept="image/*"
          onChange={props.handleImageChange}
        />
      </div>

      {props.input_data.map((item) => (
        <>
          <TextField
            key={item.id}
            id={item.id}
            inputRef={item.inputRef}
            placeholder={item.placeholder}
            name={item.inputName}
            onBlur={item.onBlur}
            required
            label="required"
            type={item.type}
          />

          {!props.isUpdate && props.validateFn[item.inputName] && (
            <ErrorInform message={`${item.item_name}은 필수 입력입니다`} />
          )}
          {item.condition && (
            <ErrorInform
              color={true}
              message={`식재료가 ${item.selectedIngredient}으로 설정되었습니다.`}
            />
          )}
          {item.duplicated_error && props.duplicateFn && (
            <ErrorInform
              message={`중복된 ${
                props.isUpdate ? '메뉴 명' : item.item_name
              }이 있습니다.`}
            />
          )}
        </>
      ))}
    </>
  );
};

export default MenuUpdateAddInputs;

const imgStyle = { display: 'flex', alignItems: 'center' };
