<script setup>
import Navigation from './components/Navigation.vue'
</script>

<template>
  <div id="header">
    <navigation />
  </div>
  <div id="content">
    <router-view />
  </div>
</template>




<script>
import { useUserStore } from '../src/stores/user';
import router from './router'


export default {
  setup() {
    return {
      store: useUserStore()
    }
  },
  name: "App",
  components: {
    Navigation
  },
  mounted() {
    this.store.autoLogin();
  }
};

</script>


<style scoped>


#header {
  line-height: 1.5;
  grid-column: 1 / 3;
}

#content {
   grid-column: 1 / 3;

   width: 100%;
}



@media (min-width: 1024px) {
  header {
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }
}
</style>
