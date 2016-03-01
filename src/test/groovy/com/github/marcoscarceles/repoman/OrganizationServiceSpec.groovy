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

    @Unroll
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
}
