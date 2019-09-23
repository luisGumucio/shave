<template lang="html">
  <div id="app">
    <div class="container-scroller">
      <app-header/>
      <div class="container-fluid page-body-wrapper">
        <app-sidebar/>
        <div class="main-panel" style="width:100%">
          <div class="col-md-14" style="    background: #F6F8FA;
              padding-top: 1.5rem;
padding-right: 1.0rem;
padding-bottom: 1.5rem;
padding-left: 1.2rem;">
            <router-view></router-view>
             <!-- <HelloWorld msg="Welcome to Your Vue.js App"/> -->
            <!-- <HelloWorld msg="Welcome to Your Vue.js App"/> -->
          </div>
          <!-- content wrapper ends -->
          <app-footer/>
        </div>
        <!-- main panel ends -->
      </div>
      <!-- page-body-wrapper ends -->
    </div>
  </div>
</template>

<script>
import HelloWorld from './components/HelloWorld.vue'

import AppHeader from './components/AppHeader.vue'
import AppSidebar from './components/AppSidebar'
import AppFooter from './components/AppFooter'

export default {
  name: 'app',
  components: {
    HelloWorld,
    AppHeader,
    AppSidebar,
    AppFooter
  }
}
</script>

<style>
  @import "../node_modules/mdi/css/materialdesignicons.min.css";
  /* @import "../node_modules/flag-icon-css/css/flag-icon.min.css";
  @import "../node_modules/font-awesome/css/font-awesome.min.css"; */
  .content-wrapper {
    background: #F6F8FA;
    padding-top: 1.5rem;
padding-right: 1.7rem;
padding-bottom: 1.5rem;
padding-left: 1.2rem;
  }
</style>

<style lang="scss">
@import "./assets/scss/style";
</style>
