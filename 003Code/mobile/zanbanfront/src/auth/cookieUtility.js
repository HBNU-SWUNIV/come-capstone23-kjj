export function getCookie(name) {
    // 현재 웹 페이지의 모든 쿠키를 나타내는 문자열을 반환
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    // 쿠키 존재 여부 판단
    if (parts.length === 2) return parts.pop().split(';').shift();
  }
  