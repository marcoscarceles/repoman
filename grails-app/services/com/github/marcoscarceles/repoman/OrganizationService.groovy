package com.github.marcoscarceles.repoman

import grails.core.GrailsApplication
import grails.transaction.Transactional
import groovy.time.TimeCategory

@Transactional
class OrganizationService {

    GithubService githubService
    GrailsApplication grailsApplication

    Organization get(String name) {
        Organization org = Organization.findByName(name.toLowerCase())
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
        organization.repoCount = organization.repos.size()
        organization.save()
        organization.repos
    }

    int saveAllOrganizations() {
        Iterator<List<Map>> orgs = githubService.allOrganizations
        log.debug 'Iterating over Organizations ...'
        while(orgs.hasNext()) {
            List<Map> page = orgs.next()
            //Because we want to flush one page at a time
            int saved = 0
            Organization.withNewTransaction {
                page.each { details ->
                    //This extra request is missing the point of caching ...
                    //details = githubService.getOrganization(details['name'])
                    if(relevant(details)) { //Otherwise it won't be listed (neither cached)
                        String name = (details.name as String)?.toLowerCase()
                        if(Organization.countByName(name) == 0) {
                            new Organization(details).save()
                            saved++
                        }
                    }
                }
            }
            log.debug "Saved another ${saved} organizations"
            if(throttling) {
                Thread.sleep(throttling as long)
            }
        }
        log.debug 'All Organizations saved!'
        return Organization.count()
    }

    List<Organization> search(String query, Map params=[:]) {
        List<Organization> results = Organization.findAllByNameLike("${query}%", params)
        if(!results && !params.containsKey('offset')) {
            Organization org = get(query)
            if(org) {
                results = [org]
            }
        }
        return results
    }

    int searchCount(String query) {
        int repos = Organization.countByNameIlike("${query}%")
        if(!repos) {
            repos = get(query) ? 1 : 0
        }
        return repos
    }

    private boolean relevant(def details) {
//        details['repoCount'] > minimumRepos  && (details['email'] || details['blog']) //At the very least, no?
//        details['description']
        true
    }

    def getThrottling() {
        grailsApplication.config.repoman.github.throttling
    }

    int getExpiry() {
        grailsApplication.config.repoman.cache.expiry
    }

    int getMinimumRepos() {
        grailsApplication.config.repoman.organization.minimumRepos
    }
}
