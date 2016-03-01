package com.github.marcoscarceles.repoman

import grails.core.GrailsApplication
import grails.transaction.Transactional
import groovy.time.TimeCategory

@Transactional
class OrganizationService {

    GithubService githubService
    GrailsApplication grailsApplication

    Organization get(String name) {
        Organization org = Organization.findByName(name)
        if(!org || !org.repos || org.lastUpdated < use(TimeCategory) { expiry.seconds.ago } ) {
            Map details = githubService.getOrganization(name)
            if(details) {
                if(org) {
                    org.properties << details
                } else {
                    org = new Organization(details)
                }
                getRepos(org)
                org.save()
            }
        }
        return org
    }

    List<Repo> getRepos(Organization organization) {
        List<String> orgRepos = organization.repos*.name
        githubService.getRepos(organization.name).collect { details ->
            if(details.name in orgRepos) {
                organization.repos.find { it.name }.properties << details
            } else {
                if(organization.repos == null) {
                    organization.repos = []
                }
                organization.repos << new Repo(details)
            }
        }
        organization.save()
        organization.repos
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

    int getExpiry() {
        grailsApplication.config.repoman.cache.expiry
    }

}
