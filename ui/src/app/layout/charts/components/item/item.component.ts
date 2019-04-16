import { Component, OnInit } from '@angular/core';
import { ItemService } from '../../services/item.service';

@Component({
    selector: 'app-item',
    templateUrl: './item.component.html',
    styleUrls: ['./item.component.scss']
})
export class ItemComponent implements OnInit {
    private page: number = 0;
    private items: Array<any>;
    private data = [];
    private pages: Array<number>;
    private maxPages: Array<number>;
    searchText: string = '';

    constructor(private ufvService: ItemService) { }
    ngOnInit() { this.getUfvs(); }

    getUfvs() {
        this.data.push({ 'key': 1, 'value': 2 });
        this.ufvService.getAll(this.page).subscribe(
            data => {
                this.items = data['content'];
                this.pages = new Array(data['totalPages']);
                this.maxPages = new Array(data['totalPages']);
                if (this.pages.length > 8) {
                    this.pages = [1,2,3,4,5,6,7,8];
                }
            },
            (error) => {
                console.log(error.error.message);
            }
        );
    }

    setPage(i, event: any) {
        console.log("click");
        event.preventDefault();
        this.page = i;
        this.getUfvs();
    }

    search() {
        if (this.searchText != "") {
            var id = parseInt(this.searchText);
            this.ufvService.getByItemId(id).subscribe(data =>
            {
                this.items = [];
                this.items.push(data);
            }, (error) => {
                this.items = [];
            })
            // this.items = this.items.filter(res => {
            //     return res.itemId == this.searchText;
            // });
        } else if (this.searchText == "") {
            this.ngOnInit();
        }
    }
}
