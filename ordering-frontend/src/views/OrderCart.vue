<template>
    <div class="container">
        <div class="page-header text-center mt-5"><h1>장바구니 목록</h1></div>
        <div class="d-flex justify-content-between">
                <button @click="clearCart" class="btn btn-primary mr-2">장바구니 비우기</button>
                <button @click="placeOrder" class="btn btn-secondary">주문하기</button>
            </div>
        <table class="table">
            <thead>
            <tr>
                <th>제품 ID</th>
                <th>제품명</th>
                <th>주문수량</th>
            </tr>
            </thead>
            <tbody>
                <!-- localStorage($store)에 담긴 cartItems의 데이터 구조와 통일 -->
                <tr v-for="item in getCartItems" :key="item.itemId">
                    <td>{{ item.itemId }}</td>
                    <td>{{ item.name }}</td>
                    <td>{{ item.count }}</td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
import {mapGetters, mapActions} from 'vuex';
import axios from 'axios';

export default {
    computed: {
        ...mapGetters(['getCartItems', 'getTotalQuantity'])
    },

    methods: {
        ...mapActions(['clearCart']),
        async placeOrder(){
            try {
                const orderItems = this.getCartItems.map(item => {
                    return {itemId: item.itemId, count: item.count};
                })
                if (this.getTotalQuantity < 1){
                    alert("장바구니에 물건이 없습니다.");
                    return;
                }
                if (!confirm(`${this.getTotalQuantity}개의 상품을 주문하시겠습니까?`)){
                    console.log("주문이 취소되었습니다.");
                    alert("주문이 취소되었습니다.");
                    return;
                }
                const token = localStorage.getItem('token');
                if (token == null){
                    alert("로그인이 필요합니다.");
                    this.$router.push({name : "Login"});
                    return;
                }
                const headers = {Authorization: `Bearer ${token}`};
                await axios.post(`${process.env.VUE_APP_API_BASE_URL}/order/create`, orderItems, {headers});
                alert("주문이 완료되었습니다.");
                // this.$store.commit('clearCart');
                this.$store.dispatch('clearCart');
            } catch(error){
                const error_message = error.response.data.error_message;
                if (error_message){
                    console.log(error_message);
                    alert(error_message);
                } else {
                    console.log(error);
                }
            }
        },
        clearCart(){
            // this.$store.commit('clearCart');
            this.$store.dispatch('clearCart');
            alert("장바구니가 초기화 되었습니다.");
            window.location.reload();
        }
    }
}

</script>
