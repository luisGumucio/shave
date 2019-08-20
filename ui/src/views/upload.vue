<template>
  <section class="badges">
    <vue-instant-loading-spinner ref="Spinner"></vue-instant-loading-spinner>
    <div class="row">
      <div class="col-md-8 grid-margin stretch-card">
        <div class="card">
          <div class="card-body">
            <h4 class="card-title">Archivos subidos</h4>
            <b-table responsive striped hover :items="items" :fields="fields"></b-table>
          </div>
        </div>
      </div>

      <div class="col-md-4 grid-margin stretch-card">
        <div class="card">
          <div class="card-body">
            <h4 class="card-title">Subir Archivo</h4>
            <form class="forms-sample" @submit.prevent="submitFile">
              <b-form-group label="cargar archivo" label-for="input8">
                <b-form-file
                  v-model="file"
                  id="inpu8"
                  :state="Boolean(file)"
                  placeholder="buscar un archivo..."
                ></b-form-file>
              </b-form-group>
              <b-form-group label="Default Input" label-for="input11">
                <b-form-select v-model="selected" :options="options" />
              </b-form-group>
              <b-form-group label="Tipo" label-for="input11">
                <b-form-radio-group id="radios2" v-model="picked" name="radioSubComponent">
                  <b-form-radio value="inicial" v-model="picked">Saldo Inicial</b-form-radio>
                  <b-form-radio value="move" v-model="picked">Movimiento</b-form-radio>
                </b-form-radio-group>
              </b-form-group>
              <b-form-group label="Processo" label-for="input12">
                <b-form-radio-group id="radios3" v-model="process" name="radioSubComponent1">
                  <b-form-radio value="1" v-model="process">1</b-form-radio>
                  <b-form-radio value="2" v-model="process">2</b-form-radio>
                </b-form-radio-group>
              </b-form-group>
              <b-button type="submit" variant="success" class="mr-2">Cargar</b-button>
              <b-button variant="light" v-on:click="clear()">Cancelar</b-button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import VueInstantLoadingSpinner from "vue-instant-loading-spinner/src/components/VueInstantLoadingSpinner.vue";

export default {
  name: "upload",
  components: {
    VueInstantLoadingSpinner
  },
  data() {
    return {
      baseUrl: "http://localhost:4000/files",
      file: null,
      picked: null,
      selected: "",
      process: null,
      options: [
        { value: null, text: "Seleccione una opcion" },
        { value: "REPUESTOS", text: "Repuestos" },
        { value: "PRIMA", text: "Materia Prima" },
        { value: "PRODUCTO", text: "Producto Terminado" }
      ],
      fileData: null,
      fields: [
        {
          key: "id",
          label: "ID"
        },
        {
          key: "name",
          label: "Archivo"
        },
        {
          key:"creationTime",
          label: "Fecha Subida"
        }
      ],
      items: []
    };
  },
  mounted() {
    this.getItems();
  },
  methods: {
    loadData() {
      let formData = new FormData();
      formData.append("file", this.file);
      formData.append("process", this.process);
      if (this.picked === "inicial") {
        formData.append("option", "SALDO_INITIAL_" + this.selected);
      } else {
        formData.append("option", this.process);
      }
      this.fileData = formData;
    },
    async getItems() {
      try {
        const response = await fetch(this.baseUrl);
        const data = await response.json();
        this.items = data["content"];
      } catch (error) {
        console.error(error);
      }
    },
    async submitFile() {
      let formData = new FormData();
      formData.append("file", this.file);
      formData.append("process", this.process);
      if (this.picked === "inicial") {
        formData.append("option", "SALDO_INITIAL_" + this.selected);
      } else {
        formData.append("option", this.process);
      }
      const response = await fetch(this.baseUrl, {
        method: "POST",
        body: formData
      });
      this.$refs.Spinner.show();
      setTimeout(
        function() {
          this.$refs.Spinner.hide();
        }.bind(this),
        10000
      );
      const data = await response.json();
      this.clear();
      this.$refs.Spinner.hide();
      this.items.push(data);
    },
    handleFileUpload() {
      this.file = this.$refs.file.files[0];
    },
    clear() {
      this.process = null;
      this.selected = null;
      this.picked = null;
    }
  }
};
</script>

<style scoped>
</style>