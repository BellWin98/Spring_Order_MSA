<template>
  <h1>hello world {{myLang}}</h1>
  <input type="text" value="myValue">
  
  <!-- :value는 v-bind:value= 의 축약어 -->
  <!-- v-bind는 input박스에서 많이 사용하는 단방향 데이터 바인딩 방식 -->
  <input type="text" :value="inputValue1"/>
  
  <!-- v-model은 양방향 데이터 바인딩 모델이라는 차이점이 있다. -->
  <input type="text" v-model="inputValue2"/>
  
  <!-- onClick == @click -->
  <button @click="showValue()">변수 변경사항 확인</button>

  <div>
    <h2>{{ count }}</h2>
    <button @click="increment">increment</button>
  </div>
  <div>
    <h2>{{ doubleCount }}</h2>
    <button @click="increment">increment</button>
  </div>
</template>

<script>
export default {  
  // computed는 종속된 반응형 데이터가 변경될 때만 함수를 다시 실행하여 값을 계산하는 계산 함수
  computed: {
    doubleCount(){
      return this.count * 2;
    }
  },
  data(){
    return {
      myLang: "python",
      inputValue1: "python2",
      inputValue2: "python3",
      count: 0,
    }
  },
  // Vue 생명주기에서 인스턴스가 생성되는 시점을 created라 함
  // 웹훅(Webhook): 특정 이벤트(데이터가 변경)가 발생하였을 때, 서버로 알림 보냄
  created(){
    this.myLang = "java";
  },
  methods: {
    showValue(){
      alert(this.inputValue2);
    },
    increment(){
      this.count++;
    },
  }
}
</script>