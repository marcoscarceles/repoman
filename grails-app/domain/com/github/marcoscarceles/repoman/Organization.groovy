package com.github.marcoscarceles.repoman

class Organization {

    String name
    String url
    String avatar
    String repos

    static constraints = {
        name unique:true
        url unique:true
        avatar unique:true
        repos unique:true
    }
}
