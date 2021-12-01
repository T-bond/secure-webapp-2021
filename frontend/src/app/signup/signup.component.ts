import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthControllerService } from '../api/services';
import { UserCreateRequestDto } from '../api/models';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent implements OnInit {

  form: FormGroup;

  constructor(
    private _formBuilder: FormBuilder,
    private router: Router,
    private authApi: AuthControllerService
  ) { }

  ngOnInit(): void {
    this.form = this._formBuilder.group({
      email: ['', Validators.compose([
        Validators.required,
        Validators.email
        ])
      ],
      password: ['', Validators.compose([
        Validators.required,
        Validators.minLength(5),
        Validators.maxLength(255)
        ])
      ],
      username: ['', Validators.compose([
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(255)
        ])
      ],
    });
  }

  public onSubmit(): void {
    var that = this
    if (this.form.valid) {
      this.authApi.signUp({
        body: {
          username: this.form.get("username").value,
          password: this.form.get("password").value,
          email: this.form.get("email").value
        }
      }).subscribe({
        complete() { that.router.navigate(["/login"]); },
        error() { alert("Registration failed. E-mail address was already taken."); alert("Operators: Did you set the address of the REST backend in /app/api/api-configuration.ts?") }
      });
    }
  }

  public login(): void {
    this.router.navigate(["/login"]);
  }
}
