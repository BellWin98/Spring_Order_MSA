<template>
<div class="container">
    <div class="page-header text-center mt-5"><h1>주문 목록</h1></div>
    <table class="table">
        <thead>
            <th>#</th>
            <th>회원 Email</th>
            <th>주문 상태</th>
            <!-- <th v-if="isAdmin === true">ACTION</th> -->
            <th>ACTION</th>
        </thead>
        <tbody>
            <template v-for="order in orderList" :key="order.orderId">
                <tr @click="toggleOrder(order.orderId)" style="cursor: pointer">
                    <td>{{ order.orderId }}</td>
                    <td>{{ order.memberEmail }}</td>
                    <td>{{ order.orderStatus }}</td>
                    <!-- <td v-if="isAdmin === true"><button v-if="order.orderStatus === 'ORDERED'" @click.stop="cancelOrder(order.orderId)">CANCEL</button></td> -->
                    <td><button v-if="order.orderStatus === 'ORDERED'" @click.stop="cancelOrder(order.orderId)">CANCEL</button></td>
                </tr>
                <tr v-if="visibleOrder.has(order.orderId)">
                    <td colspan="4">
                        <span v-for="orderItem in order.orderItems" :key="orderItem.itemId">
                            {{ orderItem.itemName }}: {{ orderItem.quantity }}개 <br>
                        </span>
                    </td>
                </tr>
            </template>
        </tbody>
    </table>
</div>
</template>

<script>
import axios from 'axios';
export default {
    props: ['isAdmin', 'apiUrl'],
    data(){
        return {
            orderList: [],
            visibleOrder: new Set(),
        }
    },
    async created(){
        try {
            const token = localStorage.getItem('token');
            // 백틱 써서 javascript의 변수를 동적으로 활용
            const headers = {Authorization: `Bearer ${token}`}
            const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}${this.apiUrl}`, {headers});
            this.orderList = response.data.result;
        } catch(error){
            console.log(error);
        }
    },
    methods: {
        toggleOrder(orderId){
            if (this.visibleOrder.has(orderId)){ 
                this.visibleOrder.delete(orderId); // 펼쳐진 토글을 닫음
            } else {
                this.visibleOrder.add(orderId); // 숨겨진 토글을 펼침
            }
        },
        async cancelOrder(orderId){
            if(confirm("주문을 취소하시겠습니까?")){
                try{
                    const token = localStorage.getItem('token');
                    const headers = {Authorization: `Bearer ${token}`}
                    await axios.delete(`${process.env.VUE_APP_API_BASE_URL}/order/${orderId}/cancel`, {headers});
                    const order = this.orderList.find(order => order.orderId === orderId);
                    order.orderStatus = "CANCELED";
                    alert("주문이 취소되었습니다.");
                }catch(error){
                    console.log(error);
                    alert("주문 취소 실패");
                }
            }
        }
    }
}
</script>