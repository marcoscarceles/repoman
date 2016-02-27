package com.github.marcoscarceles.repoman

class Organization {

    String name
    String url
    String avatar

    List<Repo> repos

    static hasMany = [repos:Repo]

    static constraints = {
        avatar unique:true
        name unique:true
        url display: false, unique:true
        repos display: false
    }
}
