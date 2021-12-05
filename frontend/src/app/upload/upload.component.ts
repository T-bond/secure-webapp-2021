import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiConfiguration } from '../api/api-configuration';

@Component({
  selector: 'app-login',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.scss']
})
export class UploadComponent implements OnInit {
  
  form: FormGroup;
  @ViewChild('file') fileUpload: ElementRef;

  constructor(
    private _formBuilder: FormBuilder,
    private router: Router,
    private apiConfiguration: ApiConfiguration
  ) { }

  ngOnInit(): void {
    this.form = this._formBuilder.group({
      title: ['', Validators.compose([
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(255)
        ])
      ],
      description: [''],
      file: ['', Validators.required]
    });
  }

  public onSubmit(): void {
    var req = new XMLHttpRequest();

    var dto = {
      title: this.form.get("title").value,
      description: this.form.get("description").value,
    }

    req.addEventListener("load", () => {
      if(req.status == 204) {
        alert("File has been upload successfully.")
        this.router.navigate(["/store"]);
      }
      else {
        alert("Please select a valid file to upload. Your CAFF file might have failed the validation check.");
      }
    });
    req.addEventListener("error", () => {
      alert("Please select a valid file to upload. Your CAFF file might have failed the validation check.");
    });
    req.open("POST", this.apiConfiguration.rootUrl + "/medias");

    var fd = new FormData();
    fd.append("file", this.fileUpload.nativeElement.files[0]);
    fd.append("data", new Blob([JSON.stringify(dto)], {type:"application/json"}));
    req.send(fd);
  }
}
