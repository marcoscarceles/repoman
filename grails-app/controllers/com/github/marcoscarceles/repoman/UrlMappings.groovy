package com.github.marcoscarceles.repoman

class UrlMappings {

    static mappings = {

        //Quick and dirty
        "/repo/show/$org/$id"(controller:'repo', action:'show')

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
