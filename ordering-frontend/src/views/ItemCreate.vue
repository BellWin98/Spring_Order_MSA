<template>
    <div class="container">
        <div class="page-header text-center mt-5">
            <h1>상품 등록</h1>
        </div>
        <form @submit.prevent="itemCreate">
            <div class="form-group">
                <label>상품명</label>
                <input type="text" v-model="name" class="form-control" >
            </div>
            <div class="form-group">
                <label>카테고리</label>
                <input type="text" v-model="category" class="form-control" >
            </div>
            <div class="form-group">
                <label>가격</label>
                <input type="number" v-model="price" class="form-control">
            </div>
            <div class="form-group">
                <label>재고수량</label>
                <input type="number" v-model="stockQuantity" class="form-control">
            </div>
            <div class="form-group">
                <label>상품 이미지</label>
                <!-- @change와 @click 비교
                    @click: 요소가 클릭될 때마다 함수 실행
                    @change: 해당 태그의 값이 변경되면 함수 실행 (input 태그에 파일이 업로드 되면 실행)
                -->
                <input type="file" class="form-control" accept="image/*" @change="fileUpload">
            </div>
            <div class="form-group">
                <button class="btn btn-primary mt-3 mr-2" type="submit">등록</button>
                <a class="btn btn-secondary mt-3" href="/">돌아가기</a>
            </div>
        </form>
    </div>
</template>

<script>
import axios from 'axios';
export default {
    data(){
        return {
            name: "",
            category: "",
            price: null,
            stockQuantity: null,
            itemImage: null,
        }
    },
    methods: {
        fileUpload(event){ // event 매개변수는 이미지가 업로드 되면 자동으로 주입됨
            // event.target: 이벤트가 발생한 DOM 요소를 가리키는 객체
            this.itemImage = event.target.files[0];
        },
        async itemCreate(){
            try{
                const token = localStorage.getItem('token');
                const headers = {Authorization: `Bearer ${token}`};
                // multi-part form data 형식의 경우, new FormData()를 통해 객체 생성
                const registerData = new FormData();
                registerData.append("name", this.name);
                registerData.append("category", this.category);
                registerData.append("price", this.price);
                registerData.append("stockQuantity", this.stockQuantity);
                registerData.append("itemImage", this.itemImage);
                headers
                await axios.post(`${process.env.VUE_APP_API_BASE_URL}/item/create`, registerData, {headers});
                this.$router.push("/items/manage");
            } catch (error){
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
    }
}
</script>
