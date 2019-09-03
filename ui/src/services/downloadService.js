import axios from 'axios';

const DownloadService = {

  downloadfile(identifier, type, loading) {
    axios({
      method: "get",
      url: "http://localhost:4000/files/download/" + identifier + "/" + type,
      responseType: "arraybuffer"
    })
      .then(response => {
        this.forceFileDownload(response, type, loading);
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