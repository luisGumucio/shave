<template lang="html">

  <section class="app-sidebar" style="width: 190px;">
    <nav class="sidebar sidebar-offcanvas" id="sidebar">
      <ul class="nav">
        <li class="nav-item"><router-link class="nav-link" to="/ufv/"><img class="menu-icon" src="../assets/images/menu_icons/04.png" alt="menu icon"><span class="menu-title">Ufv</span></router-link></li>
        <li class="nav-item"><router-link class="nav-link" to="/repuestos/"><img class="menu-icon" src="../assets/images/menu_icons/04.png" alt="menu icon"><span class="menu-title">Repuestos</span></router-link></li>
        <li class="nav-item"><router-link class="nav-link" to="/prima/"><img class="menu-icon" src="../assets/images/menu_icons/04.png" alt="menu icon"><span class="menu-title">Materia Prima</span></router-link></li>
        <li class="nav-item"><router-link class="nav-link" to="/producto/"><img class="menu-icon" src="../assets/images/menu_icons/04.png" alt="menu icon"><span class="menu-title">Productos</span></router-link></li>
        <li class="nav-item"><router-link class="nav-link" to="/upload/"><img class="menu-icon" src="../assets/images/menu_icons/04.png" alt="menu icon"><span class="menu-title">Cargar Datos</span></router-link></li>
      </ul>
    </nav>
  </section>

</template>

<script lang="js">
export default {
  name: 'app-sidebar'
}
</script>

<style scoped lang="scss">
.app-sidebar {
}
</style>
