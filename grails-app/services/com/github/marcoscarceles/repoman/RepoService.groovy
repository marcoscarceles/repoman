package com.github.marcoscarceles.repoman

import grails.transaction.Transactional

@Transactional
class RepoService {

    GithubService githubService

    Repo get(Organization owner, String name) {
        get(owner.name, name)
    }

    Repo get(String owner, String name) {
        Repo repo = Repo.findByOwnerAndName(owner, name)
        if(!repo) {
            repo = new Repo(githubService.getRepo(owner, name)).save()
        }
        return repo
    }
}