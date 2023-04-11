import {combineReducers,  configureStore,  createSlice} from '@reduxjs/toolkit';
import {persistReducer} from 'redux-persist';
import storage from 'redux-persist/lib/storage';

const User = createSlice({
    name: 'User',
    initialState:{
        S_ID:'',
        S_PW:'',
        isLogin:false,
        isFirst:true,
        gotoMain:false,
    },
    reducers:{
        R_login(state,action){
            state.S_ID = action.payload.ID;
            state.S_PW = action.payload.PW;
            state.isLogin = true;
        },
        R_logout(state){
            state.S_ID = '';
            state.S_PW = '';
            state.isLogin = false;
            state.gotoMain = false;
        }
    }
})



export default configureStore({
    reducer:{
        User : User.reducer
    }
})

// const persistConfig = {
//     key:'redux',
//     storage : storage,
     // whitelist:['User']
// }


// const rootReducer = combineReducers({
//     User : User.reducer,
// })

// export default persistReducer(persistConfig, rootReducer);
export const {R_login, R_logout} = User.actions;