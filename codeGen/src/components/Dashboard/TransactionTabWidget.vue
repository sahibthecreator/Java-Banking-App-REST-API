<script setup>
//include
Array.prototype.contains = function (obj) {
  var i = this.length;
  while (i--) {
    if (this[i] === obj) {
      return true;
    }
  }
  return false;
};
</script>

<template>
  <div
    class="transaction"
    @click="detailsPanelEnabled = true"
    v-if="transaction"
  >
    <div class="left" v-if="transaction.toAccount">
      <p><b>From </b> {{ transaction.fromAccount }}</p>
      <p><b>From </b> {{ transaction.toAccount }}</p>
    </div>
    <b-icon-arrow-right style="width: 35px; height: 35px"></b-icon-arrow-right>
    <div class="left" v-if="transaction.toAccount">
      <p><b>To </b> {{ transaction.toAccount }}</p>
      <p><b>To </b> {{ transaction.toAccount }}</p>
    </div>
    <div class="right">
      <h5>€{{ transaction.amount }}</h5>
    </div>
  </div>

  <div class="transaction" @click="detailsPanelEnabled = true" v-else>
    <div class="left">
      <p>{{ transactionHeader }}</p>
      <p>{{ transaction.fromAccount }}</p>
      <p class="dateTime">{{ time }}</p>
    </div>
    <div class="right">
      <h5>{{ transactionAmount }}</h5>
    </div>
  </div>

  <div class="card" v-if="detailsPanelEnabled">
    <div class="content">
      <p class="heading">
        €{{ transaction.amount }}
        {{ transaction.typeOfTransaction.toLowerCase() }}
      </p>
      <p class="para">
        <b>From: </b>{{ transaction.fromAccount }}
        <br />
        <b>To: </b>{{ transaction.toAccount }}
        <br />
        <b>Date: </b> {{ transaction.dateOfExecution.split(',')[0] }}
        <br />
        <b>Time: </b> {{ time }}
        <br />
        <br />
        {{ transaction.description }}
      </p>
      <button class="btn" @click="detailsPanelEnabled = false">Hide</button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TransactionWidget',
  props: {
    transaction: Object,
    accounts: Array,
  },
  data() {
    return {
      time: '',
      detailsPanelEnabled: false,
      transactionHeader: '',
      transactionAmount: '',
    };
  },
  mounted() {
    const parts = this.transaction.dateOfExecution.split(' ');
    const timePart = parts[1];
    const time = timePart.split(':').slice(0, 2).join(':');
    this.time = time;
  },
};
</script>

<style lang="scss" scoped>
.transaction {
  position: relative;
  display: flex;
  flex-direction: row;
  gap: 20px;
  padding: 15px;
  cursor: pointer;
  border-bottom: 1px var(--gray-light) solid;
  align-items: center;
  color: var(--gray-black);

  &:hover {
    box-shadow: 0 0 10px #1414141d;
  }
}

.transaction .left p {
  margin-bottom: 0px;
  font-weight: 500;
}

.transaction .left p:nth-child(2) {
  font-size: small;
  color: var(--gray-light);
}

.transaction .btn {
  padding: 5px 5px !important;
  margin-top: 10%;
}

.transaction .right {
  white-space: nowrap;
  display: flex;
  margin-left: auto;
}

.transaction .dateTime {
  font-size: smaller;
  font-weight: 500;
  margin-left: auto;
}

.card {
  position: fixed;
  top: 80%;
  left: 50%;
  transform: translate(-50%, -100%);
  display: flex;
  align-items: center;
  justify-content: center;
  width: 320px;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
  padding: 32px;
  overflow: hidden;
  border-radius: 10px;
  background: #fcfcfc;
  border: 2px solid #ffffff;
  transition: all 0.5s cubic-bezier(0.23, 1, 0.32, 1);
  z-index: 20;
}

.content {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 20px;
  color: #1b1b1b;
  transition: all 0.5s cubic-bezier(0.23, 1, 0.32, 1);
}

.content .heading {
  font-weight: 700;
  font-size: 32px;
}

.content .para {
  line-height: 1.5;
}

.content .btn {
  color: #e8e8e8;
  text-decoration: none;
  padding: 10px;
  font-weight: 600;
  border: none;
  cursor: pointer;
  background: #1b1b1b;
  border-radius: 5px;
  box-shadow: 0 5px 10px rgba(0, 0, 0, 0.2);
  transition: all 0.2s;
}

.card:hover {
  border-color: #dadada;
}

.content .btn:hover {
  outline: 2px solid #e8e8e8;
  background: transparent;
  color: #1b1b1b;
}

.content .btn:active {
  box-shadow: none;
}
</style>
