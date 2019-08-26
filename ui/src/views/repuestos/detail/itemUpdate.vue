<template>
  <div class="card-body">
    <h4 class="card-title">Actualizar Articulo</h4>
    <form @submit.prevent="handleSubmit">
      <label class="control-label" for="date">Fecha</label>
      <input
        class="form-control"
        id="date"
        name="date"
        placeholder="MM/DD/YYY"
        type="date"
        :class="{ 'has-error': submitting && invalidInitDate}"
        v-model="initDate"
        @focus="clearStatus"
        @keypress="clearStatus"
      />
      <p v-if="error && submitting" class="error-message">❗Por favor llene las fechas</p>
     <br/>
      <b-button variant="success" @click="update()">
        <i class="mdi mdi-file"></i>Actualizar Articulos
      </b-button> 
    </form>
    <!-- <div class="template-demo">
      <b-button variant="success" @click="update()">
        <i class="mdi mdi-file"></i>Actualizar Articulos
      </b-button> -->
  </div>
</template>

<script>
export default {};
</script>

<style scoped>
</style>
