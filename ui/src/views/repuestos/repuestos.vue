<template lang="html">
<section class="dashboard">
        <div class="col-md-12">
            <h4 class="card-title">Repuestos</h4>
            <div class="template-demo ">
                <b-breadcrumb :items="items" class="bg-primary" />
            </div>
        </div>
        <router-view></router-view>
</section>
</template>

<script lang="js">
export default {
  name: 'breadcrumbs',
  data () {
    return {
      items: [{
        text: 'Articulo',
        to: '/ritem'
      }, {
        text: 'Almacen',
        to: '/ralmacen'
      }, {
        text: 'Procesos',
        active: true
      }]
    }
  }
}
</script>

<style scoped lang="scss">
.breadcrumbs {
}
.paginations {
}
</style>