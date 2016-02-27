package com.marcoscarceles.github.repoman

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(GithubService)
class GithubServiceSpec extends Specification {

    def grailsApplication

    def setup() {
    }

    def cleanup() {
    }

    private static GITHUB_PAGE_SIZE = 30

    def "can fetch all the organizations"() {
        when:
        Iterator<List<Map>> orgs = service.getAllOrganizations()

        then:
        orgs.hasNext()

        when:
        def firstPage = orgs.next()

        then:
        firstPage.size() == GITHUB_PAGE_SIZE

        and:
        orgs.hasNext()

        when:
        def secondPage = orgs.next()

        then:
        secondPage.collect { it.name } as Set != firstPage.collect { it.name } as Set

        and:
        secondPage.size() == GITHUB_PAGE_SIZE
    }
}
