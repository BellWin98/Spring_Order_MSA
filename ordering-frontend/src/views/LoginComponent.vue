<template>
    <div class="container">
        <div class="page-header text-center mt-5">
            <h1>로그인</h1>
        </div>
        <!-- submit은 form 제출 시 브라우저가 페이지를 새로고침하므로, 해당 동작을 막기위해 prevent 사용 -->
        <!-- v-model을 통한 양방향 데이터 바인딩으로 자동으로 이메일, 비밀번호를 data함수에 세팅해줌 -->
        <form @submit.prevent="doLogin">
            <div class="form-group">
                <label for="email">이메일 </label>
                <input type="email" v-model="email" class="form-control">
            </div>
            <div class="form-group">
                <label for="password">비밀번호 </label>
                <input type="password" v-model="password" class="form-control">
            </div>
            <div class="form-group">
                <button class="btn btn-primary mt-3" type="submit">로그인</button>
                <a class="btn btn-secondary mt-3" href="/">돌아가기</a>
            </div>
        </form>
    </div>
</template>

<script>
import axios from 'axios';
// jwt-decode에서 export 시에 중괄호 넣어서 export하였으므로, import 할 때에도 {} 넣어야 함
import {jwtDecode} from 'jwt-decode';
export default {
    data(){
        return{
            email: "",
            password: ""
        }
    },
    methods: {
        async doLogin(){
            // 2가지 예외 가능성
            // 1) 200번대 상태 값이면서, token이 비어있는 경우
            // 2) 200번대 상태 값이 아닌 경우
            // trt-catch로 감싸면 response header가 200번대가 아니면 전부 catch로 빠진다.
            try {
                const loginData = {email: this.email, password: this.password};
                const response = await axios.post(`${process.env.VUE_APP_API_BASE_URL}/doLogin`, loginData);
                const token = response.data.result.token;
                if (token){
                    // 토큰 파싱 (디코딩)
                    const decoded = jwtDecode(token);
                    localStorage.setItem("token", token);
                    localStorage.setItem("role", decoded.role);
                    // 전역에서 router 사용 가능 (로그인 성공 시 홈 화면으로 이동)
                    // created 함수는 reload될 때 1번만 실행되는 Hook 함수
                    // 그런데, router.push를 통한 화면 전환은 reload를 실행시키지 않으므로, created 함수 호출이 되지 않음
                    // this.$router.push("/");
                    alert("로그인 성공");
                    window.location.href = "/";
                } else {
                    console.log("200 OK, but No Token"); // 개발자 디버깅 목적
                    alert("Login Failed"); // 사용자에게 알려주는 목적
                }
            } catch(error){
                const error_message = error.response.data.error_message;
                if (error_message){
                    console.log(error_message);
                    alert(error_message);
                } else {
                    console.log(error);
                    alert("Login Failed");
                }
            }
        }
    },
}
</script>