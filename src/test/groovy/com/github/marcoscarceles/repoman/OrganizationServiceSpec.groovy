package com.github.marcoscarceles.repoman

import com.github.marcoscarceles.repoman.OrganizationService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(OrganizationService)
@Mock([Organization, GithubService])
class OrganizationServiceSpec extends Specification {

    def setup() {
    }

    def cleanup() {
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
