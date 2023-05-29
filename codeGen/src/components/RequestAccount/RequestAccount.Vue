<script setup></script>

<template>
  <div class="requestPage">
    <div class="requestPanel">
      <span>Request an account</span>
      <div class="inputRequestContainer">
        <span>Account type</span>
        <b-form-select v-model="selected" :options="options"></b-form-select>
      </div>
      <div class="inputRequestContainer">
        <span>Absolute account limit</span>
        <input placeholder="0.00" type="number" step="any" />
      </div>
      <b-button variant="dark_primary" v-on:click="request">Request account</b-button>
      <span class="tos">By clicking request account, I authorize WAVR to initiate an account request and accept the Terms of Services</span>
    </div>
  </div>
</template>
<script>
export default {
  name: 'RequestAccount',
  data() {
    return {
      selected: 'CURRENT',
      options: [
        { value: 'CURRENT', text: 'Current' },
        { value: 'SAVINGS', text: 'Savings' },
      ],
    };
  },
};
</script>

<style>
.requestPage {
  background: url('@/assets/dashboard/bg.jpg');
  background-repeat: no-repeat;
  background-clip: border-box;
  background-position-x: center;
  background-size: cover;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 10px 0;
}

.requestPanel {
  background: var(--white);
  min-width: fit-content;
  max-width: 40vw;
  min-height: fit-content;
  border-radius: 35px;
  background-color: var(--white);
  margin: auto;
  display: flex;
  gap: 40px;
  flex-direction: column;
  padding: 30px;
  align-items: center;
}

.requestPanel > span:nth-child(1) {
  font-size: 24px;
}

.tos {
  text-align: center;
  width: 60%;
  font-size: 12px;
  color: var(--gray-light);
}

.inputRequestContainer {
  font-size: 16px;
  padding: 10px 20px;
  border-radius: 7px;
  border: 1px var(--gray-dark) solid;
  font-weight: 500;
  display: flex;
  flex-direction: column;
  gap: 10px;
  max-width: 350px;
  width: 350px;
  min-width: 300px;
  margin: 0px auto;
}

.inputRequestContainer > span {
  font-size: 16px;
  font-weight: 500;
}

.inputRequestContainer > input {
  font-size: 20px;
  font-weight: 400;
  border: none;
  background: transparent;
  color: var(--gray-light);
}

.inputRequestContainer > select {
  font-size: 20px;
  font-weight: 400;
  border: none;
  background: transparent;
  color: var(--gray-dark);
  box-shadow: none;
}


.requestPanel > button {
  max-width: 350px;
  width: 350px;
  min-width: 300px;
}
.inputRequestContainer > input::placeholder {
  color: var(--gray-light);
}
</style>
