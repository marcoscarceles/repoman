package com.github.marcoscarceles.repoman

class Organization {

    String name
    String description
    String avatar
    Integer repoCount
    String email
    String blog

    Date dateCreated
    Date lastUpdated

    List<Repo> repos

    static hasMany = [repos:Repo]

    static constraints = {
        name unique:true
        description nullable: true
        repoCount nullable: true
        email nullable: true
        blog nullable: true
    }

    def beforeInsert() {
        name = name?.toLowerCase()
    }

    def beforeUpdate() {
        name = name?.toLowerCase()
    }

}
