import { Component, OnInit } from '@angular/core';
import { routerTransition } from '../../router.animations';
import { ItemService } from './services/item.service';
import { FormsModule } from '@angular/forms';
import { NgbModalConfig, NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'app-charts',
    templateUrl: './charts.component.html',
    styleUrls: ['./charts.component.scss'],
    animations: [routerTransition()],
    providers: [NgbModalConfig, NgbModal]
})
export class ChartsComponent implements OnInit {

    public isViewable: boolean;
    constructor() {

    }

    ngOnInit() {
        this.isViewable = true;
        // this.getUfvs();
    }
    public toggle(event: any) {
        event.preventDefault();
        this.isViewable = !this.isViewable;
    }
    detail() {
        alert("detail");
    }
}
