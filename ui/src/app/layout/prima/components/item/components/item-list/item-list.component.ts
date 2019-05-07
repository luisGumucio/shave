import { Component, OnInit } from '@angular/core';
import { ItemService } from '../../services/item.service';
@Component({
  selector: 'app-item-list',
  templateUrl: './item-list.component.html',
  styleUrls: ['./item-list.component.scss']
})
export class ItemListComponent implements OnInit {

  private page: number = 0;
  private items: Array<any>;
  private pages: Array<number>;
  private Totalpages: Array<number>;
  private searchText: string = '';
  constructor(private itemService: ItemService) { }

  ngOnInit() {
    this.getItemAll();
  }

  show(itemId): void {
    this.itemService.showComponent(itemId);
  }

  getItemAll() {
    this.itemService.getAllItems(this.page).subscribe(
      data => {
        this.items = data['content'];
        this.pages = new Array(data['totalPages']);
        this.Totalpages = this.pages;
        if (this.pages.length > 10) {
          this.pages = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
        }
      },
      (error) => {
        console.log(error.error.message);
      });
  }

  setPage(i, event: any) {
    event.preventDefault();
    this.page = i;
    this.getItemAll();
  }

  previewPage(event: any) {
    event.preventDefault();
    const pageNumber = this.page - 1;
    if (pageNumber >= 0) {
      this.page = pageNumber;
      this.getItemAll();
    }
  }

  nextPage(event: any) {
    event.preventDefault();
    const pageNumber = this.page + 1;
    if (pageNumber < this.Totalpages.length) {
      this.page = pageNumber;
      this.getItemAll();
    }
  }

  search() {
    if (this.searchText !== '') {
      this.items = this.items.filter(res => {
        return res.id === this.searchText;
      });
    } else if (this.searchText === '') {
      this.ngOnInit();
    }
  }
}
