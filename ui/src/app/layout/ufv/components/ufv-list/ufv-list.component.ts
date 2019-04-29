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
  private Totalpages: Array<number>;
  private ufvs: Array<any>;
  private pages: Array<number>;
  constructor(private ufvService: UfvService) { }

  ngOnInit() {
    this.ufvService.receiveUfv.subscribe(() => {
      this.getUfvs();
    });
    this.getUfvs();
  }

  getUfvs() {
    this.ufvService.getAll(this.page).subscribe(
      data => {
        this.ufvs = data['content'];
        this.pages = new Array(data['totalPages']);
        this.Totalpages = this.pages;
        if (this.pages.length > 10) {
          this.pages = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
        }
        this.ufvs.sort();
      },
      (error) => {
        console.log(error.error.message);
      }
    );
  }

  setPage(i, event: any) {
    event.preventDefault();
    this.page = i;
    this.getUfvs();
  }
  previewPage(event: any) {
    event.preventDefault();
    const pageNumber = this.page - 1;
    if (pageNumber >= 0) {
      this.page = pageNumber;
      this.getUfvs();
    }
  }
  nextPage(event: any) {
    event.preventDefault();
    const pageNumber = this.page + 1;
    if (pageNumber < this.Totalpages.length) {
      this.page = pageNumber;
      this.getUfvs();
    }
  }
}
