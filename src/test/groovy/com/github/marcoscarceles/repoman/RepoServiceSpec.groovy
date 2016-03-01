package com.github.marcoscarceles.repoman

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import groovy.time.TimeCategory
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by @marcos-carceles on 29/02/2016.
 */
@TestFor(RepoService)
@Mock([Repo, GithubService])
class RepoServiceSpec extends Specification {

    @Shared expiry
    @Shared GithubService githubService

    def setupSpec() {
        expiry = grailsApplication.config.repoman.cache.expiry
        grailsApplication.config.repoman.cache.expiry = 5
    }

    def cleanupSpec() {
        grailsApplication.config.repoman.cache.expiry = expiry
    }

    def "repos are cached"() {
        given:
        githubService = service.githubService
        service.githubService = Mock(GithubService)
        Repo repo = Repo.findByOwnerAndName(owner,name)
        repo?.delete()

        expect:
        Repo.countByOwnerAndName(owner,name) == 0

        when: "Requesting a new Repo"
        repo = service.get(owner, name)
        then:
        repo.dateCreated > use(TimeCategory) { 1.seconds.ago }
        repo.lastUpdated > use(TimeCategory) { 1.seconds.ago }
        and:
        1 * service.githubService.getRepo(owner, name) >> {o, n -> githubService.getRepo(o, n) }
        1 * service.githubService.getCommits(owner, name) >> {o, n -> githubService.getCommits(o, n) }
        0 * service.githubService._(*_)

        when: "Immediately fetching a Repo"
        repo = service.get(owner, name)
        then: "There are no changes on the repo"
        repo.dateCreated == old(repo.dateCreated)
        repo.lastUpdated == old(repo.lastUpdated)
        and:
        0 * service.githubService.getRepo(owner, name)
        1 * service.githubService.getCommits(owner, name) //Commits are never cached
        0 * service.githubService._(*_)

        when: "Waiting long enough"
        Thread.sleep((grailsApplication.config.repoman.cache.expiry as int)*1000 + 1000)
        and:
        repo = service.get(owner, name)
        then: "The repo is updated"
        repo.dateCreated == old(repo.dateCreated)
        repo.lastUpdated > old(repo.lastUpdated)
        repo.lastUpdated > use(TimeCategory) { 1.seconds.ago }
        and:
        1 * service.githubService.getRepo(owner, name) >> {o, n -> githubService.getRepo(o, n) }
        1 * service.githubService.getCommits(owner, name) >> {o, n -> githubService.getCommits(o, n) }
        0 * service.githubService._(*_)

        cleanup:
        service.githubService = githubService

        where:
        owner | name
        'Netflix' | 'Hystrix'
        'Netflix' | 'asgard'
        'github' | 'developer.github.com'
    }
}
