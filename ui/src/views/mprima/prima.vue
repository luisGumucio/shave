<template>
  <section class="col-md-14">
    <div class="col-md-12">
      <h4 class="card-title">Materia Prima</h4>
      <div class="template-demo">
        <b-breadcrumb :items="menus" class="bg-primary" />
      </div>
    </div>
    <router-view></router-view>
  </section>
</template>

<script>
export default {
  name: "breadcrumbs",
  data() {
    return {
      menus: [
        {
          text: "Articulo",
          to: "/mitem"
        },
        {
          text: "Almacen",
          to: "/mAlmacen"
        },
        {
          text: "Procesos",
          active: true
        },
        {
          text: "Transaction",
          to: "/mtransaction"
        }
      ]
    };
  }
};
</script>

<style scoped>
</style>