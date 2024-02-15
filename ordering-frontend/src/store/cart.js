import {createStore} from 'vuex';

// initState, updateLocalStorage와 같은 함수는 store 정의 바깥에 위치
// 책임과 권한을 분리하는 개념적인 의도도 있으나, 다른 store나 다른 상황에서 재사용성을 높이기 위한 아키텍처
function initState(){ // 초기화 하기 위해 사용
    return {
        // Json 데이터를 Javascript 객체로 역직렬화
        // 로컬 스토리지에 데이터가 있으면 가져오고, 없으면 빈 배열(또는 0)로 세팅
        cartItems: JSON.parse(localStorage.getItem('cartItems')) || [],
        totalQuantity: localStorage.getItem('totalQuantity') || 0
    }
}

function updateLocalStorage(cartItems, totalQuantity){
    // JSON.stringify: 객체를 JSON 데이터로 직렬화
    localStorage.setItem('cartItems', JSON.stringify(cartItems));
    localStorage.setItem('totalQuantity', totalQuantity);
}
export default createStore({
    // state: 상태를 의미하는 객체로써 initState를 통해 상태 초기화
    // state.cartItems 같이 데이터 추출
    state: initState,

    // mutations: 상태를 변경하는 함수들의 집합
    // vuex에서 commit이라는 용어는 상태 변경을 위해 mutation을 호출하는 과정을 의미 
    mutations: {
        // addToCart 함수는 외부 컴포넌트(또는 action)에서 호출될 예정
        // state 매개변수는 자동으로 주입됨
        addToCart(state, item){
            const existItem = state.cartItems.find(i => i.itemId == item.itemId);
            if (existItem){
                existItem.count += item.count;
            } else {
                state.cartItems.push(item); // state에다가만 item 객체 push
            }
            state.totalQuantity = parseInt(state.totalQuantity) + item.count;
            // console.log("state.totalQuantity 자료형: " + typeof(state.totalQuantity));
            // console.log("item.count 자료형: " + typeof(item.count));
            updateLocalStorage(state.cartItems, state.totalQuantity); // 로컬 스토리지 업데이트
        },
        clearCart(state){
            state.cartItems = [];
            state.totalQuantity = 0;
            updateLocalStorage(state.cartItems, state.totalQuantity);
        }
    },
    // actions를 통해 여러 mutation을 연속적으로 커밋하거나, 비동기작업을 수행
    // 일반적으로 component에서 actions의 메서드를 호출하고, actions에서 mutation 메서드 호출
    actions: {
        // context 매개변수가 주입. context 매개변수 안에 state, commit 객체 등이 존재
        addToCart(context, item){
            context.commit('addToCart', item);
        },
        clearCart(context){
            context.commit('clearCart');
        }
    },
    // getters: 상태를 반환하는 함수들의 집합 (이 함수들을 통해 데이터를 가져옴)
    getters: {
        getCartItems: state => state.cartItems,
        getTotalQuantity: state => state.totalQuantity
    }
});