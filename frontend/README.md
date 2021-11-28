# CAFFStore

## Running

1. Start the backend application as described in the `backend` folder.
2. Edit the protocol, IP address, port number and path used by the REST backend in `src/app/api/api-configuration.ts`, if required (default is `http://localhost:8080`).
3. Run `ng serve --host 0.0.0.0` to serve the application at `http://localhost:4200/`. The best pracitce is specifying the narrowest possible IP range for allowed connections.

## CORS requests

To allow cross origin resource sharing in your browser (chrome/chromium):

1. `mkdir temp`
2. `chromium --disable-web-security --user-data-dir="temp"`

## OpenAPI code generation

In case the API of the backend changes, do the following:

1. `wget http://localhost:8080/openapi -O api.json`
2. `ng-openapi-gen --input api.json --output src/app/api`
3. `rm api.json`