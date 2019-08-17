<template lang="html">

  <section class="badges">
    <div class="row">
      <div class="col-md-8 grid-margin stretch-card">
        <div class="card">
          <div class="card-body">
            <h4 class="card-title">Archivos subidos</h4>
            <b-table responsive :items="items"></b-table>
          </div>
        </div>
      </div>
      <div class="col-md-4 grid-margin stretch-card">
                  <div class="card">
          <div class="card-body">
            <h4 class="card-title">Subir Archivo</h4>
            <form class="forms-sample">
              <b-form-group label="cargar archivo" label-for="input8">
                <b-form-file v-model="file" id="inpu8" :state="Boolean(file)" placeholder="buscar un archivo..."></b-form-file>
              </b-form-group>
              <b-form-group label="Default Input" label-for="input11">
                <b-form-select v-model="selected" :options="options" />
              </b-form-group>
              <b-form-group label="Processo" label-for="input11">
                    <b-form-radio-group id="radios2" v-model="selected" name="radioSubComponent">
                      <b-form-radio value="Free">Free</b-form-radio>
                      <b-form-radio value="Professional">Professional</b-form-radio>
                    </b-form-radio-group>
                  </b-form-group>
              <b-button type="submit" variant="success" class="mr-2">Cargar</b-button>
              <b-button variant="light">Cancelar</b-button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </section>

</template>

<script lang="js">
export default {
  name: 'upload',
    data () {
    return {
     options: [
        { value: null, text: 'Please select an option' },
        { value: 'a', text: 'This is First option' },
        { value: 'b', text: 'Selected Option' }
      ],
      items: [
        { isActive: true, age: 40, first_name: 'Dickerson', last_name: 'Macdonald' },
        { isActive: false, age: 21, first_name: 'Larsen', last_name: 'Shaw' },
        { isActive: false, age: 89, first_name: 'Geneva', last_name: 'Wilson' },
        { isActive: true, age: 38, first_name: 'Jami', last_name: 'Carney' }
      ],
      fields: {
        last_name: {
          label: 'Person last name',
          sortable: true
        },
        first_name: {
          label: 'Person first name',
          sortable: false
        },
        foo: {
          // This key overrides `foo`!
          key: 'age',
          label: 'Person age',
          sortable: true
        }
      }
    }
  }
}
</script>

<style scoped lang="scss">
.badges {
}
.tables {
}
.forms {
}
</style>
