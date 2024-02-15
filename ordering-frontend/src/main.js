import { createApp } from 'vue';
import App from './App.vue';
import router from './router/index';
import '@/assets/css/bootstrap.min.css';
import axios from 'axios';
import store from './store/cart.js';

// index.html의 id인 app에 마운트가 되도록 하는 코드
const app = createApp(App);

// 401 응답의 경우 interceptor를 통해 공통적으로 토큰 제거 후 로그아웃 처리
axios.interceptors.response.use(response => response, error => {
    if (error.response && error.response.status === 401){
        localStorage.clear();
        window.location.href = "/login";
    }
    // 나머지 에러는 무시하겠다. 이 에러 외에 다른 예외는 무시하겠음
    // 이거 안쓰면 이메일이나 비밀번호 잘못 입력했을 때, 화면에 빨간색 에러 뜸
    return Promise.reject(error); 
})

// vue application에서 전역적으로 기능을 사용할 경우, 아래와 같이 use문법 사용
app.use(router);
app.use(store);
app.mount('#app');

