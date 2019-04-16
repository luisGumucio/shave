import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { UfvService } from '../../services/ufv.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Ufv } from '../ufv';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-timeline',
  templateUrl: './timeline.component.html',
  styleUrls: ['./timeline.component.scss']
})
export class TimelineComponent implements OnInit {
  ufv: any;
  private file: any;
  registerForm: FormGroup;
  submitted = false;
  private page: number = 0;
  private ufvs: Array<any>;
  private pages: Array<number>;
  private searchText: string = '';
  constructor(private ufvService: UfvService, private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.file = File;
    this.ufv = new Ufv();
    this.getUfvs();
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
  getUfvs() {
    this.ufvService.getAll(this.page).subscribe(
      data => {
        this.ufvs = data['content'];
        this.pages = new Array(data['totalPages']);
        if (this.pages.length > 8) {
          this.pages = [1, 2, 3, 4, 5, 6, 7, 8];
        }
        this.ufvs.sort();
      },
      (error) => {
        console.log(error.error.message);
      }
    );
  }
  onSubmit() {
    if (this.isValid()) {
      this.ufvService.addUfvFile(this.file).subscribe(result => {
        alert("Registro con exito");
        this.getUfvs();
      },
      (error) => {
        alert("Fallo al registrar intente de nuevo por favor");
      })
    } else {
      alert("llene los datos por favor");
    }
    // this.ufvService.addUfv(this.registerForm.value).subscribe(result => {
    //   this.registerForm.reset();
    //   this.getUfvs();
    // }, error => alert("fallo al registrar"));
  }
  isValid() {
    return this.file.size != undefined;
  }
  setPage(i, event: any) {
    event.preventDefault();
    this.page = i;
    this.getUfvs();
  }

  search() {
    if (this.searchText != "") {
      this.ufvs = this.ufvs.filter(res => {
        return res.value == this.searchText;
      });
    } else if (this.searchText == "") {
      this.ngOnInit();
    }
  }
}
