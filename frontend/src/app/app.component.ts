import { Component } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router'
import { FormBuilder, FormGroup } from '@angular/forms';
import { ApiConfiguration } from './api/api-configuration';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'CAFF store';
  form: FormGroup; /* current user */

  constructor(
    private router: Router,
    private _formBuilder: FormBuilder,
    private apiConfiguration: ApiConfiguration
  ) {
    /* show current user here */
    this.form = this._formBuilder.group({
    });
  }

  ngOnInit(): void {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        /* get current user */
        if (this.form.valid) {
          var currentUserDiv = document.getElementById("current-user");
          var currentUserDisplay = document.getElementById("current-user-display");

          /* I was unable to extract DTOs from API responses, so I decided fall back to XMLHTTPRequest for GET requests */

          var req = new XMLHttpRequest();
          req.addEventListener("load", () => {
            var res = JSON.parse(req.response);
            if (res.username) {
              currentUserDiv.hidden = false;
              currentUserDisplay.innerHTML = res.username;
            } else {
              currentUserDiv.hidden = true;
            }
          });
          req.addEventListener("error", () => {
            currentUserDiv.hidden = true;
          });
          req.open("GET", this.apiConfiguration.rootUrl + "/users/me");
          req.send();
        }
      }
    })

    this.router.navigate(["/login"])
  }

  profile() {
    this.router.navigate(["/profile"])
  }
}