<template>
  <div id="item-table">
    <p v-if="items.length < 1" class="empty-table">No items</p>
    <table class="table" v-else id="my-table"> 
      <thead class="thead-dark">
        <tr>
          <th scope="col">ID</th>
          <th scope="col">Cantidad</th>
          <th scope="col">Precio</th>
          <th scope="col">Fecha Actualizacion</th>
          <th scope="col">Total</th>
          <!-- <th scope="col">Almacen</th> -->
          <th scope="col">Accion</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in items" :key="item.id">
          <td>{{ item.id }}</td>
          <td>{{ item.quantity }}</td>
          <td>{{ item.price | currency }}</td>
          <td>{{ item.lastUpdate }}</td>
          <td>{{ item.quantity * item.price | currency }}</td>
          <!-- <td>{{ item.id | getAlmancen }}</td> -->
          <td>
            <p data-placement="top" data-toggle="tooltip" title="Transaction">
              <button
                class="btn btn-primary btn-xs"
                data-title="Transaction"
                data-toggle="modal"
                data-target="#edit"
              >
                <span class="mdi mdi-file-chart"></span>
              </button>
            </p>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  name: "item-table",
  props: {
    items: Array,
    currentPage: 0 
  },
  filters: {
    currency(amount) {
      const amt = Number(amount);
      return (
        (amt && amt.toLocaleString(undefined, { maximumFractionDigits: 3 })) ||
        "0"
      );
    },
  },
  computed: {
    getAlmancen: function(id) {
    //   return this.messages.map(m => this.decode(m, this.trigger)); 
    return "pepe";
    }
  }
};
</script>

<style scoped>
button {
  margin: 0 0.5rem 0 0;
}
</style>