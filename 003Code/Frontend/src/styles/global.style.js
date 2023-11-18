import { css } from 'styled-components';

export const flexCenter = css`
  display: flex;
  justify-content: center;
  align-items: center;
`;

export const flexJCenter = css`
  display: flex;
  justify-content: center;
`;

export const flexJBetween = css`
  display: flex;
  justify-content: space-between;
`;

export const flexICenter = css`
  display: flex;
  align-items: center;
`;

export const flexColumn = css`
  display: flex;
  flex-direction: column;
`;

export const fullScreen = css`
  width: 100vw;
  height: 100vh;
`;

export const fullSize = css`
  width: 100%;
  height: 100%;
`;

export const scroll = css`
  overflow: scroll;
  -ms-overflow-style: none;
  scrollbar-width: none;
  &::-webkit-scrollbar {
    display: none;
  }
`;

export const positionCenter = css`
  position: absolute;
  inset: 0;
`;

export const marginCenter = css`
  display: flex;
  margin: 0 auto;
`;
