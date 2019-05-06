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
  constructor(private itemService: ItemService) { }

  ngOnInit() {
    this.getItemAll();
  }

  show(): void {
    this.itemService.showComponent();
  }

  getItemAll() {
    this.itemService.getAllItems(this.page).subscribe(
      data => {
        this.items = data['content'];
        this.pages = new Array(data['totalPages']);
        this.Totalpages = this.pages;
      },
      (error) => {
        console.log(error.error.message);
      }
    )
  }

}
