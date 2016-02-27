package com.github.marcoscarceles.repoman

import grails.core.GrailsApplication
import grails.transaction.Transactional

@Transactional
class OrganizationService {

    GithubService githubService
    GrailsApplication grailsApplication

    Organization get(String name) {
        Organization org = Organization.findByName(name)
        if(!org) {
            Map details = githubService.getOrganization(name)
            if(details) {
                org = new Organization(details).save()
            }
        }

        if(!org.repos) {
            org.repos = getRepos(name)
            org.save()
        }

        return org
    }

    List<Repo> getRepos(String owner) {
        githubService.getRepos(owner).collect {
            new Repo(it)
        }
    }

    int saveAllOrganizations() {
        Iterator<List<Map>> orgs = githubService.allOrganizations
        log.debug 'Iterating over Organizations ...'
        while(orgs.hasNext()) {
            List<Map> page = orgs.next()
            //Because we want to flush one page at a time
            Organization.withNewTransaction {
                page.each {
                    if(!Organization.findByName(it.name)) {
                        Organization org = new Organization(it).save()
                    }
                }
            }
            log.debug "Saved another ${page.size()} organizations"
            if(throttling) {
                Thread.sleep(throttling as long)
            }
        }
        log.debug 'All Organizations saved!'
        return Organization.count()
    }

    def getThrottling() {
        grailsApplication.config.repoman.github.throttling
    }
}
