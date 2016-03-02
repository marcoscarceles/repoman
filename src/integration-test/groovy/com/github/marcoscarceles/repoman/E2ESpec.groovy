package com.github.marcoscarceles.repoman

import com.github.marcoscarceles.repoman.pages.OrganizationListPage
import com.github.marcoscarceles.repoman.pages.OrganizationPage
import com.github.marcoscarceles.repoman.pages.RepoPage
import geb.spock.GebSpec
import grails.test.mixin.integration.Integration
import grails.transaction.Rollback
import spock.lang.Stepwise
import spock.lang.Unroll

/**
 * Created by @marcos-carceles on 27/02/2016.
 */
@Stepwise
@Integration
@Rollback
class E2ESpec extends GebSpec {

    void "can see the (automatically loaded) Organizations"() {
        when:
        via OrganizationListPage

        then:
        waitFor('slow') {
            to OrganizationListPage
            organizations.size() == 10
        }

        and:
        organizations[0].name == 'netflix'
    }

    @Unroll
    void "can access an Organization"() {
        given:
        to OrganizationListPage

        when:
        def entry = choose(organization)
        String entryName = entry.name
        entry.avatar.click()

        then:
        at OrganizationPage

        and:
        name == entryName
        repos.displayed

        when:
        sortByPopularity.click()

        then:
        waitFor {
            at(OrganizationPage) &&
            repos[0].popularity == repos*.popularity.min()
        }

        when:
        sortByPopularity.click()

        then:
        waitFor {
            at(OrganizationPage) &&
            repos[0].popularity == repos*.popularity.max()
        }

        where:
        organization << [1, 2, 'Netflix']
    }

    void "can show a Repo"() {
        expect:
        at OrganizationPage
        repos[0].popularity == repos*.popularity.max()
        def listPopularity = repos[0].popularity

        when:
        repos[0].link.click()

        then:
        at RepoPage

        and:
        name == 'Netflix/Hystrix'
        popularity == listPopularity //old(repos[0].popularity), but this navigator is no longer available

        and:
        commits.size() == 30
    }

    void "can search organizations"() {
        given:
        to OrganizationListPage

        when:
        searchBox << 'r'
        searchButton.click()
        then:
        at OrganizationListPage
        message =~ /Your search returned/
        and:
        organizations.every { it.name.startsWith('r') }

        when:
        searchBox << 'pivotal'
        searchButton.click()
        then:
        at OrganizationListPage
        message =~ /Your search returned 1 results/

        when:
        searchBox << 'this-cant-really-exist-can-it'
        searchButton.click()
        then:
        at OrganizationListPage
        message =~ /Your search returned 0 results/
        and:
        organizations.size() == 10

    }
}
