<template>
    <OrderListComponent
    :isAdmin="true"
    :apiUrl="`/member/${id}/orders`"
    />
</template>

<script>
import OrderListComponent from '@/components/OrderListComponent.vue';
export default {
    // 매개변수로 데이터를 넘겨받을 때 사용
    // 이 id는 MemberList.vue의 orderCount 링크를 클릭하면서 전달된 member.id가 memberRouter를 걸쳐 받아와짐
    props:['id'], 
    components: {
        OrderListComponent
    }
}
    
</script>