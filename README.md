# Mi primera aplicación rest con Grails 3

Tutorial de ejemplo, desarrollando una aplicación rest-api con grails 3. Si quieres colaborar u opinar, escríbenos: 
[@jagedn](https://twitter.com/jagedn)
[@rafbermudez](http://twitter.com/rafbermudez)


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

## Otras formas de construir los json

También podríamos crear las salidas de otra forma, como nos muestra el action elena o el action mapa
```groovy
package tutorial_grails3_webapi

import gente.Persona
import grails.converters.JSON

class ApplicationController{
    
    //Creamos una persona
    Persona persona = new Persona(
        nombre:"Elena",
        apellidos: "Nito del Bosque",
        direccion: "Bosque fantasma",
        telefono: 123456789
    )
    

    //generado con una vista
    def index() {
        //la pasamos como parámetro a la vista
        // por convención la vista de index() -> index.gson
        [persona: this.persona]
    }
    
    //generado por conversión
    def elena() {
        render this.persona as JSON
    }
    
    //generado a partir de un mapa
    def mapa() {
        render ([nombre:"Aitor", apellido:"Nillos", numeroDeLaSuerte:7] as JSON)
    }
}
```
Ejecutemos la aplicación. Tal como está configurado el UrlMappings, http://localhost:8080/application/elena y http://localhost:8080/application/mapa nos mostrará el resultado esperado.

Resumiendo, Grails es superpotente y es idóneo para realizar APIS. Tiene muchísimas facilidades para exponer objetos, podéis echar un ojo en [la sección REST de la documentación oficial](https://grails.github.io/grails-doc/3.1.2/guide/webServices.html).

## Construyendo un modelo

Ya hemos creado la clase de dominio Persona. Recordemos que una clase de dominio es una clase especial de Grails que se mapea a BD. ¿Qué más puede tener una persona? Pues una nariz, 20 dedos y una cantidad alta e indeterminada de pelos.
* Persona - Nariz (relación one-to-one)
* Persona - Dedo (relación one-to-many 0..20)
* Persona - Pelo (relación one-to-many)

Clase Nariz:
```
$ grails create-domain-class gente.Nariz
| Created grails-app/domain/gente/Nariz.groovy
| Created src/test/groovy/gente/NarizSpec.groovy
```
```groovy
package gente

class Nariz {
    
    Persona persona
    Float alturaEnCm
    Float anchoEnCm

    static constraints = {
    }
}
```
Clase Dedo:
```
$ grails create-domain-class gente.Dedo
| Created grails-app/domain/gente/Dedo.groovy
| Created src/test/groovy/gente/DedoSpec.groovy
```
```groovy
package gente

class Dedo {
    
    Persona persona
    Float longitud

    static constraints = {
    }
}
```
Clase Pelo:
```
$ grails create-domain-class gente.Pelo
| Created grails-app/domain/gente/Pelo.groovy
| Created src/test/groovy/gente/PeloSpec.groovy
```
```groovy
package gente

class Pelo {
    
    Persona persona
    Boolean esCanoso

    static constraints = {
    }
}
```
Cada tabla generada tiene la FK y las relaciones son unidireccionales. Desde Nariz, Pelo o Dedo podemos navegar a Persona pero Persona no conoce la existencia de estas. Editemos Persona:
```groovy
package gente

class Persona {
    
    String nombre
    String apellidos
    String direccion
    Integer telefono
    
    static hasOne = [nariz: Nariz]
    static hasMany= [dedos: Dedo, pelos: Pelo]


    static constraints = {
        dedos(maxSize: 20)
    }
}
```
Ahora las relaciones ya son bidireccionales.

Vamos a crear alguna instancia. En nuestro conf/application.yml tenemos configurada una base de datos en memoria, esta se inicia cada vez que arrancamos el proyecto. Si guardamos las instancias desde init/BootStrap.groovy, cada vez que se arranque el proyecto se insertarán.

Sobra decir que con cualquier otro RBDMS GORM funciona igualmente, simplemente cambiando el driver y la cadena de conexión en conf/application.yml
```groovy
import gente.*

class BootStrap {

    def init = { servletContext ->
        
        
        
        Persona elena = new Persona(
            nombre:"Elena",
            apellidos: "Nito del Bosque",
            direccion: "Bosque fantasma",
            telefono: 123456789
        )
        
        Nariz n = new Nariz(persona: elena, cmAlto:5, cmAncho:2.2)
        elena.nariz = n
        
        Dedo d1 = new Dedo (longitud: 5.2)
        elena.addToDedos(d1)
        Dedo d2 = new Dedo (longitud: 5.5)
        elena.addToDedos(d2)
        Dedo d3 = new Dedo (longitud: 5.1)
        elena.addToDedos(d3)
        
        (1..100).each{
            Pelo p = new Pelo (esCanoso:false)
            elena.addToPelos(p)
        }
        
        //Salvado en cascada. Si no salva, muestra los errores por consola
        if (!elena.save()){
            elena.errors.each{
                print it
            }
        }
        
        //Mostramos por consola un resumen
        println "--- ELENA ---"
        println "num dedos:${elena.dedos.size()}"
        println "num pelos:${elena.pelos.size()}"
        
    }
    def destroy = {
    }
}
```
Al arrancar la aplicación vemos el resumen en la consola
## La lógica de negocio, los servicios y los controladores
Grails proporciona una capa de servicios para alojar la lógica de negocio, con capacidades transaccionales y fácilmente testeable. Aunque nada nos impide meterla directamente en los controladores, decide el programador. Ojo, meter la lógica de negocio en los controladores es una mala práctica reconocida.

Creamos nuestro servicio Gente, y le añadimos 2 funciones
```
grails create-service gente.Gente
| Created grails-app/services/gente/GenteService.groovy
| Created src/test/groovy/gente/GenteServiceSpec.groovy
```
```groovy
package gente

import grails.transaction.Transactional

@Transactional
class GenteService {

    @Transactional(readOnly = true) //No transaccional
    Persona getElena() {
        return Persona.findByNombre("Elena")
    }
    
    
    @Transactional(readOnly = true) //No transaccional
    List<Pelo> get5PrimerosPelosFromPersona(Persona persona) {
        return Pelo.findAllByPersona(persona, [max: 5, sort: "id", order: "asc"])
    }
}
```
Sustituímos en el controlador el código antiguo por la llamada getElena() del servicio. Ahora ya está unido a los datos persistidos en BD. Si ejecutamos la aplicación accediendo a los endpoints veremos el resultado
```groovy
package tutorial_grails3_webapi

import gente.Persona
import grails.converters.JSON

class ApplicationController{
    
    def genteService
    
    //generado con una vista
    def index() {
        
        print genteService.get5PrimerosPelosFromPersona(genteService.getElena())
        //la pasamos como parámetro a la vista
        // por convención la vista de index() -> index.gson
        [persona: genteService.getElena()]
    }
    
    //generado por conversión
    def elena() {   
        render genteService.getElena() as JSON
    }
    
    //generado a partir de un mapa
    def mapa() {
        
        def m = [
            nombre:"Aitor", 
            apellido:"Nillos", 
            numeroDeLaSuerte:7,
            direcciones:[
                dir1:"calle sdfsd",
                dir2:"calle xsdfasd"
            ]
        ]
        
        render (m as JSON)
    }
}
```


## En construcción ... el viernes estará completo
