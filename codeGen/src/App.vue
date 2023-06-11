<script setup>
import Navigation from './components/Navigation.vue'
</script>

<template>
  <div id="content">
    <router-view />
  </div>
</template>




<script>

export default {
  setup() {
    return {
    }
  },
  name: "App",
  components: {
    Navigation
  },
  mounted() {
  }
};

</script>


<style scoped>

#app {
  background-color: var(--white);
}

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
