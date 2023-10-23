export const validateRules = {
  password: (value) => {
    if (!value) return '비밀번호를 입력해주세요.';
    else if (value.length < 4) return '비밀번호는 최소 4자리 이상 입력해주세요.';
    return '';
  },
};
