import axios from 'axios';

const DownloadService = {

  downloadfile(identifier) {
    axios({
      method: "get",
      url: "http://localhost:4000/files/download/",
      responseType: "arraybuffer"
    })
      .then(response => {
        this.forceFileDownload(response);
      })
      .catch(err => {
        alert("fallo al descargar");
      });
  },

  forceFileDownload(response) {
    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement("a");
    link.href = url;
    link.setAttribute("download", "Items.xlsx"); //or any other extension
    document.body.appendChild(link);
    link.click();
  }

}

export default DownloadService;