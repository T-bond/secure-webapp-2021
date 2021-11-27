import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthControllerService } from '../api/services';
import { ApiConfiguration } from '../api/api-configuration';

@Component({
  selector: 'app-login',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  
  form: FormGroup;

  constructor(
    private _formBuilder: FormBuilder,
    private router: Router,
    private authApi: AuthControllerService,
    private apiConfiguration: ApiConfiguration
  ) { }

  ngOnInit(): void {
    var usernameContainer= document.getElementById("username-container");
    var emailContainer = document.getElementById("email-container");

    this.form = this._formBuilder.group({
    });    

    /* I was unable to extract DTOs from API responses, so I decided fall back to XMLHTTPRequest for GET requests */

    var req = new XMLHttpRequest();
    req.addEventListener("load", () => {
      if (req.response) {
        var res = JSON.parse(req.response);
        usernameContainer.innerHTML = res.username;
        emailContainer.innerHTML = res.email;
      } else {
        alert("Something unexpected happened.");
      }
    });
    req.addEventListener("error", () => {
      alert("Something unexpected happened.");
    });
    req.open("GET", this.apiConfiguration.rootUrl + "/users/me");
    req.send();
}

  public logOut(): void {
    var that = this;
    this.authApi.logout({
    }).subscribe({
      complete() { that.router.navigate(["/login"]); },
      error() { alert("Something unexpected happened.") }
    });
  }
}
