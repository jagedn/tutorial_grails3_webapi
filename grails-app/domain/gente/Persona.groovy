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
