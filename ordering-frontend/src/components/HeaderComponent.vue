<template>
    <nav class="navbar navbar-expand-lg bg-dark navbar-dark">
        <div class="navbar-collapse w-100 order-1 order-md-0 dual-collapse2">
            <ul class="navbar-nav mr-auto" v-if="userRole === 'ROLE_ADMIN'">
                <li class="nav-item"><a class="nav-link" href="/members">회원관리</a></li>
                <li class="nav-item" ><a class="nav-link" href="/items/manage">상품관리</a></li>
                <li class="nav-item" ><a class="nav-link" href="/orders">주문관리</a></li>
            </ul>
        </div>
        <div class="mx-auto order-0">
            <a class="navbar-brand mx-auto" href="/">Java Shop</a>   
        </div>
        <div class="navbar-collapse w-100 order-3 dual-collapse2">
            <ul class="navbar-nav ml-auto">
                <li class="nav-item" v-if="!isLogin">
                    <a class="nav-link" href="/member/create">회원가입</a>
                </li>
                <li class="nav-item" v-if="userRole === 'ROLE_USER'">
                    <a class="nav-link" href="/items">상품 목록</a>
                </li>
                <li class="nav-item" v-if="isLogin && userRole === 'ROLE_USER'">
                    <a class="nav-link" href="/mypage">마이페이지</a>
                </li>
                <li class="nav-item" v-if="isLogin && userRole === 'ROLE_USER'">
                    <!-- getTotalQuantity: getters의 함수명을 명시 -->
                    <a class="nav-link" href="/ordercart">장바구니 ({{ getTotalQuantity }})</a>
                    <!-- <a class="nav-link" href="/ordercart">장바구니 ({{ $store.state.totalQuantity }})</a> -->
                    
                </li>
                <li class="nav-item" v-if="!isLogin">
                    <a class="nav-link" href="/login">로그인</a>
                </li>
                <li class="nav-item" v-if="isLogin">
                    <a class="nav-link" href="#" @click="doLogout">로그아웃</a>
                </li>
            </ul>
        </div>
    </nav>
</template>

<script>
// store의 getters 함수를 사용하기 위한 import (장바구니 로직과 관련 있음)
import {mapGetters} from 'vuex';

export default {
    // computed는 종속된 반응형 데이터가 변경될 때만 함수를 다시 실행하여 값을 계산하는 계산 함수
    // BasicComponent.vue에서 실습한 거 확인하면서 이해해보기
    computed: {
        // store 내의 변수 값이 변경되면, 이 computed 함수가 자동으로 실행되면서 변경됨. 실시간으로 변경된 값 반영
        // 즉, 한 화면에서 분리된 컴포넌트 간 값의 변경이 생겼을 때 실시간으로 동기화 해줌.
        // 상품을 장바구니에 추가하면, Header Component의 장바구니 버튼의 갯수가 상품 추가 수량만큼 변경된다.
        ...mapGetters(['getTotalQuantity'])
        // ...연산자(spread)를 통해 아래 함수를 현재 컴포넌트(getTotalQuantity)로 가져오는 것
        // getTotalQuantity: function(){
        //     return this.$store.getters.totalQuantity;
        // }
    },
    data () {
        return {
            isLogin: false,
            userRole: null
        }
    },
    created(){ // 페이지가 처음 로드될 때, 딱 한번만 실행된다.
        if (localStorage.getItem("token")){
            this.isLogin = true;
            this.userRole = localStorage.getItem("role");
        }
    },
    methods: {
        doLogout(){
            // localStorage.removeItem("token");
            // localStorage.removeItem("role");
            localStorage.clear();
            // 아래 코드가 없으면 created 상태값이 남아있으므로 새로고침 처리를 통해 created()가 재생성되도록 함.  
            window.location.reload();
        }
    }
}
</script>