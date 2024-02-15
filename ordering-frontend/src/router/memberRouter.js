import MemberList from '@/views/MemberList';
import MemberCreate from '@/views/MemberCreate';
import MemberOrders from '@/views/MemberOrders';
import MyPage from '@/views/MyPage';

export const memberRoutes = [
    {
        path: '/members', 
        name: 'MemberList',
        component: MemberList,
    },
    {
        path: '/member/create',
        name: 'MemberCreate',
        component: MemberCreate,
    },
    {
        path: '/member/:id/orders',
        name: 'MemberOrders',
        component: MemberOrders,
        // 화면에 매개변수로 던져주겠다.
        props: true,
    },
    {
        path: '/mypage',
        name: 'MyPage',
        component: MyPage,
    },

];