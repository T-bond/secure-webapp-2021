import { Component, OnInit, ViewChild, ViewContainerRef, ComponentFactoryResolver, ApplicationRef } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiConfiguration } from '../api/api-configuration';
import { StoreItemComponent } from '../store-item/store-item.component';

@Component({
  selector: 'app-login',
  templateUrl: './store.component.html',
  styleUrls: ['./store.component.scss']
})
export class StoreComponent implements OnInit {
    
  public pageSize = 20;
  public currentPage = 0;
  public totalSize = 0;
  form: FormGroup;
  @ViewChild("resultsContainer", {read: ViewContainerRef}) resultsContainer: ViewContainerRef;

  constructor(
    private _formBuilder: FormBuilder,
    private router: Router,
    private apiConfiguration: ApiConfiguration,
    private resolver: ComponentFactoryResolver,
    private applicationRef: ApplicationRef
  ) { }

  ngOnInit(): void {
    this.form = this._formBuilder.group({
      query: ['', Validators.compose([
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(255)
        ])
      ]
    });
  } 

  public displayItems(items) {
    var componentFactory = this.resolver.resolveComponentFactory(StoreItemComponent);
    this.resultsContainer.clear();
    if (items.length > 0) {
      items.forEach(item => {
        var storeItemComponent = this.resultsContainer.createComponent(componentFactory);
        storeItemComponent.instance.id = item.id;
        storeItemComponent.instance.title = item.title;
        storeItemComponent.instance.description = item.description;
        storeItemComponent.instance.createdAt = new Date(item.createdAt).toDateString();;
        storeItemComponent.instance.createdBy = item.createdBy.username;
      });
    }
    this.applicationRef.tick();
  }

  public onSubmit(): void {
    var req = new XMLHttpRequest();
    req.addEventListener("load", () => {
      if (req.response) {
        var res = JSON.parse(req.response);
        this.displayItems(res.content);
      } else {
        alert("Something unexpected happened.");
      }
    });
    req.addEventListener("error", () => {
      alert("Something unexpected happened.");
    });
    req.open("GET", this.apiConfiguration.rootUrl + "/medias/search?size=65535&titleContains=" + this.form.get("query").value);
    req.send();
  }

  public upload(): void {
    this.router.navigate(["/upload"])
  }
}
