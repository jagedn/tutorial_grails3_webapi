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
