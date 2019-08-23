<template>
  <div id="item-table">
    <p v-if="items.length < 1" class="empty-table">No items</p>
    <table class="table" v-else id="my-table">
      <thead class="thead-dark">
        <tr>
          <th scope="col">Tipo</th>
          <th scope="col">Fecha</th>
          <th scope="col">Ingreso</th>
          <th scope="col">Egreso</th>
          <th scope="col">Cantidad</th>
          <th scope="col">Precio Neto</th>
          <th scope="col">Precio Actual</th>
          <th scope="col">Ufv</th>
          <th scope="col">Ingreso total</th>
          <th scope="col">Egreso total</th>
          <th scope="col">Total</th>
          <th scope="col">Total actualizado</th>
          <th scope="col">Incremento</th>
          <th scope="col">Accion</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in items" :key="item.id">
          <td>{{ item.type | type }}</td>
          <td>{{ item.transactionDate }}</td>
          <td>{{ item.entry | currency }}</td>
          <td>{{ item.egress | currency }}</td>
          <td>{{ item.balance | currency }}</td>
          <td>{{ item.priceNeto | currency }}</td>
          <td>{{ item.priceActual | currency }}</td>
          <td>{{ item.ufv.value | currency }}</td>
          <td>{{ item.totalEntry | currency }}</td>
          <td>{{ item.totalEgress | currency }}</td>
          <td>{{ item.totalNormal | currency }}</td>
          <td>{{ item.totalUpdate | currency }}</td>
          <td>{{ item.increment | currency }}</td>
          <td>
            <button class="btn btn-primary btn-xs" @click="$emit('detail:item', item.detail)">
              <span class="mdi mdi-file-chart"></span>
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  name: "transaction-table",
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
    type(name) {
      if (name === "INITIAL") {
        return "init";
      }
      if (name == "EGRESS") {
        return "egre";
      }
      if (name == "UPDATE") {
        return "act";
      }
      if (name == "ENTRY") {
        return "ingr";
      }
    }
  }
};
</script>

<style scoped>
button {
  margin: 0 0.5rem 0 0;
}
</style>