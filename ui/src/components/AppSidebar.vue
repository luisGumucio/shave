<template lang="html">

  <section class="app-sidebar" style="width: 190px;">
    <nav class="sidebar sidebar-offcanvas" id="sidebar">
      <ul class="nav">
        <!-- <li class="nav-item"><router-link class="nav-link" to="/"><img class="menu-icon" src="../assets/images/menu_icons/01.png" alt="menu icon"><span class="menu-title">Dashboard</span></router-link></li>
        <li class="nav-item"><router-link class="nav-link" to="/widgets/"><img class="menu-icon" src="../assets/images/menu_icons/02.png" alt="menu icon"><span class="menu-title">Widgets</span></router-link></li>
        <li class="nav-item">
          <span class="nav-link" v-b-toggle="'sample-pages'">
            <img class="menu-icon" src="../assets/images/menu_icons/08.png" alt="menu icon"><span class="menu-title">Sample Pages</span><i class="menu-arrow"></i>
          </span>
          <b-collapse id="sample-pages">
            <ul class="nav flex-column sub-menu">
              <li class="nav-item"><router-link class="nav-link" to="/login/">login</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/register/">register</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/404/">404</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/500/">500</router-link></li>
            </ul>
          </b-collapse>
        </li>
        <li class="nav-item">
          <span class="nav-link" v-b-toggle="'ui-components'">
            <img class="menu-icon" src="../assets/images/menu_icons/03.png" alt="menu icon"><span class="menu-title">UI Components</span><i class="menu-arrow"></i>
          </span>
          <b-collapse id="ui-components">
            <ul class="nav flex-column sub-menu">
              <li class="nav-item"><router-link class="nav-link" to="/alerts/">Alerts</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/buttons/">Button</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/badges/">Badges</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/breadcrumbs/">Breadcrumbs</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/carousel/">Carousel</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/dropdowns/">Dropdowns</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/typography/">Typography</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/paginations/">Paginations</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/tooltips/">Tooltips</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/progress/">Progress</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/tabs/">Tabs</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/modals/">Modals</router-link></li>
            </ul>
          </b-collapse>
        </li> -->
        <!-- <li class="nav-item"><router-link class="nav-link" to="/tables/"><img class="menu-icon" src="../assets/images/menu_icons/06.png" alt="menu icon"><span class="menu-title">Tables</span></router-link></li>
        <li class="nav-item"><router-link class="nav-link" to="/icons/"><img class="menu-icon" src="../assets/images/menu_icons/07.png" alt="menu icon"><span class="menu-title">Icons</span></router-link></li>
        <li class="nav-item"><router-link class="nav-link" to="/forms/"><img class="menu-icon" src="../assets/images/menu_icons/04.png" alt="menu icon"><span class="menu-title">Forms</span></router-link></li> -->
        <li class="nav-item"><router-link class="nav-link" to="/repuestos/"><img class="menu-icon" src="../assets/images/menu_icons/04.png" alt="menu icon"><span class="menu-title">Repuestos</span></router-link></li>
        <li class="nav-item"><router-link class="nav-link" to="/prima/"><img class="menu-icon" src="../assets/images/menu_icons/04.png" alt="menu icon"><span class="menu-title">Materia Prima</span></router-link></li>
        <li class="nav-item"><router-link class="nav-link" to="/producto/"><img class="menu-icon" src="../assets/images/menu_icons/04.png" alt="menu icon"><span class="menu-title">Productos</span></router-link></li>
        <li class="nav-item"><router-link class="nav-link" to="/modals/"><img class="menu-icon" src="../assets/images/menu_icons/04.png" alt="menu icon"><span class="menu-title">Cargar Datos</span></router-link></li>
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
