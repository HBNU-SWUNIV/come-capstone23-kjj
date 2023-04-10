import {configureStore, createSlice} from '@reduxjs/toolkit';

const User = createSlice({
    name:'login',
    initialState:{
        S_ID:'',
        S_PW:'',
        isLogin:false,
        isFirst:true,
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
        }
    }
})

export const {R_login, R_logout} = User.actions;

export default configureStore({
    reducer:{
        User : User.reducer
    }
})