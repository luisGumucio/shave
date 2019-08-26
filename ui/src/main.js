import Vue from 'vue'
import App from './App.vue'
import BootstrapVue from 'bootstrap-vue'
import VueRouter from 'vue-router'
import routes from './routes';
import axios from 'axios'
import VueAxios from 'vue-axios'

Vue.use(VueAxios, axios);
Vue.use(VueRouter);
Vue.use(BootstrapVue);
Vue.config.productionTip = false

const router = new VueRouter({mode:'history', routes});

new Vue({
  router,
  render: h => h(App),
}).$mount('#app')
