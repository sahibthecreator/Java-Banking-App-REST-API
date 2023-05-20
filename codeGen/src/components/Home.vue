<script setup></script>

<template>
  <div class="homePage">
    <div class="panel" id="leftHome">
      <div class="homeWrapper">
        <span>One Card,<br>Access anywhere<br><b id="WAVR">WAVR banking</b></span>
      </div>
    </div>
    <div class="panel" id="rightHome">
      <div class="homeBG"></div>
      <div class="activity"></div>
      <div class="account"></div>

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
  width: 100%;
  height: 100%;
  background: url('../assets/HomeBG.svg');
  background-repeat: no-repeat;
  transform: translateX(+170px);
}

.activity {
  background: url('../assets/HomeActivityPanel.svg');
  background-repeat: no-repeat;
  width: auto;
  height: 300px;
  transform: translate(600px, 400px);
  border-radius: 7px;
}

.account {
  background: url('../assets/AccountPanel.svg');
  background-repeat: no-repeat;
  width: auto;
  transform: translate(400px, -100px);
  height: 80px;
}
</style>
