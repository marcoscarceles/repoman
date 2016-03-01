package com.github.marcoscarceles.repoman

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import groovy.time.TimeCategory
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@Unroll
@TestFor(OrganizationService)
@Mock([Organization, GithubService])
class OrganizationServiceSpec extends Specification {

    @Shared expiry
    @Shared GithubService githubService

    def setupSpec() {
        expiry = grailsApplication.config.repoman.cache.expiry
        grailsApplication.config.repoman.cache.expiry = 5
    }

    def cleanupSpec() {
        grailsApplication.config.repoman.cache.expiry = expiry
    }

    def "organizations are cached"() {
        given:
        githubService = service.githubService
        service.githubService = Mock(GithubService)
        Organization org = Organization.findByName(orgName)
        org?.delete()

        expect:
        Organization.countByName(orgName) == 0

        when: "Requesting a new Repo"
        org = service.get(orgName)
        then:
        org.dateCreated > use(TimeCategory) { 1.seconds.ago }
        org.lastUpdated > use(TimeCategory) { 1.seconds.ago }
        and:
        1 * service.githubService.getOrganization(orgName) >> {o -> githubService.getOrganization(o) }
        1 * service.githubService.getRepos(orgName) >> {o -> githubService.getRepos(o) }
        0 * service.githubService._(*_)

        when: "Immediately fetching a Repo"
        org = service.get(orgName)
        then: "There are no changes on the repo"
        org.dateCreated == old(org.dateCreated)
        org.lastUpdated == old(org.lastUpdated)
        and:
        0 * service.githubService.getOrganization(orgName)
        0 * service.githubService.getRepos(orgName) //Repos are cached
        0 * service.githubService._(*_)

        when: "Waiting long enough"
        Thread.sleep((grailsApplication.config.repoman.cache.expiry as int)*1000 + 1000)
        and:
        org = service.get(orgName)
        then: "The repo is updated"
        org.dateCreated == old(org.dateCreated)
        org.lastUpdated > old(org.lastUpdated)
        org.lastUpdated > use(TimeCategory) { 1.seconds.ago }
        and:
        1 * service.githubService.getOrganization(orgName) >> {o -> githubService.getOrganization(o) }
        1 * service.githubService.getRepos(orgName) >> {o -> githubService.getRepos(o) }
        0 * service.githubService._(*_)

        cleanup:
        service.githubService = githubService

        where:
        orgName << ['Netflix', 'github']
    }

    void "can fetch a single Organization : #name"() {
        given:
        Organization.findByName(name)?.delete()
        expect:
        Organization.countByName(name) == 0

        when:
        Organization organization = service.get(name)

        then:
        organization.name == name

        and:
        Organization.countByName(name) == 1

        where:
        name << ['Netflix', 'github']
    }

    void "searching for #query returns #expected"() {
        given:
        List<String> orgs = ['github', 'guardian', 'gitana', 'githubhelp', 'microsoft', 'Netflix', 'Netguru', 'NetOffice']
        orgs.each {
            service.get(it)
        }

        expect:
        Organization.count() == orgs.size()

        when:
        List<Organization> results = service.search(query,[:])

        then:
        results*.name as Set == expected as Set

        where:
        query    || expected
        'net'    || ['netflix', 'netguru', 'netoffice']
        'fli'    || []
        'git'    || ['github', 'gitana', 'githubhelp']
        'g'      || ['github', 'guardian', 'gitana', 'githubhelp', 'guardian']
        'google' || ['google']
    }

    void "search is paged with #params"() {
        given:
        List<String> orgs = ['github', 'guardian', 'gitana', 'githubhelp', 'microsoft', 'Netflix', 'Netguru', 'NetOffice']
        orgs.each {
            service.get(it)
        }

        when:
        List<Organization> results = service.search('n',params)

        then:
        results*.name*.toLowerCase() == expected*.toLowerCase()

        where:
        params                                           || expected
        [sort: 'name', order: 'asc']                     || ['netflix', 'netguru', 'netOffice']
        [sort: 'name', order: 'desc']                    || ['netOffice', 'netguru', 'netflix']
        [sort: 'name', order: 'desc', offset: 1, max: 1] || ['netguru']
        [sort: 'name', order: 'desc', offset: 1, max: 2] || ['netguru', 'netflix']
        [sort: 'name', order: 'desc', offset: 2, max: 1] || ['netflix']
        [sort: 'name', order: 'asc', offset: 2, max: 10] || ['netOffice']
    }
}
