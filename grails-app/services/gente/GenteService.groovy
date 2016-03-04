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
