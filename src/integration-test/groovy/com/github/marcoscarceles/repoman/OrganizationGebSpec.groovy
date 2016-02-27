package com.github.marcoscarceles.repoman

import com.github.marcoscarceles.repoman.pages.OrganizationList
import grails.test.mixin.integration.Integration
import grails.transaction.*

import spock.lang.*
import geb.spock.*

/**
 * See http://www.gebish.org/manual/current/ for more instructions
 */
@Integration
@Rollback
class OrganizationGebSpec extends GebSpec {

    def setup() {
    }

    def cleanup() {
    }

    void "Organizations are automatically loaded"() {
        given: "wait for the Organizations to load"
        Thread.sleep(5000)

        when:
        to OrganizationList
        then:
        organizations.size() > 0
    }
}
