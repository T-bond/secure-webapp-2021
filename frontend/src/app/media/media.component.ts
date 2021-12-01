import { Component, ComponentFactoryResolver, OnInit, ViewChild, ViewContainerRef } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiConfiguration } from '../api/api-configuration';
import { MediaControllerService } from '../api/services';
import { CommentComponent } from '../comment/comment.component';

@Component({
  selector: 'app-media',
  templateUrl: './media.component.html',
  styleUrls: ['./media.component.scss']
})

export class MediaComponent implements OnInit {

  id: string;
  title: string;
  description: string;
  createdBy: string;
  createdAt: string;
  form: FormGroup;
  @ViewChild("commentsContainer", {read: ViewContainerRef}) commentsContainer: ViewContainerRef;

  constructor(
    private _formBuilder: FormBuilder,
    public apiConfiguration: ApiConfiguration,
    private router: Router,
    private resolver: ComponentFactoryResolver,
    private mediaApi: MediaControllerService
  ) { }

  public reloadComments() {
    var commentReq = new XMLHttpRequest();
    commentReq.addEventListener("load", () => {
      if (commentReq.response) {
        var commentRes = JSON.parse(commentReq.response);
        var componentFactory = this.resolver.resolveComponentFactory(CommentComponent);
        this.commentsContainer.clear();
        if (commentRes.content.length > 0) {
          commentRes.content.forEach(item => {
            var storeItemComponent = this.commentsContainer.createComponent(componentFactory);
            storeItemComponent.instance.id = item.id;
            storeItemComponent.instance.user = item.createdBy.username;
            storeItemComponent.instance.comment = item.comment;
            storeItemComponent.instance.time = new Date(item.createdAt).toDateString();;
          });
        }
      } else {
        alert("Error: unable to reach REST backend.");
      }
    });
    commentReq.addEventListener("error", () => {
      alert("Error: unable to reach REST backend.");
    });
    commentReq.open("GET", this.apiConfiguration.rootUrl + "/medias/" + this.id + "/comments?size=99999");
    commentReq.send();
  }
  
  ngOnInit(): void {
    var path = this.router.url;
    this.id = path.split('/').pop();

    this.form = this._formBuilder.group({
      comment: [[''], Validators.required]
    });

    var req = new XMLHttpRequest();
    req.addEventListener("load", () => {
      if (req.response) {
        var res = JSON.parse(req.response);
        this.title = res.title;
        this.description = res.description;
        this.createdBy = res.createdBy.username;
        this.createdAt = new Date(res.createdAt).toDateString();
      } else {
        alert("Error: unable to reach REST backend.");
      }
    });
    req.addEventListener("error", () => {
      alert("Error: unknown comment ID.");
    });
    req.open("GET", this.apiConfiguration.rootUrl + "/medias/" + this.id);
    req.send();

    this.reloadComments();
  }

  onSubmit(): void {
    var that = this
    if (this.form.valid) {
      this.mediaApi.createComment({
        id: parseInt(this.id),
        body: {
          comment: this.form.get("comment").value
        }
      }).subscribe({
        complete() { that.reloadComments(); },
        error() { alert("Error: unable to reach REST backend.") }
      });
    }
  }

  download(): void {
    var downloadLink = document.createElement("a");
    downloadLink.setAttribute("download", this.title + ".caff");
    downloadLink.href = this.apiConfiguration.rootUrl + "/medias/" + this.id + "/raw";
    downloadLink.click();
    downloadLink.remove();
    alert("Note: if CORS requests are enabled, the .caff file keeps its orignal name ('raw').")
  }
}
