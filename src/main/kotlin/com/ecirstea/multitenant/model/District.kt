package com.ecirstea.multitenant.model

import org.springframework.data.jpa.domain.AbstractPersistable_.id
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(schema="sample", name ="district")
 class District () {
    constructor(name: String) : this(){
        this.name = name
    }

    @Id
    @Column(name ="id")
    var id: UUID = UUID.randomUUID()
    @Column(name ="name")
    lateinit var name: String
    @Column (name = "created")
    var created: Date =  Date()
    @Column (name = "modified")
    var modified: Date = Date()


    override fun toString(): String {
        return "District(id=$id, name='$name', created=$created, modified=$modified)"
    }
}