<template>

<div class="transactionPanel">
  <div class="controls">
    <input type="text" v-model="input" placeholder="Search fruits..." />
    </div>
</div>

</template>


<script>

export default {
  name: "TransactionTab",
  data() {
    return {
      input: null
    }
  }
}
</script>

<style lang="scss" scoped>

* {
  outline: 1px red solid;
}
.transactionPanel {
  width: 90vw;
  margin: auto;
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 30px;


  .controls {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 20px;
    padding: 25px 0px;
    
  }
}

</style>