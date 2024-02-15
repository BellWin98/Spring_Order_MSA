<template>
    <div class="container">
        <div class="page-header text-center mt-5">
            <h1>회원가입</h1>
        </div>
        <form @submit.prevent="memberCreate">
            <div class="form-group">
                <label>이메일</label>
                <input type="text" v-model="email" class="form-control" >
            </div>
            <div class="form-group">
                <label>비밀번호</label>
                <input type="password" v-model="password" class="form-control">
            </div>
            <div class="form-group">
                <label for="name">이름</label>
                <input type="text" v-model="name" class="form-control">
            </div>
            <div class="form-group">
                <label>도시</label>
                <input type="text" v-model="city" class="form-control">
            </div>
            <div class="form-group">
                <label>상세주소</label>
                <input type="text" v-model="street" class="form-control">
            </div>
            <div class="form-group">
                <label>우편번호</label>
                <input type="text" v-model="zipcode" class="form-control">
            </div>
            <div class="form-group">
                <button class="btn btn-primary mt-3 mr-2" type="submit">가입 완료</button>
                <a class="btn btn-secondary mt-3" href="/">돌아가기</a>
            </div>
        </form>
    </div>
</template>

<script>
import axios from 'axios';
export default {
    data(){
        return{
            email: "",
            password: "",
            name: "",
            city: "",
            street: "",
            zipcode: "",
        }
    },
    methods: {
        async memberCreate(){
            try {
                const registerData = {
                    email: this.email, 
                    password: this.password,
                    name: this.name,
                    city: this.city,
                    street: this.street,
                    zipcode: this.zipcode,
                };
                await axios.post(`${process.env.VUE_APP_API_BASE_URL}/member/create`, registerData);
                alert("회원가입 완료");
                // window.location.href = "/login"; // HeaderComponent의 created 함수 동작함 (로그인 시 필요)
                this.$router.push({name : "Login"}); // Login으로 라우터 푸시가 됨, HeaderComponent의 created 함수 미동작(토큰 자체가 필요 없으므로)
            } catch(error){
                const error_message = error.response.data.error_message;
                if (error_message){
                    console.log(error_message);
                    alert(error_message);
                } else {
                    console.log(error);
                    alert("입력값 확인 필요");
                }
            }
        }
    },
}
</script>