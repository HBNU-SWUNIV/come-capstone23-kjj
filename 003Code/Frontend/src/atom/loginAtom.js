import { atom } from 'recoil';
import { recoilPersist } from 'recoil-persist';

const { persistAtom } = recoilPersist({
  key: 'localstorage-islogin',
  storage: localStorage,
});

export const isloginAtom = atom({
  key: 'islogin',
  default: false,
  effects: [persistAtom],
});
