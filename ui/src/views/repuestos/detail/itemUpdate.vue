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
      />
      <p v-if="error && submitting" class="error-message">❗Por favor Ingrese la fecha</p>
      <br />
      <button class="btn btn-success">
        <i class="mdi mdi-file"></i>Actualizar Articulos
      </button>
    </form>
  </div>
</template>

<script>
export default {
  name: "item-update",
  data() {
    return {
      submitting: false,
      error: false,
      success: false,
      initDate: ""
    };
  },
  methods: {
    handleSubmit() {
      this.submitting = true;
      if (this.invalidInitDate) {
        this.error = true;
        return;
      }
      this.$emit("add:initDate", this.initDate);
      this.clearStatus();
    },
    clearStatus() {
      this.success = false;
      this.error = false;
    }
  },
  computed: {
    invalidInitDate() {
      return this.initDate === "";
    }
  }
};
</script>

<style scoped>
form {
  margin-bottom: 2rem;
}
</style>
