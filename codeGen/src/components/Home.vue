<script setup>
import Navigation from '@/components/Navigation.vue'
</script>

<template>
  <div id="header">
    <navigation />
  </div>


  <div class="homePage">
    <div class="panel" id="leftHome">
      <div class="homeWrapper">
        <span>One Card,<br>Access anywhere<br><b id="WAVR">WAVR banking</b></span>
      </div>
    </div>
    <div class="panel" id="rightHome">
      <img src="@/assets/HomeBG.svg" class="homeBG" />
      <img src="@/assets/HomeActivityPanel.svg" class="activity" />
      <img src="@/assets/AccountPanel.svg" class="account" />
    </div>
  </div>
</template>

<script>
export default {
  name: 'Home',
};
</script>

<style>
.homePage {
  widows: 100%;
  height: 100%;
  display: flex;
  flex-direction: row;
}
.panel {
  width: 50%;
  height: 100%;
}

#leftHome {
  line-height: 1;
  font-size: 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 0 200px;
}

#leftHome > .homeWrapper {

}
#rightHome {
  position: relative;
}

#WAVR {
  font-size: 48px;
}

.homeBG {
  position: absolute;
  width: fit-content;
  height: fit-content;
  transform: translateX(-10%);
  /* background-repeat: no-repeat; */
  /* transform: translateX(-170px); */
  z-index: 0;
}

.activity {
  position: absolute;
  width: 60%;
  border-radius: 7px;
  bottom: 0px;
  right: -20px;
  z-index: 100000;
}

.account {
  position: absolute;
  width: 50%;
  top: 10%;
  right: 0px;
  z-index: 100000;
}
</style>
