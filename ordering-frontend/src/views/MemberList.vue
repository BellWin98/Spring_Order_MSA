<template>
<div class="container">
    <div class="page-header text-center mt-5"><h1>회원 목록</h1></div>
    <table class="table">
        <thead>
        <tr>
            <th>ID</th>
            <th>이름</th>
            <th>이메일</th>
            <th>주문수량</th>
        </tr>
        </thead>
        <tbody>
            <!-- 유일한 식별자로 구분지을 수 있는 Key 값을 for문에 알려주어야 함 -->
            <tr v-for="member in memberList" :key="member.id">
                <td>{{ member.id }}</td>
                <td>{{ member.name }}</td>
                <td>{{ member.email }}</td>
                <td>
                    <a :href="`/member/${member.id}/orders`">{{ member.orderCount }}</a>
                </td>
            </tr>
        </tbody>
    </table>
</div>
</template>

<script>
import axios from 'axios';
export default {
    data(){
        return {
            memberList: []
        }
    },
    async created(){
        try {
            const token = localStorage.getItem('token');
            // 백틱 써서 javascript의 변수를 동적으로 활용
            const headers = {Authorization: `Bearer ${token}`};
            const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/members`, {headers});
            this.memberList = response.data.result;
        } catch(error){
            console.log(error);
        }
    }
}
</script>

<style lang="scss" scoped>

</style>