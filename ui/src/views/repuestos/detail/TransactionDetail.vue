<template>
  <div class="col-xl-4 col-lg-4 col-md-3 col-sm-6 grid-margin stretch-card">
    <p
      v-if="reportTransaction === undefined"
      class="empty-table"
    >No transaction detalles</p>
    <div class="card card-statistics" v-else>
      <div class="card-body">
        <div class="template-demo">
          <table class="table mb-0">
            <thead>
              <tr>
                <th class="pl-0">Transaction</th>
                <th class="text-right">total</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td class="pl-0">Total Normal</td>
                <td class="pr-0 text-right">
                  <b-badge pill variant="primary">{{ reportTransaction.totalNormal | currency }}</b-badge>
                </td>
              </tr>
              <tr>
                <td class="pl-0">Total Actualizado</td>
                <td class="pr-0 text-right">
                  <b-badge pill variant="primary">{{ reportTransaction.totalUpdate | currency}}</b-badge>
                </td>
              </tr>
              <tr>
                <td class="pl-0">Total Incremento</td>
                <td class="pr-0 text-right">
                  <b-badge pill variant="primary">{{ reportTransaction.totalIncrement | currency }}</b-badge>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: "transaction-detail",
  props: {
    reportTransaction: Object
  },
  filters: {
    currency(amount) {
      const amt = Number(amount);
      return (
        (amt && amt.toLocaleString(undefined, { maximumFractionDigits: 3 })) ||
        "0"
      );
    }
  }
};
</script>
<style scoped>
</style>