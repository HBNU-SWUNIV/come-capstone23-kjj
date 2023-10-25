import { atom } from 'recoil';
import { recoilPersist } from 'recoil-persist';

const { persistAtom } = recoilPersist({
  key: 'local',
  storage: localStorage,
});

export const IngredientsIdAtom = atom({
  key: 'isSetIngredientsIdAtomKey',
  default: {
    name: '',
    id: '',
  },
  effects: [persistAtom],
});
