import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { routerTransition } from '../../router.animations';
import { FileService } from '../services/file.service';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
    selector: 'app-grid',
    templateUrl: './grid.component.html',
    styleUrls: ['./grid.component.scss'],
    animations: [routerTransition()]
})
export class GridComponent implements OnInit {
    private files: any;
    private pages: Array<number>;
    private page: number = 0;
    private file: any;
    private selectedOption: string;
    options: any;
    printedOption: string;
    private isInitial = false;
    private marked = false;
    private marked1 = false;
    private marked2 = false;

    defaultPagination: number;
    @ViewChild('myInput')
    myInputVariable: ElementRef;
    @ViewChild('myCheck')
    myCheck: ElementRef;
    @ViewChild('myProcess1')
    myProcess1: ElementRef;
    @ViewChild('myProcess2')
    myProcess2: ElementRef;

    constructor(private fileService: FileService, private spinner: NgxSpinnerService) { }

    ngOnInit() {
        this.defaultPagination = 1;
        this.file = File;
        this.options = [
            { name: "Materia Prima", value: "MATERIA_PRIMA" },
            { name: "Producto Terminando", value: 2 },
            { name: "Repuesto", value: 3 }
        ]
        //this.getAllFile();
    }

    toggleVisibility(e) {
        this.marked = e.target.checked;
    }
    toggleVisibility1(e) {
        this.marked1 = e.target.checked;
    }
    toggleVisibility2(e) {
        this.marked2 = e.target.checked;
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
    getAllFile() {
        this.fileService.getAll(this.page).subscribe(
            result => {
                this.files = result['content'];
                this.pages = new Array(result['totalPages']);
            },
            (error) => {
                console.log(error.error.message);
            });
    }

    setPage(i, event: any) {
        event.preventDefault();
        this.page = i;
        this.getAllFile();
    }

    save() {
        console.log("validate");
        if (this.isValid()) {
            this.spinner.show();
            let option = this.options.find(b => b.name === this.selectedOption).value;
            let process = null;
            if (this.marked) {
                switch (this.selectedOption) {
                    case this.options[0].name:
                        option = "SALDO_INITIAL_MP";
                        break;
                }
            }
            if (this.marked1) {
                process = "1";
            } else if (this.marked2) {
                process = "2";
            }
            this.fileService.addFile(this.file, option, process).subscribe(result => {
                this.spinner.hide();
                this.myInputVariable.nativeElement.value = "";
                this.myCheck.nativeElement.checked = false;
                this.marked = false;
                this.selectedOption = undefined;
            }, (error) =>
                    this.executeError(error.error.message));
        } else {
            alert("llene los datos por favor");
        }
    }

    isValid() {
        return this.selectedOption != undefined && this.file.name != undefined && (this.marked1 == true && this.marked2 == false) 
        || (this.marked1 == false && this.marked2 == true);
    }

    executeError(error) {
        alert("Fallo al subir el archivo");
        this.spinner.hide();
    }
}
