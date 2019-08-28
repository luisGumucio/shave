<template>
  <div class="col-xl-8 col-lg-8 col-md-3 col-sm-6 grid-margin stretch-card">
    <div class="card card-statistics">
      <div class="card-body">
        <div class="form-group">
          <form @submit.prevent="handleSubmit">
            <label class="control-label" for="date">Desde</label>
            <input
              class="form-control"
              id="date"
              name="date"
              placeholder="MM/DD/YYY"
              type="date"
              :class="{ 'has-error': submitting && invalidInitDate}"
              v-model="filterDate.initDate"
              @focus="clearStatus"
              @keypress="clearStatus"
            />
            <label class="control-label" for="date">Hasta</label>
            <input
              class="form-control"
              id="date"
              name="date"
              placeholder="MM/DD/YYY"
              type="date"
              :class="{ 'has-error': submitting && invalidLastDate}"
              v-model="filterDate.lastDate"
              @focus="clearStatus"
            />
            <p v-if="error && submitting" class="error-message">❗Por favor llene las fechas</p>
            <!-- <p v-if="success" class="success-message">✅ Employee successfully added</p> -->

            <button class="btn btn-success">
              <i class="mdi mdi-magnify"></i> Buscar
            </button>
          </form>

          <b-button variant="success" @click="buttonClicked">
            <i class="mdi mdi-cloud-download"></i>Exportar
          </b-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "transaction-filter",
  data() {
    return {
      submitting: false,
      error: false,
      success: false,
      filterDate: {
        initDate: Date,
        lastDate: Date,
        identifier: ''
      }
    };
  },
  methods: {
    handleSubmit() {
      this.submitting = true;
      this.clearStatus();
      if (this.invalidInitDate || this.invalidLastDate) {
        this.error = true;
        return;
      }
      console.log(this.filterDate);
      this.$emit("add:filterDate", this.filterDate);
      this.error = false;
      this.success = true;
      this.submitting = false;
    },
    clearStatus() {
      this.success = false;
      this.error = false;
    },
    buttonClicked() {
      this.$emit("button-clicked");
    }
    // download() {
    //   his.$emit("add:filterDate", this.filterDate);
    // }
  },
  computed: {
    invalidInitDate() {
      if (this.filterDate.initDate === "" && this.filterDate.lastDate === "") {
        return true;
      } else {
        return false;
      }
    },
    invalidLastDate() {
      if (this.filterDate.initDate === "") {
        return true;
      } else {
        return false;
      }
    }
  }
};
</script>

<style scoped>
form {
  margin-bottom: 2rem;
}

[class*="-message"] {
  font-weight: 500;
}

.error-message {
  color: #d33c40;
}

.success-message {
  color: #32a95d;
}
</style>