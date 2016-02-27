package com.github.marcoscarceles.repoman

import com.mashape.unirest.http.HttpResponse
import com.mashape.unirest.http.Unirest
import grails.converters.JSON
import grails.transaction.Transactional

@Transactional
class GithubService {

    def grailsApplication

    private static final API_HOME = 'https://api.github.com'

    Iterator<List<Map>> getAllOrganizations() {
        new OrganizationIterator()
    }

    Map getOrganization(String name) {
        Map details = [:]
        HttpResponse<String> response = Unirest.get(API_HOME+'/orgs/'+name)
                .header('Authorization', "token ${token}")
                .asString()
        if(response.status == 200) {
            details = getOrgDetails(response.body)
        }
        details
    }

    protected String getNext(HttpResponse<?> response) {
        String next = response.headers.link.find {
            it =~ /rel="next"/
        }
        next ? (next =~ /<([^>]+)>/)[0][1] : null
    }

    String getToken() {
        grailsApplication.config.repoman.github.token
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
            HttpResponse<String> response = Unirest.get(nextUrl).header('Authorization', "token ${token}").asString()
            if(response.status == 200) {
                orgs = JSON.parse(response.body).collect { getOrgDetails(it) }
                nextUrl = getNext(response)
            } else {
                log.warn("Unable to fetch ${nextUrl}, due to ${response.status} : ${response.statusText}")
                nextUrl = null
            }
            return orgs
        }
    }

    private Map getOrgDetails(String json) {
        getOrgDetails(JSON.parse(json))
    }

    private Map getOrgDetails(def json) {
        [
                url: json.url,
                name: json.login,
                description: json.description,
                repos: json.repos_url,
                avatar: json.avatar_url
        ]
    }
}
