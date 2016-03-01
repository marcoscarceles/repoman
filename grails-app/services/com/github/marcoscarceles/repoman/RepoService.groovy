package com.github.marcoscarceles.repoman

import grails.transaction.Transactional
import groovy.time.TimeCategory

@Transactional
class RepoService {

    def grailsApplication

    GithubService githubService

    Repo get(Organization owner, String name) {
        get(owner.name, name)
    }

    //TODO: Test BUG due to Controller's @Transacational(readonly=true)
    Repo get(String owner, String name) {
        Repo repo = Repo.findByOwnerAndName(owner, name)
        if(!repo || repo.lastUpdated < use(TimeCategory) { expiry.seconds.ago } ) {
            Map details = githubService.getRepo(owner, name)
            if(details) {
                if(repo) {
                    repo.properties << details
                } else {
                    repo = new Repo(details)
                }
                repo.save()
            }
        }
        repo.commits = githubService.getCommits(owner, name).collect { new Commit(it) }

        return repo
    }

    int getExpiry() {
        grailsApplication.config.repoman.cache.expiry
    }
}