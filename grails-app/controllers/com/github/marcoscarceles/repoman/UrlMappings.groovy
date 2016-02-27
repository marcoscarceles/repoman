package com.github.marcoscarceles.repoman

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(redirect: [controller: 'organization', action: 'index'])
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
