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
                orgs = JSON.parse(response.body).collect {[
                        url: it.url,
                        name: it.url.split('/').last(),
                        description: it.description,
                        repos: it.repos_url,
                        avatar: it.avatar_url
                ]}
                nextUrl = getNext(response)
            } else {
                log.warn("Unable to fetch ${nextUrl}, due to ${response.status} : ${response.statusText}")
                nextUrl = null
            }
            return orgs
        }
    }
}
