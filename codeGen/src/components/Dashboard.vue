<script setup>
import Navigation from '@/components/Navigation.vue'
</script>

<template>
  <div id="header">
    <navigation />
  </div>

  <div class="dashboardPage">
    <div class="panel">
      <span class="dashboardName">Good afternoon,<br> <span>Jane H.</span></span>
      <div class="accounts">
        <div class="dashboardAccount"><span>1.245,90</span></div>
        <div class="dashboardAccount"><span>1.245,90</span></div>
        <div class="dashboardAccount"><span>1.245,90</span></div>
      </div>
    </div>


  </div>
</template>

<script>
export default {
  name: 'Dashboard',
  data() {
    user: {
      username: "Jane"
    }
  }
};


import Navigation from '@/components/Navigation.vue'
</script>


<style>


.dashboardPage {
  background: url('@/assets/dashboard/bg.jpg');
  background-repeat: no-repeat;
  background-clip: border-box;
  background-position-x: center;
  background-size: cover;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.panel {
  background: var(--white);
  width: 70vw;
  height: 70vh;
  border-radius: 35px;
  background-color: var(--white);
  margin: auto;
  display: flex;
  flex-direction: column;
  padding: 50px;
}

.dashboardName {
  font-size: 32px;
}

.accounts {
  background: white;
  width: 90%;
  height: 300px;
  position: absolute;
  bottom: 50px;
  border-radius: 35px;
  box-shadow: 0 0 30px #14141417;
  display: flex;
  flex-direction: row;
  gap: 20px;
  padding: 20px;
}
.dashboardAccount {
  background: white;
  position: relative;
  width: 230px;
  height: 250px;
  border-radius: 35px;
  box-shadow: 0 0 30px #14141417;
  display: flex;
  flex-direction: row;
  padding: 20px;
}


.dashboardAccount > span {
  
}

</style>