<template>
    <section class="col-md-14">
        <div class="col-md-12">
            <h4 class="card-title">Producto Terminado</h4>
            <div class="template-demo ">
                <b-breadcrumb :items="items" class="bg-primary" />
            </div>
        </div>
        <router-view></router-view>
</section>
</template>

<script>
export default {
  name: 'breadcrumbs',
  data () {
    return {
      items: [{
        text: 'Articulo',
        to: '/pitem'
      }, {
        text: 'Almacen',
        to: '/palmacen'
      }, {
        text: 'Procesos',
        active: true
      }]
    }
  }
}
</script>

<style scoped>
.breadcrumbs {
}
.paginations {
}
</style>