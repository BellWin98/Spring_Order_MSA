<template>
    <!-- env 환경변수는 Template에서 적용되지 않고, script에서만 적용되므로 Component단의 script에 ${}을 넣어줌 -->
    <OrderListComponent
    :isAdmin="true"
    apiUrl="/orders"
    />
</template>

<script>
import OrderListComponent from '@/components/OrderListComponent.vue';
export default {
    components: {
        OrderListComponent
    }
}
</script>