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
