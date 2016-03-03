# tutorial_grails3_webapi
Tutorial de ejemplo: Mi primera aplicación rest con grails 3

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
¡Listo! El esqueleto inicial está creado. 
```
__ tutorial_grails3_webapi
   |__ gradle
   |__ grails-app                   - Código principal de una aplicación grails
       |__ conf                     
       |__ controllers              - Controladores y enrutamiento (configuración de urls)
       |__ domain                   - Clases persistentes (se mapean en BD)
       |__ i18n
       |__ init
       |__ services                 - Servicios de la aplicación (patrón fachada)
       |__ utils
       |__ views                    - Aquí creamos las vistas rest (gson, por ejemplo) 
   |__ src
       |__ integration-test
       |__ main                     - Clases groovy o java normales
       |__ test
   |__ build.gradle
   |__ gradle.properties
   |__ gradlew
   |__ gradlew.bat
```
Probemos a ejecutar la aplicación.
```
$ ./grails run-app
| Running application...
Grails application running at http://localhost:8080 in environment: development
```
¡Eureka! Ahora podemos acceder con un navegador a http://localhost:8080 y ver que nos muestra el json de ejemplo.

## Entendiendo las vistas rest

En el directorio views, grails ha creado las vistas de errores y una vista de ejemplo en el paquete application: index.gson. Un archivo gson es un template generador de json. Simplifiquemos index.gson a sólo 3 líneas:
```groovy
json {
    mensaje "hola mundo"
}
```
Si ejecutamos la aplicación de nuevo, la salida que nos muestra el navegador es {"mensaje":"hola mundo"}

Vamos a crear nuestra primera clase de dominio. Creamos la clase Persona en el paquete gente. 
```
$ grails create-domain-class gente.Persona
> Configuring > 0/1 projects > root project > Resolving dependencies ':classpath1/1 projects > Resolving dependencies ':agent' > Resolving dependencies 'detache > Resolving dependencies ':runtime' > Resolving dependencies 'detachedConfigura > Resolving dependencies ':console' > Resolving dependencies 'detachedConfigura
BUILD SUCCESSFUL

Total time: 2.889 secs
> Resolving dependencies ':testRuntime' > Resolving dependencies 'detachedConfig| Created grails-app/domain/gente/Persona.groovy
| Created src/test/groovy/gente/PersonaSpec.groovy
```
Vemos que se genera tanto la clase como su clase de tests unitarios. Por el momento obviaremos su comportamiento. ¡Una persona sin atributos seria demasiado vulgar! Editemos la clase Persona

```groovy
package gente

class Persona {
    
    String nombre
    String apellidos
    String direccion
    Integer telefono

    static constraints = {
    }
}
```

Vamos a inyectar una persona en la vista. Los controladores controlan las vistas. Vamos a retocar ApplicationController (ojo, en nuestro caso y normalmente no es necesario que herede de ningún objeto):
```groovy
package tutorial_grails3_webapi

import gente.Persona

class ApplicationController{

    def index() {
        
        //Creamos una persona
        Persona persona = new Persona(
            nombre:"Elena",
            apellidos: "Nito del Bosque",
            direccion: "Bosque fantasma",
            telefono: 123456789
        )
        
        //la pasamos como parámetro a la vista
        // por convención la vista de index() -> index.gson
        [persona: persona]
    }
}
```
Ahora retocamos index.gson para mostrar algo más complejo:
```groovy
model{
    Persona persona
}

json {
    mensaje "hola ${persona.nombre} ${persona.apellidos}"

    persona {
        nombre persona.nombre
        telefono persona.telefono
    }    
}
```
Ejecutamos de nuevo la aplicación y obtenemos el resultado:
```json
{  
   "mensaje":"hola Elena Nito del Bosque",
   "persona":{  
      "nombre":"Elena",
      "telefono":123456789
   }
}
```
