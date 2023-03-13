package com.droidekar.chatapp

class User {
    var name: String? = null
    var email: String? = null
    var uuid: String? = null

    constructor() {}

    constructor(name: String?, email: String?, uuid: String?) {
        this.name = name
        this.email = email
        this.uuid = uuid
    }

}