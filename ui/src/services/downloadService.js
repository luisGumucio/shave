import axios from 'axios';

const DownloadService = {

 async downloadfile(identifier, type, loading) {
    for(let i = 0; i<6670; i++) {
      debbuger
      await axios({
        method: "post",
        url: "http://localhost:4000/files/producto/",
        
        data: {
          identifier: "transaction",
          type: "general",
          tienda: i.toString()
        },
        responseType: "arraybuffer"
      })
        .then(response => {
          this.forceFileDownload(response, "producto", loading);
        })
        .catch(err => {
          alert("fallo al descargar");
          loading.hide();
          console.log(err);
        });
    }
    // axios({
    //   method: "get",
    //   url: "http://localhost:4000/files/download/" + identifier + "/" + type,
    //   responseType: "arraybuffer"
    // })
    //   .then(response => {
    //     this.forceFileDownload(response, type, loading);
    //   })
    //   .catch(err => {
    //     alert("fallo al descargar");
    //     console.log(err);
    //   });
  },
 async downloadfileProducto(loading) {
    for(let i = 0; i<6670; i++) {
    await axios({
      method: "post",
      url: "http://localhost:4000/files/producto/",
      data: {
        identifier: "transaction",
        type: "general",
        tienda: i.toString()
      },
      responseType: "arraybuffer"
    })
      .then(response => {
        this.forceFileDownload(response, "producto", loading);
      })
      .catch(err => {
        // alert("fallo al descargar");
        loading.hide();
        console.log(err);
      });
    }
  },

  downloadfileTienda(loading) {

    axios({
      method: "get",
      url: "http://localhost:4000/stock/"
    })
      .then(response => {
        debugger
        response.data.forEach(b => {
          axios({
            method: "post",
            url: "http://localhost:4000/files/producto/",
            data: {
              identifier: "transaction",
              type: "tienda",
              tienda: b.id
            },
            responseType: "arraybuffer"
          })
            .then(response => {
              this.forceFileDownload(response, "producto", loading);
            })
            .catch(err => {
              alert("fallo al descargar");
              loading.hide();
              console.log(err);
            });
        });
      })
      .catch(err => {
        alert("fallo al descargar");
        console.log(err);
      });
  },
  downloadfileItem(loading) {
    debugger
    axios({
      method: "get",
      url: "http://localhost:4000/items/getAll/PRODUCTO"
    })
      .then(response => {
        debugger
        response.data.forEach(b => {
          debugger
          axios({
            method: "post",
            url: "http://localhost:4000/files/producto/",
            data: {
              identifier: "transaction",
              type: "item",
              tienda: b.id
            },
            responseType: "arraybuffer"
          })
            .then(response => {
              this.forceFileDownload(response, "producto", loading);
            })
            .catch(err => {
              alert("fallo al descargar");
              loading.hide();
              console.log(err);
            });
        });
      })
      .catch(err => {
        alert("fallo al descargar");
        console.log(err);
      });
  },

  forceFileDownload(response, type, loading) {
    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement("a");
    link.href = url;
    link.setAttribute("download", type + ".xlsx"); //or any other extension
    document.body.appendChild(link);
    link.click();
    loading.hide();
  },

  async updateItem(date1, type) {
    axios.post("http://localhost:4000/items/fechaUpdate", {
      date: date1,
      option: type
    });
  }

}

export default DownloadService;