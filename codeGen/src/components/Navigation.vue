<template>
  <div id="nav">
    <b-navbar-brand to="/">
      <img src="@/assets/Logo.svg" alt="logo" id="logo" />
      <span id="logoTitle">WAVR</span>
    </b-navbar-brand>
    <b-nav id="list">
      <b-nav-item to="/">Home</b-nav-item>
      <b-nav-item>Products</b-nav-item>
      <b-nav-item>App and Internet banking</b-nav-item>
    </b-nav>

    <b-navbar-nav class="ml-auto">
      <b-nav-item to="/login" id="login">Login / register</b-nav-item>
      <!-- <a href="/login"><span id="login">Login/Register</span></a> -->
      
    </b-navbar-nav>

  </div>
  <div id="line"></div>
</template>

<script>
export default {
  name: 'Navigation',
};
</script>

<style>
#line {
  width: 2000px;
  height: 1px;
  background-color: var(--gray-black);
}

#nav {
  height: 100px;
  display: flex;
  flex-direction: row;
  align-items: center;
  padding: 0 200px;
}

#list {
  list-style: none;
  gap: 20px;
  left: 10px;
}

.nav-link {
  font: 500;
  color: var(--gray-dark);
}

.nav-link:hover {
  color: var(--black);
}

#login {
  border: 1px var(--gray-black) solid;
  border-radius: 7px;
  padding: .1rem 2rem;
  color: var(--gray-black);
}


#logoTitle {
  color: var(--gray-black);
}

#logo {
  width: 40px;
  aspect-ratio: 1 / 2;
  margin-right: 20px;
}
</style>
