package com.github.marcoscarceles.repoman

import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

@Transactional
class OrganizationController {

    OrganizationService organizationService

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Organization.list(params), model:[organizationCount: Organization.count()]
    }

    def "search-query"() {
        redirect(action: 'search', id:params.query, params: params)
    }
    def search(String id) {
        params.max = Math.min((params.max ?: 10) as int, 100)
        List<Organization> results = organizationService.search(id, params)
        int count = organizationService.searchCount(id)
        flash.message = "Your search returned ${count} results"
        if(!count) {
            redirect action: 'index', params: params
        } else {
            render view: 'index', model: [organizationList: results, organizationCount: count, id: id]
        }
    }

    def show(String id) {
        Organization org = organizationService.get(id)
        if(org && params.sort && params.order) {
            //Reordering on the Domain itself? Thank god this is only a test project ...
            int order = params.order == 'asc' ? 1 : -1
            org.repos = org.repos.sort { Repo a, Repo b ->
                (a[params.sort] <=> b[params.sort]) * order
            }
        }
        respond org
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'organization.label', default: 'Organization'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
