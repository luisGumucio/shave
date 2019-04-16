import { Component, OnInit, OnDestroy } from '@angular/core';
import { UfvService } from '../../services/ufv.service';
import { Ufv } from '../ufv';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
    selector: 'app-notification',
    templateUrl: './notification.component.html',
    styleUrls: ['./notification.component.scss']
})
export class NotificationComponent implements OnInit {
    ufv: any;
    constructor(private ufvService: UfvService, private route: ActivatedRoute,
        private router: Router) { }

    ngOnInit() {
        this.ufv = new Ufv();
    }
    getAll() {
        this.router.navigate(['/dashboard']);
    }

    save(form: NgForm) {
        this.ufvService.addUfv(form).subscribe(result => {
            this.ufvService.getAll(0);
        }, error => console.error(error));
    }
}
