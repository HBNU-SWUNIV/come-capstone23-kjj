import { atom } from 'recoil';
import { recoilPersist } from 'recoil-persist';

const { persistAtom } = recoilPersist({
  key: 'localstorageAtom',
  storage: localStorage,
});

export const isloginAtom = atom({
  key: 'isloginAtom',
  default: false,
  effects: [persistAtom],
});