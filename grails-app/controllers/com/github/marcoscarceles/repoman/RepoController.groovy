package com.github.marcoscarceles.repoman

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class RepoController {

    RepoService repoService

    def show(String org, String id) {
        Repo repo = repoService.get(org, id)
        if(repo && params.sort && params.order) {
            //Reordering on the Domain itself? Thank god this is only a test project ...
            int order = params.order == 'asc' ? 1 : -1
            repo.commits = repo.commits.sort { Commit a, Commit b ->
                (a[params.sort] <=> b[params.sort]) * order
            }
        }
        respond repo
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'repo.label', default: 'Repo'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
