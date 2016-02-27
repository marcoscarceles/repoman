package com.github.marcoscarceles.repoman

import com.github.marcoscarceles.repoman.pages.OrganizationListPage
import com.github.marcoscarceles.repoman.pages.OrganizationPage
import com.github.marcoscarceles.repoman.pages.RepoPage
import geb.spock.GebSpec
import grails.test.mixin.integration.Integration
import grails.transaction.Rollback
import spock.lang.Stepwise

/**
 * Created by @marcos-carceles on 27/02/2016.
 */
@Stepwise
@Integration
@Rollback
class E2ESpec extends GebSpec {

    void "can see the Organizations"() {
        when:
        to OrganizationListPage

        then:
        organizations.size() == 10
        organizations[0].name == 'Netflix'
    }

    void "can access an Organization"() {
        expect:
        at OrganizationListPage

        when:
        choose('Netflix').avatar.click()

        then:
        at OrganizationPage

        and:
        name == 'Netflix'
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
    }

    void "can show a Repo"() {
        expect:
        at OrganizationPage
        repos[0].popularity == repos*.popularity.max()

        when:
        repos[0].link.click()

        then:
        at RepoPage

        and:
        repoOwner == 'Netflix'
        name == 'Hystrix'
    }
}
