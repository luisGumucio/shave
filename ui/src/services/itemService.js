import axios from 'axios';

const baseUrl = "http://localhost:4000/items";

const ItemService = {

    getItemById(id) {
        return axios({
            method: "get",
            url: baseUrl + "/" + id,
        });
    },
    getItems(type, page) {
        return axios({
            method: "get",
            url: baseUrl + "?page=" + page + "&identifier=" + type
        });
    },
    getTotal(type) {
        return axios({
            method: "get",
            url: baseUrl + "/itemtotal?identifier=" + type,
        });
    }
};

export default ItemService;