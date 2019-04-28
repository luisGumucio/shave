import { Component, OnInit } from '@angular/core';
import { UfvService } from '../../services/ufv.service';

@Component({
  selector: 'app-ufv-list',
  templateUrl: './ufv-list.component.html',
  styleUrls: ['./ufv-list.component.scss']
})
export class UfvListComponent implements OnInit {

  private searchText: string = '';
  private page: number = 0;
  private ufvs: Array<any>;
  private pages: Array<number>;
  constructor(private ufvService: UfvService) { }

  ngOnInit() {
    this.ufvService.receiveUfv.subscribe(() => {
      this.getUfvs();
    })
    this.getUfvs();
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
}
