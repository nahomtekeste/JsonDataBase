package com.example.jsonexample

class StudentModel {

    lateinit var name : String
    lateinit var id : String
    lateinit var email: String

    constructor(name :String , id:String , email:String)
    {
        this.name = name
        this.id = id
        this.email = email
    }
    constructor()
}