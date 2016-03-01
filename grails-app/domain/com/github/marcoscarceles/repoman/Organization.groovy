package com.github.marcoscarceles.repoman

class Organization {

    String name
    String url
    String avatar

    Date dateCreated
    Date lastUpdated

    List<Repo> repos

    static hasMany = [repos:Repo]

    static constraints = {
        avatar unique:true
        name unique:true
        url unique:true
    }
}
