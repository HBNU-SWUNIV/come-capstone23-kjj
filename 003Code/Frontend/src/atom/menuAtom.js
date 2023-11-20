import { atom } from 'recoil';
import { recoilPersist } from 'recoil-persist';

const { persistAtom } = recoilPersist({
  key: 'localstorage-ingredients',
  storage: localStorage,
});

export const IngredientsIdAtom = atom({
  key: 'ingredients',
  default: {
    name: '',
    id: '',
  },
  effects: [persistAtom],
});
