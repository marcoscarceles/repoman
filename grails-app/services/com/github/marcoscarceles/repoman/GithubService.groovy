package com.github.marcoscarceles.repoman

import com.mashape.unirest.http.HttpResponse
import com.mashape.unirest.http.Unirest
import com.mashape.unirest.request.GetRequest
import grails.converters.JSON
import grails.transaction.Transactional

import java.nio.file.Paths

@Transactional
class GithubService {

    def grailsApplication

    private static final API_HOME = 'https://api.github.com'

    Iterator<List<Map>> getAllOrganizations() {
        new OrganizationIterator()
    }

    Map getOrganization(String name) {
        get(getOrgDetails, 'orgs', name)
    }

    List<Map> getRepos(String owner) {
        get(getRepoDetails, 'orgs', owner, 'repos')
    }

    Map getRepo(String owner, String name) {
        get(getRepoDetails, 'repos', owner, name)
    }

    List<Map> getCommits(String owner, String name) {
        get(getCommitDetails, 'repos', owner, name, 'commits')
    }

    private <T> T get(Closure<T> parsingClosure, String type, String ... path) {
        T value
        String url = API_HOME +'/'+ Paths.get(type, path).toString()
        HttpResponse<String> response = authenticate(Unirest.get(url)).asString()
        if(response.status == 200) {
            if(log.debugEnabled) {
                log.debug("Pending available requests " + response.headers['x-ratelimit-remaining'])
            }
            def body = JSON.parse(response.body)
            value = body instanceof List ? body.collect(parsingClosure) : parsingClosure.call(body)
        } else {
            log.warn "Unable to fetch ${url}, response ${response.status} : ${response.statusText}"
        }
        return value
    }

    private GetRequest authenticate(GetRequest request) {
        if(clientID && secret) {
            request.queryString([
                    'client_id' : clientID,
                    'client_secret' : secret
            ])
        } else if (username && password) {
            request.basicAuth(username, password)
        } else if (token) {
            request.header('Authorization', "token ${token}")
        } else { //Hope for the best
            log.warn "Unable to authenticate request, likely to hit Github rate limit"
            request
        }
    }

    private String getNext(HttpResponse<?> response) {
        String next = response.headers.link.find {
            it =~ /rel="next"/
        }
        next ? (next =~ /<([^>]+)>/)[0][1] : null
    }

    String getClientID() {
        getConfig('clientID', /GITHUB_CLIENTID/)
    }
    String getSecret() {
        getConfig('clientID', /GITHUB_SECRET/)
    }
    String getUsername() {
        getConfig('username', /GITHUB_USERNAME/)
    }
    String getPassword() {
        getConfig('password', /GITHUB_PASSWORD/)
    }
    String getToken() {
        getConfig('token', /GITHUB_TOKEN/)
    }

    private getConfig(field, envVariable) {
        grailsApplication.config.repoman.github[field] =~ envVariable ? null : grailsApplication.config.repoman.github[field]
    }

    private class OrganizationIterator implements Iterator<List<Map>> {

        String nextUrl = API_HOME+'/organizations'

        @Override
        boolean hasNext() {
            return nextUrl != null
        }

        @Override
        List<Map> next() {
            List orgs = []
            if(!hasNext()) {
                return null
            }
            HttpResponse<String> response = authenticate(Unirest.get(nextUrl)).asString()
            if(response.status == 200) {
                orgs = JSON.parse(response.body).collect getOrgDetails
                nextUrl = getNext(response)
            } else {
                log.warn("Unable to fetch ${nextUrl}, due to ${response.status} : ${response.statusText}")
                nextUrl = null
            }
            return orgs
        }
    }

    private Closure<Map> getOrgDetails = { json ->
        [
                'url': json.url,
                'name': json.login,
                'description': json.description,
                'avatar': json.avatar_url
        ]
    }

    private Closure<Map> getRepoDetails = { json ->
        [
                'owner': json.owner.login,
                'name': json.name,
                'stargazers': json.stargazers_count,
                'forks': json.forks_count,
        ]
    }

    private Closure<Map> getCommitDetails = { json ->
        [
                'sha': json.sha.substring(0,7),
                'message': json.commit.message,
                'url': json.html_url
        ]
    }
}
