package com.github.marcoscarceles.repoman

class Repo {

    String owner
    String name
    int stargazers
    int forks

    String getFullName() {
        "$owner/$name"
    }

    void setFullName(String fullName) {
        //Do nothing
    }

    int getPopularity() {
        stargazers + forks
    }

    void setPopularity(int popularity) {
        //Do nothing
    }

    //Quick hack for quick nicer links on _table.gsp
    Map getLinkParameters() {
        ['org':owner]
    }

    static transients = ['popularity', 'fullName', 'linkParameters']

    static constraints = {
        owner display:false
        name unique:true, display:false
        popularity display:true
        stargazers display:false
        forks display:false
    }
}
