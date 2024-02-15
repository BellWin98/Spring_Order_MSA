<template>
    <div class="container">
    <div class="page-header text-center mt-5"><h1>회원 정보</h1></div>
    <table class="table">
        <tr>
            <td>이름</td>
            <td>{{ memberInfo.name }}</td>
        </tr>
        <tr>
            <td>이메일</td>
            <td>{{ memberInfo.email }}</td>
        </tr>
        <tr>
            <td>도시</td>
            <td>{{ memberInfo.city }}</td>
        </tr>
        <tr>
            <td>상세주소</td>
            <td>{{ memberInfo.street }}</td>
        </tr>
        <tr>
            <td>우편번호</td>
            <td>{{ memberInfo.zipcode }}</td>
        </tr>
    </table>
</div>
    <OrderListComponent
    :isAdmin="false"
    apiUrl="/member/my-orders"
    />
</template>

<script>
import OrderListComponent from '@/components/OrderListComponent.vue';
import axios from 'axios';
export default {
    components: {
        OrderListComponent
    },
    data(){
        return {
            memberInfo: {},
        }
    },
    created(){
        this.fetchMember();
    },
    methods: {
        async fetchMember(){
            try {
                const token = localStorage.getItem('token');
                const headers = {Authorization: `Bearer ${token}`};
                const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/member/myInfo`, {headers})
                this.memberInfo = response.data.result;
            } catch(error){
                console.log(error);
            }
        }
    }
}
    
</script>