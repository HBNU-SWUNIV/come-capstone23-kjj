import { combineReducers, configureStore, createSlice } from "@reduxjs/toolkit";
import { persistReducer, persistStore } from "redux-persist";
import storage from "redux-persist/lib/storage";

const REGISTER = 'REGISTER';

const LoginUser = createSlice({
    name: 'LoginUser',
    initialState: {
        username: "",
        userid: 0
    },
    reducers: {
        R_login(state, action) {
            state.username = action.payload.username;
            state.userid = action.payload.id;
        },
        R_logout(state) {
            state.username = "";
            state.userid = "";
        }
    }
});

const persistConfig = {
    key: "root",
    storage,
};

const rootReducer = combineReducers({
    username: LoginUser.reducer,
});

const persistedReducer = persistReducer(persistConfig, rootReducer);

const store = configureStore({
    reducer: persistedReducer,
});

const persistor = persistStore(store);

export { store, persistor, REGISTER };

export const { R_login, R_logout } = LoginUser.actions;

export default LoginUser.reducer;