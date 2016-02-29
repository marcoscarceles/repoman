package com.github.marcoscarceles.repoman

import com.mashape.unirest.http.HttpResponse
import com.mashape.unirest.http.Unirest
import grails.test.mixin.TestFor
import spock.lang.IgnoreIf
import spock.lang.Specification

import static com.github.marcoscarceles.repoman.GithubService.API_HOME


/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(GithubService)
class GithubServiceSpec extends Specification {

    //TODO: Use Betamax

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

    void "at least one security implementation is defined"() { //and tested
        expect:
        service.token || (service.username && service.password) || (service.clientID && service.secret)
    }

    @IgnoreIf({!env['GITHUB_TOKEN']})
    void "token based authentication"() {
        setup:
        authConfig('token')

        when:
        HttpResponse<InputStream> anonymousResponse = Unirest.get(API_HOME+'/organizations').asBinary()
        HttpResponse<InputStream> authedResponse = service.authenticate(Unirest.get(API_HOME+'/organizations')).asBinary()
        def anonymousRateLimit = anonymousResponse.headers['x-ratelimit-limit'][0]
        def authedRateLimit = authedResponse.headers['x-ratelimit-limit'][0]

        then:
        authedResponse.status == 200

        and:
        (authedRateLimit as int) > (anonymousRateLimit as int)

        cleanup:
        restoreConfig()
    }

    //Could we unroll with an IgnoreIf?
    @IgnoreIf({!env['GITHUB_CLIENTID'] && !env['GITHUB_SECRET']})
    void "clientID and secret based authentication"() {
        setup:
        authConfig('clientID', 'secret')

        when:
        HttpResponse<InputStream> anonymousResponse = Unirest.get(API_HOME+'/organizations').asBinary()
        HttpResponse<InputStream> authedResponse = service.authenticate(Unirest.get(API_HOME+'/organizations')).asBinary()
        def anonymousRateLimit = anonymousResponse.headers['x-ratelimit-limit'][0]
        def authedRateLimit = authedResponse.headers['x-ratelimit-limit'][0]

        then:
        authedResponse.status == 200

        and:
        (authedRateLimit as int) > (anonymousRateLimit as int)

        cleanup:
        restoreConfig()
    }

    @IgnoreIf({!env['GITHUB_USERNAME'] && !env['GITHUB_PASSWORD']})
    void "username and password based authentication"() {
        setup:
        authConfig('username', 'password')

        when:
        HttpResponse<InputStream> anonymousResponse = Unirest.get(API_HOME+'/organizations').asBinary()
        HttpResponse<InputStream> authedResponse = service.authenticate(Unirest.get(API_HOME+'/organizations')).asBinary()
        def anonymousRateLimit = anonymousResponse.headers['x-ratelimit-limit'][0]
        def authedRateLimit = authedResponse.headers['x-ratelimit-limit'][0]

        then:
        authedResponse.status == 200

        and:
        (authedRateLimit as int) > (anonymousRateLimit as int)

        cleanup:
        restoreConfig()
    }

    def originalConfig

    private void authConfig(String ... keep) {
        originalConfig = grailsApplication.config.repoman.github.clone()
        grailsApplication.config.repoman.github.each {
            if(!(it.key in keep)) {
                it.value = null
            }
        }
    }

    private void restoreConfig() {
        grailsApplication.config.repoman.github = originalConfig
    }

    //TODO: Test fetch an Org

    //Todo: Test fetch a Repo
}
