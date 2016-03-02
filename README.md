# tutorial_grails3_webapi
Tutorial de ejemplo: Aplicación rest con grails 3

## Creando la aplicación
Veamos los perfiles disponibles en grails 3 para el esqueleto inicial de una aplicación.

```
$ grails list-profiles
| Available Profiles
--------------------
* angular - A profile for creating applications using AngularJS
* rest-api - Profile for Web API applications
* base - The base profile extended by other profiles
* plugin - Profile for plugins designed to work across all profiles
* web - Profile for Web applications
* web-plugin - Profile for Plugins designed for Web applications
```
Como queremos crear un API rest, rest-api será el perfil indicado para optimizar el esqueleto inicial.
```
$ grails create-app tutorial_grails3_webapi --profile=rest-api
| Application created at /home/rafbermudez/tutorial_grails3_webapi
```
¡Listo! El esqueleto inicial está creado. Probemos a ejecutarlo.
```
$ ./grails run-app
| Running application...
Grails application running at http://localhost:8080 in environment: development
```
Ahora podemos acceder en un navegador a http://localhost:8080 y ver que nos muestra el json de ejemplo.
