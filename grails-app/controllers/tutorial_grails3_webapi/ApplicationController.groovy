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
