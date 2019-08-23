<template>
  <section class="badges">
    <vue-instant-loading-spinner ref="Spinner"></vue-instant-loading-spinner>
    <div class="row">
      <div class="col-md-12 grid-margin stretch-card">
        <div class="card">
          <div class="card-body">
            <h4 class="card-title">Procesos</h4>
            <b-table responsive striped hover :items="items" :fields="fields"></b-table>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
export default {};
</script>

<style scoped>
</style>