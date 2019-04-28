import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Subject } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
import { UfvService } from  '../../services/ufv.service';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-ufv-upload',
  templateUrl: './ufv-upload.component.html',
  styleUrls: ['./ufv-upload.component.scss']
})
export class UfvUploadComponent implements OnInit {
  private _success = new Subject<string>();
  private _danger = new Subject<string>();
  private file: any;

  staticAlertClosed = false;
  successMessage: string;
  dangerMessage: string;

  constructor(private ufvService: UfvService, private spinner: NgxSpinnerService) { }

  ngOnInit() {
    this.file = File;
    setTimeout(() => this.staticAlertClosed = true, 10000);
    this._success.subscribe((message) => this.successMessage = message);
    this._danger.subscribe((message) => this.dangerMessage = message);
    this._success.pipe(debounceTime(5000)).subscribe(() => this.successMessage = null);
    this._danger.pipe(debounceTime(5000)).subscribe(() => this.dangerMessage = null);
  }

  onSubmit(data) {
    if (this.file != undefined) {
      this.spinner.show();
      this.ufvService.addUfvFile(this.file)
      .subscribe(result => {
        data.reset();
        this.file = undefined;
        this.spinner.hide();
        this.ufvService.executeUfv();
        this._success.next(`Se registro correctamente!`);
      },
      (error) => {
        this.spinner.hide();
        this._danger.next(`Fallo al registrar intente de nuevo por favor.`);
      })
    } else {
      this._danger.next(`Por favor ingrese el archivo.`);
    }
  }

  onFileChange(event: any) {
    // Instantiate an object to read the file content
    let reader = new FileReader();
    // when the load event is fired and the file not empty
    if (event.target.files && event.target.files.length > 0) {
      // Fill file variable with the file content
      this.file = event.target.files[0];
    }
  }
}
