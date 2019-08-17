<template lang="html">
  <b-navbar class="fixed-top" toggleable="md" type="dark" variant="info" >
    <b-navbar-toggle target="nav_collapse">holas</b-navbar-toggle>
    <div class="text-center navbar-brand-wrapper d-flex align-items-top justify-content-center" style="width: 190px;">
    </div>
    <b-collapse is-nav id="nav_collapse">
      <!-- Right aligned nav items -->
      <b-navbar-nav class="ml-auto">
      </b-navbar-nav>
    </b-collapse>
  </b-navbar>
</template>

<script lang="js">
export default {
  name: 'app-header'
}
</script>

<style scoped lang="scss">
.app-header {
 }
</style>
