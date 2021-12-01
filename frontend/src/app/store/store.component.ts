import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiConfiguration } from '../api/api-configuration';

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

  constructor(
    private _formBuilder: FormBuilder,
    private router: Router,
    private apiConfiguration: ApiConfiguration
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

  }

  public onSubmit(): void {
    /* I was unable to extract DTOs from API responses, so I decided fall back to XMLHTTPRequest for GET requests */

    var req = new XMLHttpRequest();
    req.addEventListener("load", () => {
      if (req.response) {
        var res = JSON.parse(req.response);
        console.log(req.response);
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
