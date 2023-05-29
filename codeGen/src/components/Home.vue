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
        <span>One Card,<br>Access anywhere<br><b id="WAVR"><span id="fade">WAVR</span> banking</b></span>
        <span style="font-size: 16px;">Enjoy the maximum security and get more using our best services. No more complicated apps.</span>
        <div class="buttonsHome">
          <button>Create an account</button>
          <button>Download <img src="@/assets/Apple.svg" style="width: 15px; transform: translateY(-2px);"> app</button>
        </div>
      </div>
    </div>
    <div class="panel" id="rightHome">
      <img src="@/assets/HomeBG.svg" class="homeBG" />
      <img src="@/assets/HomeActivityPanel.svg" class="activity" />
      <img src="@/assets/AccountPanel.svg" class="accountImg" />
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
  transform: translateY(75px);
  width: 375px;
}
#rightHome {
  position: relative;
}

#WAVR {
  font-size: 48px;
}

#fade {
  font-weight: 500;
  background: -webkit-linear-gradient(#19198D, #00d5ffe0);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.homeWrapper {
  display: flex;
  flex-direction: column;
  gap: 15px;
}


.homeBG {
  position: absolute;
  width: fit-content;
  height: fit-content;
  transform: translateX(-10%);
  z-index: 0;
}

.buttonsHome {
  display: flex;
  flex-direction: row;
  gap: 10px;
}

.buttonsHome > button {
  font-size: 18px;
  background: var(--gray-black);
  border: 1px var(--gray-black) solid;
  color: var(--white);
  border-radius: 7px;
  padding: 15px;
  display: flex;
  gap: 4px;
  justify-content: center;
}

.buttonsHome > button:nth-child(2) {
  background: var(--white);
  color: var(--gray-black);
}


.activity {
  position: absolute;
  border-radius: 7px;
  bottom: 50px;
  right: -20px;
}

.accountImg {
  position: absolute;
  width: auto;
  top: 10%;
  right: 10%;
}
</style>
